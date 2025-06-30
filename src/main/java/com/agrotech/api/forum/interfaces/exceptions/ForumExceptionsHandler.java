package com.agrotech.api.forum.interfaces.exceptions;

import com.agrotech.api.forum.domain.exceptions.ForumFavoriteNotFoundException;
import com.agrotech.api.forum.domain.exceptions.ForumPostNotFoundException;
import com.agrotech.api.forum.domain.exceptions.ForumReplyNotFoundException;
import com.agrotech.api.shared.infrastructure.interfaces.responses.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ForumExceptionsHandler {
    @ExceptionHandler(ForumPostNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePostNotFoundException(ForumPostNotFoundException e) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO("Forum Post Not Found", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
