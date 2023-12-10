package mdteam.ait.tardis.handler;

import com.google.gson.*;
import mdteam.ait.data.Corners;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;
import java.util.UUID;

import static mdteam.ait.tardis.handler.PropertiesHolder.AUTO_LAND;

public class PropertiesHandler extends TardisHandler {
    private final NbtCompound nbt; // using nbt as its convenient for storing data in this case, can be replaced with lists/hashmap if its needed
    public PropertiesHandler(UUID tardisId, NbtCompound data) {
        super(tardisId);
        this.nbt = data;
    }
    public PropertiesHandler(UUID tardis) {
        this(tardis, createDefaultProperties());
    }

    public NbtCompound getData() {
        return this.nbt;
    }

    public static NbtCompound createDefaultProperties() {
        NbtCompound data = new NbtCompound();

        data.putBoolean(AUTO_LAND, false);

        return data;
    }

    public static Object nbtSerializer() {
        return new NbtElementSerializer();
    }

    private static class NbtElementSerializer implements JsonDeserializer<NbtElement> {

        @Override
        public NbtElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject corners = json.getAsJsonObject();

            return new NbtCompound(); // fixme dont no no
        }
    }
}
