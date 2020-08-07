package pl.lodz.p.it.boorger.exceptions.handlers;

import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.utils.MessageProvider;

import java.util.Objects;

@Log
@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = {AppBaseException.class})
    public ResponseEntity<String> handleException(Exception e, WebRequest request) {
        return ResponseEntity.badRequest().body(MessageProvider.getTranslatedText(e.getMessage(),
                Objects.requireNonNull(request.getHeader("lang"))));
    }
}
