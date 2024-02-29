package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.PostCreateDto;
import softeer.be33ma3.dto.response.PostThumbnailDto;
import softeer.be33ma3.jwt.CurrentUser;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.PostService;

import java.util.List;


@Tag(name = "Post", description = "게시글 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 센터", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "게시글 목록 조회", description = "게시글 목록 조회 메서드 입니다.")
    @GetMapping
    public ResponseEntity<?> showPosts(@RequestParam(name = "mine", required = false) Boolean mine,
                                       @RequestParam(name = "done", required = false) Boolean done,
                                       @RequestParam(name = "region", required = false) String region,
                                       @RequestParam(name = "repair", required = false) String repair,
                                       @RequestParam(name = "tuneUp", required = false) String tuneUp,
                                       @Schema(hidden = true) @CurrentUser Member member) {
        List<PostThumbnailDto> postThumbnailDtos = postService.showPosts(mine, done, region, repair, tuneUp, member);
        return ResponseEntity.ok().body(DataResponse.success("게시글 목록 조회 성공", postThumbnailDtos));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 작성 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "401", description = "센터는 글 작성이 불가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 회원" + "<br>존재하지 않는 구", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "게시글 작성", description = "게시글 작성 메서드 입니다.")
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createPost(@Schema(hidden = true) @CurrentUser Member member, @RequestPart(name = "images", required = false) List<MultipartFile> images, @Valid @RequestPart(name = "request") PostCreateDto postCreateDto){
        Long postId = postService.createPost(member,postCreateDto, images);

        return ResponseEntity.ok().body(DataResponse.success("게시글 작성 성공", postId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 완료", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "경매 중인 게시글을 보려면 로그인해주세요.", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "게시글 조회", description = "게시글 조회 메서드 입니다.")
    @Parameter(name = "post_id", description = "조회할 게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/one/{post_id}")
    public ResponseEntity<?> showPost(@PathVariable("post_id") Long postId,
                                      @Schema(hidden = true) @CurrentUser Member member) {
        Object result = postService.showPost(postId, member);
        return ResponseEntity.ok(DataResponse.success("게시글 조회 완료", result));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "작성자만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 센터", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "경매 시작 전에만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "게시글 수정", description = "게시글 수정 메서드 입니다.")
    @Parameter(name = "post_id", description = "수정할 게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @PutMapping("/{post_id}")
    public ResponseEntity<?> editPost(@Schema(hidden = true) @CurrentUser Member member,  @PathVariable("post_id") Long postId, @RequestBody @Valid PostCreateDto postCreateDto){
        postService.editPost(member, postId, postCreateDto);

        return ResponseEntity.ok().body(SingleResponse.success("게시글 수정 성공"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "작성자만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 센터", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "경매 시작 전에만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 메서드 입니다.")
    @Parameter(name = "post_id", description = "삭제할 게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @DeleteMapping("/{post_id}")
    public ResponseEntity<?> deletePost(@Schema(hidden = true) @CurrentUser Member member, @PathVariable("post_id") Long postId){
        postService.deletePost(member, postId);

        return ResponseEntity.ok().body(SingleResponse.success("게시글 삭제 성공"));
    }
}
