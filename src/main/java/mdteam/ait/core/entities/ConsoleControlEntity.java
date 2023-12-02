package mdteam.ait.core.entities;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.entities.control.ControlTypes;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.SerialDimension;
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
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisTravel;

import java.util.List;
import java.util.Objects;

import static the.mdteam.ait.TardisTravel.State.LANDED;

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
        if(hand == Hand.MAIN_HAND) {
            if(getWorld() instanceof ServerWorld server) {
                this.interactionOrHurt(player, hand, server, true);
                return ActionResult.SUCCESS;
            }
        }
        this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_CANDLE_STEP, SoundCategory.BLOCKS, 0.1f, 1f);
        return ActionResult.FAIL;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(source.getAttacker() instanceof PlayerEntity player) {
            if (this.getWorld() instanceof ServerWorld server) {
                this.interactionOrHurt(player, player.getActiveHand(), server, false);
                return true;
            }
        }
        return super.damage(source, amount);
    }

    public void setScaleAndCalculate(float scale) {
        this.setScale(scale);
        this.calculateDimensions();
    }

    @Override
    public Text getName() {
        if(this.controlTypes != null)
            return Text.translatable(this.controlTypes.getControlName());
        else
            return super.getName();
    }

    public void setControlData(ConsoleEnum consoleType, ControlTypes type, BlockPos consoleBlockPosition) {
        this.consoleBlockPos = consoleBlockPosition;
        this.controlTypes = type;
        if(consoleType != null) {
            this.setScale(type.getScale().width);
            this.setCustomName(Text.translatable(type.getControlName()).fillStyle(Style.EMPTY.withColor(Formatting.BLUE).withBold(true)));
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
        if(getWorld() instanceof ServerWorld server) {
            if (this.controlTypes == null) {
                if (this.consoleBlockPos != null) {
                    if (server.getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console) {
                        console.markDirty();
                    }
                    discard();
                }
            }
        }
    }

    // fixme this is dog water, possibly into seperate entity files ;)
    public ActionResult interactionOrHurt(PlayerEntity player, Hand hand, ServerWorld serverWorld, boolean IorH) {
        if(player.getMainHandStack().getItem() == AITItems.TOYOTA_ITEM) {
            this.remove(RemovalReason.DISCARDED);
        } else if (player.getMainHandStack().getItem() == Items.COMMAND_BLOCK) {
            controlEditorHandler(player);
        }
        if(this.consoleBlockPos != null)
            if(this.controlTypes != null)
                if(serverWorld.getBlockEntity(this.consoleBlockPos) instanceof ConsoleBlockEntity console) {
                    Tardis tardis = console.getTardis();
                    TardisTravel travel= tardis.getTravel();
                    BlockPos position = travel.getPosition();
                    World dimension = travel.getPosition().getDimension().get();
                    Direction direction = travel.getPosition().getDirection();
                    if(this.controlTypes.getControlName().matches("Throttle")) {
                        if(travel.getState() == LANDED) {
                            travel.dematerialise(true);
                            getWorld().playSound(null, this.consoleBlockPos, AITSounds.DEMAT, SoundCategory.BLOCKS, 1f, 1f);
                        } else if(travel.getState() == TardisTravel.State.FLIGHT) {
                            travel.checkShouldRemat();
                        }
                    }
                    if(this.controlTypes.getControlName().matches("Dimension")) {
                        RegistryKey<World> registryKey;
                        player.sendMessage(Text.literal("Dimension: " + serverWorld.getServer().getWorld(this.getWorld().getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER).getDimension().toString()), true);
                        dimension = serverWorld.getServer().getWorld(World.NETHER);
                    }
                    if(this.controlTypes.getControlName().matches("Door Control")) {
                        if(player.isSneaking()) {
                            tardis.setLockedTardis(!tardis.getLockedTardis());
                            String lockedState = tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";
                            player.sendMessage(Text.literal(lockedState), true);
                            getWorld().playSound(null, this.consoleBlockPos, SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);
                        } else if(tardis.getTravel().getState() == LANDED) {
                            if (!tardis.getLockedTardis()) {
                                DoorBlockEntity door = TardisUtil.getDoor(tardis);
                                if(tardis.getTravel().getState() == LANDED)
                                    if (door != null) {
                                        //TardisUtil.getTardisDimension().getChunk(door.getPos()); // force load the chunk

                                        door.setLeftDoorRot(door.getLeftDoorRotation() == 0 ? 1.2f : 0f);
                                        //door.setRightDoorRot(0);
                                    }
                                getWorld().playSound(null, this.consoleBlockPos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6f, 1f);
                            } else {
                                getWorld().playSound(null, this.consoleBlockPos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.6F, 1F);
                                player.sendMessage(Text.literal("\uD83D\uDD12"), true);
                            }
                        }
                    }
                    travel.setDestination(new AbsoluteBlockPos.Directed(position,  serverWorld.getServer().getWorld(World.NETHER), direction), true);
                }
        return ActionResult.SUCCESS;
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
            if(this.controlTypes != null) player.sendMessage(Text.literal(this.controlTypes.getControlName().toUpperCase() + ": " + "Position: " + centered + " & Scale: " + this.getScale()));
        }
    }
}
