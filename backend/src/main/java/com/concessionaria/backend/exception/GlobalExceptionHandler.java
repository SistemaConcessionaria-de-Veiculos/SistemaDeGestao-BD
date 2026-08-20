package com.concessionaria.backend.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleEmailJaCadastrado(
            EmailJaCadastradoException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }  

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(
            jakarta.persistence.EntityNotFoundException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(DocumentoClienteJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleDocumentoClienteJaCadastrado(
            DocumentoClienteJaCadastradoException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
    @ExceptionHandler(CpfVendedorJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleCpfVendedorJaCadastrado(
            CpfVendedorJaCadastradoException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put(
                "erro",
                "Não é possível concluir a operação porque existem registros vinculados"
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
    @ExceptionHandler(VeiculoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleVeiculoNaoEncontrado(
            VeiculoNaoEncontradoException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ChassiVeiculoJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleChassiVeiculoJaCadastrado(
            ChassiVeiculoJaCadastradoException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(PlacaVeiculoJaCadastradaException.class)
    public ResponseEntity<Map<String, String>> handlePlacaVeiculoJaCadastrada(
            PlacaVeiculoJaCadastradaException exception
    ) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("erro", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
