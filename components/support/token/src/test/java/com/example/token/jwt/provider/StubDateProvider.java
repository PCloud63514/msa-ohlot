package com.example.token.jwt.provider;

import com.example.token.provider.DateProvider;
import java.util.Date;

public class StubDateProvider extends DateProvider {
    public Date date = new Date();
    @Override
    public Date now() {
        return date;
    }
}
