package io.account.management.web;

import io.account.management.App;
import io.account.management.domain.posts.Posts;
import io.account.management.domain.posts.PostsRepository;
import io.account.management.web.dto.PostsSaveRequestDto;
import io.account.management.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록() throws Exception {
        // given
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("dkstpgud@gmail.com")
                .build();

        String url = "http://localhost:" + port + "/api/" + App.VERSION + "/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getHeaders().getLocation().toString()).startsWith(url);

        List<Posts> posts = postsRepository.findAll();
        Assertions.assertThat(posts.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(posts.get(0).getContent()).isEqualTo(content);

    }

    // Transactional Annotation 사용시 데이터가 save 되지만 실제 데이터에는 없기 때문에 select 시 404 에러가 발생하게 됨.
    // 따라서 Transactional 을 쓰지 않고 @AfterEach 를 이용함
    @Test
    public void Posts_수정() throws Exception {
        // given
        Posts savedPosts = postsRepository.save(
                Posts.builder()
                        .title("title")
                        .content("content")
                        .author("dkstpgud@gmail.com")
                        .build());

        Long updateId = savedPosts.getId();
        String title = "new title";
        String content = "new content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        String url = "http://localhost:" + port + "/api/" + App.VERSION + "/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);



        List<Posts> all = postsRepository.findAll();

        System.out.println("this is new " + all.get(0).toString());
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(all.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void 포스트_삭제() throws Exception {
        // given
        // given
        Posts savedPosts = postsRepository.save(
                Posts.builder()
                        .title("title")
                        .content("content")
                        .author("dkstpgud@gmail.com")
                        .build());

        Long deleteId = savedPosts.getId();
        String url = "http://localhost:" + port + "/api/" + App.VERSION + "/posts/" + deleteId;

        // when
        restTemplate.delete(url);

        // then
        List<Posts> all = postsRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(0);

        Optional<Posts> deletedPosts = postsRepository.findById(deleteId);

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, deletedPosts::get);
    }


}