package com.example.token.provider;

import com.example.token.domain.Token;

public interface TokenProvider <T extends Token<?>, G extends TokenGenerateRequest> {
    T generate(G request);
}
