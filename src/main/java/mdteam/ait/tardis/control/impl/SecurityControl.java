package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.CloakData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.List;

public class SecurityControl extends Control {
    public static final String SECURITY_KEY = "security";

    public SecurityControl() {
        super("protocol_19");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        if (!hasMatchingKey(player, tardis)) {
            return false;
        }

        boolean security = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), SECURITY_KEY);

        PropertiesHandler.set(tardis, SECURITY_KEY, !security);

        security = !security;

        return true;
    }

    public static void runSecurityProtocols(Tardis tardis) {
        boolean security = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), SECURITY_KEY);

        if (!security) return;

        List<ServerPlayerEntity> forRemoval = new ArrayList<>();

        for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(tardis)) {
            if (!hasMatchingKey(player, tardis)) {
                forRemoval.add(player);
            }
        }

        for (ServerPlayerEntity player : forRemoval) {
            TardisUtil.teleportOutside(tardis, player);
        }
    }

    public static boolean hasMatchingKey(ServerPlayerEntity player, Tardis tardis) {
        ItemStack stack = KeyItem.getFirstKeyStackInInventory(player);

        if (stack == null) {
            return false;
        }

        Tardis found = KeyItem.getTardis(stack);

        if (found == null) {
            return false;
        }

        return found.equals(tardis);
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.BWEEP;
    }

    @Override
    public long getDelayLength() {
        return (long) (2.5 * 1000L);
    }

    @Override
    public boolean ignoresSecurity() {
        return true;
    }
}