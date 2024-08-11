package loqor.ait.core.entities;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.entities.base.LinkableDummyLivingEntity;
import loqor.ait.core.item.control.ControlBlockItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.control.ControlTypes;

public class ConsoleControlEntity extends LinkableDummyLivingEntity {

    private static final TrackedData<String> IDENTITY = DataTracker.registerData(ConsoleControlEntity.class,
            TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(ConsoleControlEntity.class,
            TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(ConsoleControlEntity.class,
            TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Vector3f> OFFSET = DataTracker.registerData(ConsoleControlEntity.class,
            TrackedDataHandlerRegistry.VECTOR3F);
    private static final TrackedData<Boolean> PART_OF_SEQUENCE = DataTracker.registerData(ConsoleControlEntity.class,
            TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SEQUENCE_COLOR = DataTracker.registerData(ConsoleControlEntity.class,
            TrackedDataHandlerRegistry.INTEGER); // <--->
    private static final TrackedData<Boolean> WAS_SEQUENCED = DataTracker.registerData(ConsoleControlEntity.class,
            TrackedDataHandlerRegistry.BOOLEAN);

    private BlockPos consoleBlockPos;
    private Control control;

    public ConsoleControlEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    private ConsoleControlEntity(World world, Tardis tardis) {
        this(AITEntityTypes.CONTROL_ENTITY_TYPE, world);
        this.link(tardis);
    }

    public static ConsoleControlEntity create(World world, Tardis tardis) {
        return new ConsoleControlEntity(world, tardis);
    }

    @Override
    public void remove(RemovalReason reason) {
        AITMod.LOGGER.debug("Control entity discarded as {}", reason);
        super.remove(reason);
    }

    @Override
    public void onRemoved() {
        if (this.consoleBlockPos == null) {
            super.onRemoved();
            return;
        }

        if (this.getWorld().getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console)
            console.markNeedsControl();
    }

    @Override
    public void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(IDENTITY, "");
        this.dataTracker.startTracking(WIDTH, 0.125f);
        this.dataTracker.startTracking(HEIGHT, 0.125f);
        this.dataTracker.startTracking(OFFSET, new Vector3f(0));
        this.dataTracker.startTracking(PART_OF_SEQUENCE, false);
        this.dataTracker.startTracking(SEQUENCE_COLOR, 0);
        this.dataTracker.startTracking(WAS_SEQUENCED, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        if (consoleBlockPos != null)
            nbt.put("console", NbtHelper.fromBlockPos(this.consoleBlockPos));

        nbt.putString("identity", this.getIdentity());
        nbt.putFloat("width", this.getControlWidth());
        nbt.putFloat("height", this.getControlHeight());
        nbt.putFloat("offsetX", this.getOffset().x());
        nbt.putFloat("offsetY", this.getOffset().y());
        nbt.putFloat("offsetZ", this.getOffset().z());
        nbt.putBoolean("partOfSequence", this.isPartOfSequence());
        nbt.putInt("sequenceColor", this.getSequenceColor());
        nbt.putBoolean("wasSequenced", this.wasSequenced());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        NbtCompound console = nbt.getCompound("console");

        if (console != null)
            this.consoleBlockPos = NbtHelper.toBlockPos(console);

        if (nbt.contains("identity"))
            this.setIdentity(nbt.getString("identity"));

        if (nbt.contains("width") && nbt.contains("height")) {
            this.setControlWidth(nbt.getFloat("width"));
            this.setControlWidth(nbt.getFloat("height"));
            this.calculateDimensions();
        }

        if (nbt.contains("offsetX") && nbt.contains("offsetY") && nbt.contains("offsetZ"))
            this.setOffset(new Vector3f(nbt.getFloat("offsetX"), nbt.getFloat("offsetY"), nbt.getFloat("offsetZ")));

        if (nbt.contains("partOfSequence"))
            this.setPartOfSequence(nbt.getBoolean("partOfSequence"));

        if (nbt.contains("sequenceColor"))
            this.setSequenceColor(nbt.getInt("sequenceColor"));

        if (nbt.contains("wasSequenced"))
            this.setWasSequenced(nbt.getBoolean("wasSequenced"));
    }

    @Override
    public void onDataTrackerUpdate(List<DataTracker.SerializedEntry<?>> dataEntries) {
        this.setScaleAndCalculate(this.getDataTracker().get(WIDTH), this.getDataTracker().get(HEIGHT));
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);

        if (!this.getWorld().isClient())
            return;

        if (PART_OF_SEQUENCE.equals(data)) {
            this.setPartOfSequence(this.getDataTracker().get(PART_OF_SEQUENCE));
        } else if (SEQUENCE_COLOR.equals(data)) {
            this.setSequenceColor(this.getDataTracker().get(SEQUENCE_COLOR));
        } else if (WAS_SEQUENCED.equals(data)) {
            this.setWasSequenced(this.getDataTracker().get(WAS_SEQUENCED));
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getStackInHand(hand);

        if (player.getOffHandStack().getItem() == Items.COMMAND_BLOCK) {
            controlEditorHandler(player);
            return ActionResult.SUCCESS;
        }

        handStack.useOnEntity(player, this, hand);

        if (handStack.getItem() instanceof ControlBlockItem)
            return ActionResult.FAIL;

        if (hand == Hand.MAIN_HAND)
            this.run(player, player.getWorld(), false);

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof PlayerEntity player) {
            if (player.getOffHandStack().getItem() == Items.COMMAND_BLOCK) {
                controlEditorHandler(player);
            } else
                this.run((PlayerEntity) source.getAttacker(), source.getAttacker().getWorld(), true);
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public Text getName() {
        if (this.control != null)
            return Text.translatable(this.control.getId());
        else
            return super.getName();
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        if (this.getDataTracker().containsKey(WIDTH) && this.getDataTracker().containsKey(HEIGHT))
            return EntityDimensions.changing(this.getControlWidth(), this.getControlHeight());

        return super.getDimensions(pose);
    }

    @Nullable @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.INTENTIONALLY_EMPTY;
    }

    @Override
    public void tick() {
        if (this.getWorld().isClient())
            return;

        if (this.control == null && this.consoleBlockPos != null)
            this.discard();
    }

    @Override
    public boolean shouldRenderName() {
        return true;
    }

    public String getIdentity() {
        return this.dataTracker.get(IDENTITY);
    }

    public void setIdentity(String string) {
        this.dataTracker.set(IDENTITY, string);
    }

    public float getControlWidth() {
        return this.dataTracker.get(WIDTH);
    }

    public float getControlHeight() {
        return this.dataTracker.get(HEIGHT);
    }

    public void setControlWidth(float width) {
        this.dataTracker.set(WIDTH, width);
    }

    public void setControlHeight(float height) {
        this.dataTracker.set(HEIGHT, height);
    }

    public Control getControl() {
        return control;
    }

    public Vector3f getOffset() {
        return this.dataTracker.get(OFFSET);
    }

    public void setOffset(Vector3f offset) {
        this.dataTracker.set(OFFSET, offset);
    }

    public int getSequenceColor() {
        return this.dataTracker.get(SEQUENCE_COLOR);
    }

    public void setSequenceColor(int color) {
        this.dataTracker.set(SEQUENCE_COLOR, color);
    }

    public boolean wasSequenced() {
        return this.dataTracker.get(WAS_SEQUENCED);
    }

    public void setWasSequenced(boolean sequenced) {
        this.dataTracker.set(WAS_SEQUENCED, sequenced);
    }

    public void setPartOfSequence(boolean partOfSequence) {
        this.dataTracker.set(PART_OF_SEQUENCE, partOfSequence);
    }

    public boolean isPartOfSequence() {
        return this.dataTracker.get(PART_OF_SEQUENCE);
    }

    public void createDelay(long millis) {
        Control.createDelay(this.getControl(), this.tardis().get(), millis);
    }

    public boolean isOnDelay() {
        return Control.isOnDelay(this.getControl(), this.tardis().get());
    }

    public boolean run(PlayerEntity player, World world, boolean leftClick) {
        if (world.getRandom().nextBetween(1, 10_000) == 72)
            this.getWorld().playSound(null, this.getBlockPos(), AITSounds.EVEN_MORE_SECRET_MUSIC, SoundCategory.MASTER,
                    1F, 1F);

        if (world.isClient())
            return false;

        if (player.getMainHandStack().getItem() == AITItems.TARDIS_ITEM)
            this.remove(RemovalReason.DISCARDED);

        Tardis tardis = this.tardis().get();

        if (tardis == null) {
            AITMod.LOGGER.warn("Discarding invalid control entity at {}; console pos: {}", this.getPos(),
                    this.consoleBlockPos);

            this.discard();
            return false;
        }

        control.runAnimation(tardis, (ServerPlayerEntity) player, (ServerWorld) world);

        if (!this.control.canRun(tardis, (ServerPlayerEntity) player))
            return false;

        if (this.control.shouldHaveDelay(tardis) && !this.isOnDelay()) {
            this.createDelay(this.control.getDelayLength());
        }

        if (this.consoleBlockPos != null)
            this.getWorld().playSound(null, this.getBlockPos(), this.control.getSound(), SoundCategory.BLOCKS, 0.7f,
                    1f);

        return this.control.runServer(tardis, (ServerPlayerEntity) player, (ServerWorld) world, this.consoleBlockPos,
                leftClick); // i dont gotta check these cus i know its server
    }

    public void setScaleAndCalculate(float width, float height) {
        this.setControlWidth(width);
        this.setControlHeight(height);
        this.calculateDimensions();
    }

    public void setControlData(ConsoleTypeSchema consoleType, ControlTypes type, BlockPos consoleBlockPosition) {
        this.consoleBlockPos = consoleBlockPosition;
        this.control = type.getControl();

        if (consoleType != null) {
            this.setIdentity(this.control.getClass().getSimpleName());
            this.setControlWidth(type.getScale().width);
            this.setControlHeight(type.getScale().height);
            this.setCustomName(Text.translatable(type.getControl().id));
        }
    }

    public void controlEditorHandler(PlayerEntity player) {
        float increment = 0.0125f;
        if (player.getMainHandStack().getItem() == Items.EMERALD_BLOCK)
            this.setPosition(this.getPos().add(player.isSneaking() ? -increment : increment, 0, 0));

        if (player.getMainHandStack().getItem() == Items.DIAMOND_BLOCK)
            this.setPosition(this.getPos().add(0, player.isSneaking() ? -increment : increment, 0));

        if (player.getMainHandStack().getItem() == Items.REDSTONE_BLOCK)
            this.setPosition(this.getPos().add(0, 0, player.isSneaking() ? -increment : increment));

        if (player.getMainHandStack().getItem() == Items.COD)
            this.setScaleAndCalculate(player.isSneaking()
                    ? this.getDataTracker().get(WIDTH) - increment
                    : this.getDataTracker().get(WIDTH) + increment, this.getDataTracker().get(HEIGHT));

        if (player.getMainHandStack().getItem() == Items.COOKED_COD)
            this.setScaleAndCalculate(this.getDataTracker().get(WIDTH),
                    player.isSneaking()
                            ? this.getDataTracker().get(HEIGHT) - increment
                            : this.getDataTracker().get(HEIGHT) + increment);

        if (this.consoleBlockPos != null) {
            Vec3d centered = this.getPos().subtract(this.consoleBlockPos.toCenterPos());
            if (this.control != null)
                player.sendMessage(Text.literal("EntityDimensions.changing(" + this.getControlWidth() + ", "
                        + this.getControlHeight() + "), new Vector3f(" + centered.getX() + "f, " + centered.getY()
                        + "f, " + centered.getZ() + "f)),"));
        }
    }
}
