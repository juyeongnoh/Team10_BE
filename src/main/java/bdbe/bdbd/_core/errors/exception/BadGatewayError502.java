package bdbe.bdbd._core.errors.exception;

import bdbe.bdbd._core.errors.utils.ApiUtils;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 502 : Bad Gatwway(불량 게이트웨이)
// 게이트웨이 또는 프록시 역할을 하는 서버가 그 뒷단의 서버로부터 잘못된 응답 받음
@Getter
public class BadGatewayError502 extends RuntimeException {
    public BadGatewayError502(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}