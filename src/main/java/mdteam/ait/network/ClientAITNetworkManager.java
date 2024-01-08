package mdteam.ait.network;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

public class ClientAITNetworkManager {
    public static final Identifier ASK_FOR_INTERIOR_SUBSCRIBERS = new Identifier(AITMod.MOD_ID, "ask_for_interior_subscribers");
    public static final Identifier ASK_FOR_EXTERIOR_SUBSCRIBERS = new Identifier(AITMod.MOD_ID, "ask_for_exterior_subscribers");
    public static final Identifier SEND_EXTERIOR_UNLOADED = new Identifier(AITMod.MOD_ID, "send_exterior_unloaded");
    public static final Identifier SEND_INTERIOR_UNLOADED = new Identifier(AITMod.MOD_ID, "send_interior_unloaded");

    public static void init() {

    }
}
