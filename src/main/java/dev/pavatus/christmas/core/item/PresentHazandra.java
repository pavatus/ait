package dev.pavatus.christmas.core.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.core.item.InteriorTeleporterItem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.data.DirectedBlockPos;
import loqor.ait.data.DirectedGlobalPos;

/**
 * a festive version of the interior teleporter
 * it is a present box
 * places snow at door
 * unlocks 28/12
 */
public class PresentHazandra extends InteriorTeleporterItem {
    public PresentHazandra(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TypedActionResult<ItemStack> result = super.use(world, user, hand);

        if (!result.getResult().isAccepted() || world.isClient()) return result;

        // find a safe location and place snow at the interior door
        Tardis tardis = getTardis(world, user.getStackInHand(hand));
        DirectedBlockPos door = tardis.getDesktop().doorPos();
        DirectedGlobalPos.Cached safe = WorldUtil.locateSafe(DirectedGlobalPos.Cached.create(tardis.asServer().getInteriorWorld(), door.getPos().offset(door.toMinecraftDirection(), 2), door.getRotation()), TravelHandlerBase.GroundSearch.MEDIAN, true);

        if (!(safe.getWorld().getBlockState(safe.getPos()).isAir())) {
            AITMod.LOGGER.error("Failed to create snow at {} for {}", safe, tardis);
            return result;
        }

        safe.getWorld().setBlockState(safe.getPos(), Blocks.POWDER_SNOW.getDefaultState());
        return result;
    }

    @Override
    protected ParticleEffect getSuccessParticle() {
        return ParticleTypes.SNOWFLAKE;
    }
}
