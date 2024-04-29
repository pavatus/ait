package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.tardis.TardisTravel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Random;

public class OvergrownData extends TardisLink {
	public static final String IS_OVERGROWN = "overgrown";
	public static final String OVERGROWN_TICKS = "overgrown_ticks";
	public static final int MAXIMUM_TICKS = 600;
	public static String TEXTURE_PATH = "textures/blockentities/exteriors/";
	private static Random random;
	private int ticks; // same as usual

	public OvergrownData(Tardis tardis) {
		super(tardis, TypeId.OVERGROWN);
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
		if (findTardis().isEmpty()) return false;
		return PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), IS_OVERGROWN);
	}

	public void setOvergrown(boolean var) {
		if (findTardis().isEmpty()) return;
		PropertiesHandler.set(this.findTardis().get(), IS_OVERGROWN, var);
	}

	public void removeVegetation() {
		this.setOvergrown(false);
		this.setTicks(0);
	}

	public Identifier getOvergrownTexture() {
		if (findTardis().isEmpty()) return null;
		ExteriorCategorySchema exterior = this.findTardis().get().getExterior().getCategory();

		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + exterior.toString().toLowerCase() + "/" + exterior.toString().toLowerCase() + "_" + "overgrown" + ".png");
	}

	public static Random random() {
		if (random == null)
			random = new Random();

		return random;
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);
		if (findTardis().isEmpty()) return;

		if (findTardis().get().isGrowth()) return;

		if (this.isOvergrown() && (this.findTardis().get().getTravel().getState() == TardisTravel.State.FLIGHT || this.findTardis().get().getTravel().getState() == TardisTravel.State.MAT)) {
			this.setOvergrown(false);
			this.setTicks(0);
			return;
		}

		//if (!this.getExteriorPos().getWorld().getBiome(this.getTardis().get().getTravel().getPosition()).isIn(BiomeTags.IS_FOREST)) return;

		if (this.isOvergrown() || this.findTardis().get().getTravel().getState() != TardisTravel.State.LANDED) return;

		// We know the tardis is landed so we can start ticking away
		if (hasReachedMaxTicks()) {
			// this.setOvergrown(true);
			// this.setTicks(0);
			// this.getTardis().get().getDoor().closeDoors();
			return;
		}

		if (random().nextFloat() < 0.025f) {
			this.addTick();
		}
	}
}
