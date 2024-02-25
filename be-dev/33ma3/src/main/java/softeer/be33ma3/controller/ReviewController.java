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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import softeer.be33ma3.domain.Member;
import softeer.be33ma3.dto.request.ReviewCreateDto;
import softeer.be33ma3.dto.response.ShowCenterReviewsDto;
import softeer.be33ma3.dto.response.ShowAllReviewDto;
import softeer.be33ma3.jwt.CurrentUser;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.ReviewService;

import java.util.List;


@Tag(name = "Review", description = "센터 리뷰 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "센터 리뷰 작성 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "400", description = "이미 리뷰를 작성하였습니다." + "<br>경매가 진행 중입니다", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "작성자만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>낙찰 처리된 센터가 없습니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "post_id", description = "게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @Operation(summary = "센터 리뷰 작성", description = "센터 리뷰 작성 메서드 입니다.")
    @PostMapping("/{post_id}")
    public ResponseEntity<?> createReview(@PathVariable("post_id") Long postId,
                                          @RequestBody @Valid ReviewCreateDto reviewCreateDto,
                                          @Schema(hidden = true) @CurrentUser Member member) {
        Long reviewId = reviewService.createReview(postId, reviewCreateDto, member);
        return ResponseEntity.ok().body(DataResponse.success("센터 리뷰 작성 성공", reviewId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "센터 리뷰 삭제 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "작성자만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리뷰", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "review_id", description = "리뷰 id", required = true, example = "1", in = ParameterIn.PATH)
    @Operation(summary = "센터 리뷰 삭제", description = "센터 리뷰 삭제 메서드 입니다.")
    @DeleteMapping("/{review_id}")
    public ResponseEntity<?> deleteReview(@PathVariable("review_id") Long reviewId,
                                          @Schema(hidden = true) @CurrentUser Member member) {
        reviewService.deleteReview(reviewId, member);
        return ResponseEntity.ok().body(SingleResponse.success("센터 리뷰 삭제 성공"));
    }

    @ApiResponse(responseCode = "200", description = "전체 리뷰 조회 성공", content = @Content(schema = @Schema(implementation = DataResponse.class)))
    @Operation(summary = "모든 센터 리뷰 조회", description = "모든 센터 리뷰 조회 메서드 입니다.")
    @GetMapping
    public ResponseEntity<?> showAllReview(){
        List<ShowAllReviewDto> showAllReviewDtos = reviewService.showAllReview();

        return ResponseEntity.ok().body(DataResponse.success("전체 리뷰 조회 성공", showAllReviewDtos));
    }
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "센터 리뷰 조회 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 센터", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "센터 리뷰 조회", description = "센터 리뷰 조회 메서드 입니다.")
    @Parameter(name = "center_id", description = "center id", required = true, example = "1", in = ParameterIn.PATH)
    @GetMapping("/{center_id}")
    public ResponseEntity<?> showOneCenterReview(@PathVariable("center_id") Long centerId){
        ShowCenterReviewsDto showCenterReviewsDtos = reviewService.showOneCenterReviews(centerId);

        return ResponseEntity.ok().body(DataResponse.success("센터 리뷰 조회 성공", showCenterReviewsDtos));
    }
}
