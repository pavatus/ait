package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import net.minecraft.util.Identifier;

public class AutumnVariant extends ConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/borealis_console_autumn.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/borealis_console_autumn_emission.png"));

    public AutumnVariant() {
        super(ConsoleEnum.BOREALIS, new Identifier(AITMod.MOD_ID, "autumn"));
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
