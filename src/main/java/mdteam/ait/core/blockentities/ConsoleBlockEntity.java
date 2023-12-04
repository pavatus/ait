package mdteam.ait.core.blockentities;

import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.entities.control.ControlTypes;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisDesktop;
import the.mdteam.ait.TardisManager;
import the.mdteam.ait.TardisTravel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static the.mdteam.ait.TardisTravel.State.*;

public class ConsoleBlockEntity extends BlockEntity implements ILinkable, BlockEntityTicker<ConsoleBlockEntity> {
    public final AnimationState ANIM_FLIGHT = new AnimationState();
    public int animationTimer = 0;

    private final List<ConsoleControlEntity> controlEntities = new ArrayList<>();
    private boolean markedDirty = true;
    private Tardis tardis;

    public ConsoleBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DISPLAY_CONSOLE_BLOCK_ENTITY_TYPE, pos, state);

        this.setTardis(TardisUtil.findTardisByInterior(pos));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        //this.getAnimation().setAlpha(nbt.getFloat("alpha"));

        if (this.tardis != null) {
            nbt.putUuid("tardis", this.tardis.getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        if (nbt.contains("tardis")) {
            TardisManager.getInstance().link(nbt.getUuid("tardis"), this);
        }

        super.readNbt(nbt);

        spawnControls();
    }

    @Override
    public Tardis getTardis() {
        return tardis;
    }

    public ConsoleEnum getConsole() {
        return this.tardis.getConsole().getType();
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardis = tardis;

        // force re-link a desktop if it's not null
        this.linkDesktop();
    }

    public void useOn(ServerWorld world, boolean sneaking, PlayerEntity player) {

        if(player == null)
            return;

        if(world != TardisUtil.getTardisDimension())
            return;
    }

    @Override
    public void setDesktop(TardisDesktop desktop) {
        if(this.getWorld() == null)
            return;
        if(this.getWorld() != TardisUtil.getTardisDimension())
            return;
        desktop.setConsolePos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getWorld().getBlockState(this.getPos()).get(ConsoleBlock.FACING))
        );
    }

    public void checkAnimations() {
        // DO NOT RUN THIS ON SERVER!!

        animationTimer++;
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
        System.out.println("KillControls(): I'm getting run :) somewhere..");
    }

    public void spawnControls() {

        BlockPos current = getPos();

        if(!(getWorld() instanceof ServerWorld server))
            return;

        killControls();
        ConsoleEnum consoleType = this.getConsole();
        ControlTypes[] controls = consoleType.getControlTypesList();
        Arrays.stream(controls).toList().forEach(control -> {

            ConsoleControlEntity controlEntity = new ConsoleControlEntity(AITEntityTypes.CONTROL_ENTITY_TYPE, getWorld());

            Vector3f position = current.toCenterPos().toVector3f().add(control.getOffsetFromCenter().x(), control.getOffsetFromCenter().y(), control.getOffsetFromCenter().z());
            controlEntity.setPosition(position.x(), position.y(), position.z());
            controlEntity.setYaw(0);
            controlEntity.setPitch(0);

            controlEntity.setControlData(consoleType, control, this.getPos());

            server.spawnEntity(controlEntity);
            this.controlEntities.add(controlEntity);
        });

        this.markedDirty = false;
        System.out.println("SpawnControls(): I'm getting run :) somewhere..");
    }
    public void markDirty() {
        this.markedDirty = true;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ConsoleBlockEntity blockEntity) {
        if(this.markedDirty) {
            spawnControls();
        }
//        if (this.controlEntities.isEmpty()) {
//            killControls();
//            spawnControls();
//        }

        // idk
        if (world.isClient()) {
            this.checkAnimations();
        }
    }

}
