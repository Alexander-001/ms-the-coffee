package com.thecoffe.ms_the_coffee.security.filters;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedFilter implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Acceso Denegado: No tienes permiso para acceder a este recurso.");
        /*
         * response.setStatus(HttpServletResponse.SC_FORBIDDEN);
         * response.setContentType(CONTENT_TYPE);
         * response.getWriter().
         * write("{\"error\": \"Acceso Denegado: No tienes permiso para acceder a este recurso\"}"
         * );
         */
    }

}
