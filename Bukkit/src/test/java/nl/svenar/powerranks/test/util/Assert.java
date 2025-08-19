package nl.svenar.powerranks.test.util;

public class Assert {

    public static void assertTrue(String description, boolean condition) {
        try {
            org.junit.Assert.assertTrue(description, condition);
            System.out.println(description + " - passed");
        } catch (AssertionError e) {
            System.out.println(description + " - failed");

            throw e;
        }
    }

    public static void assertFalse(String description, boolean condition) {
        try {
            org.junit.Assert.assertFalse(description, condition);
            System.out.println(description + " - passed");
        } catch (AssertionError e) {
            System.out.println(description + " - failed");

            throw e;
        }
    }

    public static void assertNull(String description, Object object) {
        try {
            org.junit.Assert.assertNull(description, object);
            System.out.println(description + " - passed");
        } catch (AssertionError e) {
            System.out.println(description + " - failed");

            throw e;
        }
    }

    public static void assertNotNull(String description, Object object) {
        try {
            Assert.assertNotNull(description, object);
            System.out.println(description + " - passed");
        } catch (AssertionError e) {
            System.out.println(description + " - failed");

            throw e;
        }
    }

    public static void assertEquals(String description, Object expected, Object actual) {
        try {
            org.junit.Assert.assertEquals(description, expected, actual);
            System.out.println(description + " - passed");
        } catch (AssertionError e) {
            System.out.println(description + " - failed");

            throw e;
        }
    }
}
