package loqor.ait.tardis.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
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
import java.util.*;

// is StatsData a good name for this class?
public class StatsData extends KeyedTardisComponent {

	private static final Identifier NAME_PATH = new Identifier(AITMod.MOD_ID, "tardis_names.json");
	private static List<String> NAME_CACHE;

	private static final String NAME_KEY = "name";
	private static final String PLAYER_CREATOR_NAME_KEY = "player_creator_name";
	private static final String DATE_KEY = "date";

    private static final Property<RegistryKey<World>> SKYBOX = new Property<>(Property.Type.WORLD_KEY, "skybox", AITDimensions.TARDIS_DIM_WORLD);
	private static final Property<HashSet<String>> UNLOCKS = new Property<>(Property.Type.STR_SET, "unlocks", new HashSet<>());

	private final Value<RegistryKey<World>> skybox = SKYBOX.create(this);
	private final Value<HashSet<String>> unlocks = UNLOCKS.create(this);

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
		unlocks.of(this, UNLOCKS);

		for (Iterator<TardisDesktopSchema> it = DesktopRegistry.getInstance().iterator(); it.hasNext(); ) {
			this.unlock(it.next());
		}
	}

	public boolean isUnlocked(Unlockable unlockable) {
		return this.unlocks.get().contains(unlockable.id().toString());
	}

	public void unlock(Unlockable unlockable) {
		// TODO implement native v2 collection properties to avoid this
		this.unlocks.flatMap(strings -> {
			strings.add(unlockable.id().toString());
			return strings;
		});
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
