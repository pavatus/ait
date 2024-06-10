package loqor.ait.tardis.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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
public class StatsData extends KeyedTardisComponent {

	private static final Identifier NAME_PATH = new Identifier(AITMod.MOD_ID, "tardis_names.json");
	private static List<String> NAME_CACHE;

	private static final String NAME_KEY = "permission";
	private static final String PLAYER_CREATOR_NAME_KEY = "player_creator_name";
	private static final String DATE_KEY = "date";

	private static final BoolProperty USE_SKYBOX = new BoolProperty("use_skybox", true);
	private static final Property<RegistryKey<World>> SKYBOX = Property.of(Property.Type.WORLD_KEY, "skybox", AITDimensions.TARDIS_DIM_WORLD);

	private final Value<RegistryKey<World>> skybox = SKYBOX.create(this);

	public StatsData() {
		super(Id.STATS);
	}

	@Override
	public void onCreate() {
		this.markCreationDate();
		this.setName(StatsData.getRandomName());
	}

	@Override
	public void onLoaded() {
		skybox.of(this, SKYBOX);
	}

	public Value<RegistryKey<World>> skybox() {
		return skybox;
	}

	public String getName() {
		String name = (String) PropertiesHandler.get(tardis().properties(), NAME_KEY);

		if (name == null) {
			name = getRandomName();
			this.setName(name);
		}

		return name;
	}

	public String getPlayerCreatorName() {
		String name = (String) PropertiesHandler.get(tardis().properties(), PLAYER_CREATOR_NAME_KEY);

		if (name == null) {
			name = getRandomName();
			this.setPlayerCreatorName(name);
		}

		return name;
	}


	public void setName(String name) {
		PropertiesHandler.set(tardis(), NAME_KEY, name);
	}

	public void setPlayerCreatorName(String name) {
		PropertiesHandler.set(tardis(), PLAYER_CREATOR_NAME_KEY, name);
	}

	public static String getRandomName() {
		if (StatsData.shouldGenerateNames())
			StatsData.loadNames();

		if (NAME_CACHE == null)
			return "";

		return NAME_CACHE.get(AITMod.RANDOM.nextInt(NAME_CACHE.size()));
	}

	public static boolean shouldGenerateNames() {
		return (NAME_CACHE == null
				|| NAME_CACHE.isEmpty());
	}

	private static void loadNames() {
		if (NAME_CACHE == null) 
			NAME_CACHE = new ArrayList<>();

		NAME_CACHE.clear();

		try {
			Optional<Resource> resource = TardisUtil.getServerResourceManager().getResource(NAME_PATH);

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
			AITMod.LOGGER.error("ERROR in tardis_names.json", e);
		}
	}

	public Date getCreationDate() {
		Tardis tardis = this.tardis();

		if (PropertiesHandler.get(tardis.properties(), DATE_KEY) == null) {
			AITMod.LOGGER.error(tardis.getUuid().toString() + " was missing creation date! Resetting to now");
			markCreationDate();
		}

		String date = PropertiesHandler.getString(tardis.properties(), DATE_KEY);

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
		PropertiesHandler.set(tardis().properties(), DATE_KEY,
				DateFormat.getDateTimeInstance(DateFormat.LONG, 3).format(Date.from(Instant.now())));
	}

	public void markPlayerCreatorName() {
		PropertiesHandler.set(tardis().properties(), PLAYER_CREATOR_NAME_KEY, this.getPlayerCreatorName());
	}
}
