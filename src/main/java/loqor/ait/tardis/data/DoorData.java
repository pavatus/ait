package loqor.ait.tardis.data;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.AITSounds;
import loqor.ait.core.advancement.TardisCriterions;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.entities.ConsoleControlEntity;
import loqor.ait.core.item.KeyItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
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

public class DoorData extends TardisComponent implements TardisTickable {
	private boolean locked, left, right;
	private DoorStateEnum doorState;
	public DoorStateEnum tempExteriorState; // this is the previous state before it was changed, used for checking when the door has been changed so the animation can start. Set on server, used on client
	public DoorStateEnum tempInteriorState;

	public DoorData() {
		super(Id.DOOR);

		this.doorState = DoorStateEnum.CLOSED;
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
				.filter(entity -> !(entity instanceof ConsoleControlEntity)) // Exclude control entities
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

		return (tardis.travel().getState() != TravelHandlerBase.State.LANDED && tardis().travel().getState() != TravelHandlerBase.State.MAT)
				&& !tardis.areShieldsActive() && this.isOpen() && TardisUtil.getTardisDimension().getBlockEntity(
				directed.getPos()
		) instanceof DoorBlockEntity;
	}

	public void setLeftRot(boolean var) {
		this.left = var;

		if (this.left)
			this.setDoorState(DoorStateEnum.FIRST);

		this.sync();
	}

	public void setRightRot(boolean var) {
		this.right = var;
		if (this.right) this.setDoorState(DoorStateEnum.SECOND);
		this.sync();
	}

	public boolean isRightOpen() {
		return this.doorState == DoorStateEnum.SECOND || this.right;
	}

	public boolean isLeftOpen() {
		return this.doorState == DoorStateEnum.FIRST || this.left;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;

		if (locked)
			setDoorState(DoorStateEnum.CLOSED);

		this.sync();
	}

	public boolean locked() {
		return this.locked;
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
		if (var != doorState) {
			tempExteriorState = this.doorState;
			tempInteriorState = this.doorState;

			// if the last state ( doorState ) was closed and the new state ( var ) is open, fire the event
			if (doorState == DoorStateEnum.CLOSED) {
				TardisEvents.DOOR_OPEN.invoker().onOpen(tardis());
			}
			// if the last state was open and the new state is closed, fire the event
			if (doorState != DoorStateEnum.CLOSED && var == DoorStateEnum.CLOSED) {
				TardisEvents.DOOR_CLOSE.invoker().onClose(tardis());
			}
		}

		this.doorState = var;
		this.sync();
	}

	public DoorStateEnum getDoorState() {
		return doorState;
	}

	public DoorStateEnum getAnimationExteriorState() {
		return tempExteriorState;
	}

	// TODO predict the result of this on common side so the client doesnt have to wait for the server to answer
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
				world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);

			TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
					AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);

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
				tardis.door().openDoors();

				return true;
			}

			if (pos != null) // fixme will play sound twice on interior door
				world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);

			TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
					AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);

			return false;
		}

		if (tardis.getLockedTardis() || tardis.sonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC)) {
			if (player != null && pos != null) {
				player.sendMessage(Text.literal("\uD83D\uDD12"), true);
				world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);
				TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(), AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, world.getRandom().nextBoolean() ? 0.5f : 0.3f);
			}

			return false;
		}

		DoorData door = tardis.door();

		DoorSchema doorSchema = tardis.getExterior().getVariant().door();
		SoundEvent sound = doorSchema.isDouble() && door.isBothOpen() ? doorSchema.closeSound() : doorSchema.openSound();

		tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(), sound, SoundCategory.BLOCKS, 0.6F, world.getRandom().nextBoolean() ? 1f : 0.8f);

		TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
				sound, SoundCategory.BLOCKS, 0.6F, world.getRandom().nextBoolean() ? 1f : 0.8f);

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

		if (!forced && (tardis.travel().getState() == TravelHandlerBase.State.DEMAT || tardis.travel().getState() == TravelHandlerBase.State.MAT))
			return false;

		tardis.door().setLocked(locked);
		DoorData door = tardis.door();

		if (door == null)
			return false; // could have a case where the door is null but the thing above works fine meaning this false is wrong fixme

		door.setDoorState(DoorStateEnum.CLOSED);

		if (!forced)
			PropertiesHandler.set(tardis, PropertiesHandler.PREVIOUSLY_LOCKED, locked);

		if (tardis.siege().isActive())
			return true;

		String lockedState = tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";

		if (player != null)
			player.sendMessage(Text.literal(lockedState), true);

		tardis.travel().position().getWorld().playSound(null, tardis.travel().position().getPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);

		TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().doorPos().getPos(),
				SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F
		);

		return true;
	}

	public boolean previouslyLocked(){
		return PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.PREVIOUSLY_LOCKED);
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