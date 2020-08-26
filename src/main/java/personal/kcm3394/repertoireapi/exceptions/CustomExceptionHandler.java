package personal.kcm3394.repertoireapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoEntityFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNoEntityFoundException(Exception ex) {
        CustomErrorResponse response = new CustomErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setError(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserCreationException.class, RepertoireException.class})
    public ResponseEntity<CustomErrorResponse> handleUserCreationException(Exception ex) {
        CustomErrorResponse response = new CustomErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setError(ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
