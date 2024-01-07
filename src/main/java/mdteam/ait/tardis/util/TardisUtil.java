package mdteam.ait.tardis.util;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.core.interfaces.RiftChunk;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisManager;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.impl.pos.PosType;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.FlightHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SuppressWarnings("unused")
public class TardisUtil {
    private static final Random RANDOM = new Random();
    private static MinecraftServer SERVER;
    private static ServerWorld TARDIS_DIMENSION;
    public static final Identifier CHANGE_EXTERIOR = new Identifier(AITMod.MOD_ID, "change_exterior");
    public static final Identifier SNAP = new Identifier(AITMod.MOD_ID, "snap");

    public static final Identifier FIND_PLAYER = new Identifier(AITMod.MOD_ID, "find_player");

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
                    Identifier exteriorValue = Identifier.tryParse(buf.readString());
                    boolean variantChange = buf.readBoolean();
                    String variantValue = buf.readString();
                    Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);

                    tardis.getExterior().setType(ExteriorRegistry.REGISTRY.get(exteriorValue));
                    WorldOps.updateIfOnServer(server.getWorld(tardis
                                    .getTravel().getPosition().getWorld().getRegistryKey()),
                            tardis.getDoor().getExteriorPos());
                    if (variantChange) {
                        tardis.getExterior().setVariant(ExteriorVariantRegistry.REGISTRY.get(Identifier.tryParse(variantValue)));
                        WorldOps.updateIfOnServer(server.getWorld(tardis
                                        .getTravel().getPosition().getWorld().getRegistryKey()),
                                tardis.getDoor().getExteriorPos());
                    }

                    if (tardis.isGrowth())
                        tardis.getHandlers().getInteriorChanger().queueInteriorChange(TardisItemBuilder.findRandomDesktop(tardis));

                    tardis.markDirty();

                    /*ExteriorEnum[] values = ExteriorEnum.values();
                    int nextIndex = (ServerTardisManager.getInstance().getTardis(uuid).getExterior().getType().ordinal() + 1) % values.length;
                    ServerTardisManager.getInstance().getTardis(uuid).getExterior().setType(values[nextIndex]);
                    WorldOps.updateIfOnServer(server.getWorld(ServerTardisManager.getInstance().getTardis(uuid)
                                    .getTravel().getPosition().getWorld().getRegistryKey()),
                            ServerTardisManager.getInstance().getTardis(uuid).getTravel().getPosition());*/
                }
        );
        ServerPlayNetworking.registerGlobalReceiver(SNAP, (server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);

            if (tardis.getHandlers().getOvergrownHandler().isOvergrown()) {
                return;
            }

            BlockPos doorPos = tardis.getDoor().getExteriorPos();
            BlockPos playerPos = player.getBlockPos();

            double distanceSquared = playerPos.getSquaredDistance(doorPos.getX(), doorPos.getY(), doorPos.getZ());
            boolean inBox = TardisUtil.inBox(tardis.getDesktop().getCorners().getBox(), playerPos);

            if (distanceSquared <= 200 || inBox) {
                if (!player.isSneaking()) {
                    if (!tardis.getDoor().locked()) {
                        if (tardis.getDoor().isOpen()) {
                            tardis.getDoor().closeDoors();
                        } else {
                            tardis.getDoor().openDoors();
                        }
                    }
                } else {
                    DoorHandler.toggleLock(tardis, player);
                }
                tardis.markDirty();
            }

            // Play sound only if the player is in the Tardis dimension and conditions are met
            if (player.getWorld().getRegistryKey() == TardisUtil.getTardisDimension().getRegistryKey() &&
                    (distanceSquared <= 200 || inBox)) {
                player.getWorld().playSound(null, playerPos, AITSounds.SNAP, SoundCategory.PLAYERS, 4f, 1f);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(FIND_PLAYER,
                (server, currentPlayer, handler, buf, responseSender) -> {
                    UUID tardisId = buf.readUuid();
                    UUID playerUuid = buf.readUuid();
                    Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisId);
                    ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(playerUuid);
                    if(tardis.getDesktop().getConsolePos() == null) return;
                    if(serverPlayer == null) {
                        TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK, SoundCategory.BLOCKS, 3f, 1f);
                        return;
                    }
                    tardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
                            serverPlayer.getBlockX(),
                                    serverPlayer.getBlockY(),
                                    serverPlayer.getBlockZ(),
                                    serverPlayer.getWorld(),
                                    serverPlayer.getMovementDirection()),
                            PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.AUTO_LAND));
                    TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 3f, 1f);
                    tardis.markDirty();
                }
        );
    }

    public static MinecraftServer getServer() {
        //MinecraftServer server = TARDIS_DIMENSION.getServer();
        return SERVER;
    }

    public static boolean isClient() {
        return !TardisUtil.isServer();
    }

    // fixme on singleplayer worlds this isnt correct
    public static boolean isServer() {
        return SERVER != null;
    }

    public static World getTardisDimension() {
        /*if (isClient()) {
            if (MinecraftClient.getInstance().world != null) {
                if (MinecraftClient.getInstance().world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
                    return MinecraftClient.getInstance().world;
                }
            }
        }*/
        return TARDIS_DIMENSION;
    }

    public static boolean isRiftChunk(ServerWorld world,BlockPos pos) {
        return ((RiftChunk)world.getChunk(pos)).isRiftChunk();
    }
    public static int getArtronLevelsOfChunk(ServerWorld world, BlockPos pos) {
        return ((RiftChunk)world.getChunk(pos)).getArtronLevels();
    }

    public static void setArtronLevelsOfChunk(ServerWorld world, BlockPos pos, Integer artron) {
        ((RiftChunk)world.getChunk(pos)).setArtronLevels(artron);
    }

    public static AbsoluteBlockPos.Directed createFromPlayer(PlayerEntity player) {
        return new AbsoluteBlockPos.Directed(player.getBlockPos(), player.getWorld(), player.getMovementDirection());
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
        return TardisUtil.offsetExteriorDoorPosition(travel.getPosition());
    }

    public static BlockPos offsetDoorPosition(AbsoluteBlockPos.Directed pos) {
        return switch (pos.getDirection()) {
            case DOWN, UP ->
                    throw new IllegalArgumentException("Cannot adjust door position with direction: " + pos.getDirection());
            case NORTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() - 1);
            case SOUTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() + 1);
            case EAST -> new BlockPos.Mutable(pos.getX() + 1, pos.getY(), pos.getZ() + 0.5);
            case WEST -> new BlockPos.Mutable(pos.getX() - 1, pos.getY(), pos.getZ() + 0.5);
        };
    }

    public static BlockPos offsetExteriorDoorPosition(AbsoluteBlockPos.Directed pos) {
        return switch (pos.getDirection()) {
            case DOWN, UP ->
                    throw new IllegalArgumentException("Cannot adjust door position with direction: " + pos.getDirection());
            case NORTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.0125);
            case SOUTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() - 0.0125);
            case EAST -> new BlockPos.Mutable(pos.getX() + 0.0125, pos.getY(), pos.getZ() + 0.5);
            case WEST -> new BlockPos.Mutable(pos.getX() - 0.0125, pos.getY(), pos.getZ() + 0.5);
        };
    }
    public static void teleportOutside(Tardis tardis, ServerPlayerEntity player) {
        AbsoluteBlockPos.Directed pos = tardis.getTravel().getState() == TardisTravel.State.FLIGHT ? FlightUtil.getPositionFromPercentage(tardis.position(), tardis.destination(), tardis.getHandlers().getFlight().getDurationAsPercentage()) : tardis.position();
        TardisUtil.teleportWithDoorOffset(player, tardis.getDoor().getExteriorPos());
    }

    public static void teleportInside(Tardis tardis, ServerPlayerEntity player) {
        TardisUtil.teleportWithDoorOffset(player, tardis.getDoor().getDoorPos());
        TardisDesktop tardisDesktop = tardis.getDesktop();
        if(tardisDesktop.getConsolePos() != null) {
            if(tardisDesktop.getConsolePos().getBlockEntity() != null) {
                tardisDesktop.getConsolePos().getBlockEntity().markDirty();
                ((ConsoleBlockEntity) tardisDesktop.getConsolePos().getBlockEntity()).sync(); // maybe force sync when a player enters the tardis
            } else {
                tardisDesktop.setConsolePos(null);
            }
        }
    }

    private static void teleportWithDoorOffset(ServerPlayerEntity player, AbsoluteBlockPos.Directed pos) {
        Vec3d vec = TardisUtil.offsetDoorPosition(pos).toCenterPos();
        SERVER.execute(() -> {
            WorldOps.teleportToWorld(player, (ServerWorld) pos.getWorld(), vec, pos.getDirection().asRotation(), player.getPitch());
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        });
    }

    public static Tardis findTardisByInterior(BlockPos pos) {
        Map<UUID, Tardis> matchingTardises = new HashMap<>();

        for (Map.Entry<UUID, Tardis> entry : TardisManager.getInstance().getLookup().entrySet()) {
            Tardis tardis = entry.getValue();
            if (TardisUtil.inBox(tardis.getDesktop().getCorners(), pos)) {
                matchingTardises.put(entry.getKey(), tardis);
            }
        }

        if (matchingTardises.isEmpty()) {
            if (isClient()) {
                ClientTardisManager.getInstance().ask(pos);
            }
            return null;
        } else {
            // Return the first Tardis object in the Map
            return matchingTardises.values().iterator().next();
        }
    }

    public static Tardis findTardisByPosition(AbsoluteBlockPos.Directed pos) {
        Map<UUID, Tardis> matchingTardises = new HashMap<>();

        for (Map.Entry<UUID, Tardis> entry : TardisManager.getInstance().getLookup().entrySet()) {
            Tardis tardis = entry.getValue();
            if (tardis.getDoor().getExteriorPos().equals(pos)) {
                matchingTardises.put(entry.getKey(), tardis);
            }
        }

        if (matchingTardises.isEmpty()) {
            if (isClient()) {
                ClientTardisManager.getInstance().ask(pos);
            }
            return null;
        } else {
            // Return the first Tardis object in the Map
            return matchingTardises.values().iterator().next();
        }
    }

    public static Tardis findTardisByPosition(AbsoluteBlockPos pos) {
        Map<UUID, Tardis> matchingTardises = new HashMap<>();

        for (Map.Entry<UUID, Tardis> entry : TardisManager.getInstance().getLookup().entrySet()) {
            Tardis tardis = entry.getValue();
            if (tardis.getDoor().getExteriorPos().equals(pos)) {
                matchingTardises.put(entry.getKey(), tardis);
            }
        }

        if (matchingTardises.isEmpty()) {
            if (isClient()) {
                ClientTardisManager.getInstance().ask(pos);
            }
            return null;
        } else {
            // Return the first Tardis object in the Map
            return matchingTardises.values().iterator().next();
        }
    }

    public static Tardis findTardisByPosition(BlockPos pos) {
        Map<UUID, Tardis> matchingTardises = new HashMap<>();

        for (Map.Entry<UUID, Tardis> entry : TardisManager.getInstance().getLookup().entrySet()) {
            Tardis tardis = entry.getValue();
            if (tardis.getDoor().getExteriorPos().equals(pos)) {
                matchingTardises.put(entry.getKey(), tardis);
            }
        }

        if (matchingTardises.isEmpty()) {
            if (isClient()) {
                ClientTardisManager.getInstance().ask(pos);
            }
            return null;
        } else {
            // Return the first Tardis object in the Map
            return matchingTardises.values().iterator().next();
        }
    }

    public static void giveEffectToInteriorPlayers(Tardis tardis, StatusEffectInstance effect) {
        for (PlayerEntity player : getPlayersInInterior(tardis)) {
            player.addStatusEffect(effect);
        }
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
        Tardis found;
        List<PlayerEntity> list = new ArrayList<>();

        for (ServerPlayerEntity player : getServer().getPlayerManager().getPlayerList()) {
            if (player.getServerWorld() != getTardisDimension()) continue;

            found = findTardisByInterior(player.getBlockPos());
            if(found == null) continue; // fixme "Cannot invoke "..getUuid()" because "found" is null ????
            if (found.getUuid().equals(tardis.getUuid())) list.add(player);
        }

        return list;
    }

    public static List<LivingEntity> getEntitiesInInterior(Tardis tardis, int area) {
        return getTardisDimension().getEntitiesByClass(LivingEntity.class, new Box(tardis.getDoor().getDoorPos().north(area).east(area).up(area), tardis.getDoor().getDoorPos().south(area).west(area).down(area)), (e) -> true);
    }
    public static List<LivingEntity> getEntitiesInInterior(Tardis tardis) {
        return getEntitiesInInterior(tardis, 20);
    }

    public static List<PlayerEntity> getPlayersInInterior(Corners corners) {
        List<PlayerEntity> list = List.of();
        for (PlayerEntity player : TardisUtil.getTardisDimension().getPlayers()) {
            if (inBox(corners, player.getBlockPos())) list.add(player);
        }
        return list;
    }

    public static boolean isInteriorNotEmpty(Tardis tardis) {
        return TardisUtil.getPlayerInsideInterior(tardis) != null;
    }

    public static void sendMessageToPilot(Tardis tardis, Text text) {
        ServerPlayerEntity player = (ServerPlayerEntity) TardisUtil.getPlayerInsideInterior(tardis); // may not necessarily be the person piloting the tardis, but todo this can be replaced with the player with the highest loyalty in future

        if (player == null) return; // Interior is probably empty

        player.sendMessage(text, true);
    }

    public static ServerWorld findWorld(RegistryKey<World> key) {
        return TardisUtil.getTardisDimension().getServer().getWorld(key);
    }

    public static ServerWorld findWorld(Identifier identifier) {
        return TardisUtil.findWorld(RegistryKey.of(RegistryKeys.WORLD, identifier));
    }

    public static ServerWorld findWorld(String identifier) {
        return TardisUtil.findWorld(new Identifier(identifier));
    }

    @Nullable
    public static ExteriorBlockEntity findExteriorEntity(Tardis tardis) {
        if (isClient()) return null;
        return (ExteriorBlockEntity) tardis.getDoor().getExteriorPos().getWorld().getBlockEntity(tardis.getDoor().getExteriorPos());
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
        BlockPos first, second;

        if (!tardis.getDesktop().getSchema().findTemplate().isPresent()) {
            AITMod.LOGGER.warn("Could not get desktop schema! Using whole interior instead.");
            return tardis.getDesktop().getCorners();
        }

        Vec3i size = tardis.getDesktop().getSchema().findTemplate().get().getSize();

        first = PosType.X.add(centre, -size.getX() / 2);
        first = PosType.Z.add(first, -size.getZ() / 2);

        second = PosType.X.add(centre, size.getX() / 2);
        second = PosType.Y.add(second, size.getY());
        second = PosType.Z.add(second, size.getZ() / 2);

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

        return corners.getFirst().add(size.getX(), size.getY() / 2, size.getZ());
    }

    @Nullable
    public static List<PlayerEntity> findPlayerByTardisKey(ServerWorld world, Tardis tardis) {
        List<PlayerEntity> newList = new ArrayList<>();
        for(PlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
            if(KeyItem.isKeyInInventory(player)) {
                ItemStack key = KeyItem.getFirstKeyStackInInventory(player);
                if(key == null) return null;
                NbtCompound tag = key.getOrCreateNbt();
                if (!tag.contains("tardis")) return null;
                if (UUID.fromString(tag.getString("tardis")) == tardis.getUuid()) {
                    newList.add(player);
                }
            }
        }
        return newList;
    }
}