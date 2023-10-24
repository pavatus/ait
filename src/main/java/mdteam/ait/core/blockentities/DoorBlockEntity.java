package mdteam.ait.core.blockentities;

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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.util.UUID;

import static mdteam.ait.AITMod.EXTERIORNBT;
import static mdteam.ait.core.helper.desktop.TARDISDesktop.teleportToExterior;

public class DoorBlockEntity extends BlockEntity {

    private UUID tardisUUid;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);
        setTardis(getTardis());
    }

    public static void tick(World world1, BlockPos pos, BlockState state1, ExteriorBlockEntity be) {

    }

    public void useOn(BlockHitResult hit, BlockState state, PlayerEntity player, World world, boolean sneaking) {
        world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS,0.6f, 1f);
        if(!sneaking) onEntityCollision(state, world, getPos(), player);
    }

    public UUID getTardisUuid() {
        return tardisUUid;
    }

    public Tardis getTardis() {
        return TardisHandler.getTardisByInteriorPos(pos);
    }

    public void setTardis(Tardis tardis) {
        if(tardis != null) this.tardisUUid = tardis.getUuid();
    }

    public TARDISDesktop getDesktop() {
        return this.getTardis().getDesktop();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(tardisUUid != null) {
            nbt.putUuid("tardis", tardisUUid);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if(nbt.contains("tardis")) {
            this.tardisUUid = nbt.getUuid("tardis");
        }
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        Tardis tardis = TardisHandler.getTardis(this.tardisUUid);
        if (entity instanceof ServerPlayerEntity player && !world.isClient()) {
            if (tardis != null)
                teleportToExterior(player, tardis.getPosition(), tardis.getPosition().getDimension(), tardis.getPosition().getDirection());
        }
    }
}
