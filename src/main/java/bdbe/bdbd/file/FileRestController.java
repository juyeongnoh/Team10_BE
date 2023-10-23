package bdbe.bdbd.file;

import bdbe.bdbd._core.errors.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileRestController {

//    private final FileService fileService;

    @Autowired
    private FileUploadUtil fileUploadUtil;


//    @Autowired
//    public FileRestController(FileService fileService) {
//        this.fileService = fileService;
//    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("carwashId") Long carwashId) throws Exception {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file");
        }
        FileResponse.SimpleFileResponseDTO response = fileUploadUtil.uploadFile(file, carwashId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/uploadMultiple")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                 @RequestParam("carwashId") Long carwashId) throws Exception {
        if (files == null || files.length == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No files provided");
        }

        List<FileResponse.SimpleFileResponseDTO> responses = fileUploadUtil.uploadFiles(files, carwashId);
        return ResponseEntity.ok(responses);
    }

}
