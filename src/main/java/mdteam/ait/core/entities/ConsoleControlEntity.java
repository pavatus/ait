package mdteam.ait.core.entities;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.entities.control.ControlTypes;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import mdteam.ait.tardis.Tardis;

import java.util.List;

public class ConsoleControlEntity extends BaseControlEntity {

    private BlockPos consoleBlockPos;
    private ControlTypes controlTypes;

    private static final TrackedData<String> IDENTITY = DataTracker.registerData(ConsoleControlEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Float> SCALE = DataTracker.registerData(ConsoleControlEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Vector3f> OFFSET = DataTracker.registerData(ConsoleControlEntity.class, TrackedDataHandlerRegistry.VECTOR3F);

    public ConsoleControlEntity(EntityType<? extends BaseControlEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IDENTITY, "");
        this.dataTracker.startTracking(SCALE, 0.125f);
        this.dataTracker.startTracking(OFFSET, new Vector3f(0));
    }

    public String getIdentity() {
        return this.dataTracker.get(IDENTITY);
    }

    public void setIdentity(String string) {
        this.dataTracker.set(IDENTITY, string);
    }
    public float getScale() {
        return this.dataTracker.get(SCALE);
    }

    public void setScale(float scale) {
        this.dataTracker.set(SCALE, scale);
    }
    public Vector3f getOffset() {
        return this.dataTracker.get(OFFSET);
    }

    public void setOffset(Vector3f offset) {
        this.dataTracker.set(OFFSET, offset);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if(consoleBlockPos != null)
            nbt.put("console", NbtHelper.fromBlockPos(this.consoleBlockPos));
        nbt.putString("identity", this.getIdentity());
        nbt.putFloat("scale", this.getScale());
        nbt.putFloat("offsetX", this.getOffset().x());
        nbt.putFloat("offsetY", this.getOffset().y());
        nbt.putFloat("offsetZ", this.getOffset().z());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        var console = (NbtCompound) nbt.get("console");
        if(console != null)
            this.consoleBlockPos = NbtHelper.toBlockPos(console);
        if (nbt.contains("identity")) {
            this.setIdentity(nbt.getString("identity"));
        }
        if (nbt.contains("scale")) {
            this.setScale(nbt.getFloat("scale"));
            this.calculateDimensions();
        }
        if(nbt.contains("offsetX") && nbt.contains("offsetY") && nbt.contains("offsetZ")) {
            this.setOffset(new Vector3f(nbt.getFloat("offsetX"), nbt.getFloat("offsetY"), nbt.getFloat("offsetZ")));
        }
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (hand == Hand.MAIN_HAND)
            this.run(player, player.getWorld());

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity) {
            this.run((PlayerEntity) source.getAttacker(), source.getAttacker().getWorld());
        }

        return super.damage(source, amount);
    }

    public boolean run(PlayerEntity player, World world) {
        this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_CANDLE_STEP, SoundCategory.BLOCKS, 0.1f, 1f);

        if (!world.isClient()) {
            if (player.getMainHandStack().getItem() == AITItems.TARDIS_ITEM) {
                this.remove(RemovalReason.DISCARDED);
            } else if (player.getMainHandStack().getItem() == Items.COMMAND_BLOCK) {
                controlEditorHandler(player);
            }

            return this.controlTypes.control().runServer(this.getTardis(world), (ServerPlayerEntity) player, (ServerWorld) world); // i dont gotta check these cus i know its server
        } else {
            // fixme client doesnt have controltypes so this causes a null crash
            return true;
            // return this.controlTypes.control().runClient(this.getTardis(world), (ClientPlayerEntity) player, (ClientWorld) world);
        }
    }

    // clearly loqor has trust issues with running this so i do too so im overwriting it to do what he did fixme pls
    public Tardis getTardis(World world) {
        if (!(this.consoleBlockPos != null && this.controlTypes != null && world.getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console))
            return null;

        return console.getTardis();
    }

    public void setScaleAndCalculate(float scale) {
        this.setScale(scale);
        this.calculateDimensions();
    }

    @Override
    public Text getName() {
        if(this.controlTypes != null)
            return Text.translatable(this.controlTypes.control().id);
        else
            return super.getName();
    }

    public void setControlData(ConsoleEnum consoleType, ControlTypes type, BlockPos consoleBlockPosition) {
        this.consoleBlockPos = consoleBlockPosition;
        this.controlTypes = type;
        // System.out.println(type);
        if(consoleType != null) {
            this.setScale(type.getScale().width);
            this.setCustomName(Text.translatable(type.control().id).fillStyle(Style.EMPTY.withColor(Formatting.BLUE).withBold(true)));
        }
    }

    public Vec3d offsetFromCentre(BlockPos console, Vector3f vec) {
        double x = vec.x - console.toCenterPos().x;
        double y = vec.y - console.toCenterPos().y;
        double z = vec.z - console.toCenterPos().z;

        return new Vec3d(x, y, z);
    }

    @Override
    public void onDataTrackerUpdate(List<DataTracker.SerializedEntry<?>> dataEntries) {
        this.setScaleAndCalculate(this.getDataTracker().get(SCALE));
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        if(this.getDataTracker().containsKey(SCALE))
            return EntityDimensions.changing(this.getScale(), this.getScale());
        return super.getDimensions(pose);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BLOCK_NOTE_BLOCK_BIT.value();
    }

    @Override
    public void tick() {
        if(getWorld() instanceof ServerWorld server && this.controlTypes == null && this.consoleBlockPos != null) {
            if (server.getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console) {
                console.markDirty();
            }
            discard();
        }
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public boolean shouldRenderName() {
        return true;
    }

    public void controlEditorHandler(PlayerEntity player) {
        float increment = 0.025f;
        if(player.getOffHandStack().getItem() == Items.EMERALD_BLOCK) {
            this.setPosition(this.getPos().add(player.isSneaking() ? -increment : increment, 0, 0));
        }
        if(player.getOffHandStack().getItem() == Items.DIAMOND_BLOCK) {
            this.setPosition(this.getPos().add(0, player.isSneaking() ? -increment : increment, 0));
        }
        if(player.getOffHandStack().getItem() == Items.REDSTONE_BLOCK) {
            this.setPosition(this.getPos().add(0, 0, player.isSneaking() ? -increment : increment));
        }
        if(player.getOffHandStack().getItem() == Items.COD) {
            this.setScaleAndCalculate(player.isSneaking() ? this.getDataTracker().get(SCALE) - increment : this.getDataTracker().get(SCALE) + increment);
        }
        if(this.consoleBlockPos != null) {
            Vec3d centered = this.getPos().subtract(this.consoleBlockPos.toCenterPos());
            if(this.controlTypes != null) player.sendMessage(Text.literal(this.controlTypes.control().id.toUpperCase() + ": " + "Position: " + centered + " & Scale: " + this.getScale()));
        }
    }
}
