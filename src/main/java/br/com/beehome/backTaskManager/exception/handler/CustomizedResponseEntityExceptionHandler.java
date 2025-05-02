package br.com.beehome.backTaskManager.exception.handler;

import br.com.beehome.backTaskManager.exception.CustomizeException;
import br.com.beehome.backTaskManager.exception.ExceptionResponse;
import br.com.beehome.backTaskManager.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler{

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions (Exception ex, WebRequest webRequest){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                webRequest.getDescription(false)
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<?> tratarError401(){
        return ResponseEntity.badRequest().body("Seu usuário não é permitido para acessar esta rota!");
    }

    @ExceptionHandler(CustomizeException.class)
    public ResponseEntity<ExceptionResponse> handleValidacaoException(CustomizeException ex, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                webRequest.getDescription(false)
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> tratarError404(EntityNotFoundException ex, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                webRequest.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> tratarErrorBadRequest(MethodArgumentNotValidException ex, WebRequest webRequest) {
        var erroOptional = ex.getFieldErrors().stream().findFirst();

        if (erroOptional.isPresent()) {
            var fieldError = erroOptional.get();
            String fieldName = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();

            String message = String.format("O campo %s %s", fieldName, defaultMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                    new Date(),
                    message,
                    webRequest.getDescription(false)
            ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                new Date(),
                "Erro desconhecido: " + ex.getMessage(),
                webRequest.getDescription(false)
            )
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> tratarErrorAccesDenied(AccessDeniedException ex, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                webRequest.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }


}
