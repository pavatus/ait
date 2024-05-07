package loqor.ait.tardis.control.sequences;

import loqor.ait.registry.impl.SequenceRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.TardisLink;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.UUID;

public class SequenceHandler extends TardisLink {
	@Exclude
	private RecentControls recent;
	private int ticks = 0;
	@Exclude
	private Sequence activeSequence;
	private UUID playerUUID;

	public SequenceHandler() {
		super(Id.SEQUENCE);
	}

	@Override
	public void init(Tardis tardis, boolean deserialized) {
		super.init(tardis, deserialized);

		recent = new RecentControls(tardis.getUuid());
		activeSequence = null;
	}

	public void setActivePlayer(ServerPlayerEntity player) {
		this.playerUUID = player.getUuid();
	}

	public ServerPlayerEntity getActivePlayer() {
		if(this.playerUUID == null) return null;
		return (ServerPlayerEntity) TardisUtil.getTardisDimension().getPlayerByUuid(this.playerUUID);
	}

	public void add(Control control, ServerPlayerEntity player) {
		if (this.getActiveSequence() == null || recent == null) return;
		recent.add(control);
		ticks = 0;
		this.setActivePlayer(player);
		this.doesControlIndexMatch(control);
		this.compareToSequences();
	}

	public boolean doesControlIndexMatch(Control control) {
		if (recent == null || this.getActiveSequence() == null) return false;
		if (recent.indexOf(control) != this.getActiveSequence().getControls().indexOf(control)) {
			recent.remove(control);
			return false;
		}
		return true;
	}

	public boolean hasActiveSequence() {
		return this.activeSequence != null;
	}

	public void setActiveSequence(@Nullable Sequence sequence, boolean setTicksTo0) {
		if (setTicksTo0)
			this.ticks = 0;

		this.activeSequence = sequence;

		if (this.activeSequence == null)
			return;

		this.activeSequence.sendMessageToInteriorPlayers(TardisUtil.getPlayersInInterior(tardis()));
	}

	public void triggerRandomSequence(boolean setTicksTo0) {
		if (setTicksTo0) ticks = 0;
		// TODO: replace with built-in registry random
		int rand = Random.create().nextBetween(0, SequenceRegistry.REGISTRY.size());
		Sequence sequence = SequenceRegistry.REGISTRY.get(rand);

		if (sequence == null)
			return;

		this.activeSequence = sequence;
        FlightUtil.playSoundAtConsole(tardis(), SoundEvents.BLOCK_BEACON_POWER_SELECT);
		this.activeSequence.sendMessageToInteriorPlayers(TardisUtil.getPlayersInInterior(tardis()));
	}

	@Nullable
	public Sequence getActiveSequence() {
		return activeSequence;
	}

	private void compareToSequences() {
		if (this.getActiveSequence() == null)
			return;

		if (this.recent == null)
			this.recent = new RecentControls(this.tardis().getUuid());

		if (this.getActiveSequence().isFinished(this.recent)) {
			recent.clear();
			this.getActiveSequence().execute(this.tardis());
			completedControlEffects(this.tardis());
			this.setActiveSequence(null, true);
		} else if (this.getActiveSequence().wasMissed(this.recent, ticks)) {
			recent.clear();
			this.getActiveSequence().executeMissed(this.tardis(), this.getActivePlayer());
			missedControlEffects(this.tardis());
			this.setActiveSequence(null, true);
		} else if (recent.size() >= this.getActiveSequence().getControls().size()) {
			recent.clear();
		}
	}

	public static void missedControlEffects(Tardis tardis) {
		FlightUtil.playSoundAtConsole(tardis,
				SoundEvents.ENTITY_GENERIC_EXPLODE,
				SoundCategory.BLOCKS,
				3f,
				1f);
		tardis.getDesktop().getConsoles().forEach(console -> {
			Vec3d vec3d = Vec3d.ofBottomCenter(console.position()).add(0.0, 1.2f, 0.0);
			if (TardisUtil.getTardisDimension() instanceof ServerWorld world) {
				world.spawnParticles(ParticleTypes.SMALL_FLAME, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 20, 0.4F, 1F, 0.4F, 5.0F);
				world.spawnParticles(ParticleTypes.FLASH, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 4, 0.4F, 1F, 0.4F, 5.0F);
				world.spawnParticles(new DustParticleEffect(new Vector3f(0.2f, 0.2f, 0.2f), 4f), vec3d.getX(), vec3d.getY(), vec3d.getZ(), 20, 0.0F, 1F, 0.0F, 2.0F);
			}
		});
	}

	public static void completedControlEffects(Tardis tardis) {
		FlightUtil.playSoundAtConsole(tardis,
				SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
				SoundCategory.BLOCKS,
				3f,
				1f);
		tardis.getDesktop().getConsoles().forEach(console -> {
			Vec3d vec3d = Vec3d.ofBottomCenter(console.position()).add(0.0, 1.2f, 0.0);
			if (TardisUtil.getTardisDimension() instanceof ServerWorld world) {
				world.spawnParticles(ParticleTypes.GLOW, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 12, 0.4F, 1F, 0.4F, 5.0F);
				world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 12, 0.4F, 1F, 0.4F, 5.0F);
			}
		});
	}

	public boolean isConsoleDisabled() {
		return PropertiesHandler.getBool(tardis().properties(), PropertiesHandler.CONSOLE_DISABLED);
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);

		if (this.getActiveSequence() == null) return;

		ticks++;
		if (ticks >= this.getActiveSequence().timeToFail()) {
			this.compareToSequences();
			recent.clear();
			ticks = 0;
		}
	}

	public boolean controlPartOfSequence(Control control) {
		if (this.getActiveSequence() == null) return false;
		return this.getActiveSequence().controlPartOfSequence(control);
	}

}
