package loqor.ait.tardis.util;

import io.wispforest.owo.ops.WorldOps;
import it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.Corners;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.util.StackUtil;
import loqor.ait.mixin.lookup.EntityTrackingSectionAccessor;
import loqor.ait.mixin.lookup.SectionedEntityCacheAccessor;
import loqor.ait.mixin.lookup.SimpleEntityLookupAccessor;
import loqor.ait.mixin.lookup.WorldInvoker;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.control.impl.pos.PosType;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.OvergrownData;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.permissions.PermissionHandler;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.function.LazyIterationConsumer;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.api.PortalAPI;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class TardisUtil {
	private static final Random RANDOM = new Random();

	private static ServerWorld OVERWORLD;
	private static ServerWorld TARDIS_DIMENSION;
	private static ServerWorld TIME_VORTEX;

	public static final Identifier CHANGE_EXTERIOR = new Identifier(AITMod.MOD_ID, "change_exterior");
	public static final Identifier SNAP = new Identifier(AITMod.MOD_ID, "snap");

	public static final Identifier FIND_PLAYER = new Identifier(AITMod.MOD_ID, "find_player");

	public static void init() {
		ServerWorldEvents.UNLOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD)
				OVERWORLD = null;

			if (world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD)
				TARDIS_DIMENSION = null;

			if (world.getRegistryKey() == AITDimensions.TIME_VORTEX_WORLD)
				TIME_VORTEX = null;
		});

		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD)
				OVERWORLD = world;

			if (world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD)
				TARDIS_DIMENSION = world;

			if (world.getRegistryKey() == AITDimensions.TIME_VORTEX_WORLD)
				TIME_VORTEX = world;
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			OVERWORLD = server.getOverworld();
			TARDIS_DIMENSION = server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
			TIME_VORTEX = server.getWorld(AITDimensions.TIME_VORTEX_WORLD);
		});

		ServerPlayNetworking.registerGlobalReceiver(ClientTardisUtil.CHANGE_SONIC, (server, player, handler, buf, responseSender) -> {
			UUID uuid = buf.readUuid();
			Identifier id = buf.readIdentifier();

			ServerTardisManager.getInstance().getTardis(server, uuid, tardis -> {
				SonicItem.setSchema(tardis.sonic().get(SonicHandler.HAS_CONSOLE_SONIC), id); // here we trust in the server with all of our might
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(CHANGE_EXTERIOR,
				(server, player, handler, buf, responseSender) -> {
					UUID uuid = buf.readUuid();
					Identifier exteriorValue = Identifier.tryParse(buf.readString());
					boolean variantChange = buf.readBoolean();
					String variantValue = buf.readString();

					ServerTardisManager.getInstance().getTardis(server, uuid, tardis -> {
						ExteriorVariantSchema schema = ExteriorVariantRegistry.getInstance().get(Identifier.tryParse(variantValue));

						// no hax
						if (!tardis.isUnlocked(schema))
							return;

						server.execute(() -> StackUtil.playBreak(player));

						tardis.getExterior().setType(CategoryRegistry.getInstance().get(exteriorValue));
						WorldOps.updateIfOnServer(server.getWorld(tardis
										.travel2().position().getWorld().getRegistryKey()),
								tardis.travel2().position().getPos());
						if (variantChange) {
							tardis.getExterior().setVariant(schema);
							WorldOps.updateIfOnServer(server.getWorld(tardis
											.travel2().position().getWorld().getRegistryKey()),
									tardis.travel2().position().getPos());
						}
					});
				}
		);

		ServerPlayNetworking.registerGlobalReceiver(SNAP,
				(server, player, handler, buf, responseSender) -> {
					UUID uuid = buf.readUuid();
					ServerTardisManager.getInstance().getTardis(server, uuid, tardis -> {
						PermissionHandler permissions = tardis.handler(TardisComponent.Id.PERMISSIONS);

						if (tardis.loyalty().get(player).level() < Loyalty.Type.PILOT.level)
							return;

						if (tardis.<OvergrownData>handler(TardisComponent.Id.OVERGROWN).isOvergrown())
							return;

						player.getWorld().playSound(null, player.getBlockPos(), AITSounds.SNAP, SoundCategory.PLAYERS, 4f, 1f);

						if (player.getVehicle() instanceof TardisRealEntity real) {
							DoorData.DoorStateEnum state = tardis.door().getDoorState();
							if (state == DoorData.DoorStateEnum.CLOSED || state == DoorData.DoorStateEnum.FIRST) {
								DoorData.useDoor(tardis, player.getServerWorld(), null, player);
								if (tardis.door().isDoubleDoor()) {
									DoorData.useDoor(tardis, player.getServerWorld(), null, player);
								}
							} else {
								DoorData.useDoor(tardis, player.getServerWorld(), null, player);
							}
							return;
						}

						BlockPos pos = player.getWorld().getRegistryKey() == TardisUtil.getTardisDimension().getRegistryKey()
								? tardis.getDesktop().doorPos().getPos() : tardis.travel2().position().getPos();

						if ((player.squaredDistanceTo(tardis.travel2().position().getPos().getX(), tardis.travel2().position().getPos().getY(), tardis.travel2().position().getPos().getZ())) <= 200 || TardisUtil.inBox(tardis.getDesktop().getCorners().getBox(), player.getBlockPos())) {
							if (!player.isSneaking()) {
								// annoying bad code

								DoorData.DoorStateEnum state = tardis.door().getDoorState();
								if (state == DoorData.DoorStateEnum.CLOSED || state == DoorData.DoorStateEnum.FIRST) {
									DoorData.useDoor(tardis, player.getServerWorld(), null, player);
									if (tardis.door().isDoubleDoor()) {
										DoorData.useDoor(tardis, player.getServerWorld(), null, player);
									}
								} else {
									DoorData.useDoor(tardis, player.getServerWorld(), null, player);
								}
							} else {
								DoorData.toggleLock(tardis, player);
							}
						}
					});
				}
		);

		ServerPlayNetworking.registerGlobalReceiver(FIND_PLAYER,
				(server, currentPlayer, handler, buf, responseSender) -> {
					UUID tardisId = buf.readUuid();
					UUID playerUuid = buf.readUuid();

					ServerTardisManager.getInstance().getTardis(server, tardisId, tardis -> {
						ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(playerUuid);

						if (serverPlayer == null) {
							tardis.getDesktop().playSoundAtEveryConsole(SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK, SoundCategory.BLOCKS, 3f, 1f);
							return;
						}

						tardis.travel2().forceDestination(DirectedGlobalPos.Cached.create(
                                (ServerWorld) serverPlayer.getWorld(), serverPlayer.getBlockPos(),
                                (byte) RotationPropertyHelper.fromYaw(serverPlayer.getBodyYaw())
                        ));

						tardis.getDesktop().playSoundAtEveryConsole(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 3f, 1f);
					});
				}
		);
	}

	public static PlayerManager getPlayerManager() {
		return OVERWORLD.getServer().getPlayerManager();
	}

	public static ResourceManager getServerResourceManager() {
		return OVERWORLD.getServer().getResourceManager();
	}

	public static Path getSavePath(WorldSavePath path) {
		return OVERWORLD.getServer().getSavePath(path);
	}

	public static <T> T getPlayerLookup(Function<MinecraftServer, T> f) {
		return f.apply(OVERWORLD.getServer());
	}

	public static ServerWorld getOverworld() {
		return OVERWORLD;
	}

	public static World getTardisDimension() {
		return TARDIS_DIMENSION;
	}

	public static World getTimeVortex() {
		return TIME_VORTEX;
	}

	public static boolean inBox(Box box, BlockPos pos) {
		return pos.getX() <= box.maxX && pos.getX() >= box.minX &&
				pos.getZ() <= box.maxZ && pos.getZ() >= box.minZ;
	}

	public static boolean inBox(Box a, Box b) {
		return a.minX < b.maxX && a.maxX > b.minX && a.minZ < b.maxZ && a.maxZ > b.minZ;
	}

	public static boolean in3DBox(Box box, BlockPos pos) {
		return pos.getX() <= box.maxX && pos.getX() >= box.minX &&
				pos.getY() <= box.maxY && pos.getY() >= box.minY &&
				pos.getZ() <= box.maxZ && pos.getZ() >= box.minZ;
	}

	public static boolean inBox(Corners corners, BlockPos pos) {
		return inBox(corners.getBox(), pos);
	}

	public static DoorBlockEntity getDoor(Tardis tardis) {
		if (!(TardisUtil.getTardisDimension().getBlockEntity(tardis.getDesktop().doorPos().getPos()) instanceof DoorBlockEntity door))
			return null;

		return door;
	}

	public static ExteriorBlockEntity getExterior(Tardis tardis) {
		DirectedGlobalPos.Cached globalPos = tardis.travel2().position();

		if (!(globalPos.getWorld().getBlockEntity(globalPos.getPos()) instanceof ExteriorBlockEntity exterior))
			return null;

		return exterior;
	}

	public static Corners findInteriorSpot() {
		BlockPos first = findRandomPlace();

		return new Corners(
				first, first.add(256, 0, 256)
		);
	}

	public static Random random() {
		return RANDOM;
	}

	public static BlockPos findRandomPlace() {
		return new BlockPos(RANDOM.nextInt(100_000), 0, RANDOM.nextInt(100_000));
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
		TardisUtil.teleportWithDoorOffset(tardis.travel2().position().getWorld(), entity, tardis.travel2().position().toPos());
	}

	public static void dropOutside(Tardis tardis, Entity entity) {
		TardisEvents.LEAVE_TARDIS.invoker().onLeave(tardis, entity);

		DirectedGlobalPos.Cached percentageOfDestination = tardis.travel2().getProgress();
		TardisUtil.teleportWithDoorOffset(tardis.travel2().destination().getWorld(), entity, percentageOfDestination.toPos());
	}

	public static void teleportInside(Tardis tardis, Entity entity) {
		TardisEvents.ENTER_TARDIS.invoker().onEnter(tardis, entity);
		TardisUtil.teleportWithDoorOffset(TARDIS_DIMENSION, entity, tardis.getDesktop().doorPos());
	}

	public static void teleportToInteriorPosition(Tardis tardis, Entity entity, BlockPos pos) {
		if (entity instanceof ServerPlayerEntity player) {
			TardisEvents.ENTER_TARDIS.invoker().onEnter(tardis, entity);

			WorldOps.teleportToWorld(player, (ServerWorld) TardisUtil.getTardisDimension(), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), entity.getYaw(), player.getPitch());
			player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
		}
	}

	private static void teleportWithDoorOffset(ServerWorld world, Entity entity, DirectedBlockPos directed) {
		BlockPos pos = directed.getPos();

		boolean isDoor = world.getBlockEntity(pos) instanceof DoorBlockEntity;

		Vec3d vec = isDoor ? TardisUtil.offsetInteriorDoorPos(directed)
				: TardisUtil.offsetDoorPosition(directed).add(0, 0.125, 0);

		world.getServer().execute(() -> {
			if (DependencyChecker.hasPortals()) {
				PortalAPI.teleportEntity(entity, world, vec);
			} else {
				if (entity instanceof ServerPlayerEntity player) {
					WorldOps.teleportToWorld(player, world, vec, RotationPropertyHelper.toDegrees(directed.getRotation())
							+ (isDoor ? 0 : 180f), player.getPitch()
					);

					player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
				} else {
					if (entity instanceof EnderDragonEntity
							|| entity instanceof WitherEntity
							|| entity instanceof WardenEntity)
						return;

					if (entity.getWorld().getRegistryKey() == world.getRegistryKey()) {
						entity.refreshPositionAndAngles(
								offset(vec, directed, 0.5f).x, vec.y, offset(vec, directed, 0.5f).z,
								RotationPropertyHelper.toDegrees(directed.getRotation()) + (isDoor ? 0 : 180f), entity.getPitch()
						);
					} else {
						entity.teleport(world,
								offset(vec, directed, 0.5f).x, vec.y, offset(vec, directed, 0.5f).z, Set.of(),
								RotationPropertyHelper.toDegrees(directed.getRotation()) + (isDoor ? 0 : 180f), entity.getPitch()
						);
					}
				}
			}
		});
	}

	public static Vec3d offset(Vec3d vec, DirectedBlockPos direction, double value) {
		Vec3i vec3i = direction.getVector();

		return new Vec3d(vec.x + value * (double) vec3i.getX(),
				vec.y + value * (double)vec3i.getY(),
				vec.z + value * (double)vec3i.getZ()
		);
	}

	public static Tardis findTardisByInterior(BlockPos pos, boolean isServer) {
		 Tardis result = TardisManager.getInstance(isServer).find(tardis -> TardisUtil.inBox(
				tardis.getDesktop().getCorners(), pos));
		 return result == null || result.isDisposed() ? null : result;
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

	public static List<ServerPlayerEntity> getPlayersInsideInterior(Tardis tardis) {
		List<ServerPlayerEntity> list = new ArrayList<>();

		for (PlayerEntity player : TardisUtil.getTardisDimension().getPlayers()) {
			if (TardisUtil.inBox(tardis.getDesktop().getCorners(), player.getBlockPos()))
				list.add((ServerPlayerEntity) player);
		}

		return list;
	}

	public static List<LivingEntity> getEntitiesInsideInterior(Tardis tardis, Predicate<LivingEntity> predicate) {
		return fastFlatLookup(LivingEntity.class, TardisUtil.getTardisDimension(), tardis.getDesktop().getCorners().getBox(), predicate);
	}

	private static <T extends EntityLike> void forEachInFlatBox(SectionedEntityCacheAccessor<T> accessor, Box box, LazyIterationConsumer<EntityTrackingSection<T>> consumer) {
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

				if (section == null || section.isEmpty()
						|| !section.getStatus().shouldTrack()
						|| !consumer.accept(section).shouldAbort())
					continue;

				return;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <U extends T, T extends EntityLike> void forEachIntersects(SectionedEntityCacheAccessor<T> cache, TypeFilter<T, U> filter, Box box, LazyIterationConsumer<U> consumer) {
		TardisUtil.forEachInFlatBox(cache, box, section -> {
			EntityTrackingSectionAccessor<T> accessor = (EntityTrackingSectionAccessor<T>) section;
			Collection<T> collection = accessor.getCollection().getAllOfType((Class<T>) filter.getBaseClass());

			if (collection.isEmpty())
				return LazyIterationConsumer.NextIteration.CONTINUE;

			for (T entityLike : collection) {
				U downcast = filter.downcast(entityLike);

				if (downcast == null || !TardisUtil.inBox(entityLike.getBoundingBox(), box) || !consumer.accept(downcast).shouldAbort())
					continue;

				return LazyIterationConsumer.NextIteration.ABORT;
			}

			return LazyIterationConsumer.NextIteration.CONTINUE;
		});
	}

	@SuppressWarnings("unchecked")
	private static <T extends Entity> List<T> fastFlatLookup(Class<T> clazz, World world, Box box, Predicate<T> predicate) {
		List<T> result = new ArrayList<>();
		world.getProfiler().visit("getEntities");

		SectionedEntityCacheAccessor<T> cache = (SectionedEntityCacheAccessor<T>) (
				(SimpleEntityLookupAccessor<T>) ((WorldInvoker) world).getEntityLookup()
		).getCache();

		TardisUtil.forEachIntersects(cache, TypeFilter.instanceOf(clazz), box, entity -> {
			if (predicate.test(entity))
				result.add(entity);

			return LazyIterationConsumer.NextIteration.CONTINUE;
		});

		return result;
	}

	/**
	 * @see TardisUtil#getPlayersInsideInterior(Tardis)
	 */
	@Deprecated(forRemoval = true)
	public static List<ServerPlayerEntity> getPlayersInInterior(Tardis tardis) {
		List<ServerPlayerEntity> list = new ArrayList<>();
		Tardis found;

		for (ServerPlayerEntity player : TardisUtil.TARDIS_DIMENSION.getPlayers()) {
			found = findTardisByInterior(player.getBlockPos(), true);

			if (found == null)
				continue;

			if (found == tardis)
				list.add(player);
		}

		return list;
	}

	public static List<LivingEntity> getLivingEntitiesInInterior(Tardis tardis, int area) {
		BlockPos pos = tardis.getDesktop().doorPos().getPos();

		return getTardisDimension().getEntitiesByClass(LivingEntity.class, new Box(
				pos.north(area).east(area).up(area), pos.south(area).west(area).down(area)
		), (e) -> true);
	}

	public static List<Entity> getEntitiesInInterior(Tardis tardis, int area) {
		BlockPos pos = tardis.getDesktop().doorPos().getPos();

		return getTardisDimension().getEntitiesByClass(Entity.class, new Box(
				pos.north(area).east(area).up(area), pos.south(area).west(area).down(area)
		), (e) -> true);
	}

	public static List<LivingEntity> getLivingEntitiesInInterior(Tardis tardis) {
		return getLivingEntitiesInInterior(tardis, 20);
	}

	public static boolean isInteriorNotEmpty(Tardis tardis) {
		return TardisUtil.getPlayerInsideInterior(tardis) != null;
	}

	public static void sendMessageToPilot(Tardis tardis, Text text) {
		// may not necessarily be the person piloting the tardis, but todo this can be replaced with the player with the highest loyalty in future
		ServerPlayerEntity player = (ServerPlayerEntity) TardisUtil.getPlayerInsideInterior(tardis);

		if (player == null)
			return; // Interior is probably empty

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

	public static BlockPos addRandomAmount(PosType type, BlockPos pos, int limit, Random random) {
		return type.add(pos, random.nextInt(limit));
	}

	public static Corners getPlacedInteriorCorners(Tardis tardis) {
		BlockPos centre = BlockPos.ofFloored(tardis.getDesktop().getCorners().getBox().getCenter());
		BlockPos first, second;

		Optional<StructureTemplate> template = tardis.getDesktop().getSchema().findTemplate();

		if (template.isEmpty()) {
			AITMod.LOGGER.warn("Could not get desktop schema! Using whole interior instead.");
			return tardis.getDesktop().getCorners();
		}

		Vec3i size = template.get().getSize();

		first = PosType.X.add(centre, -size.getX() / 2);
		first = PosType.Z.add(first, -size.getZ() / 2);

		second = PosType.X.add(centre, size.getX() / 2);
		second = PosType.Y.add(second, size.getY());
		second = PosType.Z.add(second, size.getZ() / 2);

        return new Corners(first, second);
	}
}