package mdteam.ait.tardis.control.sequences;

import mdteam.ait.registry.SequenceRegistry;
import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.control.impl.HandBrakeControl;
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

import java.util.Objects;

import static java.lang.Double.NaN;

public class SequenceHandler extends TardisLink {

    @Exclude
    private RecentControls recent;
    private int ticks = 0;
    private Sequence activeSequence;

    public SequenceHandler(Tardis tardisId) {
        super(tardisId, "sequence");
        recent = new RecentControls(tardisId.getUuid());
        activeSequence = null;
    }

    public void add(Control control) {
        System.out.println(control);
        recent.add(control);
        ticks = 0;
        this.compareToSequences();
        sync();
    }

    public boolean hasActiveSequence() {
        return this.activeSequence != null;
    }

    public void setActiveSequence(@Nullable Sequence sequence, boolean setTicksTo0) {
        if(setTicksTo0) ticks = 0;
        this.activeSequence = sequence;
        sync();
        if(findTardis().isEmpty() || this.activeSequence == null) return;
        this.activeSequence.sendMessageToInteriorPlayers(TardisUtil.getPlayersInInterior(findTardis().get()));
    }

    public void triggerRandomSequence(boolean setTicksTo0) {
        if(setTicksTo0) ticks = 0;
        int rand = Random.create().nextBetween(0, SequenceRegistry.REGISTRY.size());
        Sequence sequence = SequenceRegistry.REGISTRY.get(rand);
        if(sequence == null) return;
        this.activeSequence = Objects.equals(sequence.id().getPath(),
                SequenceRegistry.FORCED_MAT) ? SequenceRegistry.REGISTRY.get(2) : sequence;
        sync();
        if(findTardis().isEmpty() || this.activeSequence == null) return;
        FlightUtil.playSoundAtConsole(findTardis().get(), SoundEvents.BLOCK_BEACON_POWER_SELECT);
        this.activeSequence.sendMessageToInteriorPlayers(TardisUtil.getPlayersInInterior(findTardis().get()));
    }

    @Nullable
    public Sequence getActiveSequence() {
        return activeSequence;
    }

    private void compareToSequences() {
        for (Sequence sequence : SequenceRegistry.REGISTRY) {
            if(this.findTardis().isEmpty()) break;
            if(this.recent == null)
                this.recent = new RecentControls(this.findTardis().get().getUuid());
            if (sequence.isFinished(this.recent)) {
                sequence.execute(this.findTardis().get());
                recent.clear();
                FlightUtil.playSoundAtConsole(this.findTardis().get(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
                this.setActiveSequence(null, true);
            } else if (sequence.wasMissed(this.recent, ticks)) {
                sequence.executeMissed(this.findTardis().get());
                missedControlEffects(this.findTardis().get());
                recent.clear();
                this.setActiveSequence(null, true);
            }
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
            if(TardisUtil.getTardisDimension() instanceof ServerWorld world) {
                world.spawnParticles(ParticleTypes.SMALL_FLAME, vec3d.getX(), vec3d.getY(), vec3d.getZ(), 20, 0.4F, 1F, 0.4F, 5.0F);
                world.spawnParticles(new DustParticleEffect(new Vector3f(0.2f, 0.2f, 0.2f), 4f), vec3d.getX(), vec3d.getY(), vec3d.getZ(), 20, 0.0F, 1F, 0.0F, 2.0F);
            }
        });
    }

    public boolean isConsoleDisabled() {
        if(findTardis().isEmpty()) return false;
        return PropertiesHandler.getBool(findTardis().get().getHandlers().getProperties(), PropertiesHandler.CONSOLE_DISABLED);
    }

    public void disableConsole(boolean disabled) {
        if(findTardis().isEmpty()) return;
        PropertiesHandler.set(findTardis().get(), PropertiesHandler.CONSOLE_DISABLED, disabled);
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        if(this.getActiveSequence() == null) return;

        ticks++;
        if (ticks >= this.getActiveSequence().timeToFail()) {
            this.compareToSequences();
            recent.clear();
            ticks = 0;
        }
    }

    public boolean controlPartOfSequence(Control control) {
        if(this.getActiveSequence() == null) return false;
        return this.getActiveSequence().controlPartOfSequence(control);
    }
}
