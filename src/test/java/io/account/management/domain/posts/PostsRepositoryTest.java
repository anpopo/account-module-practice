package io.account.management.domain.posts;

import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @Test
    @Transactional
    public void JPA_게시글_저장_불러오기() {
        // given
        String title = "test title";
        String content = "test content";

        postsRepository.save(
                Posts.builder()
                        .title(title)
                        .content(content)
                        .author("dkstpgud@gmail.com")
                        .build()
        );

        // when
        List<Posts> all = postsRepository.findAll();

        // then
        Posts posts = all.get(0);
        Assertions.assertThat(posts.getTitle()).isEqualTo(title);
        Assertions.assertThat(posts.getContent()).isEqualTo(content);
        Assertions.assertThat(posts.getId()).isEqualTo(1L);

    }

    @Test
    @Transactional
    public void Jpa_Audting_사용() {
        // given
        LocalDateTime now = LocalDateTime.now().minusMinutes(10);
        postsRepository.save(
                Posts.builder()
                        .title("title")
                        .content("content")
                        .author("author")
                        .build()
        );
        // when
        List<Posts> all = postsRepository.findAll();

        // then
        Posts posts = all.get(0);

        Assertions.assertThat(posts.getCreatedAt()).isAfter(now);
        Assertions.assertThat(posts.getModifiedAt()).isAfter(now);
    }

}