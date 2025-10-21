package com.trdg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class to demonstrate that VisualAssert methods throw AssertionFailedError
 * like standard JUnit assertions.
 */
@DisplayName("Assertion Behavior Tests")
class AssertionBehaviorTest {

    @Test
    @DisplayName("assertJsonEquals throws AssertionFailedError on failure")
    void testAssertJsonEqualsThrowsAssertionFailedError() throws JSONException {
        JSONObject obj1 = new JSONObject().put("name", "Alice");
        JSONObject obj2 = new JSONObject().put("name", "Bob");

        // This should throw AssertionFailedError
        AssertionFailedError exception = assertThrows(AssertionFailedError.class, () -> {
            VisualAssert.assertJsonEquals("Test", obj1, obj2, "Testing AssertionFailedError");
        });

        // Verify the exception contains expected and actual values
        assertNotNull(exception.getExpected());
        assertNotNull(exception.getActual());

        // Verify the message contains our visualization
        assertTrue(exception.getMessage().contains("Objects are not equal"));

        System.out.println("\n✓ Confirmed: assertJsonEquals throws AssertionFailedError");
        System.out.println("Expected value: " + exception.getExpected().getStringRepresentation());
        System.out.println("Actual value: " + exception.getActual().getStringRepresentation());
    }

    @Test
    @DisplayName("assertJsonEquals does NOT throw when objects are equal")
    void testAssertJsonEqualsDoesNotThrowWhenEqual() throws JSONException {
        JSONObject obj1 = new JSONObject().put("name", "Alice").put("age", 30);
        JSONObject obj2 = new JSONObject().put("name", "Alice").put("age", 30);

        // This should NOT throw
        assertDoesNotThrow(() -> {
            VisualAssert.assertJsonEquals("Test", obj1, obj2, "Testing equal objects");
        });

        System.out.println("\n✓ Confirmed: assertJsonEquals does not throw when objects are equal");
    }

    @Test
    @DisplayName("checkJsonEquals returns result without throwing")
    void testCheckJsonEqualsDoesNotThrow() throws JSONException {
        JSONObject obj1 = new JSONObject().put("name", "Alice");
        JSONObject obj2 = new JSONObject().put("name", "Bob");

        // This should NOT throw even though objects are different
        assertDoesNotThrow(() -> {
            VisualAssert.AssertionResult result = VisualAssert.checkJsonEquals(
                "Test", obj1, obj2, "Testing check method"
            );

            // But the result should indicate failure
            assertFalse(result.passed);
            assertEquals("Objects are not equal", result.message);

            System.out.println("\n✓ Confirmed: checkJsonEquals returns result without throwing");
            System.out.println("Test passed: " + result.passed);
            System.out.println("Message: " + result.message);
        });
    }

    @Test
    @DisplayName("AssertionFailedError can be caught and examined")
    void testAssertionFailedErrorCanBeCaught() throws JSONException {
        JSONObject expected = new JSONObject()
            .put("status", "active")
            .put("count", 42);

        JSONObject actual = new JSONObject()
            .put("status", "inactive")
            .put("count", 100);

        try {
            VisualAssert.assertJsonEquals("Status Check", expected, actual, "Production vs Test");
            fail("Should have thrown AssertionFailedError");
        } catch (AssertionFailedError e) {
            // Verify we can access expected and actual values from the exception
            assertNotNull(e.getExpected());
            assertNotNull(e.getActual());

            System.out.println("\n✓ Confirmed: AssertionFailedError can be caught and examined");
            System.out.println("Exception type: " + e.getClass().getSimpleName());
            System.out.println("Has expected value: " + (e.getExpected() != null));
            System.out.println("Has actual value: " + (e.getActual() != null));
        }
    }

    @Test
    @DisplayName("Visual assertions are compatible with standard JUnit assertions")
    void testCompatibilityWithJUnitAssertions() throws JSONException {
        JSONObject obj1 = new JSONObject().put("id", 1);
        JSONObject obj2 = new JSONObject().put("id", 2);

        // Both should throw AssertionFailedError
        assertThrows(AssertionFailedError.class, () -> {
            assertEquals(obj1.toString(), obj2.toString(), "Standard JUnit assertion");
        });

        assertThrows(AssertionFailedError.class, () -> {
            VisualAssert.assertJsonEquals("Visual assertion", obj1, obj2, "Testing");
        });

        System.out.println("\n✓ Confirmed: Visual assertions throw the same exception type as JUnit assertions");
    }

    @Test
    @DisplayName("assertJsonNotEquals throws when objects ARE equal")
    void testAssertJsonNotEqualsThrowsWhenEqual() throws JSONException {
        JSONObject obj1 = new JSONObject().put("name", "Alice").put("age", 30);
        JSONObject obj2 = new JSONObject().put("name", "Alice").put("age", 30);

        // This should throw because objects are equal but we expect them to be different
        AssertionFailedError exception = assertThrows(AssertionFailedError.class, () -> {
            VisualAssert.assertJsonNotEquals("Test", obj1, obj2, "Testing assertJsonNotEquals with equal objects");
        });

        // Verify the message indicates they were unexpectedly equal
        assertTrue(exception.getMessage().contains("equal but expected different"));

        System.out.println("\n✓ Confirmed: assertJsonNotEquals throws when objects are equal");
    }

    @Test
    @DisplayName("assertJsonNotEquals does NOT throw when objects are different")
    void testAssertJsonNotEqualsDoesNotThrowWhenDifferent() throws JSONException {
        JSONObject obj1 = new JSONObject().put("name", "Alice");
        JSONObject obj2 = new JSONObject().put("name", "Bob");

        // This should NOT throw because objects are different (as expected)
        assertDoesNotThrow(() -> {
            VisualAssert.AssertionResult result = VisualAssert.assertJsonNotEquals(
                "Test", obj1, obj2, "Testing with different objects"
            );

            // Result should indicate success
            assertTrue(result.passed);
            assertEquals("Objects are not equal", result.message);

            System.out.println("\n✓ Confirmed: assertJsonNotEquals does not throw when objects are different");
        });
    }

    @Test
    @DisplayName("assertJsonArrayNotEquals throws when arrays ARE equal")
    void testAssertJsonArrayNotEqualsThrowsWhenEqual() throws JSONException {
        JSONArray arr1 = new JSONArray().put(1).put(2).put(3);
        JSONArray arr2 = new JSONArray().put(1).put(2).put(3);

        // This should throw because arrays are equal but we expect them to be different
        AssertionFailedError exception = assertThrows(AssertionFailedError.class, () -> {
            VisualAssert.assertJsonArrayNotEquals("Test", arr1, arr2, "Testing with equal arrays");
        });

        // Verify the message indicates they were unexpectedly equal
        assertTrue(exception.getMessage().contains("equal but expected different"));

        System.out.println("\n✓ Confirmed: assertJsonArrayNotEquals throws when arrays are equal");
    }

    @Test
    @DisplayName("assertJsonArrayNotEquals does NOT throw when arrays are different")
    void testAssertJsonArrayNotEqualsDoesNotThrowWhenDifferent() throws JSONException {
        JSONArray arr1 = new JSONArray().put(1).put(2).put(3);
        JSONArray arr2 = new JSONArray().put(1).put(2).put(4);

        // This should NOT throw because arrays are different (as expected)
        assertDoesNotThrow(() -> {
            VisualAssert.AssertionResult result = VisualAssert.assertJsonArrayNotEquals(
                "Test", arr1, arr2, "Testing with different arrays"
            );

            // Result should indicate success
            assertTrue(result.passed);
            assertEquals("Arrays are not equal", result.message);

            System.out.println("\n✓ Confirmed: assertJsonArrayNotEquals does not throw when arrays are different");
        });
    }
}
