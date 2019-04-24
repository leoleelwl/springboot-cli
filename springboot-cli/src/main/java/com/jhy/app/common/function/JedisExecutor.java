package com.jhy.app.common.function;


import com.jhy.app.common.exceptions.RedisConnectException;

@FunctionalInterface
public interface JedisExecutor<T, R> {
    R excute(T t) throws RedisConnectException;
}
