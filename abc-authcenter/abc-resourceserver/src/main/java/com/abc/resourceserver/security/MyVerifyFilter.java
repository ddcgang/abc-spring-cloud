package com.abc.resourceserver.security;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyVerifyFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> urls=new ArrayList<>();
        urls.add("/test/list");
        if (!urls.contains(httpServletRequest.getServletPath())) {
            //验证码核验
            System.out.println("过滤器:" + httpServletRequest.getServletPath());
            Map<String, Object> map = new HashMap<>();
            map.put("error", "access_denied");
            map.put("error_description", "权限不足");
            httpServletResponse.setStatus(401);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSONObject.toJSONString(map));
            httpServletResponse.getWriter().close();
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
