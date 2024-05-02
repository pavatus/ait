package loqor.ait.core.util.vortex.server;

import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.util.bsp.BTreeGenerator;
import loqor.ait.core.util.bsp.BTreeInorderIterator;
import loqor.ait.core.util.bsp.BinaryTree;
import loqor.ait.core.util.vortex.VortexData;
import loqor.ait.core.util.vortex.VortexDataHelper;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class ServerVortexDataHelper {


    /*
        Returns true if the server has generated vortex data, false if otherwise.
     */
    public static boolean isVortexDataPresent(MinecraftServer server) {
        try {
            return getVortexDataPath(server).toFile().exists();
        } catch (IOException e) {
            AITMod.LOGGER.warn("Failed to create vortex data path!");
            AITMod.LOGGER.warn(e.getMessage());
        }
        return false;
    }

    /*
        Returns the path to a world save stored vortex data file.
     */
    public static Path getVortexDataPath(MinecraftServer server) throws IOException {
        Path result = ServerTardisManager.getRootSavePath(server)
                .resolve(VortexDataHelper.VORTEX_DATA_SERVER_CACHE_PATH);
        Files.createDirectories(result.getParent());

        return result;
    }

    /*
        Returns a VortexData object stored in the world save directory. Must
        be only called by the server.
     */
    public static VortexData getVortexData(MinecraftServer server) {
        try {
            return VortexDataHelper.readVortexData(getVortexDataPath(server));
        } catch (IOException e) {
            AITMod.LOGGER.error("ServerVortexDataHelper: Unable to read vortex data");
        }
        return null;
    }

    /*
        Stores a server-generated vortex data.
     */
    public static void storeVortexData(MinecraftServer server, VortexData data) {
        try {
            VortexDataHelper.storeVortexData(getVortexDataPath(server), data);
        } catch (IOException e) {
            AITMod.LOGGER.error("ServerVortexDataHelper: Unable to store vortex data");
        }
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

    /*
        Registers a callback for SERVER_STARTED event to check if the vortex data is present,
        and if not, generates and stores it.
     */
    public static void subscribeDataGenerator() {
        ServerLifecycleEvents.SERVER_STARTED.register((server -> {
            if (isVortexDataPresent(server)) {
                AITMod.LOGGER.info("ServerVortexDataHelper: Found VortexData present in server world directory");
                return;
            }
            VortexData data = generateData(server);
            storeVortexData(server, data);
        }));
    }
}
