package loqor.ait.tardis.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisUtil;
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
		if (findTardis().isEmpty()) return "";

		String name = (String) PropertiesHandler.get(findTardis().get().getHandlers().getProperties(), NAME_KEY);

		if (name == null) {
			name = getRandomName();
			this.setName(name);
		}

		return name;
	}

	public void setName(String name) {
		if (findTardis().isEmpty()) return;
		PropertiesHandler.set(findTardis().get(), NAME_KEY, name);
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
		if (NAME_CACHE == null) return "";

		return NAME_CACHE.get(AITMod.RANDOM.nextInt(NAME_CACHE.size()));
	}

	public static boolean shouldGenerateNames() {
		return (NAME_CACHE == null
				|| NAME_CACHE.isEmpty())
				&& TardisUtil.getServer() != null;
	}

	private static void loadNames() {
		if (TardisUtil.getServer() == null) return;
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
		}
	}

	public Date getCreationDate() {
		if (this.findTardis().isEmpty()) return Date.from(Instant.now());

		Tardis tardis=  this.findTardis().get();

		if (PropertiesHandler.get(tardis.getHandlers().getProperties(), DATE_KEY) == null) {
			AITMod.LOGGER.error(tardis.getUuid().toString() + " was missing creation date! Resetting to now");
			markCreationDate();
		}

		String date = PropertiesHandler.getString(tardis.getHandlers().getProperties(), DATE_KEY);

		try {
			return DateFormat.getDateTimeInstance(DateFormat.LONG, 3).parse(date);
		} catch (Exception e) {
			AITMod.LOGGER.error("Failed to parse date from " + date);

			this.markCreationDate();

			return Date.from(Instant.now());
		}
	}

	public String getCreationString() {
		return DateFormat.getDateTimeInstance(DateFormat.LONG, 3).format(this.getCreationDate());
	}

	public void markCreationDate() {
		if (findTardis().isEmpty()) return;
		PropertiesHandler.set(findTardis().get().getHandlers().getProperties(), DATE_KEY,
				DateFormat.getDateTimeInstance(DateFormat.LONG, 3).format(Date.from(Instant.now())));
	}
}
