package bdbe.bdbd.file;

import bdbe.bdbd._core.errors.exception.FileStorageException;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;

@Transactional
@Service
public class FileService {

    private final AmazonS3 amazonS3;
    private final FileJPARepository fileRepository;
    private final CarwashJPARepository carwashRepository; // CarwashRepository를 주입 받습니다.

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public FileService(AmazonS3 amazonS3, FileJPARepository fileRepository, CarwashJPARepository carwashRepository) {
        this.amazonS3 = amazonS3;
        this.fileRepository = fileRepository;
        this.carwashRepository = carwashRepository;
    }

    public FileResponse.FileResponseDTO uploadFile(MultipartFile multipartFile, Long carwashId) throws IOException { // carwashId 파라미터 추가
        Carwash carwash = carwashRepository.findById(carwashId)
                .orElseThrow(() -> new RuntimeException("Carwash not found")); // carwashId로 Carwash 엔티티를 조회합니다.

        File file = convertMultiPartToFile(multipartFile);
        String fileName = multipartFile.getOriginalFilename();
        URL presignedUrl = createPresignedUrl(fileName);

        uploadFileToS3Bucket(fileName, file);

        FileRequest.FileSaveRequestDTO fileSaveRequestDTO = new FileRequest.FileSaveRequestDTO();
        fileSaveRequestDTO.setName(fileName);
        fileSaveRequestDTO.setUrl(presignedUrl.toString());
        fileSaveRequestDTO.setPath(file.getPath());
        fileSaveRequestDTO.setUploadedAt(LocalDateTime.now());
        fileSaveRequestDTO.setCarwash(carwash); // Carwash 객체 설정

        bdbe.bdbd.file.File newFile = fileSaveRequestDTO.toEntity();
        newFile = fileRepository.save(newFile);

        file.delete(); // Temp file delete

        return new FileResponse.FileResponseDTO(
                newFile.getId(),
                newFile.getName(),
                newFile.getUrl(),
                newFile.getPath(),
                newFile.getUploadedAt(),
                newFile.getCarwash() // Carwash 정보를 응답에 포함시킵니다.
        );
    }


    private void uploadFileToS3Bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
        logger.info("File uploaded to S3 bucket: {}", fileName);
    }

    private File convertMultiPartToFile(MultipartFile multipart) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipart.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipart.getBytes());
        } catch (IOException e) {
            logger.error("Could not convert multipart file to file. Error: {}", e.getMessage());
            throw e;
        }
        return convFile;
    }


    private URL createPresignedUrl(String fileName) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour validity.
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        logger.info("Generated pre-signed URL: {}", url);

        return url;
    }
}
