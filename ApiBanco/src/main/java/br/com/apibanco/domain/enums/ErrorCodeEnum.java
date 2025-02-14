package br.com.apibanco.domain.enums;

public enum ErrorCodeEnum {

    CUSTOMER_NOT_FOUND("Customer not found", "ERR-001", 404),
    ADDRESS_NOT_FOUND("Address not found", "ERR-002", 404),
    AGENCY_NOT_FOUND("Agency not found", "ERR-003", 404),
    DUPLICATE_AGENCY("Duplicate agency", "ERR-018", 409),
    TRANSACTION_NOT_FOUND("Transaction not found", "ERR-004", 404),
    ACCOUNT_NOT_FOUND("Account not found", "ERR-005", 404),
    INSUFFICIENT_BALANCE("Insufficient balance", "ERR-006", 400),
    INVALID_TRANSACTION_TYPE("Invalid transaction type", "ERR-007", 400),
    INVALID_ACCOUNT_STATUS("Invalid account status", "ERR-008", 400),
    DUPLICATE_ACCOUNT_NUMBER("Duplicate account number", "ERR-009", 409),
    INVALID_REQUEST("Invalid request parameters", "ERR-010", 400),
    UNAUTHORIZED_ACCESS("Unauthorized access", "ERR-011", 401),
    OPERATION_NOT_ALLOWED("Operation not allowed", "ERR-012", 403),
    RESOURCE_CONFLICT("Resource conflict", "ERR-013", 409),
    SERVER_ERROR("Internal server error", "ERR-014", 500),
    DUPLICATE_CUSTOMER_CPF("Duplicate customer CPF", "ERR-015", 409),
    DUPLICATE_CUSTOMER_RG("Duplicate customer RG", "ERR-016", 409),
    DUPLICATE_CUSTOMER_CPF_RG("Duplicate customer CPF or RG", "ERR-017", 409),
    INVALID_INTEREST_RATE_UPDATE("Interest rate can only be updated for savings accounts", "ERR-019", 400),
    ERROR_WHILE_SAVING_ACCOUNT("Error while saving account.","ERR-019",400);



    private final String message;
    private final String code;
    private final int httpStatus;

    ErrorCodeEnum(String message, String code, int httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
