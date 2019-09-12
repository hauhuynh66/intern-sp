package com.intern.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttempService {
    private final int MAX_ATTEMP=5;
    private LoadingCache<String,Integer> cache;
    public LoginAttempService(){
        super();
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }
    public void loginSucceeded(String key){
        cache.invalidate(key);
    }
    public void loginFailed(String key){
        int attemp = 0;
        try{
            attemp = cache.get(key);
        }catch (ExecutionException ee){
            attemp = 0;
        }
        attemp++;
        cache.put(key,attemp);
    }
    public boolean isBlocked(String key){
        try{
            return cache.get(key) >= MAX_ATTEMP;
        }catch (ExecutionException ee){
            return false;
        }
    }
}
