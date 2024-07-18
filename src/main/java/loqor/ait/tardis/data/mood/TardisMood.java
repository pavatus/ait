package loqor.ait.tardis.data.mood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record TardisMood(Moods moods, MoodType type, int weight) {

    public static final Codec<TardisMood> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.optionalFieldOf("mood").forGetter(tardismood -> Optional.of(tardismood.moods.toString())),
                    Codec.STRING.optionalFieldOf("type").forGetter(tardismood -> Optional.of(tardismood.moods.getMoodType().toString())),
                    Codec.INT.optionalFieldOf("weight").forGetter(tardisMood -> Optional.of(tardisMood.weight))
            ).apply(instance, (TardisMood::deserialize)));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static TardisMood deserialize(Moods mood, Optional<Integer> weight) {
        return new TardisMood(mood, mood.getMoodType(), weight.orElse(mood.getWeight()));
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static TardisMood deserialize(Optional<String> mood, Optional<String> type, Optional<Integer> level) {
        return deserialize(type.map(Moods::valueOf).orElse(null), level);
    }

    /**
     * @author Loqor, Saturn
     * Simplified version of what Saturn said:
     * - Each mood will have a tug of war with the other depending on their weights - which are arbitrary
     * values that get set whenever an event happens
     * - This tug of war will result in moods having the highest weight - and whichever mood has the highest weight
     * at the end of some arbitrary "race" between each competing mood, it has the ability to randomly select with
     * "weighted points" a triggered action - for example, changing dimensions randomly, or going to a random structure (which
     * we'll have to implement but that's okay)
     * - When that weighted mood selects an action (each action will have a cost and reduce the mood's weight by that cost)
     * the mood events are completely over, and the values aren't reset, but set to what they were before that race happened +
     * some arbitrary amount.
     * An aside:
     * Each mood will have a predefined positive, neutral, or negative value which can be overriden if necessary. The reasoning
     * behind this is the higher the loyalty of a given player, the cheaper a positive action at the end of the race will be,
     * resulting in more wins for positive moods.
     * However, the opposite is possible. If multiple negative emotions are present, the additional points will be rejected and
     * the positive mood will be reverted to its original value.
     */

    public enum Moods {
        // 6 negative moods
        ANGRY(0, MoodType.NEGATIVE),
        HATEFUL(0, MoodType.NEGATIVE),
        FEARFUL(0, MoodType.NEGATIVE),
        DEPRESSED(0, MoodType.NEGATIVE),
        HURT(0, MoodType.NEGATIVE),
        UPSET(0, MoodType.NEGATIVE),

        // 8 neutral moods
        TOLERANT(0, MoodType.NEUTRAL.setSwayWeight(-20)),
        BORED(0, MoodType.NEUTRAL.setSwayWeight(-20)),
        IMPASSIVE(0, MoodType.NEUTRAL.setSwayWeight(-20)),
        CALM(0, MoodType.NEUTRAL.setSwayWeight(20)),
        MANIC(0, MoodType.NEUTRAL.setSwayWeight(-20)),
        CURIOUS(0, MoodType.NEUTRAL.setSwayWeight(20)),
        LONELY(0, MoodType.NEUTRAL.setSwayWeight(-20)),
        ANXIOUS(0, MoodType.NEUTRAL.setSwayWeight(-20)),

        // 5 positive moods
        HAPPY(0, MoodType.POSITIVE),
        EXCITED(0, MoodType.POSITIVE),
        COOPERATIVE(0, MoodType.POSITIVE),
        HOPEFUL(0, MoodType.POSITIVE),
        GRATEFUL(0, MoodType.POSITIVE);

        public int weight;
        public MoodType type;

        Moods(int weight, MoodType type) {
            this.weight = weight;
            this.type = type;
        }

        public int getWeight() {
            return this.weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void setMoodType(MoodType type) {
            this.type = type;
        }

        public MoodType getMoodType() {
            return type;
        }

        public static Moods get(String id) {
            return Moods.valueOf(id.toUpperCase());
        }
    }

    public enum MoodType {
        POSITIVE(0),
        NEUTRAL(0),
        NEGATIVE(0);

        public int swayWeight;

        MoodType(int swayWeight) {
            this.swayWeight = swayWeight;
        }

        public int getSwayedWeight() {
            return this.swayWeight;
        }

        // if the sway weight is positive, it results in a more-than-likely positive mood
        // if the sway weight is negative, it results in a more-than-likely negative mood
        public MoodType setSwayWeight(int swayWeight) {
            this.swayWeight = swayWeight;
            return MoodType.NEUTRAL;
        }

        public static MoodType get(String id) {
            return MoodType.valueOf(id.toUpperCase());
        }
    }
}
