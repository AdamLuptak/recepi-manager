package eu.rebase.recipe.exception;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Value
public class ExceptionMessage {

   HttpStatus status;
   String message;
   List<String> errors;

    public ExceptionMessage(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ExceptionMessage(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
