package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Random;

public class OvergrownHandler extends KeyedTardisComponent implements TardisTickable {
	private static final BoolProperty IS_OVERGROWN_PROPERTY = new BoolProperty("is_overgrown", false);
	private final BoolValue overgrown = IS_OVERGROWN_PROPERTY.create(this);
	@Exclude
	public static final int MAXIMUM_TICKS = 600;
	public static String TEXTURE_PATH = "textures/blockentities/exteriors/";
	private static Random random;
	private int ticks; // same as usual

	public OvergrownHandler() {
		super(Id.OVERGROWN);
	}

	@Override
	public void onLoaded() {
		overgrown.of(this, IS_OVERGROWN_PROPERTY);
	}

	public int getTicks() {
		return this.ticks;
	}

	private void setTicks(int ticks) {
		this.ticks = ticks;
	}

	private void addTick() {
		this.setTicks(this.getTicks() + 1);
	}

	private boolean hasReachedMaxTicks() {
		return this.getTicks() >= MAXIMUM_TICKS;
	}

	public boolean isOvergrown() {
		return overgrown.get();
	}

	public void setOvergrown(boolean var) {
		overgrown.set(var);
	}

	public void removeVegetation() {
		this.setOvergrown(false);
		this.setTicks(0);
	}

	public Identifier getOvergrownTexture() {
		ExteriorCategorySchema exterior = this.tardis().getExterior().getCategory();

		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + exterior.toString().toLowerCase() + "/" + exterior.toString().toLowerCase() + "_" + "overgrown" + ".png");
	}

	public static Random random() {
		if (random == null)
			random = new Random();

		return random;
	}

	@Override
	public void tick(MinecraftServer server) {

		if (tardis().isGrowth())
			return;

		if (this.isOvergrown() && (this.tardis().travel().getState() == TravelHandlerBase.State.FLIGHT
				|| this.tardis().travel().getState() == TravelHandlerBase.State.MAT)) {
			this.setOvergrown(false);
			this.setTicks(0);
			return;
		}

		if (this.isOvergrown() || this.tardis().travel().getState() != TravelHandlerBase.State.LANDED)
			return;

		// We know the tardis is landed so we can start ticking away
		if (this.hasReachedMaxTicks())
			return;

		if (random().nextFloat() < 0.025f)
			this.addTick();
	}
}
