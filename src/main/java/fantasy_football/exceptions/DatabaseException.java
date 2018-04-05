package fantasy_football.exceptions;

public class DatabaseException extends Exception {


    public DatabaseException() {
    }

    public DatabaseException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "Database Exception";
    }
}
