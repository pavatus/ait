package dev.pavatus.christmas;

import dev.pavatus.module.Module;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class ChristmasModule extends Module {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "christmas");
    private static final ChristmasModule INSTANCE = new ChristmasModule();

    @Override
    public void init() {

    }

    @Override
    public void initClient() {

    }

    @Override
    public Identifier id() {
        return REFERENCE;
    }

    public static ChristmasModule instance() {
        return INSTANCE;
    }
}
