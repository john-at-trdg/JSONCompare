package com.trdg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonComparator Tests")
class JsonComparatorTest {

    @BeforeEach
    void setUp() {
        // Reset static state before each test if needed
    }

    // Tests for areValuesEqual method
    @Test
    @DisplayName("areValuesEqual - Equal primitive values should return true")
    void testAreValuesEqualPrimitiveTrue() throws JSONException {
        assertTrue(JsonComparator.areValuesEqual("test", "test", "key1"));
        assertTrue(JsonComparator.areValuesEqual(123, 123, "key2"));
        assertTrue(JsonComparator.areValuesEqual(true, true, "key3"));
    }

    @Test
    @DisplayName("areValuesEqual - Different primitive values should return false")
    void testAreValuesEqualPrimitiveFalse() throws JSONException {
        assertFalse(JsonComparator.areValuesEqual("test", "test2", "key1"));
        assertFalse(JsonComparator.areValuesEqual(123, 456, "key2"));
        assertFalse(JsonComparator.areValuesEqual(true, false, "key3"));
    }

    @Test
    @DisplayName("areValuesEqual - Equal JSONObjects should return true")
    void testAreValuesEqualJsonObjectsTrue() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30}");

        assertTrue(JsonComparator.areValuesEqual(obj1, obj2, "person"));
    }

    @Test
    @DisplayName("areValuesEqual - Different JSONObjects should return false")
    void testAreValuesEqualJsonObjectsFalse() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"Jane\",\"age\":30}");

        assertFalse(JsonComparator.areValuesEqual(obj1, obj2, "person"));
    }

    @Test
    @DisplayName("areValuesEqual - Equal JSONArrays should return true")
    void testAreValuesEqualJsonArraysTrue() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 3]");

        assertTrue(JsonComparator.areValuesEqual(arr1, arr2, "numbers"));
    }

    @Test
    @DisplayName("areValuesEqual - Different JSONArrays should return false")
    void testAreValuesEqualJsonArraysFalse() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 4]");

        assertFalse(JsonComparator.areValuesEqual(arr1, arr2, "numbers"));
    }

    // Tests for areJsonEntitiesEqual method
    @Test
    @DisplayName("areJsonEntitiesEqual - Identical objects should return true")
    void testAreJsonEntitiesEqualIdentical() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}");

        assertTrue(JsonComparator.areJsonEntitiesEqual(obj1, obj2));
    }

    @Test
    @DisplayName("areJsonEntitiesEqual - Objects with different values should return false")
    void testAreJsonEntitiesEqualDifferentValues() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"Jane\",\"age\":30}");

        assertFalse(JsonComparator.areJsonEntitiesEqual(obj1, obj2));
    }

    @Test
    @DisplayName("areJsonEntitiesEqual - Object with missing key should return false")
    void testAreJsonEntitiesEqualMissingKey() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\"}");

        assertFalse(JsonComparator.areJsonEntitiesEqual(obj1, obj2));
    }

    @Test
    @DisplayName("areJsonEntitiesEqual - Nested objects should be compared correctly")
    void testAreJsonEntitiesEqualNested() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"person\":{\"name\":\"John\",\"age\":30}}");
        JSONObject obj2 = new JSONObject("{\"person\":{\"name\":\"John\",\"age\":30}}");

        assertTrue(JsonComparator.areJsonEntitiesEqual(obj1, obj2));
    }

    @Test
    @DisplayName("areJsonEntitiesEqual - Different nested objects should return false")
    void testAreJsonEntitiesEqualNestedDifferent() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"person\":{\"name\":\"John\",\"age\":30}}");
        JSONObject obj2 = new JSONObject("{\"person\":{\"name\":\"Jane\",\"age\":30}}");

        assertFalse(JsonComparator.areJsonEntitiesEqual(obj1, obj2));
    }

    // Tests for compareJsonArrays method
    @Test
    @DisplayName("compareJsonArrays - Equal arrays should return true")
    void testCompareJsonArraysEqual() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 3]");

        assertTrue(JsonComparator.compareJsonArrays(arr1, arr2));
    }

    @Test
    @DisplayName("compareJsonArrays - Different arrays should return false")
    void testCompareJsonArraysDifferent() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 4]");

        assertFalse(JsonComparator.compareJsonArrays(arr1, arr2));
    }

    @Test
    @DisplayName("compareJsonArrays - Different length arrays should return false")
    void testCompareJsonArraysDifferentLength() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2]");

        assertFalse(JsonComparator.compareJsonArrays(arr1, arr2));
    }

    @Test
    @DisplayName("compareJsonArrays - Arrays with nested objects should be compared")
    void testCompareJsonArraysNestedObjects() throws JSONException {
        JSONArray arr1 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Jane\"}]");
        JSONArray arr2 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Jane\"}]");

        assertTrue(JsonComparator.compareJsonArrays(arr1, arr2));
    }

    @Test
    @DisplayName("compareJsonArrays - Arrays with different nested objects should return false")
    void testCompareJsonArraysNestedObjectsDifferent() throws JSONException {
        JSONArray arr1 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Jane\"}]");
        JSONArray arr2 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Bob\"}]");

        assertFalse(JsonComparator.compareJsonArrays(arr1, arr2));
    }

    @Test
    @DisplayName("compareJsonArrays - Nested arrays should be compared")
    void testCompareJsonArraysNestedArrays() throws JSONException {
        JSONArray arr1 = new JSONArray("[[1, 2], [3, 4]]");
        JSONArray arr2 = new JSONArray("[[1, 2], [3, 4]]");

        assertTrue(JsonComparator.compareJsonArrays(arr1, arr2));
    }

    // Tests for generateJsonDiff (JSONObject version)
    @Test
    @DisplayName("generateJsonDiff - Identical objects should return empty diff")
    void testGenerateJsonDiffIdentical() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30}");

        JSONObject diff = JsonComparator.generateJsonDiff(obj1, obj2);
        assertTrue(diff.isEmpty());
    }

    @Test
    @DisplayName("generateJsonDiff - Different values should be reported")
    void testGenerateJsonDiffDifferentValues() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"Jane\",\"age\":30}");

        JSONObject diff = JsonComparator.generateJsonDiff(obj1, obj2);
        assertFalse(diff.isEmpty());
        assertTrue(diff.has("name"));
    }

    @Test
    @DisplayName("generateJsonDiff - Missing keys should be reported")
    void testGenerateJsonDiffMissingKey() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\"}");

        JSONObject diff = JsonComparator.generateJsonDiff(obj1, obj2);
        assertTrue(diff.has("age"));
    }

    @Test
    @DisplayName("generateJsonDiff - Additional keys should be reported")
    void testGenerateJsonDiffAdditionalKey() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\"}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30}");

        JSONObject diff = JsonComparator.generateJsonDiff(obj1, obj2);
        assertTrue(diff.has("age"));
    }

    @Test
    @DisplayName("generateJsonDiff - Nested objects should be compared")
    void testGenerateJsonDiffNested() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"person\":{\"name\":\"John\",\"age\":30}}");
        JSONObject obj2 = new JSONObject("{\"person\":{\"name\":\"Jane\",\"age\":30}}");

        JSONObject diff = JsonComparator.generateJsonDiff(obj1, obj2);
        assertTrue(diff.has("person"));
        assertTrue(diff.getJSONObject("person").has("name"));
    }

    @Test
    @DisplayName("generateJsonDiff - Arrays should be compared with generateJsonArrayDiff")
    void testGenerateJsonDiffArrays() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"numbers\":[1, 2, 3]}");
        JSONObject obj2 = new JSONObject("{\"numbers\":[1, 2, 4]}");

        JSONObject diff = JsonComparator.generateJsonDiff(obj1, obj2);
        assertTrue(diff.has("numbers"));
    }

    // Tests for generateJsonArrayDiff (JSONObject version)
    @Test
    @DisplayName("generateJsonArrayDiff - Equal arrays should return empty diff")
    void testGenerateJsonArrayDiffEqual() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 3]");

        JSONObject diff = JsonComparator.generateJsonArrayDiff(arr1, arr2, "numbers");
        assertTrue(diff.isEmpty());
    }

    @Test
    @DisplayName("generateJsonArrayDiff - Different elements should be reported")
    void testGenerateJsonArrayDiffDifferentElements() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 4]");

        JSONObject diff = JsonComparator.generateJsonArrayDiff(arr1, arr2, "numbers");
        assertTrue(diff.has("[2]"));
    }

    @Test
    @DisplayName("generateJsonArrayDiff - Different lengths should be reported")
    void testGenerateJsonArrayDiffDifferentLength() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2]");

        JSONObject diff = JsonComparator.generateJsonArrayDiff(arr1, arr2, "numbers");
        assertTrue(diff.has("length"));
        assertTrue(diff.has("[2]"));
    }

    @Test
    @DisplayName("generateJsonArrayDiff - Nested objects in arrays should be compared")
    void testGenerateJsonArrayDiffNestedObjects() throws JSONException {
        JSONArray arr1 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Jane\"}]");
        JSONArray arr2 = new JSONArray("[{\"name\":\"John\"},{\"name\":\"Bob\"}]");

        JSONObject diff = JsonComparator.generateJsonArrayDiff(arr1, arr2, "people");
        assertTrue(diff.has("[1]"));
    }

    @Test
    @DisplayName("generateJsonArrayDiff - Nested arrays should be compared")
    void testGenerateJsonArrayDiffNestedArrays() throws JSONException {
        JSONArray arr1 = new JSONArray("[[1, 2], [3, 4]]");
        JSONArray arr2 = new JSONArray("[[1, 2], [3, 5]]");

        JSONObject diff = JsonComparator.generateJsonArrayDiff(arr1, arr2, "matrix");
        assertTrue(diff.has("[1]"));
    }

    @Test
    @DisplayName("generateJsonArrayDiff - Extra elements in second array should be reported")
    void testGenerateJsonArrayDiffExtraInSecond() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2]");
        JSONArray arr2 = new JSONArray("[1, 2, 3]");

        JSONObject diff = JsonComparator.generateJsonArrayDiff(arr1, arr2, "numbers");
        assertTrue(diff.has("[2]"));
        assertTrue(diff.getString("[2]").contains("only in second array"));
    }

    // Tests for generateJsonPatch method
    @Test
    @DisplayName("generateJsonPatch - Identical objects should return empty patch")
    void testGenerateJsonPatchIdentical() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30}");

        JSONArray patch = JsonComparator.generateJsonPatch(obj1, obj2);
        assertEquals(0, patch.length());
    }

    @Test
    @DisplayName("generateJsonPatch - Replace operation for different values")
    void testGenerateJsonPatchReplace() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"Jane\",\"age\":30}");

        JSONArray patch = JsonComparator.generateJsonPatch(obj1, obj2);
        assertEquals(1, patch.length());
        assertEquals("replace", patch.getJSONObject(0).getString("op"));
        assertEquals("/name", patch.getJSONObject(0).getString("path"));
        assertEquals("Jane", patch.getJSONObject(0).getString("value"));
    }

    @Test
    @DisplayName("generateJsonPatch - Add operation for new keys")
    void testGenerateJsonPatchAdd() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\"}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30}");

        JSONArray patch = JsonComparator.generateJsonPatch(obj1, obj2);
        assertEquals(1, patch.length());
        assertEquals("add", patch.getJSONObject(0).getString("op"));
        assertEquals("/age", patch.getJSONObject(0).getString("path"));
    }

    @Test
    @DisplayName("generateJsonPatch - Remove operation for deleted keys")
    void testGenerateJsonPatchRemove() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\"}");

        JSONArray patch = JsonComparator.generateJsonPatch(obj1, obj2);
        assertEquals(1, patch.length());
        assertEquals("remove", patch.getJSONObject(0).getString("op"));
        assertEquals("/age", patch.getJSONObject(0).getString("path"));
    }

    // Tests for generateJsonDiff with path (String parent version)
    @Test
    @DisplayName("generateJsonDiff with path - Identical objects should return empty array")
    void testGenerateJsonDiffWithPathIdentical() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"name\":\"John\",\"age\":30}");
        JSONObject obj2 = new JSONObject("{\"name\":\"John\",\"age\":30}");

        JSONArray diffs = JsonComparator.generateJsonDiff("", obj1, obj2);
        assertEquals(0, diffs.length());
    }

    @Test
    @DisplayName("generateJsonDiff with path - Should generate correct paths")
    void testGenerateJsonDiffWithPathCorrectPath() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"person\":{\"name\":\"John\"}}");
        JSONObject obj2 = new JSONObject("{\"person\":{\"name\":\"Jane\"}}");

        JSONArray diffs = JsonComparator.generateJsonDiff("", obj1, obj2);
        assertEquals(1, diffs.length());
        assertEquals("person/name", diffs.getJSONObject(0).getString("path"));
    }

    @Test
    @DisplayName("generateJsonDiff with path - Nested arrays should generate operations")
    void testGenerateJsonDiffWithPathArrays() throws JSONException {
        JSONObject obj1 = new JSONObject("{\"numbers\":[1, 2, 3]}");
        JSONObject obj2 = new JSONObject("{\"numbers\":[1, 2, 4]}");

        JSONArray diffs = JsonComparator.generateJsonDiff("", obj1, obj2);
        assertTrue(diffs.length() > 0);
        assertEquals("replace", diffs.getJSONObject(0).getString("op"));
        assertEquals("numbers/2", diffs.getJSONObject(0).getString("path"));
    }

    // Tests for generateJsonArrayDiff with path
    @Test
    @DisplayName("generateJsonArrayDiff with path - Equal arrays should return empty")
    void testGenerateJsonArrayDiffWithPathEqual() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 3]");

        JSONArray diffs = JsonComparator.generateJsonArrayDiff("numbers", arr1, arr2);
        assertEquals(0, diffs.length());
    }

    @Test
    @DisplayName("generateJsonArrayDiff with path - Replace operations should have correct paths")
    void testGenerateJsonArrayDiffWithPathReplace() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2, 4]");

        JSONArray diffs = JsonComparator.generateJsonArrayDiff("numbers", arr1, arr2);
        assertEquals(1, diffs.length());
        assertEquals("replace", diffs.getJSONObject(0).getString("op"));
        assertEquals("numbers/2", diffs.getJSONObject(0).getString("path"));
        assertEquals(4, diffs.getJSONObject(0).getInt("value"));
    }

    @Test
    @DisplayName("generateJsonArrayDiff with path - Add operations for extra elements")
    void testGenerateJsonArrayDiffWithPathAdd() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2]");
        JSONArray arr2 = new JSONArray("[1, 2, 3]");

        JSONArray diffs = JsonComparator.generateJsonArrayDiff("numbers", arr1, arr2);
        assertEquals(1, diffs.length());
        assertEquals("add", diffs.getJSONObject(0).getString("op"));
        assertEquals("numbers/2", diffs.getJSONObject(0).getString("path"));
    }

    @Test
    @DisplayName("generateJsonArrayDiff with path - Remove operations for missing elements")
    void testGenerateJsonArrayDiffWithPathRemove() throws JSONException {
        JSONArray arr1 = new JSONArray("[1, 2, 3]");
        JSONArray arr2 = new JSONArray("[1, 2]");

        JSONArray diffs = JsonComparator.generateJsonArrayDiff("numbers", arr1, arr2);
        assertEquals(1, diffs.length());
        assertEquals("remove", diffs.getJSONObject(0).getString("op"));
        assertEquals("numbers/2", diffs.getJSONObject(0).getString("path"));
    }

    @Test
    @DisplayName("generateJsonArrayDiff with path - Nested objects in arrays with correct paths")
    void testGenerateJsonArrayDiffWithPathNestedObjects() throws JSONException {
        JSONArray arr1 = new JSONArray("[{\"name\":\"John\",\"age\":30}]");
        JSONArray arr2 = new JSONArray("[{\"name\":\"Jane\",\"age\":30}]");

        JSONArray diffs = JsonComparator.generateJsonArrayDiff("people", arr1, arr2);
        assertEquals(1, diffs.length());
        assertEquals("replace", diffs.getJSONObject(0).getString("op"));
        assertEquals("people/0/name", diffs.getJSONObject(0).getString("path"));
    }

    @Test
    @DisplayName("Integration test - Complex nested structure")
    void testComplexNestedStructure() throws JSONException {
        String json1 = "{\"company\":{\"name\":\"Acme\",\"employees\":[{\"name\":\"John\",\"dept\":\"IT\"},{\"name\":\"Jane\",\"dept\":\"HR\"}]}}";
        String json2 = "{\"company\":{\"name\":\"Acme\",\"employees\":[{\"name\":\"John\",\"dept\":\"IT\"},{\"name\":\"Bob\",\"dept\":\"HR\"}]}}";

        JSONObject obj1 = new JSONObject(json1);
        JSONObject obj2 = new JSONObject(json2);

        JSONArray diffs = JsonComparator.generateJsonDiff("", obj1, obj2);
        assertTrue(diffs.length() > 0);

        // Should have a replace operation for the employee name change
        boolean foundNameChange = false;
        for (int i = 0; i < diffs.length(); i++) {
            JSONObject diff = diffs.getJSONObject(i);
            if (diff.getString("path").contains("employees/1/name")) {
                foundNameChange = true;
                assertEquals("replace", diff.getString("op"));
                assertEquals("Bob", diff.getString("value"));
            }
        }
        assertTrue(foundNameChange, "Should find the name change in employees array");
    }
}
