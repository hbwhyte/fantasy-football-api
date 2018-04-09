package fantasy_football.exceptions;

public class CallsExceededException extends Exception {

    public CallsExceededException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "API Calls Exceeded Exception";
    }
}
