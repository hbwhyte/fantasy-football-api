package fantasy_football.exceptions;

public class UnauthenticatedUserException extends Exception {

    public UnauthenticatedUserException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Unauthenticated User Exception";
    }
}
