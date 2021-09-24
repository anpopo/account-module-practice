package io.account.management.web;

import io.account.management.App;
import io.account.management.service.posts.PostsService;
import io.account.management.web.dto.PostsResponseDto;
import io.account.management.web.dto.PostsSaveRequestDto;
import io.account.management.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RequiredArgsConstructor
@RequestMapping("/api/" + App.VERSION)
@RestController
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/posts")
    public ResponseEntity<URI> savePosts(@RequestBody PostsSaveRequestDto requestDto) {

        Long savedPosts = postsService.save(requestDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPosts)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Long> updatePosts(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return ResponseEntity.ok().body(postsService.update(id, requestDto));
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostsResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(postsService.findById(id));
    }

}

