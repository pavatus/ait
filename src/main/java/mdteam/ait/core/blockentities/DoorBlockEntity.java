package mdteam.ait.core.blockentities;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.MaterialStateEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.DoorBlock;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.helper.TeleportHelper;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.TardisHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.UUID;

import static mdteam.ait.AITMod.EXTERIORNBT;
import static mdteam.ait.core.helper.desktop.TARDISDesktop.offsetDoorPosition;
import static mdteam.ait.core.helper.desktop.TARDISDesktop.teleportToExterior;

public class DoorBlockEntity extends BlockEntity {

    private Tardis tardis;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);
        setTardis(getTardis());
        getTardis().getDesktop().setInteriorDoorPosition(getPos());
    }

    public static void tick(World world1, BlockPos pos, BlockState state1, ExteriorBlockEntity be) {

    }

    public void useOn(BlockHitResult hit, BlockState state, PlayerEntity player, World world, boolean sneaking) {
        world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS,0.6f, 1f);
        if(!sneaking) onEntityCollision(state, world, pos, player);
    }

    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
    }

    public Tardis getTardis() {
        if(tardis == null)
            return TardisHandler.getTardisByInteriorPos(pos);
        else return tardis;
    }

    public TARDISDesktop getDesktop() {
        return this.getTardis().getDesktop();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(tardis != null) {
            nbt.putUuid("tardis", tardis.getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        getTardis().getDesktop().setInteriorDoorPosition(getPos());
        if(nbt.contains("tardis")) {
            this.tardis = TardisHandler.getTardis(nbt.getUuid("tardis"));
        }
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        ServerPlayerEntity player = AITMod.mcServer.getPlayerManager().getPlayer(entity.getUuid());
        ServerWorld newServerWorld = AITMod.mcServer.getWorld(getTardis().getPosition().getDimension().getRegistryKey());
        if(newServerWorld != null) newServerWorld.getChunk(getTardis().getPosition().toBlockPos());
        if (player != null) {
            if (!world.isClient()) {
                if (getTardis() != null) {
                    WorldOps.teleportToWorld(player, newServerWorld,
                            offsetDoorPosition(getTardis().getPosition().toBlockPos(), getTardis().getPosition().getDimension()).toCenterPos(),
                            getTardis().getPosition().getDirection().asRotation(), player.getPitch());
                }
            }
        }
    }
}
