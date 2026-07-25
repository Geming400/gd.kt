package exceptions;

public class InvalidRawStringException extends RuntimeException {
    public final String rawString;

    public InvalidRawStringException(String rawString) {
        super(getErrorMessage(rawString));
        this.rawString = rawString;
    }

    public InvalidRawStringException(String rawString, Throwable cause) {
        super(getErrorMessage(rawString), cause);
        this.rawString = rawString;
    }

    private static String getErrorMessage(String rawString) {
        return "Raw string '%s' is malformed or invalid.".formatted(rawString);
    }
}
