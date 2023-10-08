package bdbe.bdbd._core.errors;


import bdbe.bdbd._core.errors.exception.*;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestError400.class)
    public ResponseEntity<?> badRequest(BadRequestError400 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(UnAuthorizedError401.class)
    public ResponseEntity<?> unAuthorized(UnAuthorizedError401 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(ForbiddenError403.class)
    public ResponseEntity<?> forbidden(ForbiddenError403 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(NotFoundError404.class)
    public ResponseEntity<?> notFound(NotFoundError404 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(InternalServerError500.class)
    public ResponseEntity<?> serverError(InternalServerError500 e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){
        ApiUtils.ApiResult<?> apiResult = ApiUtils.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
