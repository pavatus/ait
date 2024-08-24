package loqor.ait.tardis.base;

import net.minecraft.server.MinecraftServer;

import loqor.ait.tardis.Tardis;

/**
 * An interface for something that can be ticked by a tardis Make sure to add
 * whatever it is that needs ticking to {@link Tardis}
 */
public interface TardisTickable {

    default void tick(MinecraftServer server) { }
}
