package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.api.ICantBreak;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITConsoleVariants;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.variant.console.ConsoleVariantSchema;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisTravel;

import java.util.*;

import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class ConsoleBlockEntity extends BlockEntity implements BlockEntityTicker<ConsoleBlockEntity>, ICantBreak {
    public final AnimationState ANIM_FLIGHT = new AnimationState();
    public int animationTimer = 0;
    public final List<ConsoleControlEntity> controlEntities = new ArrayList<>();
    private boolean needsControls = true;
    private boolean needsSync = true;
    private UUID tardisId;
    private ConsoleEnum type;
    private ConsoleVariantSchema variant;

    public static final Identifier SYNC_TYPE = new Identifier(AITMod.MOD_ID, "sync_console_type");
    public static final Identifier SYNC_VARIANT = new Identifier(AITMod.MOD_ID, "sync_console_variant");

    public ConsoleBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DISPLAY_CONSOLE_BLOCK_ENTITY_TYPE, pos, state);

        Tardis found = TardisUtil.findTardisByPosition(pos);
        if (found != null)
            this.setTardis(found);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (this.getTardis() == null) {
            AITMod.LOGGER.error("this.getTardis() is null! Is " + this + " invalid? BlockPos: " + "(" + this.getPos().toShortString() + ")");
        }

        if (type != null)
            nbt.putInt("type", type.ordinal());
        if (variant != null)
            nbt.putString("variant", variant.id().toString());

        super.writeNbt(nbt);
        nbt.putString("tardis", this.tardisId.toString());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("tardis")) {
            this.setTardis(UUID.fromString(nbt.getString("tardis")));
        }

        if (nbt.contains("type"))
            setType(ConsoleEnum.values()[nbt.getInt("type")]);
        if (nbt.contains("variant")) {
            setVariant(Identifier.tryParse(nbt.getString("variant")));
        }

        spawnControls();
        markNeedsSyncing();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public Tardis getTardis() {
        if (this.tardisId == null) {
            AITMod.LOGGER.warn("Console at " + this.getPos() + " is finding TARDIS!");
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

        // ServerTardisManager.getInstance().sendToSubscribers(this.getTardis());
        getTardis().markDirty();
        syncType();
        syncVariant();

        needsSync = false;
    }

    private void syncType() {
        if (!hasWorld() || world.isClient()) return;

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(getEnum().ordinal());
        buf.writeBlockPos(getPos());

        for (PlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_TYPE, buf); // safe cast as we know its server
        }
    }

    private void syncVariant() {
        if (!hasWorld() || world.isClient()) return;

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeString(getVariant().id().toString());
        buf.writeBlockPos(getPos());

        for (PlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_VARIANT, buf); // safe cast as we know its server
        }
    }

    public void setTardis(Tardis tardis) {
        if (tardis == null) {
            AITMod.LOGGER.error("Tardis was null in ConsoleBlockEntity at " + this.getPos());
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

    public TardisDesktop getDesktop() {
        return this.getTardis().getDesktop();
    }

    public ConsoleEnum getEnum() {
        if (type == null) setType(ConsoleEnum.BOREALIS);

        return type;
    }

    public void setType(ConsoleEnum var) {
        type = var;

        syncType();
        markDirty();
    }

    public ConsoleVariantSchema getVariant() {
        if (variant == null) {
            // oh no : (
            // lets just pick any
            setVariant(AITConsoleVariants.withParent(getEnum()).stream().findAny().get());
        }

        return variant;
    }
    public void setVariant(ConsoleVariantSchema var) {
        variant = var;

        if (variant.parent() != type) {
            AITMod.LOGGER.warn("Variant was set and it doesnt match this consoles type!");
            AITMod.LOGGER.warn(variant + " | " + type);
        }

        syncVariant();
        markDirty();
    }
    public void setVariant(Identifier id) {
        setVariant(AITConsoleVariants.get(id));
    }

    /**
     * Sets the new {@link ConsoleEnum} and refreshes the console entities
     */
    private void changeConsole(ConsoleEnum var) {
        changeConsole(var, AITConsoleVariants.withParent(var).stream().findAny().get());
    }

    private void changeConsole(ConsoleEnum var, ConsoleVariantSchema variant) {
        setType(var);
        setVariant(variant);

        if (!world.isClient() && world == TardisUtil.getTardisDimension())
            redoControls();
    }

    private void redoControls() {
        killControls();
        markNeedsControl();
    }

    public static ConsoleEnum nextConsole(ConsoleEnum current) {
        return ConsoleEnum.values()[(current.ordinal() + 1 > ConsoleEnum.values().length - 1) ? 0 : current.ordinal() + 1];
    }
    public static ConsoleVariantSchema nextVariant(ConsoleVariantSchema current) {
        List<ConsoleVariantSchema> list = AITConsoleVariants.withParent(current.parent()).stream().toList();

        int idx = list.indexOf(current);
        if (idx < 0 || idx+1 == list.size() - 1) return list.get(0);
        return list.get(idx + 1);
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if (player == null)
            return;

//        if (world != TardisUtil.getTardisDimension())
//            return;

        if (player.getMainHandStack().getItem() == Items.COMMAND_BLOCK) changeConsole(nextConsole(getEnum()));
        if (player.getMainHandStack().getItem() == Items.REPEATING_COMMAND_BLOCK) setVariant(nextVariant(getVariant()));
    }

    public void setDesktop(TardisDesktop desktop) {
        if (isClient()) return;

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
        this.killControls();
    }

    public void killControls() {
        controlEntities.forEach(Entity::discard);
        controlEntities.clear();
        sync();
        //System.out.println("KillControls(): I'm getting run :) somewhere..");
    }

    public void spawnControls() {
        BlockPos current = getPos();

        if (!(getWorld() instanceof ServerWorld server))
            return;
        if (getWorld().getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return;

        killControls();
        ConsoleEnum consoleType = getEnum();
        ControlTypes[] controls = consoleType.getControlTypesList();
        Arrays.stream(controls).toList().forEach(control -> {

            ConsoleControlEntity controlEntity = new ConsoleControlEntity(AITEntityTypes.CONTROL_ENTITY_TYPE, getWorld());

            Vector3f position = current.toCenterPos().toVector3f().add(control.getOffset().x(), control.getOffset().y(), control.getOffset().z());
            controlEntity.setPosition(position.x(), position.y(), position.z());
            controlEntity.setYaw(0.0f);
            controlEntity.setPitch(0.0f);

            controlEntity.setControlData(consoleType, control, this.getPos());

            server.spawnEntity(controlEntity);
            this.controlEntities.add(controlEntity);
        });

        this.needsControls = false;
        //System.out.println("SpawnControls(): I'm getting run :) somewhere..");
    }

    public void markNeedsControl() {
        this.needsControls = true;
    }
    public void markNeedsSyncing() {
        this.needsSync = true;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, ConsoleBlockEntity blockEntity) {
        if (this.needsControls) {
            spawnControls();
        }
        if (needsSync)
            sync();

        if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) {
            this.markRemoved();
        }

        // idk
        if (world.isClient()) {
            this.checkAnimations();
        }
    }

    @Override
    public void onTryBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        markNeedsSyncing(); // As theres a gap between the breaking of the block and when it gets resynced to client, so we need to wait a bit
        // fixme im lying and that doesnt fix the issue, the blockentity on client is null when tried to sync.
    }
}
