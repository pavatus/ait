package mdteam.ait.core.entities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.tardis.console.ConsoleSchema;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.control.ControlTypes;
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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import mdteam.ait.tardis.Tardis;

import java.util.List;
import java.util.Random;

public class ConsoleControlEntity extends BaseControlEntity {

    private BlockPos consoleBlockPos;
    private Control control;
    private static final TrackedData<String> IDENTITY = DataTracker.registerData(ConsoleControlEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(ConsoleControlEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(ConsoleControlEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Vector3f> OFFSET = DataTracker.registerData(ConsoleControlEntity.class, TrackedDataHandlerRegistry.VECTOR3F);

    public ConsoleControlEntity(EntityType<? extends BaseControlEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
    }

    @Override
    public void onRemoved() {
        if(this.consoleBlockPos == null) {
            super.onRemoved();
            return;
        }
        if(this.getWorld().getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console) {
            console.markNeedsControl();
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IDENTITY, "");
        this.dataTracker.startTracking(WIDTH, 0.125f);
        this.dataTracker.startTracking(HEIGHT, 0.125f);
        this.dataTracker.startTracking(OFFSET, new Vector3f(0));
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


    // fixme idk i added it i dunno if it'll work it didnt crash when i tried it so
    public Control getControl() {
        if(control == null) return null;
        return control;
    }

    public Vector3f getOffset() {
        return this.dataTracker.get(OFFSET);
    }

    public void setOffset(Vector3f offset) {
        this.dataTracker.set(OFFSET, offset);
    }

    public String createDelayId() {
        return "delay-" + this.getControl().id + "-" + this.getTardis().getUuid();
    }
    public void createDelay(long millis) {
        DeltaTimeManager.createDelay(createDelayId(), millis);
    }
    public boolean isOnDelay() {
        return DeltaTimeManager.isStillWaitingOnDelay(createDelayId());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        //if(this.controlTypes != null)
        //    nbt.put("controlTypes", this.controlTypes.serializeTypes(nbt));
        if (consoleBlockPos != null)
            nbt.put("console", NbtHelper.fromBlockPos(this.consoleBlockPos));

        nbt.putString("identity", this.getIdentity());
        nbt.putFloat("width", this.getControlWidth());
        nbt.putFloat("height", this.getControlHeight());
        nbt.putFloat("offsetX", this.getOffset().x());
        nbt.putFloat("offsetY", this.getOffset().y());
        nbt.putFloat("offsetZ", this.getOffset().z());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        //if(this.controlTypes != null)
        //    this.controlTypes = this.controlTypes.deserializeTypes(nbt);
        var console = (NbtCompound) nbt.get("console");
        if (console != null)
            this.consoleBlockPos = NbtHelper.toBlockPos(console);
        if (nbt.contains("identity")) {
            this.setIdentity(nbt.getString("identity"));
        }
        if (nbt.contains("width") && nbt.contains("height")) {
            this.setControlWidth(nbt.getFloat("width"));
            this.setControlWidth(nbt.getFloat("height"));
            this.calculateDimensions();
        }
        if (nbt.contains("offsetX") && nbt.contains("offsetY") && nbt.contains("offsetZ")) {
            this.setOffset(new Vector3f(nbt.getFloat("offsetX"), nbt.getFloat("offsetY"), nbt.getFloat("offsetZ")));
        }
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (player.getOffHandStack().getItem() == Items.COMMAND_BLOCK) {
            controlEditorHandler(player);
            return ActionResult.SUCCESS;
        }

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

        return super.damage(source, source.getAttacker() instanceof PlayerEntity ? 0 : amount);
    }

    public boolean run(PlayerEntity player, World world, boolean leftClick) {
        Random random = new Random();
        int chance_int = random.nextInt(1, 10_000);
        if (chance_int == 72) {
            // play sound
            this.getWorld().playSound(null, this.getBlockPos(), AITSounds.EVEN_MORE_SECRET_MUSIC, SoundCategory.MASTER, 1F, 1F);
        }
        if (this.consoleBlockPos != null)
            this.getWorld().playSound(null, this.getBlockPos(), this.control.getSound(), SoundCategory.BLOCKS, 0.7f, 1f);

        if (!world.isClient()) {
            if (player.getMainHandStack().getItem() == AITItems.TARDIS_ITEM) {
                this.remove(RemovalReason.DISCARDED);
            }/* else if (player.getMainHandStack().getItem() == Items.COMMAND_BLOCK) {
                controlEditorHandler(player);
            }*/

            if (this.getTardis() == null) return false; // AAAAAAAAAAA

            //blahblah
            //if (DeltaTimeManager.isStillWaitingOnDelay(getDelayId(this.getTardis()))) return false;

            //DeltaTimeManager.createDelay(getDelayId(this.getTardis()), 500L);

            control.runAnimation(getTardis(world), (ServerPlayerEntity) player, (ServerWorld) world);

            if (this.getTardis(world) == null) {
                this.discard();
                AITMod.LOGGER.warn("Discarding invalid control entity at " + this.getPos());
                return false;
            }

            if (control.shouldFailOnNoPower() && !this.getTardis(world).hasPower()) {
                return false;
            }
            if (this.isOnDelay()) return false;
            if (this.control.shouldHaveDelay() && !this.isOnDelay()) {
                this.createDelay(this.control.getDelayLength());
            }

            // this.getTardis(world).getHandlers().getSequencing().add(this.control);

            return this.control.runServer(this.getTardis(world), (ServerPlayerEntity) player, (ServerWorld) world, leftClick); // i dont gotta check these cus i know its server
        }
        return false;
    }

    private static String getDelayId(Tardis tardis) {
        return "tardis-" + tardis.getUuid() + "-control";
    }

    // clearly loqor has trust issues with running this so i do too so im overwriting it to do what he did fixme pls
    public Tardis getTardis(World world) {
        if (!(this.consoleBlockPos != null && this.control != null && world.getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console))
            return null;

        return console.getTardis();
    }

    public void setScaleAndCalculate(float width, float height) {
        this.setControlWidth(width);
        this.setControlHeight(height);
        this.calculateDimensions();
    }

    @Override
    public Text getName() {
        if (this.control != null)
            return Text.translatable(this.control.getId());
        else
            return super.getName();
    }

    public void setControlData(ConsoleSchema consoleType, ControlTypes type, BlockPos consoleBlockPosition) {
        this.consoleBlockPos = consoleBlockPosition;
        this.control = type.getControl();
        // System.out.println(type);
        if (consoleType != null) {
            this.setControlWidth(type.getScale().width);
            this.setControlHeight(type.getScale().height);
            this.setCustomName(Text.translatable(type.getControl().id)/*.fillStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true))*/);
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
        this.setScaleAndCalculate(this.getDataTracker().get(WIDTH), this.getDataTracker().get(HEIGHT));
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        if (this.getDataTracker().containsKey(WIDTH) && this.getDataTracker().containsKey(HEIGHT))
            return EntityDimensions.changing(this.getControlWidth(), this.getControlHeight());
        return super.getDimensions(pose);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.INTENTIONALLY_EMPTY;
    }

    @Override
    public void tick() {
        if (getWorld() instanceof ServerWorld server) {
            if (this.control == null) {
                if (this.consoleBlockPos != null) {
                    if (server.getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console) {
                        // todo this wont be good for server performance..
                        console.markDirty();
                    }
                    discard();
                }
            }
        }
        /*PlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null)
            this.setCustomNameVisible(isPlayerLookingAtControl(player, this)); System.out.println("im being looked at ;) : " + this);*/
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
        float increment = 0.0125f;
        if (player.getMainHandStack().getItem() == Items.EMERALD_BLOCK) {
            this.setPosition(this.getPos().add(player.isSneaking() ? -increment : increment, 0, 0));
        }
        if (player.getMainHandStack().getItem() == Items.DIAMOND_BLOCK) {
            this.setPosition(this.getPos().add(0, player.isSneaking() ? -increment : increment, 0));
        }
        if (player.getMainHandStack().getItem() == Items.REDSTONE_BLOCK) {
            this.setPosition(this.getPos().add(0, 0, player.isSneaking() ? -increment : increment));
        }
        if (player.getMainHandStack().getItem() == Items.COD) {
            this.setScaleAndCalculate(player.isSneaking() ? this.getDataTracker().get(WIDTH) - increment : this.getDataTracker().get(WIDTH) + increment,
                    this.getDataTracker().get(HEIGHT));
        }
        if (player.getMainHandStack().getItem() == Items.COOKED_COD) {
            this.setScaleAndCalculate(this.getDataTracker().get(WIDTH),
                    player.isSneaking() ? this.getDataTracker().get(HEIGHT) - increment : this.getDataTracker().get(HEIGHT) + increment);
        }
        if (this.consoleBlockPos != null) {
            Vec3d centered = this.getPos().subtract(this.consoleBlockPos.toCenterPos());
            if (this.control != null)
                player.sendMessage(Text.literal("EntityDimensions.changing(" + this.getControlWidth() + ", " + this.getControlHeight() + "), new Vector3f(" + centered.getX() + "f, " + centered.getY() + "f, " + centered.getZ() + "f)),"));
        }
    }
}
