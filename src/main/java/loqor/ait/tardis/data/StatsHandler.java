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
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
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

public class StatsHandler extends KeyedTardisComponent {

	private static final Identifier NAME_PATH = new Identifier(AITMod.MOD_ID, "tardis_names.json");
	private static List<String> NAME_CACHE;
	private static final Property<String> NAME_PROPERTY = new Property<>(Property.Type.STR, "name", "");
	private final Value<String> tardisName = NAME_PROPERTY.create(this);
	private static final Property<String> PLAYER_CREATOR_NAME_PROPERTY = new Property<>(Property.Type.STR, "player_creator_name", "");
	private final Value<String> playerCreatorName = PLAYER_CREATOR_NAME_PROPERTY.create(this);
	private static final Property<String> DATE = new Property<>(Property.Type.STR, "date", "");
	private final Value<String> creationDate = DATE.create(this);
    private static final Property<RegistryKey<World>> SKYBOX = new Property<>(Property.Type.WORLD_KEY, "skybox", AITDimensions.TARDIS_DIM_WORLD);
	private static final Property<HashSet<String>> UNLOCKS = new Property<>(Property.Type.STR_SET, "unlocks", new HashSet<>());
	private final Value<RegistryKey<World>> skybox = SKYBOX.create(this);
	private final Value<HashSet<String>> unlocks = UNLOCKS.create(this);
	private static final BoolProperty SECURITY = new BoolProperty("security", false);
	private final BoolValue security = SECURITY.create(this);
	private static final BoolProperty HAIL_MARY = new BoolProperty("hail_mary", false);
	private final BoolValue hailMary = HAIL_MARY.create(this);

	public StatsHandler() {
		super(Id.STATS);
	}

	@Override
	public void onCreate() {
		this.markCreationDate();
		this.setName(StatsHandler.getRandomName());
	}

	@Override
	public void onLoaded() {
		skybox.of(this, SKYBOX);
		unlocks.of(this, UNLOCKS);
		tardisName.of(this, NAME_PROPERTY);
		playerCreatorName.of(this, PLAYER_CREATOR_NAME_PROPERTY);
		creationDate.of(this, DATE);
		security.of(this, SECURITY);
		hailMary.of(this, HAIL_MARY);

		for (Iterator<TardisDesktopSchema> it = DesktopRegistry.getInstance().iterator(); it.hasNext(); ) {
			this.unlock(it.next(), false);
		}
	}

	public boolean isUnlocked(Unlockable unlockable) {
		return this.unlocks.get().contains(unlockable.id().toString());
	}

	public void unlock(Unlockable unlockable) {
		this.unlock(unlockable, true);
	}

	private void unlock(Unlockable unlockable, boolean sync) {
		// TODO implement native v2 collection properties to avoid this
		this.unlocks.flatMap(strings -> {
			strings.add(unlockable.id().toString());
			return strings;
		}, sync);
	}

	public Value<RegistryKey<World>> skybox() {
		return skybox;
	}

	public String getName() {
		String name = tardisName.get();

		if (name == null) {
			name = getRandomName();
			this.setName(name);
		}

		return name;
	}

	public BoolValue security() {
		return security;
	}

	public BoolValue hailMary() {
		return hailMary;
	}

	public String getPlayerCreatorName() {
		String name = tardisName.get();

		if (name == null) {
			name = getRandomName();
			this.setPlayerCreatorName(name);
		}

		return name;
	}


	public void setName(String name) {
		tardisName.set(name);
	}

	public void setPlayerCreatorName(String name) {
		playerCreatorName.set(name);
	}

	public static String getRandomName() {
		if (StatsHandler.shouldGenerateNames())
			StatsHandler.loadNames();

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

		if (creationDate.get() == null) {
			AITMod.LOGGER.error(tardis.getUuid().toString() + " was missing creation date! Resetting to now");
			markCreationDate();
		}

		String date = creationDate.get();

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
		creationDate.set(DateFormat.getDateTimeInstance(DateFormat.LONG, 3).format(Date.from(Instant.now())));
	}

	public void markPlayerCreatorName() {
		playerCreatorName.set(this.getPlayerCreatorName());
	}
}
