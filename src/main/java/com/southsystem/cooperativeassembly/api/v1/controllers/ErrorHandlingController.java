package com.southsystem.cooperativeassembly.api.v1.controllers;

import com.southsystem.cooperativeassembly.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlingController extends ResponseEntityExceptionHandler {
    private ResponseEntity<ErrorInfo> error(String message, HttpStatus httpStatus, Exception exception,
                                            HttpServletRequest request) {
        ErrorInfo error = ErrorInfo.builder()
                .message(message)
                .status(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .detail(exception.getLocalizedMessage())
                .build();
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleTopicNotFoundException(TopicNotFoundException exception, HttpServletRequest request) {
        return error("Topic Not Found", HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(TopicNotValidException.class)
    public ResponseEntity<ErrorInfo> handleTopicNotValidException(TopicNotValidException exception, HttpServletRequest request) {
        return error("Invalid Topic", HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(VoteNotAuthorizedException.class)
    public ResponseEntity<ErrorInfo> handleVoteNotAuthorizedException(VoteNotAuthorizedException exception, HttpServletRequest request) {
        return error("Unauthorized Vote", HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(VoteNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleVoteNotFoundException(VoteNotFoundException exception, HttpServletRequest request) {
        return error("Vote Not Found", HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(VoteNotValidException.class)
    public ResponseEntity<ErrorInfo> handleVoteNotValidException(VoteNotValidException exception, HttpServletRequest request) {
        return error("Invalid Vote", HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(VotingSessionExpiredException.class)
    public ResponseEntity<ErrorInfo> handleVotingSessionExpiredException(VotingSessionExpiredException exception, HttpServletRequest request) {
        return error("Voting Session Expired", HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(VotingSessionNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleVotingSessionNotFoundException(VotingSessionNotFoundException exception, HttpServletRequest request) {
        return error("Voting Session Not Found", HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(VotingSessionNotValidException.class)
    public ResponseEntity<ErrorInfo> handleVotingSessionNotValidException(VotingSessionNotValidException exception, HttpServletRequest request) {
        return error("Invalid Voting Session", HttpStatus.BAD_REQUEST, exception, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> details = new HashMap<>();
        for(FieldError error : ex.getBindingResult().getFieldErrors()) {
            String message = error.getDefaultMessage() + ", but received: " +
                    (error.getRejectedValue() == null ? "null" : error.getRejectedValue().toString());
            details.put(error.getField(), message);
        }

        ErrorInfo error = ErrorInfo.builder()
                .message("Request Field-level Validation Failed")
                .details(details)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .uri(((ServletWebRequest)request).getRequest().getRequestURI())
                .build();
        return new ResponseEntity<>(error, status);
    }
}
