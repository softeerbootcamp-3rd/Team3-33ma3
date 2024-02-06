package softeer.be33ma3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody @Valid PostCreateDto postCreateDto){
        postService.createPost(postCreateDto);

        return ResponseEntity.ok().body(SingleResponse.success("게시글 작성 성공"));
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPost(@PathVariable("post_id") Long postId) {
        List<Object> getPostResult = postService.getPost(postId);
        return ResponseEntity.ok(DataResponse.success("게시글 조회 완료", getPostResult));
    }
}
