package bdbe.bdbd.file;

import bdbe.bdbd._core.errors.exception.NotFoundError;
import bdbe.bdbd.carwash.Carwash;
import bdbe.bdbd.carwash.CarwashJPARepository;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
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
import java.time.LocalDateTime;

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

    public FileResponse.SimpleFileResponseDTO uploadFile(MultipartFile multipartFile, Long carwashId) throws Exception {
        Carwash carwash = carwashRepository.findById(carwashId)
                .orElseThrow(() -> new NotFoundError("Carwash not found"));

        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile);

        try {
            uploadFileToS3Bucket(fileName, file);
        } catch (AmazonServiceException e) {
            logger.error("AmazonServiceException: Error Message:    {}", e.getErrorMessage());
            logger.error("HTTP Status Code: {}", e.getStatusCode());
            logger.error("AWS Error Code:   {}", e.getErrorCode());
            logger.error("Error Type:       {}", e.getErrorType());
            logger.error("Request ID:       {}", e.getRequestId());
            throw e;
        } catch (SdkClientException e) {
            logger.error("SdkClientException: {}", e.getMessage());
            throw e;
        }

        FileRequest.FileSaveRequestDTO fileSaveRequestDTO = new FileRequest.FileSaveRequestDTO();
        fileSaveRequestDTO.setName(fileName);
        fileSaveRequestDTO.setUrl(amazonS3.getUrl(bucketName, fileName).toExternalForm());
        fileSaveRequestDTO.setPath(file.getPath());
        fileSaveRequestDTO.setUploadedAt(LocalDateTime.now());
        fileSaveRequestDTO.setCarwash(carwash);

        bdbe.bdbd.file.File newFile = fileSaveRequestDTO.toEntity();
        newFile = fileRepository.save(newFile);

        file.delete();

        return new FileResponse.SimpleFileResponseDTO(
                newFile.getId(),
                newFile.getName(),
                newFile.getUrl(),
                newFile.getPath(),
                newFile.getUploadedAt(),
                new FileResponse.SimpleCarwashDTO(newFile.getCarwash().getId())
        );
    }

    private String generateFileName(MultipartFile multiPart) {
        return multiPart.getOriginalFilename().replace(" ", "_");
    }

    private File convertMultiPartToFile(MultipartFile multipart) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipart.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipart.getBytes());
        }
        return convFile;
    }

    private void uploadFileToS3Bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file));
        logger.info("File uploaded to S3 bucket: {}", fileName);
    }
}
