package loqor.ait.core;

import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.bind.KeyBind;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.core.item.KeyItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.PropertyTestHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AITKeyBinds {

    private static final List<KeyBind> BINDS = new ArrayList<>();

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (KeyBind bind : BINDS)
                bind.tick(client);
        });

        register(new KeyBind.Held("open", "snap", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, client -> {
            ClientPlayerEntity player = client.player;

            if (player == null)
                return;

            if (player.getVehicle() instanceof TardisRealEntity entity) {
                ClientTardisUtil.snapToOpenDoors(entity.getTardisID());
                return;
            }

            Collection<ItemStack> keys = KeyItem.getKeysInInventory(player);

            for (ItemStack stack : keys) {
                if (stack.getItem() instanceof KeyItem key && key.hasProtocol(KeyItem.Protocols.SNAP)) {
                    Tardis tardis = KeyItem.getTardis(player.getWorld(), stack);

                    if (tardis == null)
                        return;

                    ClientTardisUtil.snapToOpenDoors(tardis);
                }
            }
        }));

        register(new KeyBind("test", "tardis", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, client -> {
            ClientPlayerEntity player = client.player;

            if (player == null)
                return;

            Collection<ItemStack> keys = KeyItem.getKeysInInventory(player);

            for (ItemStack stack : keys) {
                if (stack.getItem() instanceof KeyItem key && key.hasProtocol(KeyItem.Protocols.SNAP)) {
                    Tardis tardis = KeyItem.getTardis(player.getWorld(), stack);

                    if (tardis == null)
                        return;

                    PropertyTestHandler handler = tardis.handler(TardisComponent.Id.TESTING);
                    player.sendMessage(Text.literal("Value: " + handler.getBool().get()));
                }
            }
        }));
    }

    private static void register(KeyBind bind) {
        bind.register();
        BINDS.add(bind);
    }
}
