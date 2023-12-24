package mdteam.ait.tardis.variant;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import net.minecraft.util.Identifier;

public class BorealisVariant extends ConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/borealis_console.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/borealis_console_emission.png"));

    public BorealisVariant() {
        super(ConsoleEnum.BOREALIS, new Identifier(AITMod.MOD_ID, "borealis"));
    }

    @Override
    public Identifier texture() {
        return TEXTURE;
    }

    @Override
    public Identifier emission() {
        return EMISSION;
    }
}
