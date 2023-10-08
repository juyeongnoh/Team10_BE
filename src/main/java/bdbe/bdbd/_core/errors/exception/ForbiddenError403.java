package bdbe.bdbd._core.errors.exception;


import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 403 : Forbidden (금지됨)
// 인증이외의 이유, 액세스 권한 없음
@Getter
public class ForbiddenError403 extends RuntimeException {
    public ForbiddenError403(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.FORBIDDEN);
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}