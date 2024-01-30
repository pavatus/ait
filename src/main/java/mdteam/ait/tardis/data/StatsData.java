package mdteam.ait.tardis.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

// is StatsData a good name for this class?
public class StatsData extends TardisLink {
    private static final Identifier NAME_PATH = new Identifier(AITMod.MOD_ID, "tardis_names.json");
    private static final String NAME_KEY = "name";
    private static List<String> NAME_CACHE;

    private static final String DATE_KEY = "date";

    public StatsData(Tardis tardis) {
        super(tardis, "stats");
    }

    public String getName() {
        if(getTardis().isEmpty()) return "";

        String name = (String) PropertiesHandler.get(getTardis().get().getHandlers().getProperties(), NAME_KEY);

        if (name == null) {
            name = getRandomName();
            this.setName(name);
        }

        return name;
    }

    public void setName(String name) {
        if(getTardis().isEmpty()) return;
        PropertiesHandler.set(getTardis().get().getHandlers().getProperties(), NAME_KEY, name);
    }

    public static String fixupName(String name) {
        String[] words = name.split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return result.toString().trim();
    }

    public static String getRandomName() {
        if (shouldGenerateNames()) loadNames();
        assert NAME_CACHE != null;
        return NAME_CACHE.get(AITMod.RANDOM.nextInt(NAME_CACHE.size()));
    }

    public static boolean shouldGenerateNames() {
        return NAME_CACHE == null || NAME_CACHE.isEmpty();
    }

    private static void loadNames() {
        if (NAME_CACHE == null) NAME_CACHE = new ArrayList<>();

        NAME_CACHE.clear();

        try {
            Optional<Resource> resource = TardisUtil.getServer().getResourceManager().getResource(NAME_PATH);

            if (resource.isEmpty()) {
                AITMod.LOGGER.error("ERROR in tardis_names.json:");
                AITMod.LOGGER.error("Missing Resource");
                return;
            }

            InputStream stream = resource.get().getInputStream();

            JsonArray list = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonArray();

            for (JsonElement element : list) {
                NAME_CACHE.add(element.getAsString());
            }
        } catch (IOException e) {
            AITMod.LOGGER.error("ERROR in tardis_names.json");
            e.printStackTrace();
            return;
        }
    }

    public Date getCreationDate() {
        if(getTardis().isEmpty()) return Date.from(Instant.now());

        if (PropertiesHandler.get(getTardis().get().getHandlers().getProperties(), DATE_KEY) == null) {
            AITMod.LOGGER.error(getTardis().get().getUuid().toString() + " was missing creation date! Resetting to now");
            markCreationDate();
        }

        try {
            return DateFormat.getDateInstance().parse(PropertiesHandler.getString(getTardis().get().getHandlers().getProperties(), DATE_KEY));
        } catch (Exception e) {
            return Date.from(Instant.now());
        }
    }
    public String getCreationString() {
        return DateFormat.getDateInstance().format(getCreationDate());
    }
    public void markCreationDate() {
        if(getTardis().isEmpty()) return;
        PropertiesHandler.set(getTardis().get().getHandlers().getProperties(), DATE_KEY, Date.from(Instant.now()).toString());
    }
}
