package com.example.whatsApp_service.config.SecurityConfig;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private LoginFeign feign;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.getToken(request);
        log.info("Validating token");
        if(token !=null){
            try {
                feign.valid(token);
                authenticateUser();
            } catch (Exception e) {
                log.warn("Unaccredited user");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unaccredited user", e);
            }

        }
        filterChain.doFilter(request,response);
    }

    private void authenticateUser() {
        var userAuth = new UsernamePasswordAuthenticationToken(null,null,null);
        SecurityContextHolder.getContext().setAuthentication(userAuth);
    }

    private String getToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if(token != null){
            return token.replace("Bearer ","");
        }
        return null;
    }
}
