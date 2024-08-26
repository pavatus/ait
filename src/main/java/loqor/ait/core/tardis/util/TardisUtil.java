package loqor.ait.core.tardis.util;

import java.util.*;
import java.util.function.Predicate;

import io.wispforest.owo.ops.WorldOps;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.api.PortalAPI;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.data.Corners;
import loqor.ait.data.DirectedBlockPos;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.mixin.lookup.EntityTrackingSectionAccessor;
import loqor.ait.mixin.lookup.SectionedEntityCacheAccessor;
import loqor.ait.mixin.lookup.SimpleEntityLookupAccessor;
import loqor.ait.mixin.lookup.WorldInvoker;
import loqor.ait.tardis.handler.DoorHandler;
import loqor.ait.tardis.handler.OvergrownHandler;
import loqor.ait.tardis.handler.loyalty.Loyalty;
import loqor.ait.tardis.handler.permissions.PermissionHandler;
import loqor.ait.tardis.manager.ServerTardisManager;

@SuppressWarnings("unused")
public class TardisUtil {

    public static final Identifier LEAVEBEHIND = new Identifier(AITMod.MOD_ID, "leavebehind");
    public static final Identifier HOSTILEALARMS = new Identifier(AITMod.MOD_ID, "hostilealarms");
    public static final Identifier REGION_LANDING_CODE = new Identifier(AITMod.MOD_ID, "region_landing_code");
    public static final Identifier LANDING_CODE = new Identifier(AITMod.MOD_ID, "landing_code");

    public static final Identifier SNAP = new Identifier(AITMod.MOD_ID, "snap");

    public static final Identifier FIND_PLAYER = new Identifier(AITMod.MOD_ID, "find_player");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SNAP, (server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            ServerTardisManager.getInstance().getTardis(server, uuid, tardis -> {
                PermissionHandler permissions = tardis.handler(TardisComponent.Id.PERMISSIONS);

                if (tardis.loyalty().get(player).level() < Loyalty.Type.PILOT.level)
                    return;

                if (tardis.<OvergrownHandler>handler(TardisComponent.Id.OVERGROWN).isOvergrown())
                    return;

                player.getWorld().playSound(null, player.getBlockPos(), AITSounds.SNAP, SoundCategory.PLAYERS, 4f, 1f);

                BlockPos exteriorPos = tardis.travel().position().getPos();

                BlockPos pos = player.getWorld().getRegistryKey() == WorldUtil.getTardisDimension().getRegistryKey()
                        ? tardis.getDesktop().doorPos().getPos()
                        : exteriorPos;

                if ((player.squaredDistanceTo(exteriorPos.getX(), exteriorPos.getY(), exteriorPos.getZ())) <= 200
                        || TardisUtil.inBox(tardis.getDesktop().getCorners().getBox(), player.getBlockPos())) {
                    if (!player.isSneaking()) {
                        // annoying bad code

                        DoorHandler.DoorStateEnum state = tardis.door().getDoorState();
                        if (state == DoorHandler.DoorStateEnum.CLOSED || state == DoorHandler.DoorStateEnum.FIRST) {
                            server.execute(() -> DoorHandler.useDoor(tardis, player.getServerWorld(), null, player));
                            if (tardis.door().isDoubleDoor()) {
                                server.execute(() -> DoorHandler.useDoor(tardis, player.getServerWorld(), null, player));
                            }
                        } else {
                            server.execute(() -> DoorHandler.useDoor(tardis, player.getServerWorld(), null, player));
                        }
                    } else {
                        server.execute(() -> DoorHandler.toggleLock(tardis, player));
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(FIND_PLAYER,
                (server, currentPlayer, handler, buf, responseSender) -> {
                    UUID tardisId = buf.readUuid();
                    UUID playerUuid = buf.readUuid();

                    ServerTardisManager.getInstance().getTardis(server, tardisId, tardis -> {
                        ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(playerUuid);

                        if (serverPlayer == null) {
                            tardis.getDesktop().playSoundAtEveryConsole(SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK,
                                    SoundCategory.BLOCKS, 3f, 1f);
                            return;
                        }

                        tardis.travel()
                                .forceDestination(DirectedGlobalPos.Cached.create((ServerWorld) serverPlayer.getWorld(),
                                        serverPlayer.getBlockPos(),
                                        (byte) RotationPropertyHelper.fromYaw(serverPlayer.getBodyYaw())));

                        tardis.getDesktop().playSoundAtEveryConsole(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                                SoundCategory.BLOCKS, 3f, 1f);
                    });
                });
    }

    public static boolean inBox(Box box, BlockPos pos) {
        return pos.getX() <= box.maxX && pos.getX() >= box.minX && pos.getZ() <= box.maxZ && pos.getZ() >= box.minZ;
    }

    public static boolean inBox(Box a, Box b) {
        return a.minX < b.maxX && a.maxX > b.minX && a.minZ < b.maxZ && a.maxZ > b.minZ;
    }

    public static boolean inBox(Corners corners, BlockPos pos) {
        return inBox(corners.getBox(), pos);
    }

    public static Corners findInteriorSpot() {
        BlockPos first = findRandomPlace();

        return new Corners(first, first.add(256, 0, 256));
    }

    public static BlockPos findRandomPlace() {
        return new BlockPos(AITMod.RANDOM.nextInt(100_000), 0, AITMod.RANDOM.nextInt(100_000));
    }

    public static Vec3d offsetInteriorDoorPosition(Tardis tardis) {
        return TardisUtil.offsetInteriorDoorPosition(tardis.getDesktop());
    }

    public static Vec3d offsetInteriorDoorPosition(TardisDesktop desktop) {
        return TardisUtil.offsetInteriorDoorPos(desktop.doorPos());
    }

    public static Vec3d offsetDoorPosition(DirectedBlockPos directed) {
        BlockPos pos = directed.getPos();

        return switch (directed.getRotation()) {
            default -> new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() - 0.5f);
            case 1, 2, 3 -> new Vec3d(pos.getX() + 1.1f, pos.getY(), pos.getZ() - 0.5f);
            case 4 -> new Vec3d(pos.getX() + 1.5f, pos.getY(), pos.getZ() + 0.5f);
            case 5, 6, 7 -> new Vec3d(pos.getX() + 1.5f, pos.getY(), pos.getZ() + 1.1f);
            case 8 -> new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 1.5f);
            case 9, 10, 11 -> new Vec3d(pos.getX(), pos.getY(), pos.getZ() + 1.5f);
            case 12 -> new Vec3d(pos.getX() - 0.5f, pos.getY(), pos.getZ() + 0.5f);
            case 13, 14, 15 -> new Vec3d(pos.getX() - 0.3f, pos.getY(), pos.getZ() - 0.5f);
        };
    }

    public static Vec3d offsetInteriorDoorPos(DirectedBlockPos directed) {
        BlockPos pos = directed.getPos();

        return switch (directed.getRotation()) {
            default -> new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.6f);
            case 4 -> new Vec3d(pos.getX() + 0.4f, pos.getY(), pos.getZ() + 0.5f);
            case 8 -> new Vec3d(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.4f);
            case 12 -> new Vec3d(pos.getX() + 0.6f, pos.getY(), pos.getZ() + 0.5f);
        };
    }

    public static void teleportOutside(Tardis tardis, Entity entity) {
        TardisEvents.LEAVE_TARDIS.invoker().onLeave(tardis, entity);
        TardisUtil.teleportWithDoorOffset(tardis.travel().position().getWorld(), entity,
                tardis.travel().position().toPos());
    }

    public static void dropOutside(Tardis tardis, Entity entity) {
        TardisEvents.LEAVE_TARDIS.invoker().onLeave(tardis, entity);

        DirectedGlobalPos.Cached percentageOfDestination = tardis.travel().getProgress();
        TardisUtil.teleportWithDoorOffset(tardis.travel().destination().getWorld(), entity,
                percentageOfDestination.toPos());
    }

    public static void teleportInside(Tardis tardis, Entity entity) {
        TardisEvents.ENTER_TARDIS.invoker().onEnter(tardis, entity);
        TardisUtil.teleportWithDoorOffset(WorldUtil.getTardisDimension(), entity, tardis.getDesktop().doorPos());
    }

    public static void teleportToInteriorPosition(Tardis tardis, Entity entity, BlockPos pos) {
        if (entity instanceof ServerPlayerEntity player) {
            TardisEvents.ENTER_TARDIS.invoker().onEnter(tardis, entity);

            WorldOps.teleportToWorld(player, WorldUtil.getTardisDimension(),
                    new Vec3d(pos.getX(), pos.getY(), pos.getZ()), entity.getYaw(), player.getPitch());
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        }
    }

    private static void teleportWithDoorOffset(ServerWorld world, Entity entity, DirectedBlockPos directed) {
        BlockPos pos = directed.getPos();

        boolean isDoor = world.getBlockEntity(pos) instanceof DoorBlockEntity;

        Vec3d vec = isDoor
                ? TardisUtil.offsetInteriorDoorPos(directed)
                : TardisUtil.offsetDoorPosition(directed).add(0, 0.125, 0);

        world.getServer().execute(() -> {
            if (DependencyChecker.hasPortals()) {
                PortalAPI.teleportEntity(entity, world, vec);
            } else {
                if (entity instanceof ServerPlayerEntity player) {
                    WorldOps.teleportToWorld(player, world, vec,
                            RotationPropertyHelper.toDegrees(directed.getRotation()) + (isDoor ? 0 : 180f),
                            player.getPitch());

                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                } else {
                    if (entity instanceof EnderDragonEntity || entity instanceof EnderDragonPart
                            || entity instanceof WitherEntity || entity instanceof WardenEntity)
                        return;

                    if (entity.getWorld().getRegistryKey() == world.getRegistryKey()) {
                        entity.refreshPositionAndAngles(offset(vec, directed, 0.5f).x, vec.y,
                                offset(vec, directed, 0.5f).z,
                                RotationPropertyHelper.toDegrees(directed.getRotation()) + (isDoor ? 0 : 180f),
                                entity.getPitch());
                    } else {
                        entity.teleport(world, offset(vec, directed, 0.5f).x, vec.y, offset(vec, directed, 0.5f).z,
                                Set.of(),
                                RotationPropertyHelper.toDegrees(directed.getRotation()) + (isDoor ? 0 : 180f),
                                entity.getPitch());
                    }
                }
            }
        });
    }

    public static Vec3d offset(Vec3d vec, DirectedBlockPos direction, double value) {
        Vec3i vec3i = direction.getVector();

        return new Vec3d(vec.x + value * (double) vec3i.getX(), vec.y + value * (double) vec3i.getY(),
                vec.z + value * (double) vec3i.getZ());
    }

    public static Tardis findTardisByInterior(BlockPos pos, boolean isServer) {
        return TardisManager.getInstance(isServer)
                .find(tardis -> TardisUtil.inBox(tardis.getDesktop().getCorners(), pos));
    }

    public static void giveEffectToInteriorPlayers(ServerTardis tardis, StatusEffectInstance effect) {
        for (PlayerEntity player : getPlayersInsideInterior(tardis)) {
            player.addStatusEffect(effect);
        }
    }

    public static @Nullable PlayerEntity getAnyPlayerInsideInterior(Tardis tardis) {
        return getAnyPlayerInsideInterior(tardis.getDesktop().getCorners());
    }

    public static @Nullable PlayerEntity getAnyPlayerInsideInterior(Corners corners) {
        for (PlayerEntity player : WorldUtil.getTardisDimension().getPlayers()) {
            if (TardisUtil.inBox(corners, player.getBlockPos()))
                return player;
        }

        return null;
    }

    public static List<ServerPlayerEntity> getPlayersInsideInterior(ServerTardis tardis) {
        List<ServerPlayerEntity> list = new ArrayList<>();

        for (ServerPlayerEntity player : WorldUtil.getTardisDimension().getPlayers()) {
            if (TardisUtil.inBox(tardis.getDesktop().getCorners(), player.getBlockPos()))
                list.add(player);
        }

        return list;
    }

    public static List<LivingEntity> getLivingInInterior(Tardis tardis, Predicate<LivingEntity> predicate) {
        return getEntitiesInBox(LivingEntity.class, WorldUtil.getTardisDimension(),
                tardis.getDesktop().getCorners().getBox(), predicate);
    }

    public static <T extends Entity> List<T> getEntitiesInBox(Class<T> clazz, World world, Box box,
            Predicate<T> predicate) {
        return fastFlatLookup(clazz, world, box, predicate);
    }

    private static <T extends EntityLike> void forEachInFlatBox(SectionedEntityCacheAccessor<T> accessor, Box box,
            LazyIterationConsumer<EntityTrackingSection<T>> consumer) {
        int j = ChunkSectionPos.getSectionCoord(box.minX - 2.0);
        int l = ChunkSectionPos.getSectionCoord(box.minZ - 2.0);

        int m = ChunkSectionPos.getSectionCoord(box.maxX + 2.0);
        int o = ChunkSectionPos.getSectionCoord(box.maxZ + 2.0);

        for (int p = j; p <= m; p++) {
            long q = ChunkSectionPos.asLong(p, 0, 0);
            long r = ChunkSectionPos.asLong(p, -1, -1);

            LongBidirectionalIterator longIterator = accessor.getTrackedPositions().subSet(q, r + 1L).iterator();

            while (longIterator.hasNext()) {
                long s = longIterator.nextLong();
                int u = ChunkSectionPos.unpackZ(s);

                if (u < l || u > o)
                    continue;

                EntityTrackingSection<T> section = accessor.getTrackingSections().get(s);

                if (section == null || section.isEmpty() || !section.getStatus().shouldTrack()
                        || !consumer.accept(section).shouldAbort())
                    continue;

                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <U extends T, T extends EntityLike> void forEachIntersects(SectionedEntityCacheAccessor<T> cache,
            TypeFilter<T, U> filter, Box box, LazyIterationConsumer<U> consumer) {
        TardisUtil.forEachInFlatBox(cache, box, section -> {
            EntityTrackingSectionAccessor<T> accessor = (EntityTrackingSectionAccessor<T>) section;
            Collection<T> collection = accessor.getCollection().getAllOfType((Class<T>) filter.getBaseClass());

            if (collection.isEmpty())
                return LazyIterationConsumer.NextIteration.CONTINUE;

            for (T entityLike : collection) {
                U downcast = filter.downcast(entityLike);

                if (downcast == null || !TardisUtil.inBox(entityLike.getBoundingBox(), box)
                        || !consumer.accept(downcast).shouldAbort())
                    continue;

                return LazyIterationConsumer.NextIteration.ABORT;
            }

            return LazyIterationConsumer.NextIteration.CONTINUE;
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> List<T> fastFlatLookup(Class<T> clazz, World world, Box box,
            Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        world.getProfiler().visit("getEntities");

        SectionedEntityCacheAccessor<T> cache = (SectionedEntityCacheAccessor<T>) ((SimpleEntityLookupAccessor<T>) ((WorldInvoker) world)
                .getEntityLookup()).getCache();

        TardisUtil.forEachIntersects(cache, TypeFilter.instanceOf(clazz), box, entity -> {
            if (predicate.test(entity))
                result.add(entity);

            return LazyIterationConsumer.NextIteration.CONTINUE;
        });

        return result;
    }

    public static List<LivingEntity> getLivingEntitiesInInterior(Tardis tardis, int area) {
        BlockPos pos = tardis.getDesktop().doorPos().getPos();

        return WorldUtil.getTardisDimension().getEntitiesByClass(LivingEntity.class,
                new Box(pos.north(area).east(area).up(area), pos.south(area).west(area).down(area)), (e) -> true);
    }

    public static List<Entity> getEntitiesInInterior(Tardis tardis, int area) {
        DirectedBlockPos directedPos = tardis.getDesktop().doorPos();

        if (directedPos == null)
            return List.of();

        BlockPos pos = directedPos.getPos();

        return WorldUtil.getTardisDimension().getEntitiesByClass(Entity.class,
                new Box(pos.north(area).east(area).up(area), pos.south(area).west(area).down(area)), (e) -> true);
    }

    public static List<LivingEntity> getLivingEntitiesInInterior(ServerTardis tardis) {
        return getLivingEntitiesInInterior(tardis, 20);
    }

    public static boolean isInteriorEmpty(ServerTardis tardis) {
        return TardisUtil.getAnyPlayerInsideInterior(tardis) == null;
    }

    public static void sendMessageToInterior(ServerTardis tardis, Text text) {
        for (ServerPlayerEntity player : getPlayersInsideInterior(tardis)) {
            player.sendMessage(text, true);
        }
    }

    public static void sendMessageToLinked(ServerTardis tardis, Text message) {
        NetworkUtil.getLinkedPlayers(tardis).forEach(player -> player.sendMessage(message, true));
    }
}
