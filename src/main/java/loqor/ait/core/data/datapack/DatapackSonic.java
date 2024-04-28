package loqor.ait.core.data.datapack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import loqor.ait.AITMod;
import loqor.ait.core.data.schema.SonicSchema;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DatapackSonic extends SonicSchema {

    public static final Codec<SonicSchema> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(SonicSchema::id),
                    Codec.STRING.fieldOf("name").forGetter(SonicSchema::name),
                    Models.CODEC.fieldOf("models").forGetter(SonicSchema::models),

                    Rendering.CODEC.optionalFieldOf("rendering")
                            .forGetter(schema -> Optional.of(schema.rendering())),

                    Loyalty.CODEC.optionalFieldOf("loyalty", Loyalty.MIN)
                            .forGetter(SonicSchema::getRequirement)
            ).apply(instance, DatapackSonic::new));

    public DatapackSonic(Identifier id, String name, Models models, Optional<Rendering> rendering, Loyalty loyalty) {
        super(id, name, models, rendering.orElse(new Rendering()), loyalty);
    }

    public static SonicSchema fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }

    public static SonicSchema fromJson(JsonObject json) {
        AtomicReference<SonicSchema> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json)
                .get()
                .ifLeft(sonic -> created.set(sonic.getFirst()))
                .ifRight(err -> {
                    created.set(null);
                    AITMod.LOGGER.error("Error decoding datapack sonic: " + err);
                });

        return created.get();
    }
}
