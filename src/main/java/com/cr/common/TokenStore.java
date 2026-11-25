package com.cr.common;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存 Token 存储：登录成功后生成 Token，拦截器据此识别当前用户
 */
public class TokenStore {

    public static class SessionUser {
        private final Long userId;
        private final String account;
        private final String role;

        public SessionUser(Long userId, String account, String role) {
            this.userId = userId;
            this.account = account;
            this.role = role;
        }

        public Long getUserId() {
            return userId;
        }

        public String getAccount() {
            return account;
        }

        public String getRole() {
            return role;
        }
    }

    private static final Map<String, SessionUser> TOKENS = new ConcurrentHashMap<>();

    public static String create(Long userId, String account, String role) {
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKENS.put(token, new SessionUser(userId, account, role));
        return token;
    }

    public static SessionUser get(String token) {
        return token == null ? null : TOKENS.get(token);
    }

    public static void remove(String token) {
        if (token != null) {
            TOKENS.remove(token);
        }
    }
}
