package loqor.ait.tardis.data.mood;

import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.registry.impl.MoodEventPoolRegistry;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static loqor.ait.core.data.base.Exclude.Strategy.NETWORK;

public class MoodHandler extends TardisComponent implements TardisTickable {
    @Exclude(strategy = NETWORK) public TardisMood.Moods[] priorityMoods;
    @Exclude private MoodDictatedEvent moodEvent;
    @Exclude private TardisMood.Moods winningMood;

    /**
     * Do NOT under any circumstances run logic in this constructor.
     * Default field values should be inlined. All logic should be done in an appropriate init method.
     *
     * @implNote The {@link TardisComponent#tardis()} will always be null at the time this constructor gets called.
     */
    public MoodHandler() {
        super(Id.MOOD);
    }

    @Override
    public void onCreate() {
        this.randomizePriorityMoods();
    }

    @Nullable
    public MoodDictatedEvent getEvent() {
        return moodEvent;
    }

    @Override
    public void tick(MinecraftServer world) {
        if (this.moodEvent == null)
            return;

        if (this.winningMood == null)
            return;

        TardisMood.Alignment alignment = this.moodEvent.getMoodTypeCompatibility();

        if (this.winningMood.alignment().equals(TardisMood.Alignment.NEUTRAL) ||
                (this.winningMood.alignment() == alignment && (this.moodEvent.getMoodsList().contains(this.winningMood) ||
                this.moodEvent.getMoodsList().isEmpty()))) {
            if (this.winningMood.weight() >= this.moodEvent.getCost()) {
                if (this.getEvent() == null) {
                    this.winningMood = null;
                    return;
                }

                switch (this.winningMood.alignment()) {
                    case NEGATIVE -> handleNegativeMood(alignment);
                    case POSITIVE -> handlePositiveMood(this.winningMood, alignment);
                    case NEUTRAL -> handleNeutralMood(this.moodEvent, alignment, this.winningMood);
                }
            }
        } else {
            this.winningMood = null;
        }
    }

    public void rollForMoodDictatedEvent() {
        int rand = AITMod.RANDOM.nextInt(0, MoodEventPoolRegistry.REGISTRY.size());
        MoodDictatedEvent moodEvent = MoodEventPoolRegistry.REGISTRY.get(rand);

        if (moodEvent == null)
            return;

        this.moodEvent = moodEvent;
        TardisUtil.getPlayersInsideInterior(this.tardis()).forEach(player -> player.sendMessage(
                Text.literal(this.moodEvent.id().getPath()).formatted(Formatting.BOLD), true));

        raceMoods();
    }

    public void raceMoods() {
        Map<TardisMood.Moods, Integer> moodWeights = new HashMap<>();

        TardisMood.Moods[] moods = priorityMoods.length == 0 ?
                TardisMood.Moods.VALUES : priorityMoods;

        for (TardisMood.Moods mood : moods) {
            int weight = 8 + (AITMod.RANDOM.nextInt(0, 11) * 8);
            moodWeights.put(mood, Math.min(weight, 256));
        }

        this.winningMood = moodWeights.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);

        this.tardis().getDesktop().getConsolePos().forEach(console ->
                TardisUtil.getTardisDimension().playSound(null, BlockPos.ofFloored(console.toCenterPos()),
                        SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.MASTER, 1f, 1f));
    }

    private void handleNegativeMood(TardisMood.Alignment alignment) {
        if (alignment == TardisMood.Alignment.NEGATIVE) {
            this.moodEvent.execute(this.tardis());
            this.updateEvent(null);
        } else if (alignment == TardisMood.Alignment.POSITIVE) {
            this.rollForMoodDictatedEvent();
        } else if (alignment == TardisMood.Alignment.NEUTRAL) {
            if (AITMod.RANDOM.nextInt(0, 10) < 5) {
                this.moodEvent.execute(this.tardis());
                this.updateEvent(null);
            } else {
                this.rollForMoodDictatedEvent();
            }
        }
    }

    private void handlePositiveMood(TardisMood.Moods mood, TardisMood.Alignment alignment) {
        if (alignment == TardisMood.Alignment.POSITIVE) {
            this.moodEvent.execute(this.tardis());
            this.updateEvent(null);
        } else if (alignment == TardisMood.Alignment.NEGATIVE) {
            this.rollForMoodDictatedEvent();
        } else if (alignment == TardisMood.Alignment.NEUTRAL) {
            if (mood.swayWeight() == 0 || (mood.swayWeight() < 0 && AITMod.RANDOM.nextInt(0, 10) < 5)) {
                this.moodEvent.execute(this.tardis());
                this.updateEvent(null);
            } else {
                this.rollForMoodDictatedEvent();
            }
        }
    }

    private void handleNeutralMood(MoodDictatedEvent mDE, TardisMood.Alignment alignment, TardisMood.Moods winningMood) {
        if (alignment == TardisMood.Alignment.NEUTRAL) {
            if (winningMood.weight() + winningMood.swayWeight() >= mDE.getCost()) {
                this.moodEvent.execute(this.tardis());
                this.updateEvent(null);
            } else {
                this.rollForMoodDictatedEvent();
            }
        } else {
            this.moodEvent.execute(this.tardis());
            this.updateEvent(null);
        }
    }

    public void updateEvent(@Nullable MoodDictatedEvent moodDictatedEvent) {
        this.moodEvent = moodDictatedEvent;
        this.winningMood = null;
    }

    public void randomizePriorityMoods() {
        TardisMood.Moods[] moods = new TardisMood.Moods[3];

        for (int i = 0; i < 3; i++) {
            moods[i] = TardisMood.Moods.VALUES[AITMod.RANDOM.nextInt(
                    TardisMood.Moods.VALUES.length)];
        }

        priorityMoods = moods;
    }
}
