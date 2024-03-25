package mdteam.ait.core.blockentities.control;

import mdteam.ait.core.item.control.ControlBlockItem;
import mdteam.ait.core.util.DeltaTimeManager;
import mdteam.ait.registry.ControlRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.control.impl.SecurityControl;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.link.LinkableBlockEntity;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

import static mdteam.ait.tardis.util.TardisUtil.findTardisByInterior;

public abstract class ControlBlockEntity extends LinkableBlockEntity {
	private Control control;

	protected ControlBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		if (this.getControl() != null) {
			nbt.putString(ControlBlockItem.CONTROL_ID_KEY, this.getControl().getId());
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		if (nbt.contains(ControlBlockItem.CONTROL_ID_KEY)) {
			this.setControl(nbt.getString(ControlBlockItem.CONTROL_ID_KEY));
		}
	}

	/**
	 * Gets the control
	 * Can be null if this hasnt been linked
	 * @return control
	 */
	public Control getControl() {
		return this.control;
	}
	public void setControl(String id) {
		Optional<Control> found = ControlRegistry.fromId(id);
		if (found.isEmpty()) return;

		this.control = found.get();
	}

	public boolean run(ServerPlayerEntity user, boolean isMine) {
		if (this.getControl() != null) {
			Optional<Tardis> found = this.findTardis();

			if (found.isEmpty()) return false;

			ServerTardis tardis = (ServerTardis) found.get();

			if (this.control.canRun(tardis, user)) return false;

			if (this.control.shouldHaveDelay(tardis) && !this.isOnDelay()) {
				this.createDelay(this.control.getDelayLength());
			}

			this.getWorld().playSound(null, pos, this.control.getSound(), SoundCategory.BLOCKS, 0.7f, 1f);


			return this.control.runServer(tardis, user, user.getServerWorld(), isMine);
		}

		return false;
	}

	public void createDelay(long millis) {
		Control.createDelay(this.getControl(), this.findTardis().get(), millis);
	}

	public boolean isOnDelay() {
		return Control.isOnDelay(this.getControl(), this.findTardis().get());
	}

	@Override
	public Optional<Tardis> findTardis() {
		if (this.tardisId == null && this.hasWorld()) {
			assert this.getWorld() != null;
			Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
			if (found != null)
				this.setTardis(found);
		}
		return super.findTardis();
	}
}
