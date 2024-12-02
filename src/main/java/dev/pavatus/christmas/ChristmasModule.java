package dev.pavatus.christmas;

import java.util.Optional;
import java.util.function.Consumer;

import dev.pavatus.christmas.core.ChristmasBlocks;
import dev.pavatus.christmas.core.ChristmasItems;
import dev.pavatus.module.Module;
import dev.pavatus.register.api.RegistryEvents;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.advancement.TardisCriterions;
import loqor.ait.datagen.datagen_providers.AITBlockTagProvider;
import loqor.ait.datagen.datagen_providers.AITItemTagProvider;
import loqor.ait.datagen.datagen_providers.AITLanguageProvider;
import loqor.ait.datagen.datagen_providers.AITRecipeProvider;

public class ChristmasModule extends Module {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "christmas");
    private static final ChristmasModule INSTANCE = new ChristmasModule();

    public static final OwoItemGroup ITEM_GROUP = OwoItemGroup
            .builder(new Identifier(AITMod.MOD_ID, "christmas_item_group"), () -> Icon.of(ChristmasItems.FESTIVE_KEY))
            .disableDynamicTitle().build();

    @Override
    public void init() {
        ITEM_GROUP.initialize();

        RegistryEvents.SUBSCRIBE.register((registries, env) -> {
        });

        FieldRegistrationHandler.register(ChristmasItems.class, AITMod.MOD_ID, false);
        FieldRegistrationHandler.register(ChristmasBlocks.class, AITMod.MOD_ID, false);
    }

    @Override
    public void initClient() {

    }

    @Override
    public Optional<DataGenerator> getDataGenerator() {
        return Optional.of(new DataGenerator() {
            @Override
            public void lang(AITLanguageProvider provider) {
                provider.addTranslation("achievement." + AITMod.MOD_ID + ".title.christmas_root", "Merry Christmas");
                provider.addTranslation("achievement." + AITMod.MOD_ID + ".description.christmas_root", "and a happy new year!");

                provider.addTranslation(ITEM_GROUP, "Advent(ures) in Snow");
                provider.addTranslation(ChristmasItems.FESTIVE_KEY, "Festive Key");
            }

            @Override
            public void recipes(AITRecipeProvider provider) {

            }

            @Override
            public void blockTags(AITBlockTagProvider provider) {

            }

            @Override
            public void itemTags(AITItemTagProvider provider) {

            }

            @Override
            public void generateItemModels(ItemModelGenerator itemModelGenerator) {

            }

            @Override
            public void models(BlockStateModelGenerator generator) {

            }

            @Override
            public void advancements(Consumer<Advancement> consumer) {
                Advancement root = Advancement.Builder.create()
                        .display(Blocks.SNOW_BLOCK, Text.translatable("achievement." + AITMod.MOD_ID + ".title.christmas_root"),
                                Text.translatable("achievement." + AITMod.MOD_ID + ".description.christmas_root"), new Identifier("textures/block/snow.png"), AdvancementFrame.TASK, false, false, false)
                        .criterion("root", TardisCriterions.ROOT.conditions())
                        .build(consumer, AITMod.MOD_ID + "/christmas_root");
            }
        });
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
