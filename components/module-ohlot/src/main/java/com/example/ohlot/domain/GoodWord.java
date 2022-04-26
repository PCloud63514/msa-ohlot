package com.example.ohlot.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class GoodWord {
    @Id
    private UUID id;
    private String content;
    @CreatedDate
    @Column(name = "create_at", updatable = false, nullable = false)
    private LocalDateTime createAt;
    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Builder
    public GoodWord(UUID id, String content, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static GoodWord create(UUID id, String content) {
        return GoodWord.builder()
                .id(id)
                .content(content)
                .build();
    }

    public void update(String content) {
        this.content = content;
    }
}

