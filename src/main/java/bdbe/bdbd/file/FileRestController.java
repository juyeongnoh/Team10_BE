package bdbe.bdbd.file;

import bdbe.bdbd._core.errors.exception.NotFoundError;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/files")
public class FileRestController {

    private final FileService fileService;

    @Autowired
    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("carwashId") Long carwashId) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file");
        }

        try {
            FileResponse.SimpleFileResponseDTO response = fileService.uploadFile(file, carwashId);
            return ResponseEntity.ok(response);
        } catch (AmazonServiceException e) {
            String errorMsg = String.format("Could not store file %s by AmazonServiceException: %s",
                    file.getOriginalFilename(), e.getErrorMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
        } catch (SdkClientException e) {
            String errorMsg = String.format("Could not store file %s by SdkClientException: %s",
                    file.getOriginalFilename(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
        } catch (Exception e) {
            String errorMsg = String.format("Could not store file %s. Please try again! Error: %s",
                    file.getOriginalFilename(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
        }
    }
}
