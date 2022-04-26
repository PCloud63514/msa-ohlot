package com.example.ohlot.service;

import com.example.ohlot.domain.GoodWord;
import com.example.ohlot.domain.GoodWordFixtures;
import com.example.ohlot.domain.SpyGoodWordRepository;
import com.example.ohlot.provider.StubUUIDProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GoodWordServiceImplTest {
    GoodWordServiceImpl goodWordService;
    StubUUIDProvider stubUUIDProvider;
    SpyGoodWordRepository spyGoodWordRepository;

    @BeforeEach
    void setUp() {
        stubUUIDProvider = new StubUUIDProvider();
        spyGoodWordRepository = new SpyGoodWordRepository();
        goodWordService = new GoodWordServiceImpl(stubUUIDProvider, spyGoodWordRepository);
    }

    @Test
    void addGoodWord_returnValue() {
        stubUUIDProvider.randomUUID_returnValue = UUID.randomUUID();
        GoodWordAddRequest givenRequest = new GoodWordAddRequest("content2");

        GoodWordAddResponse response = goodWordService.addGoodWord(givenRequest);

        assertThat(response.getId()).isEqualTo(stubUUIDProvider.randomUUID().toString());
        assertThat(response.getContent()).isEqualTo(response.getContent());
    }

    @Test
    void addGoodWord_passesEntityToRepository() {
        stubUUIDProvider.randomUUID_returnValue = UUID.randomUUID();
        GoodWordAddRequest givenRequest = new GoodWordAddRequest("content2");

        goodWordService.addGoodWord(givenRequest);

        assertThat(spyGoodWordRepository.save_argument.getId()).isEqualTo(stubUUIDProvider.randomUUID());
        assertThat(spyGoodWordRepository.save_argument.getContent()).isEqualTo(givenRequest.getContent());
    }

    @Test
    void addGoodWord_throwExceptionByExistsByIdOfRepository() {
        GoodWordAddRequest givenRequest = new GoodWordAddRequest("content2");
        spyGoodWordRepository.existsById_returnValue = true;

        assertThrows(RuntimeException.class, ()->goodWordService.addGoodWord(givenRequest));
    }

    @Test
    void getGoodWords_returnValue() {
        List<GoodWord> givenResponse = new ArrayList<>();
        givenResponse.add(GoodWordFixtures.anGoodWord().build());

        spyGoodWordRepository.findAll_returnValue = givenResponse;

        List<GoodWordGetResponse> goodWords = goodWordService.getGoodWords();

        assertThat(goodWords).isNotNull();
        assertThat(givenResponse.get(0).getId().toString()).isEqualTo(goodWords.get(0).getId());
        assertThat(givenResponse.get(0).getContent()).isEqualTo(goodWords.get(0).getContent());
        assertThat(givenResponse.get(0).getCreateAt()).isEqualTo(goodWords.get(0).getCreateAt());
        assertThat(givenResponse.get(0).getUpdateAt()).isEqualTo(goodWords.get(0).getUpdateAt());
    }

    @Test
    void updateGoodWord_wasMethodToRepository() {
        UUID givenUUID = UUID.randomUUID();
        String givenBeforeContent = "beforeContent";
        String givenAfterContent = "afterContent";
        GoodWordUpdateRequest givenRequest = new GoodWordUpdateRequest(givenUUID, givenAfterContent);
        spyGoodWordRepository.findById_returnValue = GoodWord.create(givenUUID, givenBeforeContent);

        goodWordService.updateGoodWord(givenRequest);

        assertThat(spyGoodWordRepository.was_findById).isTrue();
        assertThat(spyGoodWordRepository.was_save).isTrue();
        assertThat(spyGoodWordRepository.save_argument.getContent()).isEqualTo(givenAfterContent);
    }

    @Test
    void updateGoodWord_throwExceptionByFromStringOfUUID() {
        GoodWordUpdateRequest givenRequest = new GoodWordUpdateRequest(UUID.randomUUID(), "content");
        Assertions.assertThrows(RuntimeException.class, ()->goodWordService.updateGoodWord(givenRequest));
    }

    @Test
    void updateGoodWord_throwException_By_FindById_Of_Repository() {
        GoodWordUpdateRequest givenRequest = new GoodWordUpdateRequest(UUID.randomUUID(), "content");
        Assertions.assertThrows(RuntimeException.class, ()->goodWordService.updateGoodWord(givenRequest));
    }

    @Test
    void deleteGoodWord_passesIdToRepository() {
        UUID givenUUID = UUID.randomUUID();
        goodWordService.deleteGoodWord(givenUUID);

        assertThat(spyGoodWordRepository.deleteById_argument).isEqualTo(givenUUID);
    }
}