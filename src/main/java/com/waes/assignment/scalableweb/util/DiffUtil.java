package com.waes.assignment.scalableweb.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to compute diff between two objects.
 */
public class DiffUtil {

    private static final String WINDOWS_CARRIAGE_RETURN_CHARACTER = "\r\n";

    /**
     * Calculates the differences(start position and lenght in chars) between two Strings.
     * Both strings must have the same length.
     * Windows carriage return character '\r\n' is replaced by '\n' to count it as 1 char instead of 2. Otherwise, the starting position of the differences would not be accurate.
     * The function compares both Strings char by char. When a difference is found, the position of the difference is saved and the differenceLenght is incremented by 1.
     * @param leftString
     * @param rightString
     * @return A list of differences.
     * Each difference consist of a String specifying the position where it starts and the length in characters.
     */
    public static List<String> getDifferences(String leftString, String rightString) {
        List<String> differences = new ArrayList<>();

        char[] leftCharArray = leftString.replace(WINDOWS_CARRIAGE_RETURN_CHARACTER, "\n").toCharArray();
        char[] rightCharArray = rightString.replace(WINDOWS_CARRIAGE_RETURN_CHARACTER, "\n").toCharArray();

        int differenceLenght = 0;
        int differencePosition = 0;
        for (int pos = 0; pos < leftCharArray.length; pos++) {
            if (leftCharArray[pos] != rightCharArray[pos]) {
                if (differenceLenght == 0) {
                    differencePosition = pos;
                }
                differenceLenght++;
                if (pos == leftCharArray.length - 1) {
                    differences.add(String.format("Difference found at position %s. Difference size: %s character/s", differencePosition, differenceLenght));
                }
            } else {
                if (differenceLenght > 0) {
                    differences.add(String.format("Difference found at position %s. Difference size: %s character/s", differencePosition, differenceLenght));
                    differenceLenght = 0;
                }
            }
        }
        return differences;
    }
}
