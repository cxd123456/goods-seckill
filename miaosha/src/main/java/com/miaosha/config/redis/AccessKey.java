package com.miaosha.config.redis;

public class AccessKey extends BaseRedisKeyPrefix{

    public static final int REQUEST_COUNT = 5;
    public static final AccessKey ACCESS = new AccessKey(5, "access");

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
