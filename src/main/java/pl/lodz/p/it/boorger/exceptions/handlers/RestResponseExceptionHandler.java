package pl.lodz.p.it.boorger.exceptions.handlers;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.transaction.TransactionException;
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

@Log
@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = {AppBaseException.class})
    public ResponseEntity<String> handleException(AppBaseException e, WebRequest request) {
        log.severe("Error occurred " +  e.getClass());
        return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText(e.getMessage(),
                request.getHeader("lang") == null ? "" : Objects.requireNonNull(request.getHeader("lang"))));
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
        log.severe("Validation exception occurred: " + errors);
        return errors;
    }

    @ExceptionHandler(value = {TransactionException.class, ExhaustedRetryException.class})
        public ResponseEntity<String> handleTransactionException(TransactionException e, WebRequest request) {
        log.severe("TransactionException occurred: " + e.getClass());
        return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.default",
                request.getHeader("lang") == null ? "" : Objects.requireNonNull(request.getHeader("lang"))));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleOtherExceptions(Exception e, WebRequest request) {
        log.severe("Unexpected error occurred: " + e.getClass());
        return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText("error.default",
                request.getHeader("lang") == null ? "" : Objects.requireNonNull(request.getHeader("lang"))));
    }
}
