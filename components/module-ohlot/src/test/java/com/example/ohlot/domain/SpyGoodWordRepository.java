package com.example.ohlot.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class SpyGoodWordRepository implements GoodWordRepository {

    public GoodWord save_argument;
    public boolean existsById_returnValue = false;
    public List<GoodWord> findAll_returnValue;
    public boolean was_findById;
    public boolean was_save;
    public GoodWord findById_returnValue;
    public UUID deleteById_argument;

    @Override
    public List<GoodWord> findAll() {
        return findAll_returnValue;
    }

    @Override
    public List<GoodWord> findAll(Sort sort) {
        return null;
    }

    @Override
    public List<GoodWord> findAllById(Iterable<UUID> uuids) {
        return null;
    }

    @Override
    public Page<GoodWord> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(UUID uuid) {
        this.deleteById_argument = uuid;
    }


    @Override
    public void delete(GoodWord entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {

    }


    @Override
    public void deleteAll(Iterable<? extends GoodWord> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends GoodWord> S save(S entity) {
        save_argument = entity;
        was_save = true;
        return null;
    }

    @Override
    public <S extends GoodWord> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<GoodWord> findById(UUID uuid) {
        was_findById = true;
        return Optional.ofNullable(findById_returnValue);
    }

    @Override
    public boolean existsById(UUID uuid) {
        return existsById_returnValue;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends GoodWord> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends GoodWord> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<GoodWord> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<UUID> uuids) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public GoodWord getOne(UUID uuid) {
        return null;
    }

    @Override
    public GoodWord getById(UUID uuid) {
        return null;
    }

    @Override
    public <S extends GoodWord> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends GoodWord> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends GoodWord> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends GoodWord> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends GoodWord> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends GoodWord> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends GoodWord, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}