package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;

import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Random;

public class OvergrownData extends TardisComponent implements TardisTickable {
	public static final String IS_OVERGROWN = "overgrown";
	public static final int MAXIMUM_TICKS = 600;
	public static String TEXTURE_PATH = "textures/blockentities/exteriors/";
	private static Random random;
	private int ticks; // same as usual

	public OvergrownData() {
		super(Id.OVERGROWN);
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
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), IS_OVERGROWN);
	}

	public void setOvergrown(boolean var) {
		PropertiesHandler.set(this.tardis(), IS_OVERGROWN, var);
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

		if (this.isOvergrown() && (this.tardis().travel().getState() == TardisTravel.State.FLIGHT || this.tardis().travel().getState() == TardisTravel.State.MAT)) {
			this.setOvergrown(false);
			this.setTicks(0);
			return;
		}

		//if (!this.getExteriorPos().getWorld().getBiome(this.getTardis().get().getTravel().getPosition()).isIn(BiomeTags.IS_FOREST)) return;

		if (this.isOvergrown() || this.tardis().travel().getState() != TardisTravel.State.LANDED)
			return;

		// We know the tardis is landed so we can start ticking away
		if (this.hasReachedMaxTicks())
			return;

		if (random().nextFloat() < 0.025f)
			this.addTick();
	}
}
