package loqor.ait.datagen.datagen_providers.loot;

import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
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
        filterBlocksWithAnnotation(AITBlocks.getBlocks(), AITBlocks.class, NoBlockDrop.class, true).forEach(this::addDrop);

        this.addDrop(AITBlocks.ZEITON_CLUSTER,
                (block) -> dropsWithSilkTouch(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(4.0F)))
                        .apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))
                        .conditionally(MatchToolLootCondition
                                .builder(ItemPredicate.Builder.create().tag(AITTags.Items.CLUSTER_MAX_HARVESTABLES)))
                        .alternatively(this.applyExplosionDecay(block, ItemEntry.builder(AITItems.ZEITON_SHARD)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))))));


        addDrop(AITBlocks.WAYPOINT_BANK, doorDrops(AITBlocks.WAYPOINT_BANK));
        addDrop(AITBlocks.MARTIAN_BRICK_SLAB, slabDrops(AITBlocks.MARTIAN_BRICK_SLAB));
        addDrop(AITBlocks.MARTIAN_COBBLESTONE_SLAB, slabDrops(AITBlocks.MARTIAN_COBBLESTONE_SLAB));
        addDrop(AITBlocks.SMOOTH_MARTIAN_STONE_SLAB, slabDrops(AITBlocks.SMOOTH_MARTIAN_STONE_SLAB));
    }

    public static List<Block> filterBlocksWithAnnotation(List<Block> blocks, Class<?> parent, Class<? extends Annotation> annotationClass, boolean inverse) {
        // Get all annotated blocks with their annotations
        List<Map.Entry<Block, Annotation>> annotatedBlocks = getAnnotatedBlocks(parent, annotationClass);

        // Filter the input list to include only blocks that match annotated fields
        return blocks.stream()
                .filter(block -> inverse != annotatedBlocks.stream()
                        .anyMatch(entry -> entry.getKey().equals(block)))
                .collect(Collectors.toList());
    }


    public static List<Map.Entry<Block, Annotation>> getAnnotatedBlocks(Class<?> parent, Class<? extends Annotation> annotationClass) {
        return List.of(parent.getDeclaredFields()).stream()
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
                .collect(Collectors.toList());
    }
}
