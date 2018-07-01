package com.miaosha.config.threadlocal;

import com.miaosha.entity.MiaoshaUserEntity;

/**
 * ThreadLocal 是线程的局部变量
 * ThreadLocal为解决多线程程序的并发问题提供了一种新的思路
 * 本地方法传参
 */
public class UserContext {
    private static ThreadLocal<MiaoshaUserEntity> userHolder = new ThreadLocal<>();

    public static void setUser(MiaoshaUserEntity user) {
        userHolder.set(user);
    }

    public static MiaoshaUserEntity getUser() {
        return userHolder.get();
    }
}
