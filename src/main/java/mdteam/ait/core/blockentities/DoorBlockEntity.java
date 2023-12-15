package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.datagen.datagen_providers.AITLanguageProvider;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisManager;

import java.util.Objects;
import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class DoorBlockEntity extends BlockEntity {

    private UUID tardisId;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);

        // even though TardisDesktop links the door, we need to link it here as well to avoid desync
        Tardis found = TardisUtil.findTardisByPosition(pos);
        if (found != null)
            this.setTardis(found);
        if(this.getTardis() != null) {
            this.setDesktop(this.getDesktop());
            /*if(this.getDesktop() != null) {
                this.getDesktop().setInteriorDoorPos(new AbsoluteBlockPos.Directed(pos, TardisUtil.getTardisDimension(), this.getFacing()));
                this.getDesktop().updateDoor();
            }*/
        }
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if(player == null)
            return;
        if(player.getMainHandStack().getItem() instanceof KeyItem) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if(!tag.contains("tardis")) {
                return;
            }
            if(Objects.equals(this.getTardis().getUuid().toString(), tag.getUuid("tardis").toString())) {
                DoorHandler.toggleLock(this.getTardis(), (ServerWorld) world, (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }
        DoorHandler.useDoor(this.getTardis(), (ServerWorld) world, this.getPos(), (ServerPlayerEntity) player);
        if (sneaking)
            return;
        AbsoluteBlockPos exteriorPos = this.getTardis().getTravel().getPosition();
        ExteriorBlockEntity exterior = TardisUtil.getExterior(this.getTardis());
        if (exterior != null) {
            exteriorPos.getChunk();
            if(!world.isClient())
                exterior.sync();
        }
        this.sync();
    }
    public float getLeftDoorRotation() {
        if (this.getTardis() == null) return 5;
        return this.getTardis().getDoor().isLeftOpen() ? 1.2f : 0;
    }

    public float getRightDoorRotation() {
        if (this.getTardis() == null) return 5;
        return this.getTardis().getDoor().isRightOpen() ? 1.2f : 0;
    }

    public Direction getFacing() {
        return this.getCachedState().get(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(this.getTardis() != null) {
            nbt.putUuid("tardis", this.getTardis().getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if(nbt.contains("tardis")) {
            this.setTardis(nbt.getUuid("tardis"));
        }
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player) || this.getWorld() != TardisUtil.getTardisDimension())
            return;
        if (this.getTardis() != null && this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0) {
            if(!this.getTardis().getLockedTardis())
                TardisUtil.teleportOutside(this.getTardis(), player);
        }
    }

    public Tardis getTardis() {
        if (this.tardisId == null) {
            AITMod.LOGGER.warn("Door at " + this.getPos() + " is finding TARDIS!");
            this.findTardis();
        }

        if (isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(this.tardisId);
        }

        return ServerTardisManager.getInstance().getTardis(this.tardisId);
    }
    private void findTardis() {
        this.setTardis(TardisUtil.findTardisByInterior(pos));
    }
    public void sync() {
        if (isClient()) return;

        ServerTardisManager.getInstance().sendToSubscribers(this.getTardis());
    }

    public void setTardis(Tardis tardis) {
        if (tardis == null) {
            AITMod.LOGGER.error("Tardis was null in DoorBlockEntity at " + this.getPos());
            return;
        }

        this.tardisId = tardis.getUuid();
        // force re-link a desktop if it's not null
        this.linkDesktop();
    }
    public void setTardis(UUID uuid) {
        this.tardisId = uuid;

        this.linkDesktop();
    }

    public void linkDesktop() {
        if (this.getTardis() == null)
            return;
        if (this.getTardis() != null)
            this.setDesktop(this.getDesktop());
    }

    public TardisDesktop getDesktop() { return this.getTardis().getDesktop(); }

    public void setDesktop(TardisDesktop desktop) {
        desktop.setInteriorDoorPos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getFacing())
        );
    }
}