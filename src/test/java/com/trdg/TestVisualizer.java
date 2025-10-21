package com.trdg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class for visualizing JSON comparisons in tests.
 * Provides formatted output to make differences easy to see.
 */
public class TestVisualizer {

    // ANSI color codes for better visualization in terminals that support it
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private static final String BOX_TOP = "╔════════════════════════════════════════════════════════════════════════════╗";
    private static final String BOX_MIDDLE = "╠════════════════════════════════════════════════════════════════════════════╣";
    private static final String BOX_BOTTOM = "╚════════════════════════════════════════════════════════════════════════════╝";
    private static final String BOX_SIDE = "║";

    /**
     * Renders a comparison between two JSON objects with clear visual formatting
     */
    public static String renderComparison(JSONObject obj1, JSONObject obj2, String label) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(BOX_TOP).append("\n");
        sb.append(formatLine(BOLD + CYAN + "JSON COMPARISON: " + label + RESET)).append("\n");
        sb.append(BOX_MIDDLE).append("\n");

        // Expected section
        sb.append(formatLine(GREEN + "EXPECTED (Object 1):" + RESET)).append("\n");
        sb.append(formatJsonContent(obj1.toString(2))).append("\n");

        sb.append(BOX_MIDDLE).append("\n");

        // Actual section
        sb.append(formatLine(YELLOW + "ACTUAL (Object 2):" + RESET)).append("\n");
        sb.append(formatJsonContent(obj2.toString(2))).append("\n");

        // Differences section
        try {
            JSONObject diff = JsonComparator.generateJsonDiff(obj1, obj2);
            if (!diff.isEmpty()) {
                sb.append(BOX_MIDDLE).append("\n");
                sb.append(formatLine(RED + "DIFFERENCES:" + RESET)).append("\n");
                sb.append(formatJsonContent(diff.toString(2))).append("\n");
            }
        } catch (JSONException e) {
            sb.append(formatLine("Error generating diff: " + e.getMessage())).append("\n");
        }

        sb.append(BOX_BOTTOM).append("\n");
        return sb.toString();
    }

    /**
     * Renders a comparison between two JSON arrays with clear visual formatting
     */
    public static String renderArrayComparison(JSONArray arr1, JSONArray arr2, String label) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(BOX_TOP).append("\n");
        sb.append(formatLine(BOLD + CYAN + "ARRAY COMPARISON: " + label + RESET)).append("\n");
        sb.append(BOX_MIDDLE).append("\n");

        // Expected section
        sb.append(formatLine(GREEN + "EXPECTED (Array 1):" + RESET)).append("\n");
        sb.append(formatJsonContent(arr1.toString(2))).append("\n");

        sb.append(BOX_MIDDLE).append("\n");

        // Actual section
        sb.append(formatLine(YELLOW + "ACTUAL (Array 2):" + RESET)).append("\n");
        sb.append(formatJsonContent(arr2.toString(2))).append("\n");

        // Differences section
        try {
            JSONObject diff = JsonComparator.generateJsonArrayDiff(arr1, arr2, label);
            if (!diff.isEmpty()) {
                sb.append(BOX_MIDDLE).append("\n");
                sb.append(formatLine(RED + "DIFFERENCES:" + RESET)).append("\n");
                sb.append(formatJsonContent(diff.toString(2))).append("\n");
            }
        } catch (JSONException e) {
            sb.append(formatLine("Error generating diff: " + e.getMessage())).append("\n");
        }

        sb.append(BOX_BOTTOM).append("\n");
        return sb.toString();
    }

    /**
     * Renders a side-by-side comparison of two values
     */
    public static String renderValueComparison(Object value1, Object value2, String key) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(BOX_TOP).append("\n");
        sb.append(formatLine(BOLD + CYAN + "VALUE COMPARISON: " + key + RESET)).append("\n");
        sb.append(BOX_MIDDLE).append("\n");

        String v1Str = formatValue(value1);
        String v2Str = formatValue(value2);

        sb.append(formatLine(GREEN + "Expected: " + RESET + v1Str)).append("\n");
        sb.append(formatLine(YELLOW + "Actual:   " + RESET + v2Str)).append("\n");

        if (!value1.equals(value2)) {
            sb.append(formatLine(RED + "Status:   DIFFERENT" + RESET)).append("\n");
        } else {
            sb.append(formatLine(GREEN + "Status:   EQUAL" + RESET)).append("\n");
        }

        sb.append(BOX_BOTTOM).append("\n");
        return sb.toString();
    }

    /**
     * Renders a detailed diff using the patch format
     */
    public static String renderPatchDiff(JSONObject obj1, JSONObject obj2, String label) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(BOX_TOP).append("\n");
        sb.append(formatLine(BOLD + CYAN + "JSON PATCH DIFF: " + label + RESET)).append("\n");
        sb.append(BOX_MIDDLE).append("\n");

        try {
            JSONArray patch = JsonComparator.generateJsonPatch(obj1, obj2);

            if (patch.length() == 0) {
                sb.append(formatLine(GREEN + "No differences found - objects are identical" + RESET)).append("\n");
            } else {
                sb.append(formatLine("Operations to transform Object 1 into Object 2:")).append("\n");
                sb.append(formatLine("")).append("\n");

                for (int i = 0; i < patch.length(); i++) {
                    JSONObject operation = patch.getJSONObject(i);
                    String op = operation.getString("op");
                    String path = operation.getString("path");

                    String opColor = op.equals("add") ? GREEN : (op.equals("remove") ? RED : YELLOW);
                    String opSymbol = op.equals("add") ? "+" : (op.equals("remove") ? "-" : "~");

                    sb.append(formatLine(opColor + opSymbol + " " + op.toUpperCase() + RESET + " " + BLUE + path + RESET));

                    if (operation.has("value")) {
                        String valueStr = formatValue(operation.get("value"));
                        sb.append(formatLine("  Value: " + valueStr));
                    }
                    sb.append("\n");
                }
            }
        } catch (JSONException e) {
            sb.append(formatLine(RED + "Error generating patch: " + e.getMessage() + RESET)).append("\n");
        }

        sb.append(BOX_BOTTOM).append("\n");
        return sb.toString();
    }

    /**
     * Renders a full comparison report with all available comparison methods
     */
    public static String renderFullReport(JSONObject obj1, JSONObject obj2, String label) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append("═".repeat(80)).append("\n");
        sb.append(BOLD + CYAN + "FULL COMPARISON REPORT: " + label + RESET).append("\n");
        sb.append("═".repeat(80)).append("\n\n");

        // Basic comparison
        sb.append(renderComparison(obj1, obj2, label));
        sb.append("\n");

        // Patch format
        sb.append(renderPatchDiff(obj1, obj2, label));
        sb.append("\n");

        // Path-based diff
        try {
            JSONArray pathDiff = JsonComparator.generateJsonDiff("", obj1, obj2);
            if (pathDiff.length() > 0) {
                sb.append(BOX_TOP).append("\n");
                sb.append(formatLine(BOLD + CYAN + "PATH-BASED DIFFERENCES" + RESET)).append("\n");
                sb.append(BOX_MIDDLE).append("\n");
                sb.append(formatJsonContent(pathDiff.toString(2))).append("\n");
                sb.append(BOX_BOTTOM).append("\n");
            }
        } catch (JSONException e) {
            sb.append("Error generating path-based diff: ").append(e.getMessage()).append("\n");
        }

        sb.append("\n").append("═".repeat(80)).append("\n");
        return sb.toString();
    }

    // Helper methods

    private static String formatLine(String content) {
        return BOX_SIDE + " " + content;
    }

    private static String formatJsonContent(String json) {
        StringBuilder sb = new StringBuilder();
        String[] lines = json.split("\n");
        for (String line : lines) {
            sb.append(formatLine(line)).append("\n");
        }
        return sb.toString();
    }

    private static String formatValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof JSONObject) {
            try {
                return ((JSONObject) value).toString(2);
            } catch (JSONException e) {
                return value.toString();
            }
        }
        if (value instanceof JSONArray) {
            try {
                return ((JSONArray) value).toString(2);
            } catch (JSONException e) {
                return value.toString();
            }
        }
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        return value.toString();
    }
}
