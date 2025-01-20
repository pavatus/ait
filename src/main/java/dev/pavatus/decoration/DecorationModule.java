package dev.pavatus.decoration;

import java.util.Optional;
import java.util.function.Consumer;

import dev.pavatus.decoration.core.DecorationBlocks;
import dev.pavatus.decoration.core.DecorationItems;
import dev.pavatus.lib.container.RegistryContainer;
import dev.pavatus.lib.container.impl.BlockContainer;
import dev.pavatus.lib.container.impl.ItemContainer;
import dev.pavatus.lib.datagen.lang.SakitusLanguageProvider;
import dev.pavatus.lib.datagen.model.SakitusModelProvider;
import dev.pavatus.lib.itemgroup.AItemGroup;
import dev.pavatus.module.Module;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.advancement.Advancement;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.datagen.datagen_providers.AITBlockTagProvider;
import loqor.ait.datagen.datagen_providers.AITItemTagProvider;
import loqor.ait.datagen.datagen_providers.AITRecipeProvider;



public class DecorationModule extends Module {
    private static final DecorationModule INSTANCE = new DecorationModule();

    public static final Identifier ID = AITMod.id("decoration");

    @Override
    public void init() {
        RegistryContainer.register(DecorationItems.class, AITMod.MOD_ID);
        RegistryContainer.register(DecorationBlocks.class, AITMod.MOD_ID);
    }

    @Override
    protected AItemGroup.Builder buildItemGroup() {
        return AItemGroup.builder(id()).icon(() -> new ItemStack(DecorationBlocks.MINT_ROUNDEL));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void initClient() {
    }

    @Override
    public Identifier id() {
        return ID;
    }


    @Override
    public Optional<Class<? extends BlockContainer>> getBlockRegistry() {
        return Optional.of(DecorationBlocks.class);
    }

    @Override
    public Optional<Class<? extends ItemContainer>> getItemRegistry() {
        return Optional.of(DecorationItems.class);
    }

    @Override
    public Optional<DataGenerator> getDataGenerator() {
        return Optional.of(new DataGenerator() {
            @Override
            public void lang(SakitusLanguageProvider provider) {
                provider.addTranslation(getItemGroup(), "AIT: Decoration");
                provider.addTranslation("itemGroup.ait.decoration", "AIT: Decoration");
                provider.addTranslation(DecorationBlocks.MINT_ROUNDEL, "Roundel");
                provider.addTranslation(DecorationBlocks.DARK_OAK_ROUNDEL, "Roundel");
                provider.addTranslation(DecorationBlocks.WHITE_ROUNDEL, "Roundel");
                provider.addTranslation(DecorationBlocks.TARDIM_ROUNDEL, "Roundel");
                provider.addTranslation(DecorationBlocks.RENAISSANCE_ROUNDEL, "Roundel");
                provider.addTranslation(DecorationBlocks.RENAISSANCE_ROUNDEL_SIDE, "Roundel");
                provider.addTranslation(DecorationBlocks.MINT_ROUNDEL_SIDE, "Roundel");
                provider.addTranslation(DecorationBlocks.DARK_OAK_ROUNDEL_SIDE, "Roundel");
                provider.addTranslation(DecorationBlocks.WHITE_ROUNDEL_SIDE, "Roundel");
                provider.addTranslation(DecorationBlocks.TARDIM_ROUNDEL_SIDE, "Roundel");
                provider.addTranslation(DecorationBlocks.CRYSTALLINE_BLOCK, "Crystalline");

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
            public void generateItemModels(SakitusModelProvider provider, ItemModelGenerator generator) {

            }

            @Override
            public void models(SakitusModelProvider provider, BlockStateModelGenerator generator) {

            }

            @Override
            public void advancements(Consumer<Advancement> consumer) {

            }
        });
    }

    public static DecorationModule instance() {
        return INSTANCE;
    }
}
