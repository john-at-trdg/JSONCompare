package com.trdg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Visual version of JsonComparatorTest using VisualAssert for better test output.
 * This demonstrates how VisualAssert can make test failures easier to understand.
 */
@DisplayName("JsonComparator Visual Tests")
class JsonComparatorVisualTest {

    // Tests for JSON Objects - Equality

    @Test
    @DisplayName("Visual: Equal JSONObjects should pass")
    void testEqualJsonObjects() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");

        VisualAssert.assertJsonEquals(
            "Equal Objects Test",
            obj1,
            obj2,
            "Comparing two identical person objects"
        );
    }

    @Test
    @DisplayName("Visual: Different JSONObjects should pass NotEquals assertion")
    void testDifferentJsonObjects() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"Jane\",\"age\":25}");

        VisualAssert.assertJsonNotEquals(
            "Different Objects Test",
            obj1,
            obj2,
            "Comparing two different person objects - expecting differences"
        );
    }

    @Test
    @DisplayName("Visual: Objects with missing key should be different")
    void testObjectsWithMissingKey() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30,\"email\":\"john@example.com\"}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30}");

        VisualAssert.assertJsonNotEquals(
            "Missing Key Test",
            obj1,
            obj2,
            "Object 1 has email, Object 2 doesn't - expecting difference"
        );
    }

    // Tests for Nested Objects

    @Test
    @DisplayName("Visual: Equal nested objects should pass")
    void testEqualNestedObjects() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"person\":{\"name\":\"John\",\"age\":30}}");
        JSONObject obj2 = new JSONObject("{\"person\":{\"name\":\"John\",\"age\":30}}");

        VisualAssert.assertJsonEquals(
            "Equal Nested Objects",
            obj1,
            obj2,
            "Comparing objects with nested person data"
        );
    }

    @Test
    @DisplayName("Visual: Different nested objects should be different")
    void testDifferentNestedObjects() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"person\":{\"name\":\"John\",\"age\":30}}");
        JSONObject obj2 = new JSONObject("{\"person\":{\"name\":\"Jane\",\"age\":30}}");

        VisualAssert.assertJsonNotEquals(
            "Different Nested Objects",
            obj1,
            obj2,
            "Nested person names differ - expecting difference"
        );
    }

    // Tests for JSON Arrays

    @Test
    @DisplayName("Visual: Equal arrays should pass")
    void testEqualArrays() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3, 4, 5]");
        JSONArray arr2 = new JSONArray("[1, 2, 3, 4, 5]");

        VisualAssert.assertJsonArrayEquals(
            "Equal Arrays Test",
            arr1,
            arr2,
            "Comparing two identical number arrays"
        );
    }

    @Test
    @DisplayName("Visual: Different arrays should be different")
    void testDifferentArrays() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 4]");

        VisualAssert.assertJsonArrayNotEquals(
            "Different Arrays Test",
            arr1,
            arr2,
            "Last element differs (3 vs 4) - expecting difference"
        );
    }

    @Test
    @DisplayName("Visual: Different length arrays should be different")
    void testDifferentLengthArrays() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 3, 4, 5]");

        VisualAssert.assertJsonArrayNotEquals(
            "Different Length Arrays",
            arr1,
            arr2,
            "Arrays have different lengths (3 vs 5) - expecting difference"
        );
    }

    // Tests for Arrays with Objects

    @Test
    @DisplayName("Visual: Equal arrays with nested objects should pass")
    void testEqualArraysWithNestedObjects() throws JSONException {
        JSONArray arr1 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Jane\"}]");
        JSONArray arr2 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Jane\"}]");

        VisualAssert.assertJsonArrayEquals(
            "Equal Arrays with Objects",
            arr1,
            arr2,
            "Comparing arrays containing person objects"
        );
    }

    @Test
    @DisplayName("Visual: Arrays with different nested objects should be different")
    void testArraysWithDifferentNestedObjects() throws JSONException {
        JSONArray arr1 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Jane\"}]");
        JSONArray arr2 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Bob\"}]");

        VisualAssert.assertJsonArrayNotEquals(
            "Different Nested Objects in Array",
            arr1,
            arr2,
            "Second person name differs (Jane vs Bob) - expecting difference"
        );
    }

    // Tests for Nested Arrays

    @Test
    @DisplayName("Visual: Equal nested arrays should pass")
    void testEqualNestedArrays() throws JSONException {
        JSONArray arr1 = new JSONArray("[[1, 2], [3, 4]]");
        JSONArray arr2 = new JSONArray("[[1, 2], [3, 4]]");

        VisualAssert.assertJsonArrayEquals(
            "Equal Nested Arrays",
            arr1,
            arr2,
            "Comparing 2D arrays with same values"
        );
    }

    @Test
    @DisplayName("Visual: Different nested arrays should be different")
    void testDifferentNestedArrays() throws JSONException {
        JSONArray arr1 = new JSONArray("[[1, 2], [3, 4]]");
        JSONArray arr2 = new JSONArray("[[1, 2], [3, 5]]");

        VisualAssert.assertJsonArrayNotEquals(
            "Different Nested Arrays",
            arr1,
            arr2,
            "Last element in second sub-array differs (4 vs 5) - expecting difference"
        );
    }

    // Complex Integration Tests

    @Test
    @DisplayName("Visual: Complex nested structure - equal")
    void testComplexNestedStructureEqual() throws JSONException {
        String json1 = "{\"company\":{\"name\":\"Acme\",\"employees\":[{\"name\":\"John\",\"dept\":\"IT\"},{\"name\":\"Jane\",\"dept\":\"HR\"}]}}";
        String json2 = "{\"company\":{\"name\":\"Acme\",\"employees\":[{\"name\":\"John\",\"dept\":\"IT\"},{\"name\":\"Jane\",\"dept\":\"HR\"}]}}";

        JSONObject obj1 = new JSONObject(json1);
        JSONObject obj2 = new JSONObject(json2);

        VisualAssert.assertJsonEquals(
            "Complex Company Structure - Equal",
            obj1,
            obj2,
            "Comparing complex company objects with nested employee arrays"
        );
    }

    @Test
    @DisplayName("Visual: Complex nested structure - different")
    void testComplexNestedStructureDifferent() throws JSONException {
        String json1 = "{\"company\":{\"name\":\"Acme\",\"employees\":[{\"name\":\"John\",\"dept\":\"IT\"},{\"name\":\"Jane\",\"dept\":\"HR\"}]}}";
        String json2 = "{\"company\":{\"name\":\"Acme\",\"employees\":[{\"name\":\"John\",\"dept\":\"IT\"},{\"name\":\"Bob\",\"dept\":\"HR\"}]}}";

        JSONObject obj1 = new JSONObject(json1);
        JSONObject obj2 = new JSONObject(json2);

        VisualAssert.assertJsonNotEquals(
            "Complex Company Structure - Different",
            obj1,
            obj2,
            "Second employee name differs (Jane vs Bob) - expecting difference in nested structure"
        );
    }

    // Edge Cases

    @Test
    @DisplayName("Visual: Empty objects should be equal")
    void testEmptyObjectsEqual() throws JSONException {
        JSONObject obj1 = new JSONObject("{}");
        JSONObject obj2 = new JSONObject("{}");

        VisualAssert.assertJsonEquals(
            "Empty Objects Test",
            obj1,
            obj2,
            "Comparing two empty JSON objects"
        );
    }

    @Test
    @DisplayName("Visual: Empty arrays should be equal")
    void testEmptyArraysEqual() throws JSONException {
        JSONArray arr1 = new JSONArray("[]");
        JSONArray arr2 = new JSONArray("[]");

        VisualAssert.assertJsonArrayEquals(
            "Empty Arrays Test",
            arr1,
            arr2,
            "Comparing two empty JSON arrays"
        );
    }

    @Test
    @DisplayName("Visual: Object vs empty object should be different")
    void testObjectVsEmptyObject() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\"}");
        JSONObject obj2 = new JSONObject("{}");

        VisualAssert.assertJsonNotEquals(
            "Object vs Empty Object",
            obj1,
            obj2,
            "One object has data, other is empty - expecting difference"
        );
    }

    // Real-world Scenarios

    @Test
    @DisplayName("Visual: API response comparison - matching")
    void testApiResponseMatching() throws JSONException {
        JSONObject expected = new JSONObject()
            .put("status", "success")
            .put("code", 200)
            .put("data", new JSONObject()
                .put("userId", 12345)
                .put("username", "johndoe")
                .put("email", "john@example.com"));

        JSONObject actual = new JSONObject()
            .put("status", "success")
            .put("code", 200)
            .put("data", new JSONObject()
                .put("userId", 12345)
                .put("username", "johndoe")
                .put("email", "john@example.com"));

        VisualAssert.assertJsonEquals(
            "API Response Validation",
            expected,
            actual,
            "Verifying API response matches expected structure and values"
        );
    }

    @Test
    @DisplayName("Visual: API response comparison - field value mismatch")
    void testApiResponseFieldMismatch() throws JSONException {
        JSONObject expected = new JSONObject()
            .put("status", "success")
            .put("code", 200)
            .put("data", new JSONObject()
                .put("userId", 12345)
                .put("username", "johndoe"));

        JSONObject actual = new JSONObject()
            .put("status", "success")
            .put("code", 200)
            .put("data", new JSONObject()
                .put("userId", 12345)
                .put("username", "janedoe"));  // Different username

        VisualAssert.assertJsonNotEquals(
            "API Response Field Mismatch",
            expected,
            actual,
            "API response has different username - detecting regression"
        );
    }

    @Test
    @DisplayName("Visual: Configuration comparison - environment differences")
    void testConfigurationEnvironmentDifferences() throws JSONException {
        JSONObject devConfig = new JSONObject()
            .put("environment", "development")
            .put("database", new JSONObject()
                .put("host", "localhost")
                .put("port", 5432)
                .put("name", "dev_db"))
            .put("debug", true);

        JSONObject prodConfig = new JSONObject()
            .put("environment", "production")
            .put("database", new JSONObject()
                .put("host", "prod-db-server")
                .put("port", 5432)
                .put("name", "prod_db"))
            .put("debug", false);

        VisualAssert.assertJsonNotEquals(
            "Dev vs Prod Config",
            devConfig,
            prodConfig,
            "Verifying dev and prod configurations are different as expected"
        );
    }
}
