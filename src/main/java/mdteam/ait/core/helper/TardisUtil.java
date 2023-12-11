package mdteam.ait.core.helper;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.entities.control.impl.pos.PosType;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.Corners;
import mdteam.ait.datagen.datagen_providers.AITLanguageProvider;
import mdteam.ait.tardis.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.model.CubeFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static javax.management.timer.Timer.ONE_SECOND;

@SuppressWarnings("unused")
public class TardisUtil {

    private static final Random RANDOM = new Random();
    private static MinecraftServer SERVER; //@TODO fixme this does not work on multiplayer.
    private static ServerWorld TARDIS_DIMENSION;
    public static final Identifier CHANGE_EXTERIOR = new Identifier(AITMod.MOD_ID, "change_exterior");

    public static void init() {
        ServerWorldEvents.UNLOAD.register((server, world) -> {
            SERVER = server;
            TARDIS_DIMENSION = server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SERVER = server;
            TARDIS_DIMENSION = server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            SERVER = null;
        });
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_EXTERIOR,
                (server, player, handler, buf, responseSender) -> {
                    UUID uuid = buf.readUuid();

                    ExteriorEnum[] values = ExteriorEnum.values();
                    int nextIndex = (ServerTardisManager.getInstance().getTardis(uuid).getExterior().getType().ordinal() + 1) % values.length;
                    ServerTardisManager.getInstance().getTardis(uuid).getExterior().setType(values[nextIndex]);
                    WorldOps.updateIfOnServer(server.getWorld(ServerTardisManager.getInstance().getTardis(uuid)
                                    .getTravel().getPosition().getWorld().getRegistryKey()),
                            ServerTardisManager.getInstance().getTardis(uuid).getTravel().getPosition());
                }
        );
    }

    public static void changeExteriorWithScreen(UUID uuid) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(uuid);
        ClientPlayNetworking.send(CHANGE_EXTERIOR, buf);
    }

    public static MinecraftServer getServer() {
        //MinecraftServer server = TARDIS_DIMENSION.getServer();
        return SERVER;
    }

    public static boolean isClient() {
        return !TardisUtil.isServer();
    }

    public static boolean isServer() {
        return SERVER != null;
    }

    public static ServerWorld getTardisDimension() {
        return TARDIS_DIMENSION;
    }

    public static boolean inBox(Box box, BlockPos pos) {
        return pos.getX() <= box.maxX && pos.getX() >= box.minX &&
                pos.getZ() <= box.maxZ && pos.getZ() >= box.minZ;
    }

    public static boolean inBox(Corners corners, BlockPos pos) {
        return inBox(corners.getBox(), pos);
    }

    public static DoorBlockEntity getDoor(Tardis tardis) {
        if (!(TardisUtil.getTardisDimension().getBlockEntity(tardis.getDesktop().getInteriorDoorPos()) instanceof DoorBlockEntity door))
            return null;

        return door;
    }

    public static ExteriorBlockEntity getExterior(Tardis tardis) {
        if (!(tardis.getTravel().getPosition().getBlockEntity() instanceof ExteriorBlockEntity exterior))
            return null;

        return exterior;
    }

    public static Corners findInteriorSpot() {
        BlockPos first = findRandomPlace();

        return new Corners(
                first, first.add(256, 0, 256)
        );
    }

    public static BlockPos findRandomPlace() {
        return new BlockPos(RANDOM.nextInt(100000), 0, RANDOM.nextInt(100000));
    }

    public static BlockPos findBlockInTemplate(StructureTemplate template, BlockPos pos, Direction direction, Block targetBlock) {
        List<StructureTemplate.StructureBlockInfo> list = template.getInfosForBlock(
                pos, new StructurePlacementData().setRotation(
                        TardisUtil.directionToRotation(direction)
                ), targetBlock
        );

        if (list.isEmpty())
            return null;

        return list.get(0).pos();
    }

    public static BlockRotation directionToRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> BlockRotation.CLOCKWISE_180;
            case EAST -> BlockRotation.COUNTERCLOCKWISE_90;
            case WEST -> BlockRotation.CLOCKWISE_90;
            default -> BlockRotation.NONE;
        };
    }

    public static BlockPos offsetInteriorDoorPosition(Tardis tardis) {
        return TardisUtil.offsetInteriorDoorPosition(tardis.getDesktop());
    }

    public static BlockPos offsetInteriorDoorPosition(TardisDesktop desktop) {
        return TardisUtil.offsetDoorPosition(desktop.getInteriorDoorPos());
    }

    public static BlockPos offsetExteriorDoorPosition(Tardis tardis) {
        return TardisUtil.offsetExteriorDoorPosition(tardis.getTravel());
    }

    public static BlockPos offsetExteriorDoorPosition(TardisTravel travel) {
        return TardisUtil.offsetDoorPosition(travel.getPosition());
    }

    public static BlockPos offsetDoorPosition(AbsoluteBlockPos.Directed pos) {
        return switch (pos.getDirection()) {
            case DOWN, UP -> throw new IllegalArgumentException("Cannot adjust door position with direction: " + pos.getDirection());
            case NORTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() - 1);
            case SOUTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() + 1);
            case EAST -> new BlockPos.Mutable(pos.getX() + 1, pos.getY(), pos.getZ() + 0.5);
            case WEST -> new BlockPos.Mutable(pos.getX() - 1, pos.getY(), pos.getZ() + 0.5);
        };
    }

    public static void teleportOutside(Tardis tardis, ServerPlayerEntity player) {
        TardisUtil.teleportWithDoorOffset(player, tardis.getTravel().getPosition());
    }

    public static void teleportInside(Tardis tardis, ServerPlayerEntity player) {
        TardisUtil.teleportWithDoorOffset(player, tardis.getDesktop().getInteriorDoorPos());
    }

    private static void teleportWithDoorOffset(ServerPlayerEntity player, AbsoluteBlockPos.Directed pos) {
        Vec3d vec = TardisUtil.offsetDoorPosition(pos).toCenterPos();
        SERVER.execute(() -> {WorldOps.teleportToWorld(player, (ServerWorld) pos.getWorld(), vec, pos.getDirection().asRotation(), player.getPitch());
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player)); });
    }

    public static Tardis findTardisByInterior(BlockPos pos) {
        for (Tardis tardis : TardisManager.getInstance().getLookup().values()) {
            if (TardisUtil.inBox(tardis.getDesktop().getCorners(), pos))
                return tardis;
        }

        return null;
    }

    public static Tardis findTardisByPosition(AbsoluteBlockPos.Directed pos) {
        for (Tardis tardis : TardisManager.getInstance().getLookup().values()) {
            if (tardis.getTravel().getPosition() != pos) continue;

            return tardis;
        }

        return null;
    }

    @Nullable
    public static PlayerEntity getPlayerInsideInterior(Tardis tardis) {
        return getPlayerInsideInterior(tardis.getDesktop().getCorners());
    }

    @Nullable
    public static PlayerEntity getPlayerInsideInterior(Corners corners) {
        for (PlayerEntity player : TardisUtil.getTardisDimension().getPlayers()) {
            if (TardisUtil.inBox(corners, player.getBlockPos()))
                return player;
        }

        return null;
    }

    public static List<PlayerEntity> getPlayersInInterior(Tardis tardis) {
        return getPlayersInInterior(tardis);
    }

    @Nullable
    public static List<PlayerEntity> getPlayersInInterior(Corners corners) {
        List<PlayerEntity> list = List.of();

        for (PlayerEntity player : TardisUtil.getTardisDimension().getPlayers()) {
            if (inBox(corners, player.getBlockPos())) list.add(player);
        }

        return list;
    }

    public static ServerWorld findWorld(RegistryKey<World> key) {
        return TardisUtil.getServer().getWorld(key);
    }

    public static ServerWorld findWorld(Identifier identifier) {
        return TardisUtil.findWorld(RegistryKey.of(RegistryKeys.WORLD, identifier));
    }

    public static ServerWorld findWorld(String identifier) {
        return TardisUtil.findWorld(new Identifier(identifier));
    }

    @Nullable
    public static ExteriorBlockEntity findExteriorEntity(Tardis tardis) {
        if(tardis.getTravel().getPosition().getWorld().isClient())
            return null;
        if (tardis.getTravel().getPosition().getWorld() == null)
            return null;

        return (ExteriorBlockEntity) tardis.getTravel().getPosition().getWorld().getBlockEntity(tardis.getTravel().getPosition());
    }

    public static BlockPos addRandomAmount(PosType type, BlockPos pos, int limit, Random random) {
        return type.add(pos, random.nextInt(limit));
    }

    public static BlockPos getRandomPos(Corners corners, Random random) {
        BlockPos temp;

        temp = addRandomAmount(PosType.X, corners.getFirst(), corners.getSecond().getX() - corners.getFirst().getX(), random);
        temp = addRandomAmount(PosType.Y, temp, 0 + corners.getSecond().getY(), random);
        temp = addRandomAmount(PosType.Z, temp, corners.getSecond().getZ() - corners.getFirst().getZ(), random);

        return temp;
    }

    public static BlockPos getRandomPosInWholeInterior(Tardis tardis, Random random) {
        return getRandomPos(tardis.getDesktop().getCorners(), random);
    }
    public static BlockPos getRandomPosInWholeInterior(Tardis tardis) {
        return getRandomPosInWholeInterior(tardis, new Random());
    }
    public static BlockPos getRandomPosInPlacedInterior(Tardis tardis, Random random) {
        return getRandomPos(getPlacedInteriorCorners(tardis), random);
    }
    public static BlockPos getRandomPosInPlacedInterior(Tardis tardis) {
        return getRandomPosInPlacedInterior(tardis, new Random());
    }
    public static Corners getPlacedInteriorCorners(Tardis tardis) {
        BlockPos centre = BlockPos.ofFloored(tardis.getDesktop().getCorners().getBox().getCenter());
        BlockPos first,second;

        if (!tardis.getDesktop().getSchema().findTemplate().isPresent()) {
            AITMod.LOGGER.warn("Could not get desktop schema! Using whole interior instead.");
            return tardis.getDesktop().getCorners();
        }

        Vec3i size = tardis.getDesktop().getSchema().findTemplate().get().getSize();

        first = PosType.X.add(centre, -size.getX()/2);
        first = PosType.Z.add(first, -size.getZ()/2);

        second = PosType.X.add(centre, size.getX()/2);
        second = PosType.Y.add(second, size.getY());
        second = PosType.Z.add(second, size.getZ()/2);

        Corners corners = new Corners(first, second);

        return corners;
    }
    public static BlockPos getPlacedInteriorCentre(Tardis tardis) {
        Corners corners = getPlacedInteriorCorners(tardis);

        if (!tardis.getDesktop().getSchema().findTemplate().isPresent()) {
            AITMod.LOGGER.warn("Could not get desktop schema! Returning bad centre instead.");
            return BlockPos.ofFloored(corners.getBox().getCenter());
        }

        Vec3i size = tardis.getDesktop().getSchema().findTemplate().get().getSize();

        return corners.getFirst().add(size.getX(),size.getY()/2,size.getZ());
    }
}