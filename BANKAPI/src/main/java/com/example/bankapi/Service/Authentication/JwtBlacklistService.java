package com.example.bankapi.Service.Authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {
    private final RedisTemplate<String,String> redisTemplate;

    public void addToBlackList(String token,long expiration){
        redisTemplate.opsForValue().set(token,"blacklisted",expiration, TimeUnit.MILLISECONDS);
    }
    public boolean isBlackListed(String token){
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

}
