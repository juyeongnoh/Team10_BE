package bdbe.bdbd._core.errors.exception;


import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 404 : Not Found (찾을 수 없음)
// 리소스 찾을 수 없음
@Getter
public class NotFoundError404 extends RuntimeException {
    public NotFoundError404(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}