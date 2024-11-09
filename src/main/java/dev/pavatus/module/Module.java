package dev.pavatus.module;

import java.util.Optional;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.Identifiable;
import loqor.ait.datagen.datagen_providers.AITBlockTagProvider;
import loqor.ait.datagen.datagen_providers.AITLanguageProvider;
import loqor.ait.datagen.datagen_providers.AITRecipeProvider;

public abstract class Module implements Identifiable {
    public abstract void init();

    @Environment(EnvType.CLIENT)
    public abstract void initClient();

    protected Block register(Block block, Identifier id, boolean withItem) {
        Registry.register(Registries.BLOCK, id, block);
        if (withItem) {
            register(createBlockItem(block, id.getPath()), id);
        }

        return block;
    }
    protected Item register(Item item, Identifier id) {
        Registry.register(Registries.ITEM, id, item);
        return item;
    }
    protected SoundEvent register(SoundEvent sound, Identifier id) {
        Registry.register(Registries.SOUND_EVENT, id, sound);
        return sound;
    }
    protected SoundEvent registerSound(String name) {
        return register(SoundEvent.of(AITMod.id(name)), AITMod.id(name));
    }
    protected <T extends BlockEntity> BlockEntityType<T> register(BlockEntityType<T> type, Identifier id) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, id, type);
        return type;
    }

    public BlockItem createBlockItem(Block block, String id) {
        return new BlockItem(block, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    }

    public Optional<Class<?>> getBlockRegistry() {
        return Optional.empty();
    }
    public Optional<Class<?>> getItemRegistry() {
        return Optional.empty();
    }


    public Optional<DataGenerator> getDataGenerator() {
        return Optional.empty();
    }

    public interface DataGenerator {
        /**
         * Called when the ENGLISH language provider is generating
         */
        void lang(AITLanguageProvider provider);
        void recipes(AITRecipeProvider provider);
        void tags(AITBlockTagProvider provider);

        void generateItemModels(ItemModelGenerator itemModelGenerator);

        void models(BlockStateModelGenerator generator);
    }
}
