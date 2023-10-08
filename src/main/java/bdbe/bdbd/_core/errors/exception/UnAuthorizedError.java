package bdbe.bdbd._core.errors.exception;



import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 401 (Unauthorized) : 권한 없음
 * 인증이 안되거나 액세스 권한 없을 때 발생합니다.
 */
@Getter
public class UnAuthorizedError extends RuntimeException {
    public UnAuthorizedError(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public HttpStatus status(){
        return HttpStatus.UNAUTHORIZED;
    }
}