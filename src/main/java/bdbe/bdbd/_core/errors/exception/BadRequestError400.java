package bdbe.bdbd._core.errors.exception;

import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 400 : Bad Request(잘못된 요청)
// 유효성 검사 실패, 잘못된 파라미터 요청
@Getter
public class BadRequestError400 extends RuntimeException {

    public BadRequestError400(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}