package com.chun.back.interceptors;

import com.chun.back.pojo.Result;
import com.chun.back.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorization = request.getHeader("Authorization");
        if (authorization == null || authorization.isBlank()) {
            response.setStatus(401);
            response.getWriter().write(mapper.writeValueAsString(Result.error("需要登录")));
            return false;
        }

        String token = authorization;
        if (authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            request.setAttribute("claims", claims);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.getWriter().write(mapper.writeValueAsString(Result.error("需要登录")));
            return false;
        }
    }
}
