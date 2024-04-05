package loqor.ait.tardis.link;


import blue.endless.jankson.annotation.Nullable;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class LinkableBlockEntity extends BlockEntity implements Linkable {

	protected UUID tardisId;

	public LinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean isLinked() {
		return this.tardisId != null;
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);

		if (this.tardisId != null) {
			nbt.put("tardis", NbtHelper.fromUuid(this.tardisId));
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		if (nbt.contains("tardis")) {
			boolean isServer = true;
			if (nbt.contains("client")) {
				isServer = !nbt.getBoolean("client");
			}
			if (nbt.get("tardis") == null) return;
			if (Objects.requireNonNull(nbt.get("tardis")).getType() != NbtElement.INT_ARRAY_TYPE) {
				AITMod.LOGGER.warn("Received wrong type in datapack");
				return;
			}
			this.tardisId = NbtHelper.toUuid(nbt.get("tardis"));
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound compound = this.createNbt();
		compound.putBoolean("client", true);

		return compound;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void sync() {
		if (this.world == null)
			return;

		this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
	}

	// lord save us.

	/**
	 * Attempts to find the TARDIS related to this block entity
	 * It looks for the block entities tardis id stored in the lookup
	 * Some overrides also search for the tardis based off the exterior pos / interior pos
	 * This is resource intensive and the result of this should be stored in a variable (you should be doing this anyway for things you are repeatedly calling)
	 * @return the found TARDIS, or empty.
	 */
	@Override
	public Optional<Tardis> findTardis(boolean isClient) {
		if (isClient) { // todo replace deprecated check
			if (!ClientTardisManager.getInstance().hasTardis(this.tardisId)) {
				if (this.tardisId != null)
					ClientTardisManager.getInstance().loadTardis(this.tardisId, tardis -> {
					});
				return Optional.empty();
			}
			return Optional.ofNullable(ClientTardisManager.getInstance().getTardis(this.tardisId));
		}
		return Optional.ofNullable(ServerTardisManager.getInstance().getTardis(this.tardisId));
	}

	public Optional<Tardis> findTardis() {
		return this.findTardis(this.getWorld().isClient());
	}

	@Override
	public void setTardis(Tardis tardis) {
		this.setTardis(tardis.getUuid());
	}

	public void setTardis(UUID uuid) {
		this.tardisId = uuid;
		markDirty();
	}
}