package eu.rebase.recipe.exception;

public class UserIdNotSetException extends RuntimeException {
    public UserIdNotSetException(String message) {
        super(message);
    }
}
