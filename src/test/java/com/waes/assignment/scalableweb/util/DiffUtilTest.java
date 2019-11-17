package com.waes.assignment.scalableweb.util;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiffUtilTest {

    @Test
    void givenTwoSameLengthDifferentContentStrings_getDifferences_shouldFindDifferences() {
        // Setup
        String aString = "aString";
        String anotherString = "aStrong";
        List<String> expectedResult = Collections.singletonList("Difference found at position 4. Difference size: 1 character/s");

        // Test
        List<String> result = DiffUtil.getDifferences(aString, anotherString);

        // Result verification
        assertEquals(expectedResult, result);
    }

    @Test
    void givenEqualStrings_getDifferences_shouldReturnEmptyList() {
        // Setup
        String aString = "aString";

        // Test
        List<String> result = DiffUtil.getDifferences(aString, aString);

        // Result verification
        assertTrue(result.isEmpty());
    }
}