package com.trdg;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsonComparator {
    private static String parentKey = "";
    private static List<String> differences = new ArrayList<>();

    public static boolean areValuesEqual(Object value1, Object value2, String key) throws JSONException {
        if ((value1 instanceof JSONObject) && (value2 instanceof JSONObject)) {
            parentKey = key;
            return areJsonEntitiesEqual((JSONObject) value1, (JSONObject) value2);
        } else if ((value1 instanceof JSONArray) && (value2 instanceof JSONArray)) {
            parentKey = key;
            return compareJsonArrays((JSONArray) value1, (JSONArray) value2);
        } else if (!value1.equals(value2)) {
            differences.add(String.format("Values are not equal for key: %s - Value 1: %s, Value 2: %s",
                    (parentKey.isEmpty() ? key : parentKey + "->" + key), value1.toString(), value2.toString()));
            return false;
        }
        return true;
    }

    public static boolean areJsonEntitiesEqual(JSONObject obj1, JSONObject obj2) throws JSONException {
        boolean isEqual = true;
        for (String key : obj1.keySet()) {
            if (!obj2.has(key)) {
                differences.add(String.format("Missing key \"%s\" in JSON Object 2", key));
                isEqual = false;
            }
            else if (!areValuesEqual(obj1.get(key), obj2.get(key), key)) {
                isEqual = false;
            }
        }
        return isEqual;
    }

    public static boolean compareJsonArrays(JSONArray jsonArray1, JSONArray jsonArray2) throws JSONException {
        boolean isEqual = true;

        if (jsonArray1.length() == jsonArray2.length()) {
            for (int i = 0; i < jsonArray1.length(); i++) {
                Object a = jsonArray1.get(i);
                Object b = jsonArray2.get(i);

                if (a instanceof JSONObject && b instanceof JSONObject) {
                    isEqual = areJsonEntitiesEqual((JSONObject) a, (JSONObject) b);
                } else if (a instanceof JSONArray && b instanceof JSONArray) {
                    isEqual = compareJsonArrays((JSONArray) a, (JSONArray) b);
                } else if (!a.equals(b)) {
                    differences.add(String.format("Arrays are not same: %s, %s", a, b));
                    isEqual = false;
                }

                if (!isEqual) break;
            }
        } else {
            differences.add("Sizes of JSON Arrays are not same");
            isEqual = false;
        }

        return isEqual;
    }

    public static void main(String[] args) throws Exception {
        //String json1 = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("person1.json").toURI())));
        //String json2 = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("person2.json").toURI())));


        String json1 = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("array1.json").toURI())));
        String json2 = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("array2.json").toURI())));




        // Convert string to JSONObjects
        JSONObject obj1 = new JSONObject(json1);
        JSONObject obj2 = new JSONObject(json2);

        if (!areJsonEntitiesEqual(obj1, obj2)) {
            System.out.println("JSONObjects are not equal. Differences found: ");
            differences.forEach(System.out::println);
        } else {
            System.out.println("JSONObjects are equal");
        }

        System.out.println(generateJsonDiff(obj1, obj2));
        System.out.println("\n");
        System.out.println(generateJsonPatch(obj1, obj2));
        System.out.println("\n");

        JSONArray diffs = generateJsonDiff("", obj1, obj2);
        System.out.println(diffs);

    }

    public static JSONObject generateJsonDiff(JSONObject obj1, JSONObject obj2) throws JSONException {
        JSONObject result = new JSONObject();

        // get keys from both objects
        Set<String> keys1 = obj1.keySet();
        Set<String> keys2 = obj2.keySet();

        // get all unique keys
        Set<String> allKeys = new HashSet<String>(keys1);
        allKeys.addAll(keys2);

        for (String key : allKeys) {
            // if key only exists in obj1
            if (!obj2.has(key) && obj1.has(key)) {
                result.put(key, String.format("Key only in first JSONObject: Value - %s", obj1.get(key).toString()));
            }
            // if key only exists in obj2
            else if (obj2.has(key) && !obj1.has(key)) {
                result.put(key, String.format("Key only in second JSONObject: Value - %s", obj2.get(key).toString()));
            }
            // if key exists in both obj1 and obj2
            else {
                Object value1 = obj1.get(key);
                Object value2 = obj2.get(key);

                if (value1 instanceof JSONObject && value2 instanceof JSONObject) {
                    JSONObject valueDiff = generateJsonDiff((JSONObject) value1, (JSONObject) value2);
                    if (!valueDiff.isEmpty()) {
                        result.put(key, valueDiff);
                    }
                } else if (value1 instanceof JSONArray && value2 instanceof JSONArray) {
                    // Implement your array comparison
                    // You can also utilize a similar approach as your updated areJsonEntitiesEqual method here
                } else if (!value1.equals(value2)) {
                    result.put(key, String.format("Values do not match: Value 1 - %s, Value 2 - %s", value1, value2));
                }
            }
        }
        return result;
    }

    public static JSONArray generateJsonPatch(JSONObject obj1, JSONObject obj2) throws JSONException {
        JSONArray diffs = new JSONArray();

        Set<String> keys1 = obj1.keySet();
        Set<String> keys2 = obj2.keySet();

        Set<String> allKeys = new HashSet<String>(keys1);
        allKeys.addAll(keys2);

        for (String key : allKeys) {
            JSONObject diff = new JSONObject();
            if (!obj2.has(key) && obj1.has(key)) {
                diff.put("op", "remove");
                diff.put("path", "/" + key);
                diffs.put(diff);
            } else if (obj2.has(key) && !obj1.has(key)) {
                diff.put("op", "add");
                diff.put("path", "/" + key);
                diff.put("value", obj2.get(key));
                diffs.put(diff);
            } else {
                Object value1 = obj1.get(key);
                Object value2 = obj2.get(key);
                if (!value1.equals(value2)) {
                    diff.put("op", "replace");
                    diff.put("path", "/" + key);
                    diff.put("value", obj2.get(key));
                    diffs.put(diff);
                }
            }
        }

        return diffs;
    }

    public static JSONArray generateJsonDiff(String parent, JSONObject obj1, JSONObject obj2) throws JSONException {
        JSONArray diffs = new JSONArray();

        Set<String> keys1 = obj1.keySet();
        Set<String> keys2 = obj2.keySet();

        Set<String> allKeys = new HashSet<String>(keys1);
        allKeys.addAll(keys2);

        for (String key : allKeys) {
            String thisPath = parent.isEmpty() ? key : parent + "/" + key;

            JSONObject diff = new JSONObject();
            if (obj2.has(key) && !obj1.has(key)) {
                diff.put("op", "add");
                diff.put("path", thisPath);
                diff.put("value", obj2.get(key));
                diffs.put(diff);
            } else if (!obj2.has(key) && obj1.has(key)) {
                diff.put("op", "remove");
                diff.put("path", thisPath);
                diffs.put(diff);
            } else {
                Object value1 = obj1.get(key);
                Object value2 = obj2.get(key);

                if ((value1 instanceof JSONObject) && (value2 instanceof JSONObject)) {
                    diffs.put(generateJsonDiff(thisPath, (JSONObject)value1, (JSONObject)value2));
                } else if ((value1 instanceof JSONArray) && (value2 instanceof JSONArray)) {
                    //Not handling array differences in this example
                } else if (!value1.equals(value2)) {
                    diff.put("op", "replace");
                    diff.put("path", thisPath);
                    diff.put("value", value2);
                    diffs.put(diff);
                }
            }
        }

        return diffs;
    }
}