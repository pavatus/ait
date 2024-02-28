package mdteam.ait.tardis.control.sequences;

import mdteam.ait.registry.SequenceRegistry;
import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.TardisLink;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class SequenceHandler extends TardisLink {
	@Exclude
	private RecentControls recent;
	private int ticks = 0;
	@Exclude
	private Sequence activeSequence;

	public SequenceHandler(Tardis tardisId) {
		super(tardisId, "sequence");
		recent = new RecentControls(tardisId.getUuid());
		activeSequence = null;
	}

	public void add(Control control) {
		if (this.getActiveSequence() == null || recent == null) return;
		recent.add(control);
		ticks = 0;
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
		if (setTicksTo0) ticks = 0;
		this.activeSequence = sequence;
		if (findTardis().isEmpty() || this.activeSequence == null) return;
		this.activeSequence.sendMessageToInteriorPlayers(TardisUtil.getPlayersInInterior(findTardis().get()));
		//sync();
	}

	public void triggerRandomSequence(boolean setTicksTo0) {
		if (setTicksTo0) ticks = 0;
		int rand = Random.create().nextBetween(0, SequenceRegistry.REGISTRY.size() - 4);
		Sequence sequence = SequenceRegistry.REGISTRY.get(rand);
		if (sequence == null) return;
		this.activeSequence = sequence == SequenceRegistry.TAKE_OFF
				|| sequence == SequenceRegistry.ENTER_VORTEX
				|| sequence == SequenceRegistry.EXIT_VORTEX
				|| sequence == SequenceRegistry.LANDING ? null : sequence;
		if (findTardis().isEmpty() || this.activeSequence == null) return;
		FlightUtil.playSoundAtConsole(findTardis().get(), SoundEvents.BLOCK_BEACON_POWER_SELECT);
		this.activeSequence.sendMessageToInteriorPlayers(TardisUtil.getPlayersInInterior(findTardis().get()));
		//sync();
	}

	@Nullable
	public Sequence getActiveSequence() {
		return activeSequence;
	}

	private void compareToSequences() {
		if (this.findTardis().isEmpty() || this.getActiveSequence() == null) return;
		if (this.recent == null)
			this.recent = new RecentControls(this.findTardis().get().getUuid());
		if (this.getActiveSequence().isFinished(this.recent)) {
			recent.clear();
			this.getActiveSequence().execute(this.findTardis().get());
			completedControlEffects(this.findTardis().get());
			this.setActiveSequence(null, true);
		} else if (this.getActiveSequence().wasMissed(this.recent, ticks)) {
			recent.clear();
			this.getActiveSequence().executeMissed(this.findTardis().get());
			missedControlEffects(this.findTardis().get());
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
		if (findTardis().isEmpty()) return false;
		return PropertiesHandler.getBool(findTardis().get().getHandlers().getProperties(), PropertiesHandler.CONSOLE_DISABLED);
	}

	public void disableConsole(boolean disabled) {
		if (findTardis().isEmpty()) return;
		PropertiesHandler.set(findTardis().get(), PropertiesHandler.CONSOLE_DISABLED, disabled);
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

	public RecentControls getRecent() {
		return recent;
	}

	public int getActiveSequenceTicks() {
		return ticks;
	}
}
