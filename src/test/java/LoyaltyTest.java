import loqor.ait.tardis.data.loyalty.Loyalty;

// TODO: replace this with JUnit or something
public class LoyaltyTest {

    public static void main(String[] args) {
        assertEquals(Loyalty.Type.get("owner"), Loyalty.Type.OWNER);
        assertEquals(Loyalty.Type.get("PILOT"), Loyalty.Type.PILOT);

        assertEquals(Loyalty.Type.get(75), Loyalty.Type.PILOT);
        
        assertFails(() -> Loyalty.Type.get(-1));
        assertFails(() -> Loyalty.Type.get("ASDASDASDASD"));
    }

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
