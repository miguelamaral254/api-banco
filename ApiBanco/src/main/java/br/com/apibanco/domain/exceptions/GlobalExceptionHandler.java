    package br.com.apibanco.domain.exceptions;

    import br.com.apibanco.domain.exceptions.BusinessException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.bind.annotation.RestControllerAdvice;

    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
            return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus()))
                    .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
        }
        @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
        public ResponseEntity<Object> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("ERR-017", "Duplicate customer CPF or RG"));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleGenericException(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("ERR-500", "An unexpected error occurred."));
        }

        static class ErrorResponse {
            private final String code;
            private final String message;

            public ErrorResponse(String code, String message) {
                this.code = code;
                this.message = message;
            }

            public String getCode() {
                return code;
            }

            public String getMessage() {
                return message;
            }
        }
    }
