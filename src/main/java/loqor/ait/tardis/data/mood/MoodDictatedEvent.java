package loqor.ait.tardis.data.mood;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class MoodDictatedEvent {
    public Identifier id() {
        return new Identifier(AITMod.MOD_ID, "mood_dictated_event");
    }
    public void execute(Tardis tardis) {
        System.out.println("Executing mood dictated event");
    }

    public int getCost() {return 0;}

    public List<TardisMood.Moods> getMoodsList() {return List.of();}
    public TardisMood.MoodType getMoodTypeCompatibility() {return TardisMood.MoodType.NEUTRAL;}

    public static class Builder extends MoodDictatedEvent {
        private final Identifier id;
        private final ExecuteMoodEvent execute;
        private final int cost;
        private final List<TardisMood.Moods> moodsList;
        private final TardisMood.MoodType moodTypeCompatibility;
        public Builder(Identifier id, ExecuteMoodEvent execute, int cost, TardisMood.MoodType moodType, TardisMood.Moods... moods) {
            this.id = id;
            this.execute = execute;
            this.cost = cost;
            this.moodsList = List.of(moods);
            this.moodTypeCompatibility = moodType;
        }
        public static MoodDictatedEvent create(Identifier id, ExecuteMoodEvent execute, int cost, TardisMood.MoodType moodType, TardisMood.Moods... moods) {
            return new MoodDictatedEvent.Builder(id, execute, cost, moodType, moods);
        }

        @Override
        public Identifier id() {
            return this.id;
        }

        @Override
        public int getCost() {
            return this.cost;
        }

        @Override
        public void execute(Tardis tardis) {
            this.execute.run(tardis);
        }

        @Override
        public List<TardisMood.Moods> getMoodsList() {
            return this.moodsList;
        }

        @Override
        public TardisMood.MoodType getMoodTypeCompatibility() {
            return this.moodTypeCompatibility;
        }

        public interface ExecuteMoodEvent {
            void run(Tardis tardis);
        }
    }
}
