package com.trdg;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class to demonstrate the prettyPrint flag functionality.
 */
@DisplayName("Pretty Print Flag Tests")
class PrettyPrintFlagTest {

    @AfterEach
    void resetPrettyPrint() {
        // Reset to default after each test
        VisualAssert.setPrettyPrint(true);
    }

    @Test
    @DisplayName("Pretty print enabled (default) - formatted JSON output")
    void testPrettyPrintEnabled() throws JSONException {
        // Pretty print is enabled by default
        VisualAssert.setPrettyPrint(true);

        JSONObject person1 = new JSONObject()
            .put("name", "Alice")
            .put("age", 30)
            .put("city", "New York")
            .put("occupation", "Engineer");

        JSONObject person2 = new JSONObject()
            .put("name", "Alice")
            .put("age", 30)
            .put("city", "New York")
            .put("occupation", "Engineer");

        System.out.println("\n========== PRETTY PRINT ENABLED ==========");
        VisualAssert.assertJsonEquals(
            "Pretty Print ON",
            person1,
            person2,
            "JSON should be formatted with indentation (pretty printed)"
        );
    }

    @Test
    @DisplayName("Pretty print disabled - compact JSON output")
    void testPrettyPrintDisabled() throws JSONException {
        // Disable pretty print for compact output
        VisualAssert.setPrettyPrint(false);

        JSONObject person1 = new JSONObject()
            .put("name", "Alice")
            .put("age", 30)
            .put("city", "New York")
            .put("occupation", "Engineer");

        JSONObject person2 = new JSONObject()
            .put("name", "Alice")
            .put("age", 30)
            .put("city", "New York")
            .put("occupation", "Engineer");

        System.out.println("\n========== PRETTY PRINT DISABLED ==========");
        VisualAssert.assertJsonEquals(
            "Pretty Print OFF",
            person1,
            person2,
            "JSON should be compact (single line, no indentation)"
        );
    }

    @Test
    @DisplayName("Comparison: Pretty print ON vs OFF")
    void testComparisonPrettyPrintOnVsOff() throws JSONException {
        JSONObject config = new JSONObject()
            .put("database", new JSONObject()
                .put("host", "localhost")
                .put("port", 5432)
                .put("name", "testdb"))
            .put("cache", new JSONObject()
                .put("enabled", true)
                .put("ttl", 3600));

        JSONObject sameConfig = new JSONObject()
            .put("database", new JSONObject()
                .put("host", "localhost")
                .put("port", 5432)
                .put("name", "testdb"))
            .put("cache", new JSONObject()
                .put("enabled", true)
                .put("ttl", 3600));

        // Test with pretty print ON
        System.out.println("\n========== COMPARISON WITH PRETTY PRINT ON ==========");
        VisualAssert.setPrettyPrint(true);
        VisualAssert.demonstrateJsonComparison(
            "Config Check (Pretty Print ON)",
            config,
            sameConfig,
            "Nested JSON with indentation - easier to read"
        );

        // Test with pretty print OFF
        System.out.println("\n========== COMPARISON WITH PRETTY PRINT OFF ==========");
        VisualAssert.setPrettyPrint(false);
        VisualAssert.demonstrateJsonComparison(
            "Config Check (Pretty Print OFF)",
            config,
            sameConfig,
            "Nested JSON compact - takes less space"
        );

        // Reset for other tests
        VisualAssert.setPrettyPrint(true);
    }

    @Test
    @DisplayName("Get/Set pretty print flag")
    void testGetSetPrettyPrint() {
        // Check default
        System.out.println("Default pretty print: " + VisualAssert.isPrettyPrint());

        // Set to false
        VisualAssert.setPrettyPrint(false);
        System.out.println("After setting to false: " + VisualAssert.isPrettyPrint());
        assert !VisualAssert.isPrettyPrint() : "Should be false";

        // Set to true
        VisualAssert.setPrettyPrint(true);
        System.out.println("After setting to true: " + VisualAssert.isPrettyPrint());
        assert VisualAssert.isPrettyPrint() : "Should be true";
    }

    @Test
    @DisplayName("Pretty print flag affects difference display")
    void testPrettyPrintAffectsDifferences() throws JSONException {
        // NOTE: This is a demonstration test that uses checkJsonEquals() (non-throwing).
        // It compares two DIFFERENT objects to show how the pretty print flag affects
        // the visualization of differences. This test is designed to PASS and display
        // visual output in both formats (pretty printed vs compact).

        JSONObject obj1 = new JSONObject()
            .put("user", new JSONObject()
                .put("name", "John")
                .put("email", "john@example.com")
                .put("roles", new org.json.JSONArray()
                    .put("admin")
                    .put("user")));

        JSONObject obj2 = new JSONObject()
            .put("user", new JSONObject()
                .put("name", "Jane")
                .put("email", "jane@example.com")
                .put("roles", new org.json.JSONArray()
                    .put("admin")
                    .put("moderator")));

        // Test with pretty print ON
        System.out.println("\n========== DIFFERENCES WITH PRETTY PRINT ON ==========");
        VisualAssert.setPrettyPrint(true);
        VisualAssert.demonstrateJsonComparison(
            "User Comparison (Pretty ON)",
            obj1,
            obj2,
            "Differences should be formatted with indentation"
        );

        // Test with pretty print OFF
        System.out.println("\n========== DIFFERENCES WITH PRETTY PRINT OFF ==========");
        VisualAssert.setPrettyPrint(false);
        VisualAssert.demonstrateJsonComparison(
            "User Comparison (Pretty OFF)",
            obj1,
            obj2,
            "Differences should be compact"
        );
    }
}
