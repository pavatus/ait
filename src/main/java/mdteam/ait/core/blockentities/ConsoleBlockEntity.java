package mdteam.ait.core.blockentities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.entities.ConsoleControlEntity;
import mdteam.ait.core.managers.RiftChunkManager;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.registry.ConsoleVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.console.type.ConsoleTypeSchema;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.link.LinkableBlockEntity;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.console.variant.ConsoleVariantSchema;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

import static mdteam.ait.tardis.util.TardisUtil.findTardisByInterior;
import static mdteam.ait.tardis.util.TardisUtil.isClient;

public class ConsoleBlockEntity extends LinkableBlockEntity implements BlockEntityTicker<ConsoleBlockEntity> {
    public final AnimationState ANIM_FLIGHT = new AnimationState();
    public int animationTimer = 0;
    public final List<ConsoleControlEntity> controlEntities = new ArrayList<>();
    private boolean needsControls = true;
    private boolean needsSync = true;
    private Identifier type;
    private Identifier variant;
    private boolean wasPowered = false;
    private boolean needsReloading = true;
    private UUID parent;

    public static final Identifier SYNC_TYPE = new Identifier(AITMod.MOD_ID, "sync_console_type");
    public static final Identifier SYNC_VARIANT = new Identifier(AITMod.MOD_ID, "sync_console_variant");
    public static final Identifier SYNC_PARENT = new Identifier(AITMod.MOD_ID, "sync_console_parent");
    public static final Identifier ASK = new Identifier(AITMod.MOD_ID, "client_ask_console");

    public ConsoleBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.CONSOLE_BLOCK_ENTITY_TYPE, pos, state);
        if(!this.hasWorld()) return;
        if(this.findTardis().isEmpty()) return;
        this.linkDesktop();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (type != null)
            nbt.putString("type", type.toString());
        if (variant != null)
            nbt.putString("variant", variant.toString());
        if (parent != null) {
            nbt.put("parent", NbtHelper.fromUuid(parent));
        }

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("type"))
            setType(Identifier.tryParse(nbt.getString("type")));
        if (nbt.contains("variant")) {
            setVariant(Identifier.tryParse(nbt.getString("variant")));
        }
        if (nbt.contains("parent")) {
            setParent(NbtHelper.toUuid(nbt.get("parent")));
        }

        spawnControls();
        markNeedsSyncing();
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        if (nbt.contains("type"))
            setType(ConsoleRegistry.REGISTRY.get(Identifier.tryParse(nbt.getString("type"))));
        if (nbt.contains("variant")) {
            setVariant(Identifier.tryParse(nbt.getString("variant")));
        }

        if (type != null)
            nbt.putString("type", type.toString());
        if (variant != null)
            nbt.putString("variant", variant.toString());
        markNeedsControl();
        markNeedsSyncing();
        return nbt;
    }

    @Override
    public Optional<Tardis> findTardis() {
        if(this.tardisId == null) {
            Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
            if (found != null)
                this.setTardis(found);
        }
        return super.findTardis();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void ask() {
        if (!getWorld().isClient()) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.getPos());
        ClientPlayNetworking.send(ASK, buf);
    }

    public void sync() {
        if (isClient()) return;
        syncType();
        syncVariant();
        this.syncParent();
        needsSync = false;
    }

    private void syncType() {
        if (!hasWorld() || world.isClient()) return;

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeString(getConsoleSchema().id().toString());
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
//            AITMod.LOGGER.error("Tardis was null in ConsoleBlockEntity at " + this.getPos());
            return;
        }

        this.tardisId = tardis.getUuid();
        // force re-link a desktop if it's not null
        this.linkDesktop();
    }

    public void setTardis(UUID uuid) {
        this.tardisId = uuid;
        markDirty();

        this.linkDesktop();
    }

    public void linkDesktop() {
        if (this.findTardis().isEmpty())
            return;
        this.setDesktop(this.getDesktop());
    }

    public TardisDesktop getDesktop() {
        if(this.findTardis().isEmpty()) return null;
        return this.findTardis().get().getDesktop();
    }

    public ConsoleTypeSchema getConsoleSchema() {
        if (type == null) setType(ConsoleRegistry.HARTNELL);

        return ConsoleRegistry.REGISTRY.get(type);
    }

    public void setType(Identifier var) {
        type = var;

        syncType();
        markDirty();
    }
    public void setType(ConsoleTypeSchema schema) {
        setType(schema.id());
    }


    public ConsoleVariantSchema getVariant() {
        if (variant == null) {
            // oh no : (
            // lets just pick any
            setVariant(this.getConsoleSchema().getDefaultVariant());
        }

        return ConsoleVariantRegistry.getInstance().get(variant);
    }
    public void setVariant(Identifier var) {
        variant = var;

        if (!(getVariant().parent().id().equals(type))) {
            AITMod.LOGGER.warn("Variant was set and it doesnt match this consoles type!");
            AITMod.LOGGER.warn(variant + " | " + type);

            if (hasWorld() && getWorld().isClient()) ask();
        }

        syncVariant();
        markDirty();
    }
    public void setVariant(ConsoleVariantSchema schema) {
        setVariant(schema.id());
    }

    public void setParent(UUID uuid) {
        this.parent = uuid;

        this.syncParent();
    }
    private void syncParent() {
        if (!hasWorld() || world.isClient() || this.findParent().isEmpty()) return;

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeUuid(this.parent);
        buf.writeBlockPos(getPos());

        for (PlayerEntity player : world.getPlayers()) {
            ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_PARENT, buf); // safe cast as we know its server
        }
    }
    public Optional<TardisConsole> findParent() {
        if (this.findTardis().isEmpty()) return Optional.empty();

        TardisDesktop desktop = this.getDesktop();

        if (this.parent == null) {
            TardisConsole found = desktop.findConsole(new AbsoluteBlockPos(this.getPos(), this.getWorld()));

            if (found == null) {
                found = new TardisConsole(this.findTardis().get(), new AbsoluteBlockPos(this.getPos(), this.getWorld()));
                desktop.addConsole(found);
            }

            this.setParent(found.uuid());
        }

        return Optional.ofNullable(desktop.findConsole(this.parent));
    }

    /**
     * Sets the new {@link ConsoleTypeSchema} and refreshes the console entities
     */
    private void changeConsole(ConsoleTypeSchema var) {
        changeConsole(var, ConsoleVariantRegistry.withParent(var).stream().findAny().get());
    }

    private void changeConsole(ConsoleTypeSchema var, ConsoleVariantSchema variant) {
        setType(var);
        setVariant(variant);

        if (!world.isClient() && world == TardisUtil.getTardisDimension())
            redoControls();
    }

    private void redoControls() {
        killControls();
        markNeedsControl();
    }

    public static ConsoleTypeSchema nextConsole(ConsoleTypeSchema current) {
        List<ConsoleTypeSchema> list = ConsoleRegistry.REGISTRY.stream().toList();

        int idx = list.indexOf(current);
        if (idx < 0 || idx+1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }
    public static ConsoleVariantSchema nextVariant(ConsoleVariantSchema current) {
        List<ConsoleVariantSchema> list = ConsoleVariantRegistry.withParentToList(current.parent());

        int idx = list.indexOf(current);
        if (idx < 0 || idx+1 == list.size()) return list.get(0);
        return list.get(idx + 1);
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {

    }
    @Override
    public void markRemoved() {
        this.killControls();
        super.markRemoved();
    }
    public void setDesktop(TardisDesktop desktop) {
        if (this.getWorld() == null || this.getWorld().isClient()) return;

        AITMod.LOGGER.info("Linking destkop " + this.findTardis().get().getUuid());

        this.findParent();
    }

    public boolean wasPowered() {
        if(this.findTardis().isEmpty()) return false;
        return this.wasPowered ^ this.findTardis().get().hasPower();
    }

    public void checkAnimations() {
        // DO NOT RUN THIS ON SERVER!!

        animationTimer++;

        ANIM_FLIGHT.startIfNotRunning(animationTimer);
    }


    public void onBroken() {
        this.killControls();
    }

    public void killControls() {
        controlEntities.forEach(Entity::discard);
        controlEntities.clear();
        sync();
    }

    public void spawnControls() {
        BlockPos current = getPos();

        if (!(getWorld() instanceof ServerWorld server))
            return;
        if (getWorld().getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return;

        killControls();
        ConsoleTypeSchema consoleType = getConsoleSchema();
        ControlTypes[] controls = consoleType.getControlTypes();
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
        Random random = new Random();
        if (this.needsControls) {
            spawnControls();
        }
        if (needsSync)
            sync();
        if (needsReloading) {
            markNeedsSyncing();
            needsReloading = false;
        }

        if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) {
            this.markRemoved();
        }

        // idk
        if (world.isClient()) {
            this.checkAnimations();
        }
        if(this.findTardis().isEmpty()) return;

        if (world.isClient()) return;

        ServerTardis tardis = (ServerTardis) this.findTardis().get();

        boolean isRiftChunk = RiftChunkManager.isRiftChunk(tardis.getExterior().getExteriorPos());

        if (tardis.getTravel().isCrashing()) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5f, pos.getY() + 1.25,
                    pos.getZ() + 0.5f, 1, 0,0,0, 0.025f);
            ((ServerWorld) world).spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX() + 0.5f, pos.getY() + 1.25,
                    pos.getZ() + 0.5f, 20, 0,0,0, 0.01f);
            ((ServerWorld) world).spawnParticles(new DustColorTransitionParticleEffect(
                            new Vector3f(0.75f, 0.75f, 0.75f), new Vector3f(0.1f, 0.1f, 0.1f), 1), pos.getX() + 0.5f, pos.getY() + 1.25,
                    pos.getZ() + 0.5f, 20, 0,0,0, 0.01f);
        }
        if (tardis.isRefueling()) {
            ((ServerWorld) world).spawnParticles((isRiftChunk) ? ParticleTypes.FIREWORK : ParticleTypes.END_ROD, pos.getX() + 0.5f, pos.getY() + 1.25,
                    pos.getZ() + 0.5f, 1, 0,0,0, (isRiftChunk) ? 0.05f : 0.025f);
        }
    }

}
