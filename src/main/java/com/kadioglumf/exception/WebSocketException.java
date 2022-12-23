package com.kadioglumf.exception;

public class WebSocketException extends RuntimeException {

    private final BaseErrorResponse errorResponse;

    public WebSocketException(BaseErrorResponse errorResponse) {
        super(errorResponse.getErrorMessage());
        this.errorResponse = errorResponse;
    }

    public WebSocketException(ErrorType errorType) {
        super(errorType.getDescription());
        this.errorResponse = new BaseErrorResponse(errorType);
    }

    public WebSocketException(int statusCode, ErrorType errorType) {
        super(errorType.getDescription());
        this.errorResponse = new BaseErrorResponse(statusCode, errorType);
    }

    public WebSocketException(int statusCode, ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorResponse = new BaseErrorResponse(statusCode, errorType, errorMessage);
    }

    public WebSocketException(ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorResponse = new BaseErrorResponse(errorType, errorMessage);
    }

    public BaseErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
