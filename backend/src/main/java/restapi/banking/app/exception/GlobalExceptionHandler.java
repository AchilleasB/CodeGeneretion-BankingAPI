package restapi.banking.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import restapi.banking.app.dto.ExceptionDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        ExceptionDTO response = new ExceptionDTO(
                HttpStatus.NOT_FOUND.value(),
                ex.getClass().getSimpleName(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<ExceptionDTO> handleBadRequest(Exception ex) {
        ExceptionDTO response = new ExceptionDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getClass().getSimpleName(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericException(Exception ex) {
        ExceptionDTO response = new ExceptionDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getClass().getSimpleName(),
                "An unexpected error has occurred.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
