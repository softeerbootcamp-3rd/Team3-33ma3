package softeer.be33ma3.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.dto.response.ImageListDto;
import softeer.be33ma3.response.DataResponse;
import softeer.be33ma3.service.ImageService;

import java.util.List;

@Tag(name = "Image", description = "이미지 저장 api")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<?> saveImages(@RequestPart("images") List<MultipartFile> images){
        ImageListDto imageListDto = imageService.saveImage(images);

        return ResponseEntity.ok().body(DataResponse.success("이미지 저장 성공", imageListDto));
    }
}
