package dev.loqor.ait;

import loqor.ait.tardis.data.loyalty.Loyalty;
import static dev.loqor.ait.Test.*;

// TODO: replace this with JUnit or something
public class LoyaltyTest {

    public static void main(String[] args) {
        assertEquals(Loyalty.Type.get("owner"), Loyalty.Type.OWNER);
        assertEquals(Loyalty.Type.get("PILOT"), Loyalty.Type.PILOT);

        assertEquals(Loyalty.Type.get(75), Loyalty.Type.PILOT);
        
        assertFails(() -> Loyalty.Type.get(-1));
        assertFails(() -> Loyalty.Type.get("ASDASDASDASD"));
    }
}
