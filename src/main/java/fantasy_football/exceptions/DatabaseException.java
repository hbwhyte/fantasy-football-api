package fantasy_football.exceptions;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "DatabaseException{}";
    }
}
