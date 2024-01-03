package mdteam.ait.tardis.handler;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Random;
import java.util.UUID;

public class OvergrownHandler extends TardisLink {
    public static final String IS_OVERGROWN = "overgrown";
    public static final String OVERGROWN_TICKS = "overgrown_ticks";
    public static final int MAXIMUM_TICKS = 600;
    public static String TEXTURE_PATH = "textures/blockentities/exteriors/";
    private static Random random;

    public OvergrownHandler(UUID tardisId) {
        super(tardisId);
    }

    public int getTicks() {
        return PropertiesHandler.getInt(this.getLinkedTardis().getHandlers().getProperties(), OVERGROWN_TICKS);
    }
    private void setTicks(int ticks) {
        PropertiesHandler.set(this.getLinkedTardis().getHandlers().getProperties(), OVERGROWN_TICKS, ticks);
    }
    private void addTick() {
        this.setTicks(this.getTicks() + 1);
    }
    private boolean hasReachedMaxTicks() {
        return this.getTicks() >= MAXIMUM_TICKS;
    }

    public boolean isOvergrown() {
        return PropertiesHandler.getBool(this.getLinkedTardis().getHandlers().getProperties(), IS_OVERGROWN);
    }
    public void setOvergrown(boolean var) {
        PropertiesHandler.setBool(this.getLinkedTardis().getHandlers().getProperties(), IS_OVERGROWN, var);
        getLinkedTardis().markDirty();
    }
    public void removeVegetation() {
        this.setOvergrown(false);
        this.setTicks(0);
    }

    public Identifier getOvergrownTexture() {
        ExteriorSchema exterior = this.getLinkedTardis().getExterior().getType();

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

        if (getLinkedTardis().isGrowth()) return;

        if (this.isOvergrown() && (this.getLinkedTardis().getTravel().getState() == TardisTravel.State.FLIGHT || this.getLinkedTardis().getTravel().getState() == TardisTravel.State.MAT)) {
            this.setOvergrown(false);
            this.setTicks(0);
            return;
        }

        if (!this.getExteriorPos().getWorld().getBiome(this.getLinkedTardis().getTravel().getPosition()).isIn(BiomeTags.IS_FOREST)) return;

        if (this.isOvergrown() || this.getLinkedTardis().getTravel().getState() != TardisTravel.State.LANDED) return;

        // We know the tardis is landed so we can start ticking away
        if (hasReachedMaxTicks()) {
            this.setOvergrown(true);
            this.setTicks(0);
            this.getLinkedTardis().getDoor().closeDoors();
            return;
        }

        if (random().nextFloat() < 0.025f) {
            this.addTick();
        }
    }
}
