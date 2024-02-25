package softeer.be33ma3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import softeer.be33ma3.exception.BusinessException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static softeer.be33ma3.exception.ErrorCode.INVALID_FILE;
import static softeer.be33ma3.exception.ErrorCode.UNABLE_TO_CONVERT_FILE;

@RequiredArgsConstructor
@Component
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    //S3로 파일 업로드 하기
    public String uploadFile(MultipartFile file) {
        //파일 이름 생성
        String fileName = createFileName(file.getOriginalFilename());

        //파일 변환
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        //파일 업로드
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicReadWrite));
        } catch (IOException e) {
            throw new BusinessException(UNABLE_TO_CONVERT_FILE);
        }

        return fileName;
    }

    //이미지 link 가져오기
    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //S3에 올라간 이미지 삭제 기능
    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    //파일 이름 생성 : 이름이 같으면 같은 이름인 이미지를 덮어씌우는 문제가 생기기 때문에 이미지 이름을 생성해서 저장하도록 구현
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    //파일 확장자 가져오기
    private String getFileExtension(String originalFileName){
        try{
            return originalFileName.substring(originalFileName.lastIndexOf("."));
        }catch(StringIndexOutOfBoundsException e) {
            throw new BusinessException(INVALID_FILE);
        }
    }
}
