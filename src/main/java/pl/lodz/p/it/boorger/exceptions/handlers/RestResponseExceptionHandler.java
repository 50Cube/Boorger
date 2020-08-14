package pl.lodz.p.it.boorger.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = {AppBaseException.class})
    public ResponseEntity<String> handleException(Exception e, WebRequest request) {
        return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText(e.getMessage(),
                Objects.requireNonNull(request.getHeader("lang"))));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Map<String, String> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleOtherExceptions(Exception e, WebRequest request) {
        return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.default",
                Objects.requireNonNull(request.getHeader("lang"))));
    }
}
