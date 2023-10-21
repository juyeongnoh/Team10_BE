package bdbe.bdbd._core.errors.exception;

import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 파일 저장에 대한 오류에 대해 발생하는 예외입니다.
 **/

@Getter
public class FileStorageException extends RuntimeException {
    private final HttpStatus status;

    public FileStorageException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public FileStorageException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), status);
    }
}
