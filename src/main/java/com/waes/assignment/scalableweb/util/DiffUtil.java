package com.waes.assignment.scalableweb.util;

import java.util.ArrayList;
import java.util.List;

public class DiffUtil {

    private static final String WINDOWS_CARRIAGE_RETURN_CHARACTER = "\r\n";

    /**
     * @param leftJsonString
     * @param rightJsonString
     * @return
     */
    public static List<String> jsonDiff(String leftJsonString, String rightJsonString) {
        List<String> differences = new ArrayList<>();

        char[] leftJsonCharArray = leftJsonString.replace(WINDOWS_CARRIAGE_RETURN_CHARACTER,"\n").toCharArray();
        char[] rightJsonCharArray = rightJsonString.replace(WINDOWS_CARRIAGE_RETURN_CHARACTER,"\n").toCharArray();

        int differenceLenght = 0;
        int differencePosition = 0;
        for (int pos = 0; pos < leftJsonCharArray.length; pos++) {
            if (leftJsonCharArray[pos] != rightJsonCharArray[pos]) {
                if (differenceLenght == 0) {
                    differencePosition = pos;
                }
                differenceLenght++;
            } else {
                if (differenceLenght > 0) {
                    differences.add(String.format("Difference found at position %s. Difference size: %s characters", differencePosition, differenceLenght));
                    differenceLenght = 0;
                }
            }
        }
        return differences;
    }
}
