package ar.rindoresu.apinotificationchallenge.exception;

import ar.rindoresu.apinotificationchallenge.api.dto.ErrorResponse;
import ar.rindoresu.apinotificationchallenge.api.dto.ValidationErrorResponse;
import ar.rindoresu.apinotificationchallenge.pokemon.exception.PokemonNotFoundException;
import ar.rindoresu.apinotificationchallenge.user.exception.UserNotFoundException;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    static final String ERROR = "error";

    // 404 for domain exceptions
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(UserNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    // 409 for unique constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(DataIntegrityViolationException ex) {
        return new ErrorResponse("Email or username already exists");
    }

    // 400 for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidation(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ValidationErrorResponse("Validation failed", errors);
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePokemonNotFound(PokemonNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

}