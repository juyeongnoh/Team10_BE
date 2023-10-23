package bdbe.bdbd.file;

import bdbe.bdbd._core.errors.utils.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
public class FileService {

    private final FileUploadUtil fileUploadUtil;

    public FileService(FileUploadUtil fileUploadUtil) {
        this.fileUploadUtil = fileUploadUtil;
    }

    public FileResponse.SimpleFileResponseDTO uploadFile(MultipartFile multipartFile, Long carwashId) throws Exception {
        return fileUploadUtil.uploadFile(multipartFile, carwashId);
    }

    public List<FileResponse.SimpleFileResponseDTO> uploadFiles(MultipartFile[] multipartFile, Long carwashId) throws Exception {
        return fileUploadUtil.uploadFiles(multipartFile, carwashId);
    }

}
