package com.example.whatsApp_service.config.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class GatewayFilter extends OncePerRequestFilter {
    private static final List<String> PUBLIC_URIS = List.of("/public");
    private static final String TRUSTED_PROXY_IP = "172.27.64.1";
    private static final String TRUSTED_ORIGIN = "http://172.27.64.1:8082";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("request validation");
        String requestURI = request.getRequestURI();
        if (isPublicUri(requestURI) ||isTrustedForwardedHeader(request)||isTrustedOrigin(request)) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        }
    }
    private boolean isPublicUri(String uri) {
        return PUBLIC_URIS.stream().anyMatch(uri::startsWith);
    }
    private boolean isTrustedForwardedHeader(HttpServletRequest request) {
        String forwardedHeader = request.getHeader("X-Forwarded-For");
        if (forwardedHeader != null) {
            return forwardedHeader.contains(TRUSTED_PROXY_IP);
        }
        return false;
    }
    private boolean isTrustedOrigin(HttpServletRequest request) {

        String originHeader = request.getHeader("Origin");
        String refererHeader = request.getHeader("Referer");

        //Display information in the log (for debugging)
        log.info("Origin: " + (originHeader != null ? originHeader : "N/A"));
        log.info("Referer: " + (refererHeader != null ? refererHeader : "N/A"));


        return TRUSTED_ORIGIN.equals(originHeader) || (refererHeader != null && refererHeader.startsWith(TRUSTED_ORIGIN));
    }
}
