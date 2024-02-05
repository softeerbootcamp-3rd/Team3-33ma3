package softeer.be33ma3.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.PostService;

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
}
