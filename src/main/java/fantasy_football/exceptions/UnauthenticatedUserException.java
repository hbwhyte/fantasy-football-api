package fantasy_football.exceptions;

public class UnauthenticatedUserException extends Exception {

    public UnauthenticatedUserException() {
    }

    public UnauthenticatedUserException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "UnauthenticatedUserException{"+super.getMessage()+"}";
    }
}
