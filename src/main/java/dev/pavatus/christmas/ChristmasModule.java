package dev.pavatus.christmas;

import java.util.Optional;

import dev.pavatus.christmas.core.ChristmasBlocks;
import dev.pavatus.christmas.core.ChristmasItems;
import dev.pavatus.module.Module;
import dev.pavatus.register.api.RegistryEvents;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.AITItems;

public class ChristmasModule extends Module {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "christmas");
    private static final ChristmasModule INSTANCE = new ChristmasModule();

    public static final OwoItemGroup ITEM_GROUP = OwoItemGroup
            .builder(new Identifier(AITMod.MOD_ID, "christmas_item_group"), () -> Icon.of(AITItems.GOLD_KEY))
            .disableDynamicTitle().build();

    @Override
    public void init() {
        RegistryEvents.SUBSCRIBE.register((registries, env) -> {
        });

        FieldRegistrationHandler.register(ChristmasItems.class, AITMod.MOD_ID, false);
        FieldRegistrationHandler.register(ChristmasBlocks.class, AITMod.MOD_ID, false);
    }

    @Override
    public void initClient() {

    }

    @Override
    public BlockItem createBlockItem(Block block, String id) {
        return new BlockItem(block, new OwoItemSettings().group(ITEM_GROUP));
    }

    @Override
    public Optional<Class<?>> getBlockRegistry() {
        return Optional.of(ChristmasBlocks.class);
    }

    @Override
    public Optional<Class<?>> getItemRegistry() {
        return Optional.of(ChristmasItems.class);
    }

    @Override
    public Identifier id() {
        return REFERENCE;
    }

    public static ChristmasModule instance() {
        return INSTANCE;
    }
}
