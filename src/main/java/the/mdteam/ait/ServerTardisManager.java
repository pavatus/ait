package the.mdteam.ait;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITardisManager;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class ServerTardisManager extends TardisManager {

    private static final ServerTardisManager instance = new ServerTardisManager();
    public static final Identifier SEND = new Identifier("ait", "send_tardis");

    private ServerTardisManager() {
        ServerPlayNetworking.registerGlobalReceiver(
                ClientTardisManager.ASK, (server, player, handler, buf, responseSender) -> this.sync(buf.readUuid())
        );
    }

    @Override
    public ITardis findTardisByExterior(BlockPos pos) {
        for (ITardis tardis : this.lookup.values()) {
            if (tardis.getTravel().getPosition().equals(pos))
                return tardis;
        }

        return null;
    }

    @Override
    public ITardis findTardisByInterior(BlockPos pos) {
        for (ITardis tardis : this.lookup.values()) {
            if (TardisUtil.inBox(tardis.getDesktop().getCorners(), pos))
                return tardis;
        }

        return null;
    }

    @Override
    public ITardis create(AbsoluteBlockPos.Directed pos, ExteriorEnum exteriorType, IDesktopSchema desktopSchema) {
        UUID uuid = UUID.randomUUID();
        ITardis tardis = new Tardis(uuid, pos, desktopSchema, exteriorType);
        this.lookup.put(uuid, tardis);

        tardis.getTravel().placeExterior();
        return tardis;
    }

    /**
     * Loads the tardis from a file and returns it, or, returns the cached instance.
     *
     * @param uuid uuid of the tardis
     * @return the tardis or null if no tardis file was found
     */
    @Override
    public ITardis loadTardis(UUID uuid) {
        return this.loadTardis(ServerTardisManager.getSavePath(uuid));
    }

    @Override
    public ITardis loadTardis(File file) {
        file.getParentFile().mkdirs();

        try {
            if (!file.exists())
                throw new IOException("Tardis file " + file + " doesn't exist!");

            String json = Files.readString(file.toPath());
            ITardis tardis = this.gson.fromJson(json, Tardis.class);
            this.lookup.put(tardis.getUuid(), tardis);

            this.sync(tardis.getUuid(), json);

            AITMod.LOGGER.info("Loaded TARDIS: {}", tardis.getUuid());
            AITMod.LOGGER.info("Schema ID: {}", tardis.getDesktop().getSchema().id());
            AITMod.LOGGER.info("Corners: {}", tardis.getDesktop().getCorners());
            AITMod.LOGGER.info("Door Pos: {}", tardis.getDesktop().getInteriorDoorPos());
            AITMod.LOGGER.info("Exterior Type: {}", tardis.getExteriorType());
            AITMod.LOGGER.info("Travel state: {}", tardis.getTravel().getState());
            AITMod.LOGGER.info("Pos: {}", tardis.getTravel().getPosition());
            AITMod.LOGGER.info("Destination: {}", tardis.getTravel().getDestination());

            return tardis;
        } catch (IOException e) {
            AITMod.LOGGER.warn("Failed to load tardis with uuid {}!", file);
            AITMod.LOGGER.warn(e.getMessage());
        }

        return null;
    }

    @Override
    public void loadTardis() {
        for (File file : ServerTardisManager.getSavePath().listFiles()) {
            this.loadTardis(file);
        }
    }

    @Override
    public void saveTardis(ITardis tardis) {
        File savePath = ServerTardisManager.getSavePath(tardis);
        savePath.getParentFile().mkdirs();

        try {
            Files.writeString(savePath.toPath(), this.gson.toJson(tardis, Tardis.class));
        } catch (IOException e) {
            AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
            AITMod.LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void saveTardis() {
        for (ITardis tardis : this.lookup.values()) {
            this.saveTardis(tardis);
        }
    }

    private void sync(UUID uuid) {
        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            this.sync(player, uuid);
        }
    }

    private void sync(ServerPlayerEntity player, UUID uuid) {
        this.sync(player, uuid, this.gson.toJson(this.lookup.get(uuid), Tardis.class));
    }

    private void sync(UUID uuid, String json) {
        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            this.sync(player, uuid, json);
        }
    }

    private void sync(ServerPlayerEntity player, UUID uuid, String json) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);
        data.writeString(json);

        ServerPlayNetworking.send(player, SEND, data);
    }

    public static ITardisManager getInstance() {
        return instance;
    }

    private static File getSavePath(UUID uuid) {
        // TODO: maybe, make WorldSavePath.AIT?
        return new File(TardisUtil.getServer().getSavePath(WorldSavePath.ROOT) + "ait/" + uuid + ".json");
    }

    private static File getSavePath(ITardis tardis) {
        return ServerTardisManager.getSavePath(tardis.getUuid());
    }

    private static File getSavePath() {
        return new File(TardisUtil.getServer().getSavePath(WorldSavePath.ROOT) + "ait/");
    }
}
