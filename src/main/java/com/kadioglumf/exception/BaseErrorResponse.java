package com.kadioglumf.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

@Data
@NoArgsConstructor
public class BaseErrorResponse {

    private Date timestamp;
    private int httpStatusCode;
    private int errorCode;
    private String errorMessage;
    private String errorType;
    private String transactionId;

    public BaseErrorResponse(int httpStatusCode, ErrorType errorType) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorType.getDescription();
        this.errorType = errorType.name();
        this.errorCode = errorType.getCode();
        this.timestamp = new Date();
        this.transactionId = RandomStringUtils.randomNumeric(20);
    }

    public BaseErrorResponse(int httpStatusCode, ErrorType errorType, String errorMessage) {
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
        this.errorType = errorType.name();
        this.errorCode = errorType.getCode();
        this.timestamp = new Date();
        this.transactionId = RandomStringUtils.randomNumeric(20);
    }

    public BaseErrorResponse(ErrorType errorType) {
        this.httpStatusCode = errorType.getHttpStatusCode();
        this.errorMessage = errorType.getDescription();
        this.errorType = errorType.name();
        this.errorCode = errorType.getCode();
        this.timestamp = new Date();
        this.transactionId = RandomStringUtils.randomNumeric(20);
    }

    public BaseErrorResponse(ErrorType errorType, String errorMessage) {
        this.httpStatusCode = errorType.getHttpStatusCode();
        this.errorMessage = errorMessage;
        this.errorType = errorType.name();
        this.errorCode = errorType.getCode();
        this.timestamp = new Date();
        this.transactionId = RandomStringUtils.randomNumeric(20);
    }
}

