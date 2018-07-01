package com.miaosha.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.miaosha.common.CodeMsg;
import com.miaosha.common.Result;
import com.miaosha.config.annotation.AccessLimit;
import com.miaosha.config.redis.AccessKey;
import com.miaosha.config.redis.RedisService;
import com.miaosha.config.threadlocal.UserContext;
import com.miaosha.entity.MiaoshaUserEntity;
import com.miaosha.service.MiaoshaUserService;
import com.miaosha.service.impl.MiaoshaUserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 控制RequestMapping请求访问次数的拦截器
 *
 * 针对@AccessLimit注解
 * @see com.miaosha.config.annotation.AccessLimit
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private RedisService redisService;

    /**
     * 方法执行前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {

            // 根据request中token获取user
            MiaoshaUserEntity miaoshaUser = getUser(response, request);
            UserContext.setUser(miaoshaUser);   // 将user放到本地线程ThreadLocal

            HandlerMethod hm = (HandlerMethod)handler;  // 从handler中取请求的方法requestMapping信息
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);    // 取出AccessLimit注解
            if (accessLimit == null) {
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            String key = request.getRequestURI();
            if (needLogin) {
                if (miaoshaUser == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                Long userId = miaoshaUser.getId();
                key += "_" + userId;
            } else {
                // do nothing
            }

            Integer count = redisService.get(AccessKey.ACCESS, key, Integer.class);
            if (count == null) {
                redisService.set(AccessKey.ACCESS.withExpire(seconds), key, 1);
            } else if (count < maxCount) {
                redisService.incr(AccessKey.ACCESS, key);
            } else {
                render(response, CodeMsg.ACCESS_LIMIT);
                return false;
            }

        }

        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws IOException {
        response.setContentType("application/json");
        ServletOutputStream outputStream = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(sessionError));
        outputStream.write(str.getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }

    private MiaoshaUserEntity getUser(HttpServletResponse response, HttpServletRequest request) {
        String paramToken = request.getParameter(MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserServiceImpl.COOKIE_NAME_TOKEN);

        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }

        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;

        return miaoshaUserService.getByToken(token, response);
    }

    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieNameToken)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
