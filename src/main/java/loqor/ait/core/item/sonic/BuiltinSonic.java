package loqor.ait.core.item.sonic;

import loqor.ait.AITMod;
import loqor.ait.client.renderers.CustomItemRendering;
import loqor.ait.core.item.SonicItem;
import loqor.ait.registry.SonicRegistry;
import net.minecraft.util.Identifier;

import static loqor.ait.AITMod.MOD_ID;

public class BuiltinSonic extends SonicSchema {

    private static final String prefix = "item/sonic/";

    private static final String[] SONICS = new String[] {
            "coral", "fob", "mechanical", "prime", "renaissance"
    };

    private static final String[] MODES = new String[] {
            "inactive", "interaction", "overload", "scanning", "tardis"
    };

    protected BuiltinSonic(Identifier id, Models models) {
        super(id, null, models, new Rendering());
    }

    public static BuiltinSonic create(String id) {
        Identifier[] identifiers = new Identifier[SONICS.length];

        int i = 0;
        for (String sonic : SONICS) {
            for (String mode : MODES) {
                identifiers[i >= 5 ? 4 : i] = new Identifier(MOD_ID, prefix + sonic + "/" + mode);
                i++;
            }
        }

        return new BuiltinSonic(new Identifier(AITMod.MOD_ID, id), new Models(
                identifiers[0], identifiers[1], identifiers[2], identifiers[3], identifiers[4]
        ));
    }
}
