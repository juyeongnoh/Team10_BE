package bdbe.bdbd._core.errors;


import bdbe.bdbd._core.errors.exception.*;
import bdbe.bdbd._core.errors.utils.ApiUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<?> badRequest(BadRequestError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(UnAuthorizedError.class)
    public ResponseEntity<?> unAuthorized(UnAuthorizedError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(ForbiddenError.class)
    public ResponseEntity<?> forbidden(ForbiddenError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<?> notFound(NotFoundError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> serverError(InternalServerError e){
        return new ResponseEntity<>(e.body(), e.status());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unknownServerError(Exception e){
        ApiUtils.ApiResult<?> apiResult = ApiUtils.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
