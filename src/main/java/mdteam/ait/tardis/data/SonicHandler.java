package mdteam.ait.tardis.data;

import mdteam.ait.api.tardis.ArtronHolderItem;
import mdteam.ait.core.item.SonicItem;
import mdteam.ait.core.item.WaypointItem;
import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.util.Waypoint;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;

public class SonicHandler extends TardisLink implements ArtronHolderItem {
    public static final String HAS_SONIC = "has_sonic";
    @Exclude // Excluded as gson cannot serialise this ( TODO this means sonic is NOT saved )
    private ItemStack current; // The current sonic in the slot
    public SonicHandler(Tardis tardis) {
        super(tardis, "sonic");
    }

    public boolean hasSonic() {
        if (this.findTardis().isEmpty()) return false;
        return PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), HAS_SONIC);
    }
    public void markHasSonic() {
        if (this.findTardis().isEmpty()) return;
        PropertiesHandler.set(this.findTardis().get(), HAS_SONIC, true);
    }
    private void clearSonicMark() {
        if (this.findTardis().isEmpty()) return;
        PropertiesHandler.set(this.findTardis().get(), HAS_SONIC, false);
    }

    /**
     * Sets the new sonic
     * @param var
     * @return The optional of the previous sonic
     */
    public Optional<ItemStack> set(ItemStack var, boolean spawnItem) {
        Optional<ItemStack> prev = Optional.ofNullable(this.current);
        // System.out.println(var);
        // System.out.println(this.current);
        this.current = var;

        if (spawnItem && prev.isPresent()) {
            this.spawnItem(prev.get());
        }

        return prev;
    }

    public ItemStack get() {
        return this.current;
    }
    public boolean isSonicNull() {
        return this.current == null;
    }
    public void clear(boolean spawnItem) {
        this.set(null, spawnItem);
    }
    public void spawnItem() {
        if (this.isSonicNull()) return;

        spawnItem(this.get());
        this.clear(false);
    }

    public void spawnItem(ItemStack sonic) {
        if (this.findTardis().isEmpty() || !this.hasSonic()) return;

        Tardis tardis = this.findTardis().get();

        if (tardis.getDesktop().findCurrentConsole().isEmpty()) return;

        spawnItem(sonic, tardis.getDesktop().findCurrentConsole().get().position());
        this.clearSonicMark();
    }
    public static ItemEntity spawnItem(ItemStack sonic, AbsoluteBlockPos pos) {
        ItemEntity entity = new ItemEntity(pos.getWorld(), pos.getX(), pos.getY(), pos.getZ(), sonic);
        pos.getWorld().spawnEntity(entity);
        return entity;
    }

    @Override
    public double getMaxFuel(ItemStack stack) {
        return SonicItem.MAX_FUEL;
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        if (!this.hasSonic() || this.isSonicNull()) return;

        ItemStack sonic = this.get();

        if (this.hasMaxFuel(sonic)) return;

        // Safe to get as ^ that method runs the check for us
        ServerTardis tardis = (ServerTardis) this.findTardis().get();

        if (!tardis.hasPower()) return;

        this.addFuel(1, sonic);
        tardis.getHandlers().getFuel().removeFuel(1);
    }
}
