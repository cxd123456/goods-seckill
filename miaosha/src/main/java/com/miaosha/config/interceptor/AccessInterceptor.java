package com.miaosha.config.interceptor;

import com.miaosha.common.CodeMsg;
import com.miaosha.config.annotation.AccessLimit;
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
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

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

            MiaoshaUserEntity miaoshaUser = getUser(response, request);
            UserContext.setUser(miaoshaUser);

            HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            if (needLogin) {
                if (miaoshaUser == null) {
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
            }



        }

        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
//        outputStream.write();
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
