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
import softeer.be33ma3.dto.request.OfferCreateDto;
import softeer.be33ma3.dto.response.OfferDetailDto;
import softeer.be33ma3.jwt.CurrentUser;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.OfferService;

@Tag(name = "Offer", description = "견적 제시 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class OfferController {

    private final OfferService offerService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "견적 불러오기 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 견적", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "post_id", description = "게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @Parameter(name = "offer_id", description = "offer id", required = true, example = "2", in = ParameterIn.PATH)
    @Operation(summary = "견적 댓글 불러오기", description = "견적 댓글 불러오기 메서드 입니다.")
    @GetMapping("/{post_id}/offer/{offer_id}")
    public ResponseEntity<?> showOffer(@PathVariable("post_id") Long postId,
                                       @PathVariable("offer_id") Long offerId) {
        OfferDetailDto offerDetailDto = offerService.showOffer(postId, offerId);
        return ResponseEntity.ok(DataResponse.success("견적 불러오기 성공", offerDetailDto));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "입찰 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 센터", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "완료된 게시글", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "post_id", description = "입찰할 게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @Operation(summary = "입찰 하기", description = "입찰하기 메서드 입니다.")
    @PostMapping("/{post_id}/offer")
    public ResponseEntity<?> createOffer(@PathVariable("post_id") Long postId,
                                         @RequestBody @Valid OfferCreateDto offerCreateDto,
                                         @Schema(hidden = true) @CurrentUser Member member) {
        Long offerId = offerService.createOffer(postId, offerCreateDto, member);
        return ResponseEntity.ok(DataResponse.success("입찰 성공", offerId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 센터" + "<br>존재하지 않는 견적", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "작성자만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "기존 금액보다 낮은 금액으로만 수정 가능합니다."  + "<br>완료된 게시글", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "post_id", description = "게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @Parameter(name = "offer_id", description = "offer id", required = true, example = "2", in = ParameterIn.PATH)
    @Operation(summary = "견적 댓글 수정하기", description = "견적 댓글 수정하기 메서드 입니다.")
    @PatchMapping("/{post_id}/offer/{offer_id}")
    public ResponseEntity<?> updateOffer(@PathVariable("post_id") Long postId,
                                         @PathVariable("offer_id") Long offerId,
                                         @RequestBody @Valid OfferCreateDto offerCreateDto,
                                         @Schema(hidden = true) @CurrentUser Member member) {
        offerService.updateOffer(postId, offerId, offerCreateDto, member);
        return ResponseEntity.ok(DataResponse.success("댓글 수정 성공", offerId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = DataResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 센터" + "<br>존재하지 않는 견적", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "작성자만 가능합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "완료된 게시글" , content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "post_id", description = "게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @Parameter(name = "offer_id", description = "offer id", required = true, example = "2", in = ParameterIn.PATH)
    @Operation(summary = "견적 댓글 삭제하기", description = "견적 댓글 삭제하기 메서드 입니다.")
    @DeleteMapping("/{post_id}/offer/{offer_id}")
    public ResponseEntity<?> deleteOffer(@PathVariable("post_id") Long postId,
                                         @PathVariable("offer_id") Long offerId,
                                         @Schema(hidden = true) @CurrentUser Member member) {
        offerService.deleteOffer(postId, offerId, member);
        return ResponseEntity.ok(DataResponse.success("댓글 삭제 성공", offerId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "낙찰 완료, 게시글 마감", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "401", description = "작성자만 가능합니다." + "<br>로그인이 필요합니다.", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글" + "<br>존재하지 않는 견적", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description =  "완료된 게시글", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Parameter(name = "post_id", description = "게시글 id", required = true, example = "1", in = ParameterIn.PATH)
    @Parameter(name = "offer_id", description = "offer id", required = true, example = "2", in = ParameterIn.PATH)
    @Operation(summary = "견적 댓글 낙찰하기", description = "견적 댓글 낙찰하기 메서드 입니다.")
    @GetMapping("/{post_id}/offer/{offer_id}/select")
    public ResponseEntity<?> selectOffer(@PathVariable("post_id") Long postId,
                                         @PathVariable("offer_id") Long offerId,
                                         @Schema(hidden = true) @CurrentUser Member member) {
        offerService.selectOffer(postId, offerId, member);
        return ResponseEntity.ok(SingleResponse.success("낙찰 완료, 게시글 마감"));
    }
}
