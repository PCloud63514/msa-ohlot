package com.example.ohlot.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GoodWordRepository extends JpaRepository<GoodWord, UUID> {

}
