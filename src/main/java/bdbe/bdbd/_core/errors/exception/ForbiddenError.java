package bdbe.bdbd._core.errors.exception;


import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * HTTP 상태 코드 403 (Forbidden) : 금지됨
 * 인증 이외의 이유로 액세스 권한 없을 때 발생합니다.
 */
@Getter
public class ForbiddenError extends RuntimeException {
    public ForbiddenError(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.FORBIDDEN);
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}