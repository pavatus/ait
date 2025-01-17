package dev.pavatus.planet.core.blockentities;

import dev.pavatus.planet.core.PlanetBlockEntities;
import dev.pavatus.planet.core.planet.Planet;
import dev.pavatus.planet.core.planet.PlanetRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import loqor.ait.core.AITStatusEffects;

public class OxygenatorBlockEntity extends BlockEntity {
    public OxygenatorBlockEntity(BlockPos pos, BlockState state) {
        super(PlanetBlockEntities.OXYGENATOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void tick(World world, BlockPos blockPos, BlockState blockState, OxygenatorBlockEntity oxygenatorBlockEntity) {
        if (world.isClient()) return;
        Planet planet = PlanetRegistry.getInstance().get(world);
        if (planet == null) return;
        if (planet.hasOxygen()) return;
        world.getOtherEntities(null, new Box(blockPos).expand(20), entity -> entity instanceof LivingEntity).forEach(entity -> {
            if  (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(AITStatusEffects.OXYGENATED, 20, 1, true, false));
            }
        });
    }
}
