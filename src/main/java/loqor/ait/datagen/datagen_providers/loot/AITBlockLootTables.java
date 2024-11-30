package loqor.ait.datagen.datagen_providers.loot;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.pavatus.module.ModuleRegistry;
import dev.pavatus.planet.core.PlanetBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.item.ItemPredicate;

import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITTags;
import loqor.ait.datagen.datagen_providers.util.NoBlockDrop;

public class AITBlockLootTables extends FabricBlockLootTableProvider {

    public AITBlockLootTables(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        filterBlocksWithAnnotation(AITBlocks.get(), NoBlockDrop.class, true).forEach(this::addDrop);

        this.addDrop(AITBlocks.ZEITON_CLUSTER,
                (block) -> dropsWithSilkTouch(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4.0F)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                        .conditionally(MatchToolLootCondition
                                .builder(ItemPredicate.Builder.create().tag(AITTags.Items.CLUSTER_MAX_HARVESTABLES)))
                        .alternatively(this.applyExplosionDecay(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))))));


        addDrop(AITBlocks.WAYPOINT_BANK, doorDrops(AITBlocks.WAYPOINT_BANK));

        // Martian
        addDrop(PlanetBlocks.MARTIAN_STONE, drops(PlanetBlocks.MARTIAN_STONE, PlanetBlocks.MARTIAN_COBBLESTONE));
        addDrop(PlanetBlocks.MARTIAN_BRICK_SLAB, slabDrops(PlanetBlocks.MARTIAN_BRICK_SLAB));
        addDrop(PlanetBlocks.MARTIAN_COBBLESTONE_SLAB, slabDrops(PlanetBlocks.MARTIAN_COBBLESTONE_SLAB));
        addDrop(PlanetBlocks.SMOOTH_MARTIAN_STONE_SLAB, slabDrops(PlanetBlocks.SMOOTH_MARTIAN_STONE_SLAB));

        // Ore
        addDrop(PlanetBlocks.MARTIAN_COAL_ORE, oreDrops(PlanetBlocks.MARTIAN_COAL_ORE, Items.COAL));
        addDrop(PlanetBlocks.MARTIAN_COPPER_ORE, oreDrops(PlanetBlocks.MARTIAN_COPPER_ORE, Items.COPPER_INGOT));
        addDrop(PlanetBlocks.MARTIAN_IRON_ORE, oreDrops(PlanetBlocks.MARTIAN_IRON_ORE, Items.IRON_INGOT));
        addDrop(PlanetBlocks.MARTIAN_LAPIS_ORE, oreDrops(PlanetBlocks.MARTIAN_LAPIS_ORE, Items.LAPIS_LAZULI));
        addDrop(PlanetBlocks.MARTIAN_REDSTONE_ORE, oreDrops(PlanetBlocks.MARTIAN_REDSTONE_ORE, Items.REDSTONE));
        addDrop(PlanetBlocks.MARTIAN_GOLD_ORE, oreDrops(PlanetBlocks.MARTIAN_GOLD_ORE, Items.GOLD_INGOT));
        addDrop(PlanetBlocks.MARTIAN_DIAMOND_ORE, oreDrops(PlanetBlocks.MARTIAN_DIAMOND_ORE, Items.DIAMOND));
        addDrop(PlanetBlocks.MARTIAN_EMERALD_ORE, oreDrops(PlanetBlocks.MARTIAN_EMERALD_ORE, Items.EMERALD));

        // Anorthosite
        addDrop(PlanetBlocks.ANORTHOSITE_BRICK_SLAB, slabDrops(PlanetBlocks.ANORTHOSITE_BRICK_SLAB));
        addDrop(PlanetBlocks.ANORTHOSITE_SLAB, slabDrops(PlanetBlocks.ANORTHOSITE_SLAB));
        addDrop(PlanetBlocks.POLISHED_ANORTHOSITE_SLAB, slabDrops(PlanetBlocks.POLISHED_ANORTHOSITE_SLAB));
    }

    public static List<Block> filterBlocksWithAnnotation(List<Block> blocks, Class<? extends Annotation> annotationClass, boolean inverse) {
        // Get all annotated blocks with their annotations
        List<Map.Entry<Block, Annotation>> annotatedBlocks = getAnnotatedBlocks(annotationClass);

        // Filter the input list to include only blocks that match annotated fields
        return blocks.stream()
                .filter(block -> inverse != annotatedBlocks.stream()
                        .anyMatch(entry -> entry.getKey().equals(block)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Map.Entry<Block, Annotation>> getAnnotatedBlocks(Class<?> parent, Class<? extends Annotation> annotationClass) {
        return Stream.of(parent.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotationClass.asSubclass(java.lang.annotation.Annotation.class)))
                .filter(field -> Block.class.isAssignableFrom(field.getType())) // Ensure it's a Block
                .map(field -> {
                    try {
                        // Access the NoLootTable annotation
                        Annotation annotation = field.getAnnotation(annotationClass);
                        // Pair the Block instance with its annotation in a Map.Entry
                        return new AbstractMap.SimpleEntry<>((Block) field.get(null), annotation);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Map.Entry<Block, Annotation>> getAnnotatedBlocks(Class<? extends Annotation> annotationClass) {
        List<Map.Entry<Block, Annotation>> list = getAnnotatedBlocks(AITBlocks.class, annotationClass);

        ModuleRegistry.instance().iterator().forEachRemaining(module -> module.getBlockRegistry().ifPresent(blocks -> list.addAll(getAnnotatedBlocks(blocks, annotationClass))));

        return list;
    }

    public static List<Map.Entry<Item, Annotation>> getAnnotatedItems(Class<?> parent, Class<? extends Annotation> annotationClass) {
        return Stream.of(parent.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotationClass.asSubclass(java.lang.annotation.Annotation.class)))
                .filter(field -> Item.class.isAssignableFrom(field.getType())) // Ensure it's a Block
                .map(field -> {
                    try {
                        // Access the NoLootTable annotation
                        Annotation annotation = field.getAnnotation(annotationClass);
                        // Pair the Block instance with its annotation in a Map.Entry
                        return new AbstractMap.SimpleEntry<>((Item) field.get(null), annotation);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Map.Entry<Item, Annotation>> getAnnotatedItems(Class<? extends Annotation> annotationClass) {
        List<Map.Entry<Item, Annotation>> list = getAnnotatedItems(AITItems.class, annotationClass);

        ModuleRegistry.instance().iterator().forEachRemaining(module -> module.getItemRegistry().ifPresent(var -> list.addAll(getAnnotatedItems(var, annotationClass))));

        return list;
    }

    public static List<Item> filterItemsWithAnnotation(List<Item> list, Class<? extends Annotation> annotationClass, boolean inverse) {
        // Get all annotated blocks with their annotations
        List<Map.Entry<Item, Annotation>> annotatedBlocks = getAnnotatedItems(annotationClass);

        // Filter the input list to include only blocks that match annotated fields
        return list.stream()
                .filter(block -> inverse != annotatedBlocks.stream()
                        .anyMatch(entry -> entry.getKey().equals(block)))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
