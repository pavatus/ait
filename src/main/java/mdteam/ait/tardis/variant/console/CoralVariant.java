package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.CoralConsoleModel;
import mdteam.ait.tardis.console.CoralConsole;
import net.minecraft.util.Identifier;

public class CoralVariant extends ConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral_console.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral_console_emission.png"));

    public CoralVariant() {
        super(CoralConsole.REFERENCE, new Identifier(AITMod.MOD_ID, "console/coral"));
    }

    //@Override
    //public Identifier texture() {
    //    return TEXTURE;
    //}
//
    //@Override
    //public Identifier emission() {
    //    return EMISSION;
    //}
    //@Override
    //public ConsoleModel model() {
    //    return new CoralConsoleModel(CoralConsoleModel.getTexturedModelData().createModel());
    //}
}
