package com.example.token.jwt.util;

import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * auth service에서 token 읽고 request 객체에 넣는 방법
 * auth 과정을 통해 request에 넣어진 값을 가져오는 방법
 * auth api를 통해 토큰 발급받고 사용자에게 주는 방법
 */
public class JwtUtil {
    public static final String HEADER_TOKEN_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_SYNTAX = "access_token";
    public static final String REFRESH_TOKEN_SYNTAX = "refresh_token";

    public static void injectAuthorization(String accessToken, String refreshToken, HttpServletResponse response) {
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

    public static void reactiveInjectAuthorization(String accessToken, String refreshToken, ServerHttpResponse response) {
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

//    public static ExportToken exportAuthorization(HttpServletRequest request) {
//        String accessToken = Optional.ofNullable(request.getHeader(ACCESS_TOKEN_SYNTAX))
//                .orElseThrow(RuntimeException::new);
//        String refreshToken = Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_SYNTAX))
//                .findFirst().orElseThrow(RuntimeException::new).getValue();
//
//        return new AuthToken(accessToken, refreshToken);
//    }
//
//    public static AuthToken exportReactiveAuthorization(ServerHttpRequest serverHttpRequest) {
//        String accessToken = Optional.ofNullable(serverHttpRequest.getHeaders().getFirst(ACCESS_TOKEN_SYNTAX)).orElseThrow(RuntimeException::new);
//        String refreshToken = Optional.ofNullable(serverHttpRequest.getCookies().getFirst(REFRESH_TOKEN_SYNTAX)).orElseThrow(RuntimeException::new).getValue();
//
//        return new AuthToken(accessToken, refreshToken);
//    }
}
