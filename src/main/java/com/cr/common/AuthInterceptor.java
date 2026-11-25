package com.cr.common;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录与权限拦截器：
 * - 拦截 /api/** 下需要登录的接口
 * - /api/admin/** 仅管理员可访问
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        TokenStore.SessionUser user = TokenStore.get(token);
        if (user == null) {
            throw new UnauthorizedException("请先登录");
        }
        if (request.getRequestURI().startsWith("/api/admin/")
                && !"ADMIN".equals(user.getRole())) {
            throw new UnauthorizedException("无管理员权限");
        }
        request.setAttribute("userId", user.getUserId());
        request.setAttribute("role", user.getRole());
        request.setAttribute("token", token);
        return true;
    }
}
