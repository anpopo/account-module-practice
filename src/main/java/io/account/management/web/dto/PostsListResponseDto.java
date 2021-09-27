package io.account.management.web.dto;

import io.account.management.domain.posts.Posts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsListResponseDto {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedAt;

    public PostsListResponseDto(Posts e) {
        this.id = e.getId();
        this.title = e.getTitle();
        this.author = e.getAuthor();
        this.modifiedAt = e.getModifiedAt();
    }
}
