package io.account.management.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class ExceptionResponse {
    private Integer errorCode;
    private String message;
    private Date timestamp;

    @Builder
    public ExceptionResponse(Integer errorCode, String message, Date timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }
}
