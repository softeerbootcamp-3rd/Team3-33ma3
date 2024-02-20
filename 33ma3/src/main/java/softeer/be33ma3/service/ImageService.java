package softeer.be33ma3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.domain.Image;
import softeer.be33ma3.repository.ImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    @Transactional
    public List<Image> saveImages(List<MultipartFile> multipartFiles){
        return multipartFiles.stream()
                .map(this::saveImage)
                .toList();
    }

    @Transactional
    public Image saveImage(MultipartFile file) {
        String fileName = s3Service.uploadFile(file);
        return imageRepository.save(Image.createImage(s3Service.getFileUrl(fileName), fileName));
    }

    @Transactional
    public void deleteImage(List<Image> images){
        List<Long> imageIds = images.stream()
                .map(Image::getImageId)
                .toList();

        List<String> fileNames = imageRepository.findFileNamesByImageIds(imageIds);

        for (String fileName : fileNames) {
            s3Service.deleteFile(fileName);
        }
    }
}
