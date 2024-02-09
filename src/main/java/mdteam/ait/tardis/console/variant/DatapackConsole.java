package mdteam.ait.tardis.console.variant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.exterior.variant.DatapackExterior;
import mdteam.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

// Example usage
/*
{
  "id": "ait:white_alnico",
  "parent": "ait:console/alnico",
  "texture": "ait:textures/console/alnico.png",
  "emission": "ait:textures/console/alnico_emission.png"
}
 */
public class DatapackConsole extends ConsoleVariantSchema {
    public static final Identifier DEFAULT_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/desktop/missing_preview.png");

    protected final Identifier texture;
    protected final Identifier emission;
    protected boolean initiallyDatapack;

    public static final Codec<DatapackConsole> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(ConsoleVariantSchema::id),
                    Identifier.CODEC.fieldOf("parent").forGetter(ConsoleVariantSchema::parentId),
                    Identifier.CODEC.fieldOf("texture").forGetter(DatapackConsole::texture),
                    Identifier.CODEC.fieldOf("emission").forGetter(DatapackConsole::emission),
                    Codec.BOOL.optionalFieldOf("isDatapack", true).forGetter(DatapackConsole::wasDatapack)
            ).apply(instance, DatapackConsole::new));

    public DatapackConsole(Identifier id, Identifier category, Identifier texture, Identifier emission, boolean isDatapack) {
        super(category, id);
        this.texture = texture;
        this.emission = emission;
        this.initiallyDatapack = isDatapack;
    }
    public DatapackConsole(Identifier id, Identifier category, Identifier texture, Identifier emission) {
        this(id, category, texture, emission, true);
    }

    public boolean wasDatapack() {
        return this.initiallyDatapack;
    }
    public Identifier texture() {
        return this.texture;
    }

    public Identifier emission() {
        return this.emission;
    }

    public static DatapackConsole fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }
    public static DatapackConsole fromJson(JsonObject json) {
        AtomicReference<DatapackConsole> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json)
                .get()
                .ifLeft(var -> { created.set((DatapackConsole) var.getFirst()); })
                .ifRight(err -> { created.set(null);
                    AITMod.LOGGER.error("Error decoding datapack console variant: " + err);
                });

        return created.get();
    }
}
