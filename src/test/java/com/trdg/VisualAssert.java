package com.trdg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opentest4j.AssertionFailedError;

/**
 * Custom assertion class that provides visual feedback for test assertions.
 * Shows expected values, actual values, inputs, and results in a clear format.
 */
public class VisualAssert {

    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private static final String PASS_SYMBOL = "✓";
    private static final String FAIL_SYMBOL = "✗";
    private static final String INFO_SYMBOL = "ℹ";

    // Pretty print configuration
    private static boolean prettyPrint = true;

    /**
     * Sets whether JSON output should be pretty-printed (formatted with indentation)
     * @param enabled true to enable pretty printing, false for compact output
     */
    public static void setPrettyPrint(boolean enabled) {
        prettyPrint = enabled;
    }

    /**
     * Gets the current pretty print setting
     * @return true if pretty printing is enabled, false otherwise
     */
    public static boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Result object that captures assertion details
     */
    public static class AssertionResult {
        public final boolean passed;
        public final String testName;
        public final Object expected;
        public final Object actual;
        public final Object input;
        public final String message;
        public final String visualization;

        private AssertionResult(boolean passed, String testName, Object expected,
                               Object actual, Object input, String message, String visualization) {
            this.passed = passed;
            this.testName = testName;
            this.expected = expected;
            this.actual = actual;
            this.input = input;
            this.message = message;
            this.visualization = visualization;
        }

        @Override
        public String toString() {
            return visualization;
        }
    }

    /**
     * Assert that two JSON objects are equal, with visualization
     */
    public static AssertionResult assertJsonEquals(String testName, JSONObject expected,
                                                   JSONObject actual, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.areJsonEntitiesEqual(expected, actual);
        } catch (JSONException e) {
            return createResult(false, testName, expected, actual, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildVisualization(testName, expected, actual, input, areEqual, "JSON Objects", true, false);
        AssertionResult result = createResult(areEqual, testName, expected, actual, input,
                                             areEqual ? "Objects are equal" : "Objects are not equal");

        System.out.println(visualization);

        if (!areEqual) {
            throw new AssertionFailedError(result.message + "\n" + visualization,
                                          toJsonString(expected), toJsonString(actual));
        }

        return result;
    }

    /**
     * Assert that two JSON arrays are equal, with visualization
     */
    public static AssertionResult assertJsonArrayEquals(String testName, JSONArray expected,
                                                        JSONArray actual, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.compareJsonArrays(expected, actual);
        } catch (JSONException e) {
            return createResult(false, testName, expected, actual, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildArrayVisualization(testName, expected, actual, input, areEqual, true, false);
        AssertionResult result = createResult(areEqual, testName, expected, actual, input,
                                             areEqual ? "Arrays are equal" : "Arrays are not equal");

        System.out.println(visualization);

        if (!areEqual) {
            throw new AssertionFailedError(result.message + "\n" + visualization,
                                          toJsonString(expected), toJsonString(actual));
        }

        return result;
    }

    /**
     * Assert that two values are equal, with visualization
     */
    public static AssertionResult assertValueEquals(String testName, Object expected,
                                                    Object actual, String key) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.areValuesEqual(expected, actual, key);
        } catch (JSONException e) {
            return createResult(false, testName, expected, actual, key,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildValueVisualization(testName, expected, actual, key, areEqual);
        AssertionResult result = createResult(areEqual, testName, expected, actual, key,
                                             areEqual ? "Values are equal" : "Values are not equal");

        System.out.println(visualization);

        if (!areEqual) {
            throw new AssertionFailedError(result.message + "\n" + visualization,
                                          String.valueOf(expected), String.valueOf(actual));
        }

        return result;
    }

    /**
     * Assert that two JSON objects are NOT equal, with visualization
     */
    public static AssertionResult assertJsonNotEquals(String testName, JSONObject expected,
                                                      JSONObject actual, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.areJsonEntitiesEqual(expected, actual);
        } catch (JSONException e) {
            return createResult(false, testName, expected, actual, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildVisualization(testName, expected, actual, input, !areEqual, "JSON Objects", false, false);
        AssertionResult result = createResult(!areEqual, testName, expected, actual, input,
                                             areEqual ? "Objects are equal but expected different" : "Objects are not equal");

        System.out.println(visualization);

        if (areEqual) {
            throw new AssertionFailedError(result.message + "\n" + visualization,
                                          "different from " + toJsonString(expected), toJsonString(actual));
        }

        return result;
    }

    /**
     * Assert that two JSON arrays are NOT equal, with visualization
     */
    public static AssertionResult assertJsonArrayNotEquals(String testName, JSONArray expected,
                                                           JSONArray actual, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.compareJsonArrays(expected, actual);
        } catch (JSONException e) {
            return createResult(false, testName, expected, actual, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildArrayVisualization(testName, expected, actual, input, !areEqual, false, false);
        AssertionResult result = createResult(!areEqual, testName, expected, actual, input,
                                             areEqual ? "Arrays are equal but expected different" : "Arrays are not equal");

        System.out.println(visualization);

        if (areEqual) {
            throw new AssertionFailedError(result.message + "\n" + visualization,
                                          "different from " + toJsonString(expected), toJsonString(actual));
        }

        return result;
    }

    /**
     * Assert JSON equality without throwing, just returns result (useful for logging)
     */
    public static AssertionResult checkJsonEquals(String testName, JSONObject expected,
                                                  JSONObject actual, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.areJsonEntitiesEqual(expected, actual);
        } catch (JSONException e) {
            return createResult(false, testName, expected, actual, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildVisualization(testName, expected, actual, input, areEqual, "JSON Objects", true, false);
        System.out.println(visualization);

        return createResult(areEqual, testName, expected, actual, input,
                          areEqual ? "Objects are equal" : "Objects are not equal");
    }

    /**
     * Assert JSON array equality without throwing, just returns result (useful for logging)
     */
    public static AssertionResult checkJsonArrayEquals(String testName, JSONArray expected,
                                                       JSONArray actual, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.compareJsonArrays(expected, actual);
        } catch (JSONException e) {
            return createResult(false, testName, expected, actual, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildArrayVisualization(testName, expected, actual, input, areEqual, true, false);
        System.out.println(visualization);

        return createResult(areEqual, testName, expected, actual, input,
                          areEqual ? "Arrays are equal" : "Arrays are not equal");
    }

    /**
     * Demonstrates JSON object comparison without assertion language.
     * Shows comparison result using neutral "info" style instead of PASS/FAIL.
     * Useful for demonstration tests that compare different objects to show visualization.
     */
    public static AssertionResult demonstrateJsonComparison(String testName, JSONObject obj1,
                                                            JSONObject obj2, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.areJsonEntitiesEqual(obj1, obj2);
        } catch (JSONException e) {
            return createResult(false, testName, obj1, obj2, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildVisualization(testName, obj1, obj2, input, areEqual, "JSON Objects", true, true);
        System.out.println(visualization);

        return createResult(true, testName, obj1, obj2, input,
                          areEqual ? "Objects are equal" : "Objects are different");
    }

    /**
     * Demonstrates JSON array comparison without assertion language.
     * Shows comparison result using neutral "info" style instead of PASS/FAIL.
     * Useful for demonstration tests that compare different arrays to show visualization.
     */
    public static AssertionResult demonstrateJsonArrayComparison(String testName, JSONArray arr1,
                                                                 JSONArray arr2, String input) {
        boolean areEqual = false;
        try {
            areEqual = JsonComparator.compareJsonArrays(arr1, arr2);
        } catch (JSONException e) {
            return createResult(false, testName, arr1, arr2, input,
                              "JSONException: " + e.getMessage());
        }

        String visualization = buildArrayVisualization(testName, arr1, arr2, input, areEqual, true, true);
        System.out.println(visualization);

        return createResult(true, testName, arr1, arr2, input,
                          areEqual ? "Arrays are equal" : "Arrays are different");
    }

    /**
     * Displays a full comparison report (doesn't assert, just visualizes)
     */
    public static void visualizeComparison(String testName, JSONObject obj1, JSONObject obj2) {
        System.out.println(TestVisualizer.renderFullReport(obj1, obj2, testName));
    }

    /**
     * Displays a patch-based diff (doesn't assert, just visualizes)
     */
    public static void visualizePatchDiff(String testName, JSONObject obj1, JSONObject obj2) {
        System.out.println(TestVisualizer.renderPatchDiff(obj1, obj2, testName));
    }

    // Private helper methods

    private static AssertionResult createResult(boolean passed, String testName, Object expected,
                                               Object actual, Object input, String message) {
        String visualization = passed
            ? GREEN + PASS_SYMBOL + " PASS" + RESET
            : RED + FAIL_SYMBOL + " FAIL" + RESET;

        return new AssertionResult(passed, testName, expected, actual, input, message, visualization);
    }

    private static String buildVisualization(String testName, JSONObject expected, JSONObject actual,
                                            String input, boolean testPassed, String type, boolean expectingEquality, boolean isDemonstration) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("╔════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║ ").append(BOLD).append(CYAN).append("TEST: ").append(testName).append(RESET);
        sb.append(" ".repeat(Math.max(1, 74 - testName.length()))).append("║\n");
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Input section
        sb.append("║ ").append(BOLD).append("Input:").append(RESET).append(" ").append(input);
        sb.append(" ".repeat(Math.max(1, 72 - input.length()))).append("║\n");
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Expected section
        sb.append("║ ").append(GREEN).append("EXPECTED:").append(RESET);
        sb.append(" ".repeat(65)).append("║\n");
        appendFormattedJson(sb, expected);

        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Actual section
        sb.append("║ ").append(YELLOW).append("ACTUAL:").append(RESET);
        sb.append(" ".repeat(67)).append("║\n");
        appendFormattedJson(sb, actual);

        // Result section
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Determine actual equality
        boolean areActuallyEqual;
        try {
            areActuallyEqual = JsonComparator.areJsonEntitiesEqual(expected, actual);
        } catch (Exception e) {
            areActuallyEqual = false;
        }

        // Build status message based on expectation and result
        String status;
        if (isDemonstration) {
            // Demonstration mode - use neutral language
            status = areActuallyEqual
                ? CYAN + INFO_SYMBOL + " " + type + " are equal" + RESET
                : CYAN + INFO_SYMBOL + " " + type + " are different" + RESET;
        } else if (expectingEquality) {
            // Testing for equality
            status = testPassed
                ? GREEN + PASS_SYMBOL + " PASSED - " + type + " are equal" + RESET
                : RED + FAIL_SYMBOL + " FAILED - " + type + " are NOT equal" + RESET;
        } else {
            // Testing for inequality
            status = testPassed
                ? GREEN + PASS_SYMBOL + " PASSED - " + type + " are NOT equal (as expected)" + RESET
                : RED + FAIL_SYMBOL + " FAILED - " + type + " are equal (expected different)" + RESET;
        }
        sb.append("║ ").append(BOLD).append("RESULT:").append(RESET).append(" ").append(status);

        // Calculate padding for the status line
        int baseLength;
        if (isDemonstration) {
            baseLength = areActuallyEqual ? 13 : 16;
        } else {
            baseLength = expectingEquality ? 15 : (testPassed ? 30 : 35);
        }
        int symbolAndSpaces = isDemonstration ? 3 : 11;
        int actionWord = isDemonstration ? 0 : (testPassed ? "PASSED".length() : "FAILED".length());
        int statusLength = 8 + symbolAndSpaces + actionWord + type.length() + baseLength;
        sb.append(" ".repeat(Math.max(1, 72 - statusLength))).append("║\n");

        // Show differences if not equal
        if (!areActuallyEqual) {
            sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ ").append(RED).append("DIFFERENCES:").append(RESET);
            sb.append(" ".repeat(62)).append("║\n");

            try {
                JSONObject diff = JsonComparator.generateJsonDiff(expected, actual);
                appendFormattedJson(sb, diff);
            } catch (JSONException e) {
                sb.append("║ Error generating diff: ").append(e.getMessage());
                sb.append(" ".repeat(Math.max(1, 52 - e.getMessage().length()))).append("║\n");
            }
        }

        sb.append("╚════════════════════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }

    private static String buildArrayVisualization(String testName, JSONArray expected,
                                                 JSONArray actual, String input, boolean testPassed, boolean expectingEquality, boolean isDemonstration) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("╔════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║ ").append(BOLD).append(CYAN).append("TEST: ").append(testName).append(RESET);
        sb.append(" ".repeat(Math.max(1, 74 - testName.length()))).append("║\n");
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Input section
        sb.append("║ ").append(BOLD).append("Input:").append(RESET).append(" ").append(input);
        sb.append(" ".repeat(Math.max(1, 72 - input.length()))).append("║\n");
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Expected section
        sb.append("║ ").append(GREEN).append("EXPECTED ARRAY:").append(RESET);
        sb.append(" ".repeat(59)).append("║\n");
        appendFormattedJson(sb, expected);

        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Actual section
        sb.append("║ ").append(YELLOW).append("ACTUAL ARRAY:").append(RESET);
        sb.append(" ".repeat(61)).append("║\n");
        appendFormattedJson(sb, actual);

        // Result section
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Determine actual equality
        boolean areActuallyEqual;
        try {
            areActuallyEqual = JsonComparator.compareJsonArrays(expected, actual);
        } catch (Exception e) {
            areActuallyEqual = false;
        }

        // Build status message based on expectation and result
        String status;
        if (isDemonstration) {
            // Demonstration mode - use neutral language
            status = areActuallyEqual
                ? CYAN + INFO_SYMBOL + " Arrays are equal" + RESET
                : CYAN + INFO_SYMBOL + " Arrays are different" + RESET;
        } else if (expectingEquality) {
            // Testing for equality
            status = testPassed
                ? GREEN + PASS_SYMBOL + " PASSED - Arrays are equal" + RESET
                : RED + FAIL_SYMBOL + " FAILED - Arrays are NOT equal" + RESET;
        } else {
            // Testing for inequality
            status = testPassed
                ? GREEN + PASS_SYMBOL + " PASSED - Arrays are NOT equal (as expected)" + RESET
                : RED + FAIL_SYMBOL + " FAILED - Arrays are equal (expected different)" + RESET;
        }
        sb.append("║ ").append(BOLD).append("RESULT:").append(RESET).append(" ").append(status);

        // Calculate padding for the status line
        int baseLength;
        if (isDemonstration) {
            baseLength = areActuallyEqual ? 18 : 21;
        } else {
            baseLength = expectingEquality ? 20 : (testPassed ? 35 : 40);
        }
        int symbolAndSpaces = isDemonstration ? 3 : 11;
        int actionWord = isDemonstration ? 0 : (testPassed ? "PASSED".length() : "FAILED".length());
        int statusLength = 8 + symbolAndSpaces + actionWord + baseLength;
        sb.append(" ".repeat(Math.max(1, 72 - statusLength))).append("║\n");

        // Show differences if not equal
        if (!areActuallyEqual) {
            sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");
            sb.append("║ ").append(RED).append("DIFFERENCES:").append(RESET);
            sb.append(" ".repeat(62)).append("║\n");

            try {
                JSONObject diff = JsonComparator.generateJsonArrayDiff(expected, actual, input);
                appendFormattedJson(sb, diff);
            } catch (JSONException e) {
                sb.append("║ Error generating diff: ").append(e.getMessage());
                sb.append(" ".repeat(Math.max(1, 52 - e.getMessage().length()))).append("║\n");
            }
        }

        sb.append("╚════════════════════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }

    private static String buildValueVisualization(String testName, Object expected,
                                                 Object actual, String key, boolean areEqual) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("╔════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║ ").append(BOLD).append(CYAN).append("TEST: ").append(testName).append(RESET);
        sb.append(" ".repeat(Math.max(1, 74 - testName.length()))).append("║\n");
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Key section
        sb.append("║ ").append(BOLD).append("Key:").append(RESET).append(" ").append(key);
        sb.append(" ".repeat(Math.max(1, 74 - key.length()))).append("║\n");
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");

        // Expected section
        String expectedStr = formatValue(expected);
        sb.append("║ ").append(GREEN).append("Expected:").append(RESET).append(" ").append(expectedStr);
        sb.append(" ".repeat(Math.max(1, 66 - expectedStr.length()))).append("║\n");

        // Actual section
        String actualStr = formatValue(actual);
        sb.append("║ ").append(YELLOW).append("Actual:").append(RESET).append("   ").append(actualStr);
        sb.append(" ".repeat(Math.max(1, 66 - actualStr.length()))).append("║\n");

        // Result section
        sb.append("╠════════════════════════════════════════════════════════════════════════════╣\n");
        String status = areEqual
            ? GREEN + PASS_SYMBOL + " PASSED - Values are equal" + RESET
            : RED + FAIL_SYMBOL + " FAILED - Values are NOT equal" + RESET;
        sb.append("║ ").append(BOLD).append("RESULT:").append(RESET).append(" ").append(status);

        int statusLength = 9 + (areEqual ? "PASSED".length() : "FAILED".length()) + 20;
        sb.append(" ".repeat(Math.max(1, 72 - statusLength))).append("║\n");

        sb.append("╚════════════════════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }

    private static void appendFormattedJson(StringBuilder sb, Object json) {
        try {
            String formatted;
            if (json instanceof JSONObject) {
                formatted = prettyPrint ? ((JSONObject) json).toString(2) : ((JSONObject) json).toString();
            } else if (json instanceof JSONArray) {
                formatted = prettyPrint ? ((JSONArray) json).toString(2) : ((JSONArray) json).toString();
            } else {
                formatted = String.valueOf(json);
            }

            String[] lines = formatted.split("\n");
            for (String line : lines) {
                // Truncate long lines
                if (line.length() > 74) {
                    line = line.substring(0, 71) + "...";
                }
                sb.append("║ ").append(line);
                sb.append(" ".repeat(Math.max(1, 76 - line.length()))).append("║\n");
            }
        } catch (Exception e) {
            sb.append("║ Error formatting: ").append(e.getMessage());
            sb.append(" ".repeat(Math.max(1, 56 - e.getMessage().length()))).append("║\n");
        }
    }

    private static String formatValue(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return "\"" + value + "\"";
        return value.toString();
    }

    /**
     * Formats a JSON object or array as a string, respecting the prettyPrint setting
     */
    private static String toJsonString(Object json) {
        try {
            if (json instanceof JSONObject) {
                return prettyPrint ? ((JSONObject) json).toString(2) : ((JSONObject) json).toString();
            } else if (json instanceof JSONArray) {
                return prettyPrint ? ((JSONArray) json).toString(2) : ((JSONArray) json).toString();
            }
        } catch (JSONException e) {
            // Fall through to default
        }
        return String.valueOf(json);
    }
}
