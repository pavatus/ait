package mdteam.ait.tardis.handler;

import com.google.gson.*;
import it.unimi.dsi.fastutil.Hash;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.Corners;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

import static mdteam.ait.tardis.handler.PropertiesHolder.AUTO_LAND;

public class PropertiesHandler extends TardisHandler {
    private final HashMap<String, Object> data; // might replace the generic object with a property class that has impls eg Property.Boolean, etc
    public PropertiesHandler(UUID tardisId, HashMap<String, Object> data) {
        super(tardisId);
        this.data = data;
    }
    public PropertiesHandler(UUID tardis) {
        this(tardis, createDefaultProperties());
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public static HashMap<String, Object> createDefaultProperties() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(AUTO_LAND, false);

        return map;
    }
}
