package mdteam.ait.tardis.exterior.category;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

public class DatapackCategory extends ExteriorCategory {
    public static final Codec<ExteriorCategory> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(ExteriorCategory::id),
                    Codec.STRING.fieldOf("name").forGetter(ExteriorCategory::name)
            ).apply(instance, DatapackCategory::new));

    public DatapackCategory(Identifier id, String name) {
        super(id, name);
    }

    public static DatapackCategory fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }
    public static DatapackCategory fromJson(JsonObject json) {
        AtomicReference<DatapackCategory> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json)
                .get()
                .ifLeft(var -> { created.set((DatapackCategory) var.getFirst()); })
                .ifRight(err -> { created.set(null);
                    AITMod.LOGGER.error("Error decoding datapack category: " + err);
                });

        return created.get();
    }
}
