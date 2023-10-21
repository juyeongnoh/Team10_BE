package bdbe.bdbd.file;

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
    private final CarwashJPARepository carwashRepository;

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public FileService(AmazonS3 amazonS3, FileJPARepository fileRepository, CarwashJPARepository carwashRepository) {
        this.amazonS3 = amazonS3;
        this.fileRepository = fileRepository;
        this.carwashRepository = carwashRepository;
    }

    public FileResponse.SimpleFileResponseDTO uploadFile(MultipartFile multipartFile, Long carwashId) throws IOException {
        Carwash carwash = carwashRepository.findById(carwashId)
                .orElseThrow(() -> new RuntimeException("Carwash not found"));

        File file = convertMultiPartToFile(multipartFile);
        String fileName = multipartFile.getOriginalFilename();
        URL presignedUrl = createPresignedUrl(fileName);

        uploadFileToS3Bucket(fileName, file);

        FileRequest.FileSaveRequestDTO fileSaveRequestDTO = new FileRequest.FileSaveRequestDTO();
        fileSaveRequestDTO.setName(fileName);
        fileSaveRequestDTO.setUrl(presignedUrl.toString());
        fileSaveRequestDTO.setPath(file.getPath());
        fileSaveRequestDTO.setUploadedAt(LocalDateTime.now());
        fileSaveRequestDTO.setCarwash(carwash);

        bdbe.bdbd.file.File newFile = fileSaveRequestDTO.toEntity();
        newFile = fileRepository.save(newFile);

        file.delete(); // 로컬에 저장된 파일 삭제

        return new FileResponse.SimpleFileResponseDTO(
                newFile.getId(),
                newFile.getName(),
                newFile.getUrl(),
                newFile.getPath(),
                newFile.getUploadedAt(),
                new FileResponse.SimpleCarwashDTO(newFile.getCarwash().getId())
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
        expTimeMillis += 1000 * 60 * 60; // 1 hour
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
