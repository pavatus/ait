package dev.loqor.ait;

public class Test {

    private static void fail() {
        throw new AssertionError();
    }

    public static void assertTrue(boolean b) {
        if (!b) fail();
    }

    public static void assertEquals(Object a, Object b) {
        try {
            assertTrue(a.equals(b));
        } catch (AssertionError e) {
            System.err.printf("Expected '%s' but got '%s'%n", b, a);
            throw e;
        }
    }

    public static <T> void assertFails(FaultySupplier<T> obj) {
        try {
            obj.get();
            fail();
        } catch (Exception ignored) { }
    }
}
