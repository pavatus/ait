package loqor.ait.core.item.sonic.impl;

import loqor.ait.AITMod;
import loqor.ait.core.item.sonic.SonicSchema;
import net.minecraft.util.Identifier;

public class BuiltInSonic extends SonicSchema {

    public BuiltInSonic(String id, String name, int model) {
        super(new Identifier(AITMod.MOD_ID, id), name, model);
    }
}
