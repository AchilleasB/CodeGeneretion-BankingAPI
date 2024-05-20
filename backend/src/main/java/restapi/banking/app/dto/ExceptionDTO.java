package restapi.banking.app.dto;

public record ExceptionDTO(int status, String exception, String message) {
}