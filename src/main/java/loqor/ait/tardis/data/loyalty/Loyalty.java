package loqor.ait.tardis.data.loyalty;

public record Loyalty(int level, Type type) {

    public Loyalty(Type type) {
        this(type.level, type);
    }

    public Loyalty add(int level) {
        return Loyalty.fromLevel(this.level + level);
    }

    public Loyalty subtract(int level) {
        return Loyalty.fromLevel(this.level - level);
    }

    public static Loyalty fromLevel(int level) {
        level = Type.normalize(level);
        return new Loyalty(level, Type.get(level));
    }

    public enum Type {
        REJECT(0),
        NEUTRAL(25),
        COMPANION(50),
        PILOT(75),
        OWNER(100);

        public final int level;

        Type(int start) {
            this.level = start;
        }

        public static Type get(String id) {
            return Type.valueOf(id.toUpperCase());
        }

        public static Type get(int level) {
            level = Type.normalize(level);

            for (int i = 0; i < values().length - 1; i++) {
                Type current = values()[i];
                Type next = values()[i + 1];

                if (current.level <= level && level < next.level)
                    return current;
            }

            return Type.OWNER;
        }

        public static int normalize(int level) {
            return Math.min(Math.max(level, Type.REJECT.level), Type.OWNER.level);
        }
    }
}
