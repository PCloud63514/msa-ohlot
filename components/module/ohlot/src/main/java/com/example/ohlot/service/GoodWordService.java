package com.example.ohlot.service;

import java.util.List;
import java.util.UUID;

public interface GoodWordService {
    GoodWordAddResponse addGoodWord(GoodWordAddRequest request);
    List<GoodWordGetResponse> getGoodWords();
    void updateGoodWord(GoodWordUpdateRequest request);
    void deleteGoodWord(UUID id);
}
