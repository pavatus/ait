package loqor.ait.core.tardis.vortex.reference;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.Identifiable;
import loqor.ait.client.renderers.VortexUtil;

public record VortexReference(Identifier id, Identifier texture) implements Identifiable {
    public static final Codec<VortexReference> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(VortexReference::id),
            Identifier.CODEC.fieldOf("texture").forGetter(VortexReference::texture)
    ).apply(instance, VortexReference::new));

    @Environment(EnvType.CLIENT)
    public VortexUtil toUtil() {
        return new VortexUtil(this.texture());
    }

    public static VortexReference fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }

    public static VortexReference fromJson(JsonObject json) {
        AtomicReference<VortexReference> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json).get().ifLeft(planet -> created.set(planet.getFirst())).ifRight(err -> {
            created.set(null);
            AITMod.LOGGER.error("Error decoding datapack vortex: {}", err);
        });

        return created.get();
    }

}
