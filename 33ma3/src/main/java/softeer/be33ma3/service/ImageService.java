package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.dto.response.ImageListDto;
import softeer.be33ma3.repository.ImageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    @Transactional
    public ImageListDto saveImage(List<MultipartFile> multipartFiles){
        //TODO: 로그인 구현하고 존재하는 회원인지 확인하는 로직 추가하기

        List<Image> images = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = s3Service.uploadFile(file);
            images.add(Image.createImage(s3Service.getFileUrl(fileName)));
        }

        List<Image> savedImages = imageRepository.saveAll(images);
        List<Long> savedImageIds = savedImages.stream()
                .map(Image::getImageId)
                .toList();

        return new ImageListDto(savedImageIds);
    }
}
