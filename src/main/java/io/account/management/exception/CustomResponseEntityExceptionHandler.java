package io.account.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PostsNotFoundException.class)
    public final ResponseEntity<Object> handlePostsNotFoundException(Exception exception, WebRequest request) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .message(exception.getMessage())
                .timestamp(new Date())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

    }


}
