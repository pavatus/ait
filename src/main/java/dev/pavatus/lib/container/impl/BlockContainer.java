package dev.pavatus.lib.container.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dev.pavatus.lib.block.ABlockSettings;
import dev.pavatus.lib.container.RegistryContainer;
import dev.pavatus.lib.item.AItem;
import dev.pavatus.lib.mixin.AbstractBlockAccessor;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public abstract class BlockContainer implements RegistryContainer<Block> {

    private List<Item> items;

    @Override
    public void start(int fields) {
        this.items = new ArrayList<>(fields);
    }

    @Override
    public Class<Block> getTargetClass() {
        return Block.class;
    }

    @Override
    public Registry<Block> getRegistry() {
        return Registries.BLOCK;
    }

    @Override
    public void postProcessField(Identifier identifier, Block value, Field field) {
        if (field.isAnnotationPresent(NoBlockItem.class))
            return;

        Item.Settings itemSettings = null;

        if (((AbstractBlockAccessor) value).getSettings() instanceof ABlockSettings abs)
            itemSettings = abs.itemSettings();

        Item item = this.createBlockItem(value, itemSettings);
        Registry.register(Registries.ITEM, identifier, item);

        this.items.add(item);
    }

    @Override
    public void finish() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            for (Item item : items) {
                if (((AItem) item).a$group() == group)
                    entries.add(item);
            }
        });
    }

    public BlockItem createBlockItem(Block block, @Nullable Item.Settings settings) {
        return new BlockItem(block, settings == null ? this.createBlockItemSettings(block) : settings);
    }

    public Item.Settings createBlockItemSettings(Block block) {
        return new Item.Settings();
    }
}
