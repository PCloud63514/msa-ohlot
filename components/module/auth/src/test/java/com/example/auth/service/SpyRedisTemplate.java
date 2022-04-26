package com.example.auth.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SpyRedisTemplate extends RedisTemplate<String, Object> {
    public boolean opsForValue_wasCall;
    public boolean opsForHash_wasCall;
    public SpyValueOperations spyValueOperations = new SpyValueOperations();
    public SpyHashOperations spyHashOperations = new SpyHashOperations();
    public List<String> expire_key_argument = new ArrayList<>();
    public List<Long> expire_timeout_argument = new ArrayList<>();
    public List<TimeUnit> expire_unit_argument = new ArrayList<>();
    public List<String> delete_argument = new ArrayList<>();
    @Override
    public ValueOperations<String, Object> opsForValue() {
        this.opsForValue_wasCall = true;
        return spyValueOperations;
    }

    @Override
    public <HK, HV> HashOperations<String, HK, HV> opsForHash() {
        this.opsForHash_wasCall = true;
        return (HashOperations<String, HK, HV>) spyHashOperations;
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        expire_key_argument.add(key);
        expire_timeout_argument.add(timeout);
        expire_unit_argument.add(unit);
        return true;
    }

    @Override
    public Boolean expireAt(String key, Instant expireAt) {
        return super.expireAt(key, expireAt);
    }

    @Override
    public Boolean delete(String key) {
        delete_argument.add(key);
        return true;
    }
}

