package loqor.ait.core.blockentities;

import java.util.Objects;
import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.api.TardisComponent;
import loqor.ait.api.link.LinkableItem;
import loqor.ait.api.link.v2.TardisRef;
import loqor.ait.api.link.v2.block.AbstractLinkableBlockEntity;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.item.KeyItem;
import loqor.ait.core.item.SiegeTardisItem;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.animation.ExteriorAnimation;
import loqor.ait.core.tardis.handler.DoorHandler;
import loqor.ait.core.tardis.handler.InteriorChangingHandler;
import loqor.ait.core.tardis.handler.SonicHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.tardis.util.TardisUtil;

public class ExteriorBlockEntity extends AbstractLinkableBlockEntity implements BlockEntityTicker<ExteriorBlockEntity> {
    public int animationTimer = 0;
    public final AnimationState DOOR_STATE = new AnimationState();
    private ExteriorAnimation animation;

    public ExteriorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public ExteriorBlockEntity(BlockPos pos, BlockState state, Tardis tardis) {
        this(pos, state);
        this.link(tardis);
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {
        if (this.tardis().isEmpty() || player == null)
            return;

        ServerTardis tardis = (ServerTardis) this.tardis().get();

        if (tardis.isGrowth())
            return;


        SonicHandler handler = tardis.sonic();

        boolean hasSonic = handler.getExteriorSonic() != null;
        boolean shouldEject = player.isSneaking();

        if (player.getMainHandStack().getItem() instanceof KeyItem && !tardis.siege().isActive()
                && !tardis.<InteriorChangingHandler>handler(TardisComponent.Id.INTERIOR).isQueued()) {
            ItemStack key = player.getMainHandStack();
            UUID keyId = LinkableItem.getTardisIdFromUuid(key, "tardis");

            if (Objects.equals(tardis.getUuid(), keyId)) {
                DoorHandler.toggleLock(tardis, (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.translatable("tardis.key.identity_error"), true); // TARDIS does not identify
                                                                                            // with key
            }

            return;
        }

        if (hasSonic) {
            if (shouldEject) {
                player.giveItemStack(handler.takeExteriorSonic());
                world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.BLOCKS, 1F,
                        0.2F);
                return;
            }

            player.sendMessage(Text.translatable("tardis.exterior.sonic.repairing")
                    .append(Text.literal(": " + tardis.crash().getRepairTicksAsSeconds() + "s")
                            .formatted(Formatting.BOLD, Formatting.GOLD)),
                    true);
            return;
        }

        if (player.getMainHandStack().getItem() instanceof SonicItem && !tardis.siege().isActive()
                && !tardis.<InteriorChangingHandler>handler(TardisComponent.Id.INTERIOR).isQueued()
                && tardis.door().isClosed() && tardis.crash().getRepairTicks() > 0) {
            ItemStack sonic = player.getMainHandStack();
            UUID sonicId = LinkableItem.getTardisIdFromUuid(sonic, "tardis");

            if (Objects.equals(tardis.getUuid(), sonicId)) {
                ItemStack stack = player.getMainHandStack();

                if (!(stack.getItem() instanceof SonicItem))
                    return;

                handler.insertExteriorSonic(stack);

                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1F, 0.2F);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.value(), SoundCategory.BLOCKS, 1F,
                        0.2F);
                player.sendMessage(Text.translatable("tardis.tool.cannot_repair"), true); // Unable to repair TARDIS
                                                                                            // with current tool!
            }

            return;
        }

        if (sneaking && tardis.siege().isActive() && !tardis.isSiegeBeingHeld()) {
            SiegeTardisItem.pickupTardis(tardis, (ServerPlayerEntity) player);
            return;
        }

        if (!tardis.travel().isLanded())
            return;

        DoorHandler.useDoor(tardis, (ServerWorld) this.getWorld(), this.getPos(), (ServerPlayerEntity) player);
    }

    public void onEntityCollision(Entity entity) {
        TardisRef ref = this.tardis();

        if (ref.isEmpty())
            return;

        Tardis tardis = ref.get();
        TravelHandler travel = tardis.travel();

        boolean previouslyLocked = tardis.door().previouslyLocked().get();

        if (!previouslyLocked && travel.getState() == TravelHandlerBase.State.MAT
                && travel.getAnimTicks() >= 0.9 * travel.getMaxAnimTicks())
            TardisUtil.teleportInside(tardis, entity);

        if (tardis.door().isClosed())
            return;

        if (!tardis.getLockedTardis()
                && (!DependencyChecker.hasPortals() || !tardis.getExterior().getVariant().hasPortals()))
            TardisUtil.teleportInside(tardis, entity);
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState blockState, ExteriorBlockEntity blockEntity) {
        TardisRef ref = this.tardis();

        if (ref == null || ref.isEmpty())
            return;

        Tardis tardis = ref.get();

        TravelHandlerBase travel = tardis.travel();
        TravelHandlerBase.State state = travel.getState();

        if (!world.isClient()) {
            if (tardis.travel().isLanded()) {
                world.scheduleBlockTick(this.getPos(), this.getCachedState().getBlock(), 2);
            }
        }

        if (!world.isClient())
            return;

        if (state.animated())
            this.getAnimation().tick(tardis);
        else
            this.getAnimation().reset();

        this.exteriorLightBlockState(blockState, pos, state);
        this.checkAnimations();
    }

    public void verifyAnimation() {
        TardisRef ref = this.tardis();

        if (this.animation != null || ref == null || ref.isEmpty())
            return;

        Tardis tardis = ref.get();

        this.animation = tardis.getExterior().getVariant().animation(this);
        this.animation.setupAnimation(tardis.travel().getState());

        if (this.getWorld() != null && !this.getWorld().isClient()) {
            this.animation.tellClientsToSetup(tardis.travel().getState());
        }
    }

    @Environment(EnvType.CLIENT)
    public void checkAnimations() {
        if (this.tardis().isEmpty())
            return;

        animationTimer++;
        Tardis tardis = this.tardis().get();
        DoorHandler door = tardis.door();

        DoorHandler.DoorStateEnum doorState = door.getDoorState();
        DoorHandler.DoorStateEnum animState = door.getAnimationExteriorState();

        if (animState == null)
            return;

        if (animState != doorState) {
            DOOR_STATE.start(animationTimer);
            door.tempExteriorState = doorState;
        }
    }

    public ExteriorAnimation getAnimation() {
        this.verifyAnimation();
        return this.animation;
    }

    @Environment(EnvType.CLIENT)
    public float getAlpha() {
        return this.getAnimation().getAlpha();
    }

    private void exteriorLightBlockState(BlockState blockState, BlockPos pos, TravelHandlerBase.State state) {
        if (!state.animated())
            return;

        if (!blockState.isOf(AITBlocks.EXTERIOR_BLOCK))
            return;

        this.getWorld().setBlockState(pos, blockState.with(ExteriorBlock.LEVEL_9, Math.round(this.getAlpha() * 9)));
    }
}
