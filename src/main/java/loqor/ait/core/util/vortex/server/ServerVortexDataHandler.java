package loqor.ait.core.util.vortex.server;

import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.util.bsp.BTreeGenerator;
import loqor.ait.core.util.bsp.BinaryTree;
import loqor.ait.core.util.vortex.VortexData;
import loqor.ait.core.util.vortex.VortexDataHelper;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.nio.file.Path;

public class ServerVortexDataHandler {
    /*
        Registers a callback for receiving a VortexData object request from client. Must only
        be called by the server.
     */
    public static void init() {
        /*
        Registers a callback for SERVER_STARTED event to check if the vortex data is present,
        and if not, generates and stores it.
        */
        ServerLifecycleEvents.SERVER_STARTED.register((server -> {
            if (isVortexDataPresent(server)) {
                AITMod.LOGGER.info("ServerVortexDataHelper: Found VortexData present in server world directory");
                return;
            }

            VortexData data = generateData(server);
            storeVortexData(server, data);

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                sendSyncPacketTo(player);
            }
        }));

        //ServerPlayNetworking.registerGlobalReceiver(VortexDataHelper.REQUEST_SYNC_PACKET,
        //        (((server, player, handler, buf, responseSender) -> ServerVortexDataHandler.sendSyncPacketTo(player))));
    }

    /*
        Sends SYNC_PACKET to the client. Must be registered as a callback for
        ServerPlayConnectionEvents.JOIN. Caller shall be the server, or else
        the call to this function becomes obsolete.
     */
    private static void sendSyncPacketTo(PlayerEntity player) {
        if (player.getWorld().isClient()) {
            AITMod.LOGGER.error("Obsolete sendSyncPacketTo call from a client");
            return;
        }

        VortexData vortexData = getVortexData(player.getServer());
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBytes(vortexData.serialize());
        ServerPlayNetworking.send((ServerPlayerEntity) player, VortexDataHelper.SYNC_PACKET, buf);

        AITMod.LOGGER.info("SEND {} -> {}",
                VortexDataHelper.SYNC_PACKET,
                player.getName()
        );
    }

    /*
        Returns true if the server has generated vortex data, false if otherwise.
     */
    public static boolean isVortexDataPresent(MinecraftServer server) {
        return getVortexDataPath(server).toFile().exists();
    }

    /*
        Returns the path to a world save stored vortex data file.
     */
    public static Path getVortexDataPath(MinecraftServer server) {
        return ServerTardisManager.getRootSavePath(server)
                .resolve(VortexDataHelper.VORTEX_DATA_SERVER_CACHE_PATH);
    }

    /*
        Returns a VortexData object stored in the world save directory. Must
        be only called by the server.
     */
    public static VortexData getVortexData(MinecraftServer server) {
        return VortexDataHelper.readVortexData(getVortexDataPath(server));
    }

    /*
        Stores a server-generated vortex data.
     */
    public static void storeVortexData(MinecraftServer server, VortexData data) {
        VortexDataHelper.storeVortexData(getVortexDataPath(server), data);
    }

    /*
        Generates random data for the time vortex and returns a serialised result.
     */
    public static VortexData generateData(MinecraftServer server) {
        long seed = server.getWorld(AITDimensions.TIME_VORTEX_WORLD).getSeed();
        AITMod.LOGGER.info("ServerVortexDataHelper: Generating vortex data, seed {}", seed);

        BinaryTree vortexTree = new BinaryTree(new Vec3d(0, 100, 0));
        BTreeGenerator vortexTreeGenerator = new BTreeGenerator(server.getWorld(AITDimensions.TIME_VORTEX_WORLD));

        vortexTreeGenerator.gen(vortexTree);

        AITMod.LOGGER.info(
                "ServerVortexDataHelper: Server vortex data generated, total nodes: {}",
                BinaryTree.Node.getChildrenCount(vortexTree.getRootNode())
        );

        AITMod.LOGGER.info("ServerVortexDataHelper: Serialising vortex data");

        byte[] vortexDataBuffer = vortexTree.toByteArray();
        VortexData vortexData = VortexData.deserialize(vortexDataBuffer);

        AITMod.LOGGER.info("ServerVortexDataHelper: VortexData object received, tree serialised");
        return vortexData;
    }
}
