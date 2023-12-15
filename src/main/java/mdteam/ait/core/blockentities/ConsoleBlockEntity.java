package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisManager;
import mdteam.ait.tardis.TardisTravel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class ConsoleBlockEntity extends BlockEntity implements BlockEntityTicker<ConsoleBlockEntity> {
    public final AnimationState ANIM_FLIGHT = new AnimationState();
    public int animationTimer = 0;
    public final List<ConsoleControlEntity> controlEntities = new ArrayList<>();
    private boolean markedDirty = true;
    private UUID tardisId;

    public ConsoleBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DISPLAY_CONSOLE_BLOCK_ENTITY_TYPE, pos, state);

        Tardis found = TardisUtil.findTardisByPosition(pos);
        if (found != null)
            this.setTardis(found);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (this.getTardis() != null) {
            nbt.putUuid("tardis", this.tardisId);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        if (nbt.contains("tardis")) {
            this.setTardis(nbt.getUuid("tardis"));
        }

        super.readNbt(nbt);

        spawnControls();
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

    public ConsoleEnum getConsole() {
        return this.getTardis().getConsole().getType();
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {

        if(player == null)
            return;

        if(world != TardisUtil.getTardisDimension())
            return;
    }

    public void setDesktop(TardisDesktop desktop) {
        desktop.setConsolePos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getCachedState().get(HorizontalDirectionalBlock.FACING))
        );
    }

    public void checkAnimations() {
        // DO NOT RUN THIS ON SERVER!!

        animationTimer++;

        if (this.getTardis() == null)
            return;
        TardisTravel.State state = this.getTardis().getTravel().getState();

        if (!ANIM_FLIGHT.isRunning()) {
            ANIM_FLIGHT.start(animationTimer);
        }
    }
    private void stopAllAnimations() {
        // DO NOT RUN ON SERVER
        ANIM_FLIGHT.stop();
    }

    public void onBroken() {
    }

    public void killControls() {
        controlEntities.forEach(Entity::discard);
        controlEntities.clear();
        this.sync();
        //System.out.println("KillControls(): I'm getting run :) somewhere..");
    }

    public void spawnControls() {

        BlockPos current = getPos();

        if(!(getWorld() instanceof ServerWorld server))
            return;
        if(getWorld().getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return;

        killControls();
        ConsoleEnum consoleType = this.getConsole();
        ControlTypes[] controls = consoleType.getControlTypesList();
        Arrays.stream(controls).toList().forEach(control -> {

            ConsoleControlEntity controlEntity = new ConsoleControlEntity(AITEntityTypes.CONTROL_ENTITY_TYPE, getWorld());

            Vector3f position = current.toCenterPos().toVector3f().add(control.getOffset().x(), control.getOffset().y(), control.getOffset().z());
            controlEntity.setPosition(position.x(), position.y(), position.z());
            controlEntity.setYaw(0);
            controlEntity.setPitch(0);

            controlEntity.setControlData(consoleType, control, this.getPos());

            server.spawnEntity(controlEntity);
            this.controlEntities.add(controlEntity);
        });

        this.markedDirty = false;
        //System.out.println("SpawnControls(): I'm getting run :) somewhere..");
    }
    public void markDirty() {
        this.markedDirty = true;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ConsoleBlockEntity blockEntity) {
        if(this.markedDirty) {
            spawnControls();
        }

        if(world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            this.markRemoved();

        // idk
        if (world.isClient()) {
            this.checkAnimations();
        }
    }

}
