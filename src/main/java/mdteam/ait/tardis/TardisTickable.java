package mdteam.ait.tardis;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

/**
 * An interface for something that can be ticked by a tardis
 * Make sure to add whatever it is that needs ticking to
 * {@link Tardis}
 */
public interface TardisTickable { // todo, actually use this class where its needed eg desktop, exterior, console, etc.
    void tick(MinecraftServer server);
    void tick(ServerWorld world);
}
