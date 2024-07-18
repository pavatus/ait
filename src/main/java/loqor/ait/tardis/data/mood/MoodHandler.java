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

public class MoodHandler extends TardisComponent implements TardisTickable {

    @Exclude
    private MoodDictatedEvent moodDictatedEvent;
    @Exclude
    private TardisMood.Moods winningMood;
    @Exclude
    private final Random RANDOM = AITMod.RANDOM;
    public static List<TardisMood.Moods> PRIORITY_MOODS; // <3>

    /**
     * Do NOT under any circumstances run logic in this constructor.
     * Default field values should be inlined. All logic should be done in an appropriate init method.
     *
     * @implNote The {@link TardisComponent#tardis()} will always be null at the time this constructor gets called.
     */
    public MoodHandler() {
        super(Id.MOOD);
    }

    public void setPriorityMoods(List<TardisMood.Moods> moods) {
        PRIORITY_MOODS = moods;
    }

    public List<TardisMood.Moods> getPriorityMoods() {
        return PRIORITY_MOODS;
    }

    @Override
    protected void onInit(InitContext ctx) {
        this.setMoodDictatedEvent(null);
    }

    @Nullable
    public MoodDictatedEvent getMoodDictatedEvent() {
        return moodDictatedEvent;
    }

    @Override
    public void tick(MinecraftServer world) {

        if (this.getMoodDictatedEvent() == null) return;

        if (this.winningMood != null) {
            TardisMood.MoodType moodType = this.getMoodDictatedEvent().getMoodTypeCompatibility();
            if (this.winningMood.getMoodType().equals(TardisMood.MoodType.NEUTRAL) ||
                    (this.winningMood.getMoodType() == moodType && (this.getMoodDictatedEvent().getMoodsList().contains(this.winningMood) ||
                    this.getMoodDictatedEvent().getMoodsList().isEmpty()))) {
                if (this.winningMood.getWeight() >= this.getMoodDictatedEvent().getCost()) {
                    switch (this.winningMood.getMoodType()) {
                        case NEGATIVE:
                            handleNegativeMood(moodType);
                            break;
                        case POSITIVE:
                            handlePositiveMood(moodType);
                            break;
                        case NEUTRAL:
                            handleNeutralMood(this.getMoodDictatedEvent(), moodType, this.winningMood);
                            break;
                        default:
                            System.out.println(this + ": How did we get here?");
                    }
                }
            } else {
                this.winningMood = null;
            }
        }
    }

    public void rollForMoodDictatedEvent() {

        int rand = RANDOM.nextInt(0, MoodEventPoolRegistry.REGISTRY.size());
        MoodDictatedEvent moodEvent = MoodEventPoolRegistry.REGISTRY.get(rand);

        if (moodEvent == null)
            return;

        this.moodDictatedEvent = moodEvent;
        TardisUtil.getPlayersInsideInterior(this.tardis()).forEach(player -> player.sendMessage(
                Text.literal(moodDictatedEvent.id().getPath()).formatted(Formatting.BOLD), true));

        raceMoods();
    }

    public void raceMoods() {
        Map<TardisMood.Moods, Integer> moodWeights = new HashMap<>();

        if (this.getPriorityMoods().isEmpty()) {
            for (TardisMood.Moods mood : TardisMood.Moods.values()) {
                int weight = 8 + (RANDOM.nextInt(0, 11) * 8);
                moodWeights.put(mood, Math.min(weight, 256));
            }
        } else {
            for (TardisMood.Moods mood : this.getPriorityMoods()) {
                int weight = 8 + (RANDOM.nextInt(0, 11) * 8);
                moodWeights.put(mood, Math.min(weight, 256));
            }
        }

        this.winningMood = moodWeights.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);
        this.winningMood.setWeight(moodWeights.get(this.winningMood));

        this.tardis().getDesktop().getConsolePos().forEach(console ->
                TardisUtil.getTardisDimension().playSound(null, BlockPos.ofFloored(console.toCenterPos()),
                        SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.MASTER, 1f, 1f));
    }

    private void handleNegativeMood(TardisMood.MoodType moodType) {
        if (this.getMoodDictatedEvent() == null) {
            this.winningMood = null;
            return;
        }
        if (moodType == TardisMood.MoodType.NEGATIVE) {
            this.getMoodDictatedEvent().execute(this.tardis());
            this.setMoodDictatedEvent(null);
        } else if (moodType == TardisMood.MoodType.POSITIVE) {
            this.rollForMoodDictatedEvent();
        } else if (moodType == TardisMood.MoodType.NEUTRAL) {
            if (RANDOM.nextInt(0, 10) < 5) {
                this.getMoodDictatedEvent().execute(this.tardis());
                this.setMoodDictatedEvent(null);
            } else {
                this.rollForMoodDictatedEvent();
            }
        }
    }

    private void handlePositiveMood(TardisMood.MoodType moodType) {
        if (this.getMoodDictatedEvent() == null) {
            this.winningMood = null;
            return;
        }
        if (moodType == TardisMood.MoodType.POSITIVE) {
            this.getMoodDictatedEvent().execute(this.tardis());
            this.setMoodDictatedEvent(null);
        } else if (moodType == TardisMood.MoodType.NEGATIVE) {
            this.rollForMoodDictatedEvent();
        } else if (moodType == TardisMood.MoodType.NEUTRAL) {
            if (moodType.getSwayedWeight() == 0 || (moodType.getSwayedWeight() < 0 && RANDOM.nextInt(0, 10) < 5)) {
                this.getMoodDictatedEvent().execute(this.tardis());
                this.setMoodDictatedEvent(null);
            } else {
                this.rollForMoodDictatedEvent();
            }
        }
    }

    private void handleNeutralMood(MoodDictatedEvent mDE, TardisMood.MoodType moodType, TardisMood.Moods winningMood) {
        if (this.getMoodDictatedEvent() == null) {
            this.winningMood = null;
            return;
        }
        if (moodType == TardisMood.MoodType.NEUTRAL) {
            if (winningMood.getWeight() + winningMood.getMoodType().getSwayedWeight() >= mDE.getCost()) {
                this.getMoodDictatedEvent().execute(this.tardis());
                this.setMoodDictatedEvent(null);
            } else {
                this.rollForMoodDictatedEvent();
            }
        } else {
            this.getMoodDictatedEvent().execute(this.tardis());
            this.setMoodDictatedEvent(null);
        }
    }

    public void setMoodDictatedEvent(@Nullable MoodDictatedEvent moodDictatedEvent) {
        this.moodDictatedEvent = moodDictatedEvent;
        this.winningMood = null;
    }

    public void randomizePriorityMoods() {
        List<TardisMood.Moods> moods = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            moods.add(TardisMood.Moods.values()[RANDOM.nextInt(TardisMood.Moods.values().length)]);
        }
        this.setPriorityMoods(moods);
    }
}
