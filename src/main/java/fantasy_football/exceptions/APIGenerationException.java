package fantasy_football.exceptions;

public class APIGenerationException extends Exception {

    public APIGenerationException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "API Generation Exception";
    }
}
