package com.thecoffe.ms_the_coffee.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thecoffe.ms_the_coffee.exceptions.EmailNotFoundException;
import com.thecoffe.ms_the_coffee.models.User;
import com.thecoffe.ms_the_coffee.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // * Method to validate user logged
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new EmailNotFoundException("Correo: %s no existe en el sistema.".formatted(email));
        }
        User user = userOptional.orElseThrow();
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                user.isEnabled(), true, true, true, authorities);

    }
}
