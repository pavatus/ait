package loqor.ait.tardis.sound;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import loqor.ait.registry.impl.HumsRegistry;

public class HumSound {
    private final Identifier id;
    private final SoundEvent sound;
    private final String name;

    protected HumSound(String name, Identifier id, SoundEvent sound) {
        this.name = name;
        this.id = id;
        this.sound = sound;
    }

    public Identifier id() {
        return this.id;
    }

    public SoundEvent sound() {
        return this.sound;
    }

    public String name() {
        return this.name;
    }

    public static HumSound create(String modId, String name, SoundEvent sound) {
        return new HumSound(name, createId(modId, name), sound);
    }

    public static HumSound fromName(String modId, String name) {
        return HumsRegistry.REGISTRY.get(createId(modId, name));
    }

    private static Identifier createId(String modid, String name) {
        return new Identifier(modid, "hum/" + name);
    }
}
