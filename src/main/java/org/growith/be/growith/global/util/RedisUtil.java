package org.growith.be.growith.global.util;

import java.time.Duration;

public interface RedisUtil {
    <T> void set(String key, T value, Duration duration);
    <T> T get(String key, Class<T> clz);
    void delete(String key);
    boolean has(String key);
}
