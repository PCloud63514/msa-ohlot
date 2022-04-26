package com.example.ohlot.api;

import com.example.ohlot.service.GoodWordAddRequest;
import com.example.ohlot.service.GoodWordAddResponse;
import com.example.ohlot.service.GoodWordGetResponse;
import com.example.ohlot.service.GoodWordService;
import com.example.ohlot.service.GoodWordUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("good-words")
@RestController
public class GoodWordApi {
    private final GoodWordService goodWordService;

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public GoodWordAddResponse addGoodWord(@RequestBody GoodWordAddRequest request) {
        return goodWordService.addGoodWord(request);
    }

    @GetMapping
    public List<GoodWordGetResponse> getGoodWords() {
        return goodWordService.getGoodWords();
    }

    @PatchMapping("{id}")
    public void updateGoodWord(@PathVariable UUID id, @RequestParam(name = "content") String content) {
        goodWordService.updateGoodWord(new GoodWordUpdateRequest(id, content));
    }

    @DeleteMapping("{id}")
    public void deleteGoodWord(@PathVariable(name = "id") UUID id) {
        goodWordService.deleteGoodWord(id);
    }
}
