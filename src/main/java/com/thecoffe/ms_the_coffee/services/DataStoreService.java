package com.thecoffe.ms_the_coffee.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class DataStoreService {

    private Map<String, Object> data = new ConcurrentHashMap<>();

    public void save(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void delete(String key) {
        data.remove(key);
    }
}
