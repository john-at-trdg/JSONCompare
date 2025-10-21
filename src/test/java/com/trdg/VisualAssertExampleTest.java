package com.trdg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Example test class demonstrating the VisualAssert and TestVisualizer features.
 * Run these tests to see the visualization in action!
 */
@DisplayName("Visual Assert Examples")
class VisualAssertExampleTest {

    @Test
    @DisplayName("Example: Compare two different person objects")
    void exampleCompareDifferentPersons() throws JSONException {
        JSONObject person1 = new JSONObject()
            .put("name", "John Doe")
            .put("age", 30)
            .put("city", "New York")
            .put("occupation", "Engineer");

        JSONObject person2 = new JSONObject()
            .put("name", "Jane Smith")
            .put("age", 28)
            .put("city", "New York")
            .put("occupation", "Designer");

        // Assert that these two persons are NOT equal (we expect them to be different)
        VisualAssert.AssertionResult result = VisualAssert.assertJsonNotEquals(
            "Person Comparison",
            person1,
            person2,
            "Comparing two employee records - expecting differences"
        );

        System.out.println("\nTest completed. Result: " + (result.passed ? "PASSED" : "FAILED"));
        System.out.println("Message: " + result.message);
    }

    @Test
    @DisplayName("Example: Compare nested JSON objects")
    void exampleCompareNestedObjects() throws JSONException {
        JSONObject company1 = new JSONObject()
            .put("name", "TechCorp")
            .put("founded", 2010)
            .put("address", new JSONObject()
                .put("street", "123 Main St")
                .put("city", "Boston")
                .put("zip", "02101"))
            .put("employees", 500);

        JSONObject company2 = new JSONObject()
            .put("name", "TechCorp")
            .put("founded", 2010)
            .put("address", new JSONObject()
                .put("street", "456 Oak Ave")  // Different street
                .put("city", "Boston")
                .put("zip", "02101"))
            .put("employees", 750);  // Different employee count

        // Assert that companies are NOT equal (we expect differences)
        VisualAssert.assertJsonNotEquals(
            "Company Comparison",
            company1,
            company2,
            "Comparing company records - expecting address and employee differences"
        );
    }

    @Test
    @DisplayName("Example: Compare JSON arrays")
    void exampleCompareArrays() throws JSONException {
        JSONArray array1 = new JSONArray()
            .put(new JSONObject().put("id", 1).put("name", "Alice"))
            .put(new JSONObject().put("id", 2).put("name", "Bob"))
            .put(new JSONObject().put("id", 3).put("name", "Charlie"));

        JSONArray array2 = new JSONArray()
            .put(new JSONObject().put("id", 1).put("name", "Alice"))
            .put(new JSONObject().put("id", 2).put("name", "Robert"))  // Different name
            .put(new JSONObject().put("id", 3).put("name", "Charlie"));

        // Assert that arrays are NOT equal (we expect Bob vs Robert difference)
        VisualAssert.assertJsonArrayNotEquals(
            "User Array Comparison",
            array1,
            array2,
            "Comparing user lists - expecting name difference in second user"
        );
    }

    @Test
    @DisplayName("Example: Compare primitive values")
    void exampleCompareValues() throws JSONException {
        String value1 = "Hello World";
        String value2 = "Hello Universe";

        // Assert that values are NOT equal (World vs Universe)
        VisualAssert.assertJsonNotEquals(
            "String Value Comparison",
            new JSONObject().put("greeting", value1),
            new JSONObject().put("greeting", value2),
            "Comparing greeting messages - expecting different values"
        );
    }

    @Test
    @DisplayName("Example: Visualize patch diff")
    void exampleVisualizePatchDiff() throws JSONException {
        JSONObject before = new JSONObject()
            .put("version", "1.0.0")
            .put("features", new JSONArray().put("login").put("search"))
            .put("bugFixes", 5);

        JSONObject after = new JSONObject()
            .put("version", "1.1.0")  // Version changed
            .put("features", new JSONArray().put("login").put("search").put("export"))  // Feature added
            .put("bugFixes", 12)  // More bug fixes
            .put("newFeature", true);  // New field added

        System.out.println("\n=== Demonstrating Patch-based Diff Visualization ===");
        VisualAssert.visualizePatchDiff("Version Update", before, after);
    }

    @Test
    @DisplayName("Example: Full comparison report")
    void exampleFullReport() throws JSONException {
        JSONObject config1 = new JSONObject()
            .put("database", new JSONObject()
                .put("host", "localhost")
                .put("port", 5432)
                .put("name", "mydb"))
            .put("cache", new JSONObject()
                .put("enabled", true)
                .put("ttl", 3600));

        JSONObject config2 = new JSONObject()
            .put("database", new JSONObject()
                .put("host", "prod-server")  // Different host
                .put("port", 5432)
                .put("name", "mydb_prod"))  // Different db name
            .put("cache", new JSONObject()
                .put("enabled", false)  // Cache disabled
                .put("ttl", 7200));  // Different TTL

        System.out.println("\n=== Demonstrating Full Comparison Report ===");
        VisualAssert.visualizeComparison("Configuration Comparison", config1, config2);
    }

    @Test
    @DisplayName("Example: Compare equal objects (should pass)")
    void exampleCompareEqualObjects() throws JSONException {
        JSONObject obj1 = new JSONObject()
            .put("status", "active")
            .put("count", 42)
            .put("verified", true);

        JSONObject obj2 = new JSONObject()
            .put("status", "active")
            .put("count", 42)
            .put("verified", true);

        VisualAssert.checkJsonEquals(
            "Equal Objects Test",
            obj1,
            obj2,
            "These objects should be identical"
        );
    }

    @Test
    @DisplayName("Example: Using TestVisualizer directly")
    void exampleTestVisualizerDirect() throws JSONException {
        JSONObject product1 = new JSONObject()
            .put("name", "Laptop")
            .put("price", 999.99)
            .put("inStock", true)
            .put("specs", new JSONObject()
                .put("cpu", "Intel i7")
                .put("ram", "16GB"));

        JSONObject product2 = new JSONObject()
            .put("name", "Laptop")
            .put("price", 899.99)  // Different price
            .put("inStock", false)  // Out of stock
            .put("specs", new JSONObject()
                .put("cpu", "Intel i7")
                .put("ram", "32GB"));  // More RAM

        System.out.println("\n=== Using TestVisualizer Directly ===");

        // Basic comparison
        System.out.println(TestVisualizer.renderComparison(product1, product2, "Product Comparison"));

        // Patch-based diff
        System.out.println(TestVisualizer.renderPatchDiff(product1, product2, "Product Changes"));
    }

    @Test
    @DisplayName("Example: Array length difference")
    void exampleArrayLengthDifference() throws JSONException {
        JSONArray shortArray = new JSONArray()
            .put("item1")
            .put("item2");

        JSONArray longArray = new JSONArray()
            .put("item1")
            .put("item2")
            .put("item3")
            .put("item4");

        // Assert that arrays are NOT equal (different lengths)
        VisualAssert.assertJsonArrayNotEquals(
            "Array Length Test",
            shortArray,
            longArray,
            "Comparing arrays of different lengths - expecting 2 vs 4 elements"
        );
    }

    @Test
    @DisplayName("Example: Complex nested structure")
    void exampleComplexNested() throws JSONException {
        JSONObject complex1 = new JSONObject()
            .put("project", new JSONObject()
                .put("name", "JSONCompare")
                .put("version", "1.0.0")
                .put("contributors", new JSONArray()
                    .put(new JSONObject().put("name", "Alice").put("role", "Developer"))
                    .put(new JSONObject().put("name", "Bob").put("role", "Tester")))
                .put("dependencies", new JSONArray()
                    .put("junit")
                    .put("json-simple")));

        JSONObject complex2 = new JSONObject()
            .put("project", new JSONObject()
                .put("name", "JSONCompare")
                .put("version", "2.0.0")  // Version bump
                .put("contributors", new JSONArray()
                    .put(new JSONObject().put("name", "Alice").put("role", "Lead Developer"))  // Role changed
                    .put(new JSONObject().put("name", "Bob").put("role", "Tester"))
                    .put(new JSONObject().put("name", "Charlie").put("role", "DevOps")))  // New contributor
                .put("dependencies", new JSONArray()
                    .put("junit")
                    .put("org.json")));  // Dependency changed

        VisualAssert.visualizeComparison("Complex Project Structure", complex1, complex2);
    }
}
