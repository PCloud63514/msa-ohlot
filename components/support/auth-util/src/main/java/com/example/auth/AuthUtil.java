package com.example.auth;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthUtil {
    public static final String HEADER_TOKEN_PREFIX = "Bearer "; //RF6750
    public static final String ACCESS_TOKEN_SYNTAX = "Authorization";
    public static final String REFRESH_TOKEN_SYNTAX = "refresh_token";

    /**
     * web module 기준 토큰 넣기
     */
    public void injectAuthorization(String accessToken, String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) {
            Cookie cookie = new Cookie(REFRESH_TOKEN_SYNTAX, refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setSecure(true);
            response.addCookie(cookie);
        }
        if (accessToken != null) {
            response.addHeader(ACCESS_TOKEN_SYNTAX, accessToken);
        }
    }

    /**
     * flux module 기준 토큰 넣기
     */
    public void reactiveInjectAuthorization(String accessToken, String refreshToken, ServerHttpResponse response) {
        if (refreshToken != null) {
            ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN_SYNTAX, refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .build();
            response.getCookies().clear();
            response.addCookie(responseCookie);
        }
        if (accessToken != null) {
            response.getHeaders().add(ACCESS_TOKEN_SYNTAX, accessToken);
        }
    }

    // web 환경에서 토큰 찾기
    public AuthRequestToken exportAuthorization(HttpServletRequest request) {
        String authorization = request.getHeader(ACCESS_TOKEN_SYNTAX);
        String refreshToken = null;
        if (StringUtils.hasText(authorization) && authorization.startsWith(HEADER_TOKEN_PREFIX)) {
            authorization = authorization.replaceAll(HEADER_TOKEN_PREFIX, "");
        }

        Optional<Cookie> cookieOpt = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_SYNTAX))
                .findFirst();
        if (cookieOpt.isPresent()) {
            refreshToken = cookieOpt.get().getValue();
        }

        return new AuthRequestToken(authorization, refreshToken);
    }

    public AuthRequestToken reactiveExportAuthorization(ServerHttpRequest request) {
        String authorization = null;
        String refreshToken = null;
        String _authorization = request.getHeaders().getFirst(ACCESS_TOKEN_SYNTAX);
        if (StringUtils.hasText(_authorization) && _authorization.startsWith(HEADER_TOKEN_PREFIX)) {
            authorization = _authorization.replaceAll(HEADER_TOKEN_PREFIX, "");
        }

        HttpCookie cookie = request.getCookies().getFirst(REFRESH_TOKEN_SYNTAX);
        if (cookie != null) {
            refreshToken = cookie.getValue();
        }

        return new AuthRequestToken(authorization, refreshToken);
    }
}
