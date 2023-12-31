package mdteam.ait.tardis.variant.console;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.TempConsoleModel;
import mdteam.ait.tardis.console.TempConsole;
import net.minecraft.util.Identifier;

public class TempVariant extends ConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/console.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/console_emission.png"));

    public TempVariant() {
        super(TempConsole.REFERENCE, new Identifier(AITMod.MOD_ID, "console/temp"));
    }

    // @Override
    // public Identifier texture() {
    //     return TEXTURE;
    // }
//
    // @Override
    // public Identifier emission() {
    //     return EMISSION;
    // }
    // @Override
    // public ConsoleModel model() {
        //return new TempConsoleModel(TempConsoleModel.getTexturedModelData().createModel());
    //}
}
