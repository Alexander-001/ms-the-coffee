package com.thecoffe.ms_the_coffee.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class DataStoreServiceTest {


    private DataStoreService dataStoreService;

    @BeforeEach
    void setUp() {
        dataStoreService = new DataStoreService();
    }

    @Test
    void save() {
        dataStoreService.save("key", "value");
        Object result = dataStoreService.get("key");
        assertEquals("value", result, "save");
    }

    @Test
    void delete() {
        dataStoreService.save("key", "value");
        dataStoreService.delete("key");
        Object result = dataStoreService.get("key");
        assertNull(result, "Delete");
    }
}