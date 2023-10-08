package bdbe.bdbd._core.errors.exception;



import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 401 : Unauthorized (권한 없음)
// 인증 안됨, 액세스 권한 없음
@Getter
public class UnAuthorizedError401 extends RuntimeException {
    public UnAuthorizedError401(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public HttpStatus status(){
        return HttpStatus.UNAUTHORIZED;
    }
}