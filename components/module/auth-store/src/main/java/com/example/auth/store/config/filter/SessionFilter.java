package com.example.auth.store.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class SessionFilter implements WebFilter {
    private final PathPattern basePattern;
    private final List<PathPattern> excludePatterns;

    public SessionFilter(@Value("${filter.exclude-list:/auth/crypt}") List<String> excludes) {
        basePattern = new PathPatternParser().parse("/auth/**");
        excludePatterns = new ArrayList<>();
        excludes.forEach(s -> excludePatterns.add(new PathPatternParser().parse(s)));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Object address = request.getHeaders().getFirst("X-Forwarded-For") == null ?
                request.getRemoteAddress() : request.getHeaders().getFirst("X-Forwarded-For");
        log.info("{} : {} {}", address, request.getMethod(), request.getURI().toString());

        if (basePattern.matches(request.getPath().pathWithinApplication()) &&
                excludePatterns.stream().noneMatch(pathPattern -> pathPattern.matches(request.getPath().pathWithinApplication()))) {
            return exchange.getSession()
                    .doOnNext(webSession -> Optional.ofNullable(webSession.getAttribute("crypt"))
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not found session."))
                    ).then(chain.filter(exchange));
        }
        return chain.filter(exchange);
    }
}
