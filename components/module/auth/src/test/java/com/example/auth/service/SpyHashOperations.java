package com.example.auth.service;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpyHashOperations implements HashOperations<String, String, Object> {
    public String putAll_key_argument;
    public Map<? extends String, ?> putAll_value_argument;

    @Override
    public Long delete(String key, Object... hashKeys) {
        return null;
    }

    @Override
    public Boolean hasKey(String key, Object hashKey) {
        return null;
    }

    @Override
    public Object get(String key, Object hashKey) {
        return null;
    }

    @Override
    public List<Object> multiGet(String key, Collection<String> hashKeys) {
        return null;
    }

    @Override
    public Long increment(String key, String hashKey, long delta) {
        return null;
    }

    @Override
    public Double increment(String key, String hashKey, double delta) {
        return null;
    }

    @Override
    public String randomKey(String key) {
        return null;
    }

    @Override
    public Map.Entry<String, Object> randomEntry(String key) {
        return null;
    }

    @Override
    public List<String> randomKeys(String key, long count) {
        return null;
    }

    @Override
    public Map<String, Object> randomEntries(String key, long count) {
        return null;
    }

    @Override
    public Set<String> keys(String key) {
        return null;
    }

    @Override
    public Long lengthOfValue(String key, String hashKey) {
        return null;
    }

    @Override
    public Long size(String key) {
        return null;
    }

    @Override
    public void putAll(String key, Map<? extends String, ?> m) {
        this.putAll_key_argument = key;
        this.putAll_value_argument = m;
    }

    @Override
    public void put(String key, String hashKey, Object value) {

    }

    @Override
    public Boolean putIfAbsent(String key, String hashKey, Object value) {
        return null;
    }

    @Override
    public List<Object> values(String key) {
        return null;
    }

    @Override
    public Map<String, Object> entries(String key) {
        return null;
    }

    @Override
    public Cursor<Map.Entry<String, Object>> scan(String key, ScanOptions options) {
        return null;
    }

    @Override
    public RedisOperations<String, ?> getOperations() {
        return null;
    }
}

