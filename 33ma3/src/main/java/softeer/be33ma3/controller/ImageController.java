package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.dto.response.ImageListDto;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.response.SingleResponse;
import softeer.be33ma3.service.ImageService;

import java.util.List;

@Tag(name = "Image", description = "이미지 저장 api")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 저장 성공", content = @Content(schema = @Schema(implementation = SingleResponse.class))),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 회원", content = @Content(schema = @Schema(implementation = SingleResponse.class)))
    })
    @Operation(summary = "차 이미지 저장", description = "이미지 저장 메서드 입니다.")
    @PostMapping(value ="/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveImages(@Parameter(description = "이미지 리스트") @RequestPart("images") List<MultipartFile> images){
        ImageListDto imageListDto = imageService.saveImage(images);

        return ResponseEntity.ok().body(DataResponse.success("이미지 저장 성공", imageListDto));
    }
}
