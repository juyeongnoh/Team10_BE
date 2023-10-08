package bdbe.bdbd._core.errors.exception;


import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 500 : Internal Server Error(내부 서버 오류)
// 서버에 에러 발생
@Getter
public class InternalServerError500 extends RuntimeException {
    public InternalServerError500(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
