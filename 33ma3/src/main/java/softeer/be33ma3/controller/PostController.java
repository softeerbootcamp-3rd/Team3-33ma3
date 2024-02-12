package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.jwt.CurrentUser;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.PostService;

import java.util.List;


@Tag(name = "Post", description = "게시글 관련 api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시글" + "<br>존재하지 않는 회원" + "<br>존재하지 않는 구",
                    content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "게시글 작성", description = "게시글 작성 메서드 입니다.")
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createPost(@Schema(hidden = true) @CurrentUser Member member, @RequestPart(name = "images") List<MultipartFile> images, @RequestPart(name = "request") PostCreateDto postCreateDto){
        postService.createPost(member,postCreateDto, images);

        return ResponseEntity.ok().body(SingleResponse.success("게시글 작성 성공"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 게시글" + "<br>존재하지 않는 서비스 센터",
                    content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "게시글 조회", description = "게시글 조회 메서드 입니다.")
    @Parameter(name = "post_id", description = "조회할 게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/{post_id}")
    public ResponseEntity<?> showPost(@PathVariable("post_id") Long postId) {
        List<Object> getPostResult = postService.showPost(postId);
        return ResponseEntity.ok(DataResponse.success("게시글 조회 완료", getPostResult));
    }
}
