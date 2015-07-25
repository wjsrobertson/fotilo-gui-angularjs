package net.xylophones.fotilo;

public class InputValidationUtil {

    public static void checkWithinRange(String message, int valueToCheck, int lowerBound, int upperBound) {
        if (valueToCheck < lowerBound || valueToCheck > upperBound) {
            String error = message + " - value " + valueToCheck + " must be within " + lowerBound + " to " + upperBound;
            throw new IllegalArgumentException(error);
        }
    }

}
