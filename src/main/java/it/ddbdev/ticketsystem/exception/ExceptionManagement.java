package it.ddbdev.ticketsystem.exception;

import javax.validation.ConstraintViolationException;

import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionManagement {


    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex){
        String exception = ex.getConstraintViolations().stream().iterator().next().getMessage();
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler ({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleArgumentNotValidValue(MethodArgumentNotValidException ex) {

        BindingResult bindingResults = ex.getBindingResult();
        List<String> errors = bindingResults
                .getFieldErrors()
                .stream().map(e -> {
                    return e.getDefaultMessage();
                })
                .collect(Collectors.toList());

        return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
    }

}