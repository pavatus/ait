package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITSounds;
import loqor.ait.core.advancement.TardisCriterions;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.entities.BaseControlEntity;
import loqor.ait.core.item.KeyItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import static loqor.ait.tardis.TardisTravel.State.*;

public class DoorData extends KeyedTardisComponent implements TardisTickable {

	private static final BoolProperty LOCKED = new BoolProperty("locked", false);
	private static final BoolProperty LEFT = new BoolProperty("left", false);
	private static final BoolProperty RIGHT = new BoolProperty("right", false);
	private static final BoolProperty LAST_LOCKED = new BoolProperty("last_locked", t -> Property.warnCompat("last_locked", false));

	private static final Property<DoorStateEnum> STATE = Property.forEnum("doorState", DoorStateEnum.class, DoorStateEnum.CLOSED);
	private static final Property<DoorStateEnum> TEMP_EXTERIOR_STATE = STATE.copy("tempExteriorState");
	private static final Property<DoorStateEnum> TEMP_INTERIOR_STATE = STATE.copy("tempInteriorState");

	private final BoolValue locked = LOCKED.create(this);
	private final BoolValue left = LEFT.create(this);
	private final BoolValue right = RIGHT.create(this);
	private final BoolValue lastLocked = LAST_LOCKED.create(this);

	private final Value<DoorStateEnum> doorState = STATE.create(this);
	private final Value<DoorStateEnum> tempExteriorState = TEMP_EXTERIOR_STATE.create(this);
	private final Value<DoorStateEnum> tempInteriorState = TEMP_INTERIOR_STATE.create(this);

	public DoorData() {
		super(Id.DOOR);
	}

	@Override
	public void onLoaded() {
		locked.of(this, LOCKED);
		left.of(this, LEFT);
		right.of(this, RIGHT);
		lastLocked.of(this, LAST_LOCKED);

		doorState.of(this, STATE);
		tempExteriorState.of(this, TEMP_EXTERIOR_STATE);
		tempInteriorState.of(this, TEMP_INTERIOR_STATE);
	}

	@Override
	public void tick(MinecraftServer server) {
		if (shouldSucc())
			this.succ();

		if (locked() && isOpen())
			closeDoors();
	}

	/**
	 * Moves entities in the Tardis interior towards the door.
	 */
	private void succ() {
		// Get all entities in the Tardis interior
		TardisUtil.getLivingEntitiesInInterior(tardis())
				.stream()
				.filter(entity -> !(entity instanceof BaseControlEntity)) // Exclude control entities
				.filter(entity -> !(entity instanceof ServerPlayerEntity && entity.isSpectator())) // Exclude spectators
				.forEach(entity -> {
					// Calculate the motion vector away from the door

					DirectedBlockPos directed = tardis.getDesktop().doorPos();
					BlockPos pos = directed.getPos();

					Vec3d motion = pos.offset(RotationPropertyHelper.toDirection(directed.getRotation()).get().getOpposite())
							.toCenterPos().subtract(entity.getPos()).normalize().multiply(0.05);

					// Apply the motion to the entity
					entity.setVelocity(entity.getVelocity().add(motion));
					entity.velocityDirty = true;
					entity.velocityModified = true;
				});
	}

	private boolean shouldSucc() {
		DirectedBlockPos directed = tardis.getDesktop().doorPos();

		if (directed == null)
			return false;

		return (tardis.travel().getState() != LANDED && tardis().travel().getState() != MAT)
				&& !tardis.areShieldsActive() && this.isOpen() && TardisUtil.getTardisDimension().getBlockEntity(
						directed.getPos()
		) instanceof DoorBlockEntity;
	}

	public void setLeftRot(boolean var) {
		this.left.set(var);

		if (var)
			this.doorState.set(DoorStateEnum.FIRST);
	}

	public void setRightRot(boolean var) {
		this.right.set(var);

		if (var)
			this.doorState.set(DoorStateEnum.SECOND);
	}

	public boolean isRightOpen() {
		return this.doorState.get() == DoorStateEnum.SECOND || this.right.get();
	}

	public boolean isLeftOpen() {
		return this.doorState.get() == DoorStateEnum.FIRST || this.left.get();
	}

	public void setLocked(boolean locked) {
		this.locked.set(locked);

		if (locked)
			this.doorState.set(DoorStateEnum.CLOSED);
	}

	public boolean locked() {
		return this.locked.get();
	}

	public boolean isDoubleDoor() {
		return tardis().getExterior().getVariant().door().isDouble();
	}

	public boolean isOpen() {
		return this.isLeftOpen() || this.isRightOpen();
	}

	public boolean isClosed() {
		return !isOpen();
	}

	public boolean isBothOpen() {
		return this.isRightOpen() && this.isLeftOpen();
	}

	public boolean isBothClosed() {
		return !isBothOpen();
	}

	public void openDoors() {
		setLeftRot(true);

		if (isDoubleDoor()) {
			setRightRot(true);
			this.setDoorState(DoorStateEnum.BOTH);
		}
	}

	public void closeDoors() {
		setLeftRot(false);
		setRightRot(false);
		this.setDoorState(DoorStateEnum.CLOSED);
	}

	public void setDoorState(DoorStateEnum var) {
		if (var != this.doorState.get()) {
			this.tempExteriorState.set(this.doorState);
			this.tempInteriorState.set(this.doorState);

			// if the last state ( doorState ) was closed and the new state ( var ) is open, fire the event
			if (this.doorState.get() == DoorStateEnum.CLOSED) {
				TardisEvents.DOOR_OPEN.invoker().onOpen(tardis());
			}
			// if the last state was open and the new state is closed, fire the event
			if (this.doorState.get() != DoorStateEnum.CLOSED && var == DoorStateEnum.CLOSED) {
				TardisEvents.DOOR_CLOSE.invoker().onClose(tardis());
			}
		}

		this.doorState.set(var);
	}

	public DoorStateEnum getDoorState() {
		return doorState.get();
	}

	public DoorStateEnum getAnimationExteriorState() {
		return tempExteriorState.get();
	}

	public void setTempExteriorState(DoorStateEnum value) {
		tempExteriorState.set(value);
	}

	public boolean lastLocked() {
		return this.lastLocked.get();
	}

	public static boolean useDoor(Tardis tardis, ServerWorld world, @Nullable BlockPos pos, @Nullable ServerPlayerEntity player) {
		if (tardis.getHandlers().getOvergrown().isOvergrown()) {
			// Bro cant escape
			if (player == null)
				return false;

			// if holding an axe then break off the vegetation
			ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
			if (stack.getItem() instanceof AxeItem) {
				player.swingHand(Hand.MAIN_HAND);
				tardis.getHandlers().getOvergrown().removeVegetation();
				stack.setDamage(stack.getDamage() - 1);

				if (pos != null)
					world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS, 1f, 1f);

				TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
						SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS);

				TardisCriterions.VEGETATION.trigger(player);
				return true;
			}

			if (pos != null) // fixme will play sound twice on interior door
				world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 0.5f);

			TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
					AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 0.5f);

			return false;
		}

		if (!tardis.engine().hasPower() && tardis.getLockedTardis()) {
			// Bro cant escape
			if (player == null)
				return false;

			ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

			// if holding a key and in siege mode and have an empty interior, disable siege mode !!
			if (stack.getItem() instanceof KeyItem && tardis.siege().isActive() && KeyItem.isOf(world, stack, tardis) && !TardisUtil.isInteriorNotEmpty(tardis)) {
				player.swingHand(Hand.MAIN_HAND);
				tardis.siege().setActive(false);
				lockTardis(false, tardis, player, true);
			}

			// if holding an axe then break open the door RAHHH
			if (stack.getItem() instanceof AxeItem) {
				if (tardis.siege().isActive())
					return false;

				player.swingHand(Hand.MAIN_HAND);
				stack.setDamage(stack.getDamage() - 1);

				if (pos != null)
					world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS, 1f, 1f);

				TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
						SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS);

				lockTardis(false, tardis, player, true); // forcefully unlock the tardis
				tardis.getDoor().openDoors();

				return true;
			}

			if (pos != null) // fixme will play sound twice on interior door
				world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 0.5f);

			TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
					AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 0.5f);

			return false;
		}

		if (tardis.getLockedTardis() || tardis.getHandlers().getSonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC)) {
			if (player != null && pos != null) {
				player.sendMessage(Text.literal("\uD83D\uDD12"), true);
				world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 0.5f);
				TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(), AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 0.5f);
			}

			return false;
		}

		DoorData door = tardis.getDoor();

		DoorSchema doorSchema = tardis.getExterior().getVariant().door();
		SoundEvent sound = doorSchema.isDouble() && door.isBothOpen() ? doorSchema.closeSound() : doorSchema.openSound();

		tardis.getExteriorPos().getWorld().playSound(null, tardis.getExteriorPos(), sound, SoundCategory.BLOCKS, 0.6F, 1F);

		TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
				sound, SoundCategory.BLOCKS, 0.6F, 1F);

		if (!doorSchema.isDouble()) {
			door.setDoorState(door.getDoorState() == DoorStateEnum.FIRST ? DoorStateEnum.CLOSED : DoorStateEnum.FIRST);
			return true;
		}

		if (door.isBothOpen()) {
			door.closeDoors();
		} else {
			if (door.isOpen() && player.isSneaking()) {
				door.closeDoors();
			} else if (door.isBothClosed() && player.isSneaking()) {
				door.openDoors();
			} else {
				door.setDoorState(door.getDoorState().next());
			}
		}

		return true;
	}

	public static boolean toggleLock(Tardis tardis, @Nullable ServerPlayerEntity player) {
		return lockTardis(!tardis.getLockedTardis(), tardis, player, false);
	}

	public static boolean lockTardis(boolean locked, Tardis tardis, @Nullable ServerPlayerEntity player, boolean forced) {
		if (tardis.getLockedTardis() == locked)
			return true;

		if (!forced && (tardis.travel().getState() == DEMAT || tardis.travel().getState() == MAT))
			return false;

		tardis.setLockedTardis(locked);
		DoorData door = tardis.getDoor();

		if (door == null)
			return false; // could have a case where the door is null but the thing above works fine meaning this false is wrong fixme

		door.setDoorState(DoorStateEnum.CLOSED);

		if (!forced) {
			door.lastLocked.set(locked);
		}

		if (tardis.siege().isActive()) return true;

		String lockedState = tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";
		if (player != null)
			player.sendMessage(Text.literal(lockedState), true);

		tardis.getExteriorPos().getWorld().playSound(null, tardis.getExteriorPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);

		TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
				SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F
		);

		return true;
	}

	public enum DoorStateEnum {
		CLOSED {
			@Override
			public DoorStateEnum next() {
				return FIRST;
			}

		},
		FIRST {
			@Override
			public DoorStateEnum next() {
				return SECOND;
			}

		},
		SECOND {
			@Override
			public DoorStateEnum next() {
				return CLOSED;
			}

		},
		BOTH {
			@Override
			public DoorStateEnum next() {
				return CLOSED;
			}

		};

		public abstract DoorStateEnum next();

	}
}
