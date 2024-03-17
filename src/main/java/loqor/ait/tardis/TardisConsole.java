package loqor.ait.tardis;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.tardis.data.TardisLink;
import loqor.ait.tardis.util.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

// TODO - move variant and type over to here
public class TardisConsole extends TardisLink {
	private static final int VALIDATE_TICK = 30 * 20;

	private final UUID uuid;
	private final AbsoluteBlockPos position;

	@Exclude
	private int ticks = 0;

	protected TardisConsole(Tardis tardis, AbsoluteBlockPos pos, UUID uuid) {
		super(tardis, "console");
		this.position = pos;
		this.uuid = uuid;
	}

	public TardisConsole(Tardis tardis, AbsoluteBlockPos pos) {
		this(tardis, pos, UUID.randomUUID());
	}

	public boolean inUse() {
		if (this.findEntity().isEmpty()) return false;

		ConsoleBlockEntity entity = this.findEntity().get();

		if (!entity.hasWorld()) return false;

		assert entity.getWorld() != null;

		int radius = 3;

		return !entity.getWorld().getEntitiesByClass(
				ServerPlayerEntity.class,
				new Box(
						entity.getPos().up(radius).north(radius).west(radius),
						entity.getPos().down(radius).south(radius).east(radius)
				),
				player -> !player.isSpectator()
		).isEmpty();
	}

	public AbsoluteBlockPos position() {
		return this.position;
	}

	public UUID uuid() {
		return this.uuid;
	}

	public Optional<ConsoleBlockEntity> findEntity() {
		BlockEntity found = TardisUtil.getTardisDimension().getBlockEntity(this.position);

		return (found instanceof ConsoleBlockEntity) ? Optional.of((ConsoleBlockEntity) found) : Optional.empty();
	}

	@Override
	public Optional<Tardis> findTardis() {
		if (this.tardisId == null) {
			if (!this.validate()) return Optional.empty();

			Tardis found = TardisUtil.findTardisByInterior(this.position(), !this.findEntity().get().getWorld().isClient());
			if (found != null)
				this.setTardis(found);
		}
		return super.findTardis();
	}

	public boolean validate() {
		if (this.shouldRemove()) {
			AITMod.LOGGER.warn("Removing " + this.uuid() + " from desktop as it was invalid");
			this.remove();
			return false;
		}

		return true;
	}

	private boolean shouldRemove() {
		return this.findEntity().isEmpty();
	}

	private void remove() {
		if (this.findTardis().isEmpty()) return;

		Tardis tardis = this.findTardis().get();
		tardis.getDesktop().removeConsole(this);
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);
	}

	/**
	 * Tick from the {@link ConsoleBlockEntity#tick(World, BlockPos, BlockState, ConsoleBlockEntity)}
	 * Ensures this console is loaded in the world to avoid performance issues
	 */
	public void tickConsole(ConsoleBlockEntity block) {
		ticks++;

		// todo this is bad for performance (or so ive heard)
		if (ticks >= VALIDATE_TICK) {
			ticks = 0;
			this.validate();
		}
	}
}
