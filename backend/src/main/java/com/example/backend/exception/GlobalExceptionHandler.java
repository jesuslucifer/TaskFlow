package com.example.backend.exception;

import com.example.backend.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Имя пользователя уже занято",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Электронная почта уже занята",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Имя пользователя не найдено",
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmailNotFound(EmailNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Электронная почта не найдена",
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIdNotFound(IdNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "ID не найден",
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity<ErrorResponse> handleUploadFile(UploadFileException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Ошибка загрузки файла",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileType(InvalidFileTypeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Недопустимый тип файла",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(UploadFileIsEmptyException.class)
    public ResponseEntity<ErrorResponse> handleUploadFileIsEmpty(UploadFileIsEmptyException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Файл пуст",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationFailed(AuthenticationFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Ошибка авторизации",
                HttpStatus.UNAUTHORIZED
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(ProjectAlreadyExist.class)
    public ResponseEntity<ErrorResponse> handleProjectAlreadyExist(ProjectAlreadyExist ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Данный проект уже существует",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(ProjectNotExist.class)
    public ResponseEntity<ErrorResponse> handleProjectNotExist(ProjectNotExist ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Данного проекта не существует",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(ProjectGetFailedException.class)
    public ResponseEntity<ErrorResponse> ProjectGetFailedException(ProjectGetFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Ошибка при получении проекта",
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Пользователь не найден",
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
