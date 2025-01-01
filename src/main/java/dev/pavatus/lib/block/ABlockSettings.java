package dev.pavatus.lib.block;

import java.util.function.Function;
import java.util.function.ToIntFunction;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class ABlockSettings extends FabricBlockSettings {

    public static ABlockSettings create() {
        return new ABlockSettings();
    }

    private Item.Settings settings;

    public ABlockSettings itemSettings(Item.Settings settings) {
        this.settings = settings;
        return this;
    }

    @Override
    public ABlockSettings noCollision() {
        return (ABlockSettings) super.noCollision();
    }

    @Override
    public ABlockSettings nonOpaque() {
        return (ABlockSettings) super.nonOpaque();
    }

    @Override
    public ABlockSettings slipperiness(float value) {
        return (ABlockSettings) super.slipperiness(value);
    }

    @Override
    public ABlockSettings velocityMultiplier(float velocityMultiplier) {
        return (ABlockSettings) super.velocityMultiplier(velocityMultiplier);
    }

    @Override
    public ABlockSettings jumpVelocityMultiplier(float jumpVelocityMultiplier) {
        return (ABlockSettings) super.jumpVelocityMultiplier(jumpVelocityMultiplier);
    }

    @Override
    public ABlockSettings sounds(BlockSoundGroup group) {
        return (ABlockSettings) super.sounds(group);
    }

    @Override
    public ABlockSettings lightLevel(ToIntFunction<BlockState> levelFunction) {
        return (ABlockSettings) super.lightLevel(levelFunction);
    }

    @Override
    public ABlockSettings luminance(ToIntFunction<BlockState> luminanceFunction) {
        return (ABlockSettings) super.luminance(luminanceFunction);
    }

    @Override
    public ABlockSettings strength(float hardness, float resistance) {
        return (ABlockSettings) super.strength(hardness, resistance);
    }

    @Override
    public ABlockSettings breakInstantly() {
        return (ABlockSettings) super.breakInstantly();
    }

    @Override
    public ABlockSettings strength(float strength) {
        return (ABlockSettings) super.strength(strength);
    }

    @Override
    public ABlockSettings ticksRandomly() {
        return (ABlockSettings) super.ticksRandomly();
    }

    @Override
    public ABlockSettings dynamicBounds() {
        return (ABlockSettings) super.dynamicBounds();
    }

    @Override
    public ABlockSettings dropsNothing() {
        return (ABlockSettings) super.dropsNothing();
    }

    @Override
    public ABlockSettings dropsLike(Block block) {
        return (ABlockSettings) super.dropsLike(block);
    }

    @Override
    public ABlockSettings air() {
        return (ABlockSettings) super.air();
    }

    @Override
    public ABlockSettings allowsSpawning(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
        return (ABlockSettings) super.allowsSpawning(predicate);
    }

    @Override
    public ABlockSettings solidBlock(AbstractBlock.ContextPredicate predicate) {
        return (ABlockSettings) super.solidBlock(predicate);
    }

    @Override
    public ABlockSettings suffocates(AbstractBlock.ContextPredicate predicate) {
        return (ABlockSettings) super.suffocates(predicate);
    }

    @Override
    public ABlockSettings blockVision(AbstractBlock.ContextPredicate predicate) {
        return (ABlockSettings) super.blockVision(predicate);
    }

    @Override
    public ABlockSettings postProcess(AbstractBlock.ContextPredicate predicate) {
        return (ABlockSettings) super.postProcess(predicate);
    }

    @Override
    public ABlockSettings emissiveLighting(AbstractBlock.ContextPredicate predicate) {
        return (ABlockSettings) super.emissiveLighting(predicate);
    }

    @Override
    public ABlockSettings requiresTool() {
        return (ABlockSettings) super.requiresTool();
    }

    @Override
    public ABlockSettings mapColor(MapColor color) {
        return (ABlockSettings) super.mapColor(color);
    }

    @Override
    public ABlockSettings hardness(float hardness) {
        return (ABlockSettings) super.hardness(hardness);
    }

    @Override
    public ABlockSettings resistance(float resistance) {
        return (ABlockSettings) super.resistance(resistance);
    }

    @Override
    public ABlockSettings offset(AbstractBlock.OffsetType offsetType) {
        return (ABlockSettings) super.offset(offsetType);
    }

    @Override
    public ABlockSettings noBlockBreakParticles() {
        return (ABlockSettings) super.noBlockBreakParticles();
    }

    @Override
    public ABlockSettings requires(FeatureFlag... features) {
        return (ABlockSettings) super.requires(features);
    }

    @Override
    public ABlockSettings mapColor(Function<BlockState, MapColor> mapColorProvider) {
        return (ABlockSettings) super.mapColor(mapColorProvider);
    }

    @Override
    public ABlockSettings burnable() {
        return (ABlockSettings) super.burnable();
    }

    @Override
    public ABlockSettings liquid() {
        return (ABlockSettings) super.liquid();
    }

    @Override
    public ABlockSettings solid() {
        return (ABlockSettings) super.solid();
    }

    @Override
    public ABlockSettings notSolid() {
        return (ABlockSettings) super.notSolid();
    }

    @Override
    public ABlockSettings pistonBehavior(PistonBehavior pistonBehavior) {
        return (ABlockSettings) super.pistonBehavior(pistonBehavior);
    }

    @Override
    public ABlockSettings instrument(Instrument instrument) {
        return (ABlockSettings) super.instrument(instrument);
    }

    @Override
    public ABlockSettings replaceable() {
        return (ABlockSettings) super.replaceable();
    }

    @Override
    public ABlockSettings lightLevel(int lightLevel) {
        return (ABlockSettings) super.lightLevel(lightLevel);
    }

    @Override
    public ABlockSettings luminance(int luminance) {
        return (ABlockSettings) super.luminance(luminance);
    }

    @Override
    public ABlockSettings drops(Identifier dropTableId) {
        return (ABlockSettings) super.drops(dropTableId);
    }

    @Override
    public ABlockSettings materialColor(MapColor color) {
        return (ABlockSettings) super.materialColor(color);
    }

    @Override
    public ABlockSettings materialColor(DyeColor color) {
        return (ABlockSettings) super.materialColor(color);
    }

    @Override
    public ABlockSettings mapColor(DyeColor color) {
        return (ABlockSettings) super.mapColor(color);
    }

    @Override
    public ABlockSettings collidable(boolean collidable) {
        return (ABlockSettings) super.collidable(collidable);
    }

    public Item.Settings itemSettings() {
        return settings;
    }
}
