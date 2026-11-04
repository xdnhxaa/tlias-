package com.tlias.interceptor;

import com.tlias.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录校验拦截器：校验请求头中的 token
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 浏览器预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        String token = request.getHeader("token");
        log.debug("拦截器校验 -> uri={}, token={}", uri, token);

        if (token == null || token.isEmpty()) {
            log.warn("请求 {} 未携带令牌，拒绝访问", uri);
            response.setStatus(401);
            return false;
        }

        try {
            Claims claims = JwtUtils.parseJWT(token);
            log.debug("令牌解析成功：{}", claims);
            return true;
        } catch (Exception e) {
            log.warn("令牌无效或已过期：{}", e.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}
