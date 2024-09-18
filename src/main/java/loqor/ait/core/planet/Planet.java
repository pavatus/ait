package loqor.ait.core.planet;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import loqor.ait.AITMod;
import loqor.ait.api.Identifiable;
import loqor.ait.core.item.SpacesuitItem;

public record Planet(Identifier dimension, float gravity, boolean hasOxygen, int temperature) implements Identifiable {
    public static final Codec<Planet> CODEC = Codecs.exceptionCatching(RecordCodecBuilder.create(instance -> instance.group(Identifier.CODEC.fieldOf("dimension").forGetter(Planet::dimension), Codec.FLOAT.fieldOf("gravity").forGetter(Planet::gravity), Codec.BOOL.fieldOf("has_oxygen").forGetter(Planet::hasOxygen), Codec.INT.optionalFieldOf("temperature").forGetter(planet -> Optional.of(planet.temperature()))).apply(instance, Planet::new)));

    @Override
    public Identifier id() {
        return this.dimension();
    }

    public Planet(Identifier dimension, float gravity, boolean hasOxygen, Optional<Integer> temperature) {
        this(dimension, gravity, hasOxygen, temperature.orElse(288));
    }

    public float celcius() {
        return this.temperature() - 273.15f;
    }

    public Planet with(Identifier dimension) {
        return new Planet(dimension, this.gravity, this.hasOxygen, this.temperature);
    }
    public static boolean hasFullSuit(LivingEntity entity) {
        return entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof SpacesuitItem
                && entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof SpacesuitItem
                && entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof SpacesuitItem
                && entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof SpacesuitItem;
    }

    public static Planet fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }

    public static Planet fromJson(JsonObject json) {
        AtomicReference<Planet> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json).get().ifLeft(planet -> created.set(planet.getFirst())).ifRight(err -> {
            created.set(null);
            AITMod.LOGGER.error("Error decoding datapack planet: {}", err);
        });

        return created.get();
    }
}
