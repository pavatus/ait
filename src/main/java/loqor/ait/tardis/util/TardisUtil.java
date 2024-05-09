package loqor.ait.tardis.util;

import io.wispforest.owo.ops.WorldOps;
import loqor.ait.AITMod;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.blockentities.DoorBlockEntity;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.Corners;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.core.item.KeyItem;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.util.StackUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.control.impl.pos.PosType;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.OvergrownData;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.api.PortalAPI;

import java.util.*;

@SuppressWarnings("unused")
public class TardisUtil {
	private static final Random RANDOM = new Random();

	/**
	 * @deprecated do NOT use this method.
	 */
	@Deprecated
	private static MinecraftServer SERVER;

	private static ServerWorld TARDIS_DIMENSION;
	private static ServerWorld TIME_VORTEX;
	public static final Identifier CHANGE_EXTERIOR = new Identifier(AITMod.MOD_ID, "change_exterior");
	public static final Identifier SNAP = new Identifier(AITMod.MOD_ID, "snap");

	public static final Identifier FIND_PLAYER = new Identifier(AITMod.MOD_ID, "find_player");

	public static void init() {
		ServerWorldEvents.UNLOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD) {
				SERVER = null;
			}
		});

		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD) {
				SERVER = server;
			}

			if (world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
				TARDIS_DIMENSION = world;
			}

			if (world.getRegistryKey() == AITDimensions.TIME_VORTEX_WORLD) {
				TIME_VORTEX = world;
			}
		});

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			SERVER = server;
			TARDIS_DIMENSION = server.getWorld(AITDimensions.TARDIS_DIM_WORLD);
			TIME_VORTEX = server.getWorld(AITDimensions.TIME_VORTEX_WORLD);
		});

		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			SERVER = null;
		});

		ServerPlayNetworking.registerGlobalReceiver(ClientTardisUtil.CHANGE_SONIC, (server, player, handler, buf, responseSender) -> {
			UUID uuid = buf.readUuid();
			Identifier id = buf.readIdentifier();

			Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);
			SonicItem.setSchema(tardis.sonic().get(SonicHandler.HAS_CONSOLE_SONIC), id); // here we trust in the server with all of our might
		});
		ServerPlayNetworking.registerGlobalReceiver(CHANGE_EXTERIOR,
				(server, player, handler, buf, responseSender) -> {
					UUID uuid = buf.readUuid();
					Identifier exteriorValue = Identifier.tryParse(buf.readString());
					boolean variantChange = buf.readBoolean();
					String variantValue = buf.readString();
					ServerTardis tardis = ServerTardisManager.getInstance().getTardis(uuid);

					ExteriorVariantSchema schema = ExteriorVariantRegistry.getInstance().get(Identifier.tryParse(variantValue));

					// no hax
					if (!tardis.isUnlocked(schema))
						return;

					server.execute(() -> StackUtil.playBreak(player));

					tardis.getExterior().setType(CategoryRegistry.getInstance().get(exteriorValue));
					WorldOps.updateIfOnServer(server.getWorld(tardis
									.getTravel().getPosition().getWorld().getRegistryKey()),
							tardis.getDoor().getExteriorPos());
					if (variantChange) {
						tardis.getExterior().setVariant(schema);
						WorldOps.updateIfOnServer(server.getWorld(tardis
										.getTravel().getPosition().getWorld().getRegistryKey()),
								tardis.getDoor().getExteriorPos());
					}
				}
		);
		ServerPlayNetworking.registerGlobalReceiver(SNAP,
				(server, player, handler, buf, responseSender) -> {
					UUID uuid = buf.readUuid();
					Tardis tardis = ServerTardisManager.getInstance().getTardis(uuid);

					if (tardis.loyalty().get(player).level() <= Loyalty.Type.PILOT.level)
						return;

					if (tardis.<OvergrownData>handler(TardisComponent.Id.OVERGROWN).isOvergrown())
						return;

					player.getWorld().playSound(null, player.getBlockPos(), AITSounds.SNAP, SoundCategory.PLAYERS, 4f, 1f);

					if (player.getVehicle() instanceof TardisRealEntity real) {
						DoorData.DoorStateEnum state = tardis.getDoor().getDoorState();
						if (state == DoorData.DoorStateEnum.CLOSED || state == DoorData.DoorStateEnum.FIRST) {
							DoorData.useDoor(tardis, player.getServerWorld(), null, player);
							if (tardis.getDoor().isDoubleDoor()) {
								DoorData.useDoor(tardis, player.getServerWorld(), null, player);
							}
						} else {
							DoorData.useDoor(tardis, player.getServerWorld(), null, player);
						}
						return;
					}

					BlockPos pos = player.getWorld().getRegistryKey() ==
							TardisUtil.getTardisDimension().getRegistryKey() ? tardis.getDoor().getDoorPos() : tardis.getDoor().getExteriorPos();
					if ((player.squaredDistanceTo(tardis.getDoor().getExteriorPos().getX(), tardis.getDoor().getExteriorPos().getY(), tardis.getDoor().getExteriorPos().getZ())) <= 200 || TardisUtil.inBox(tardis.getDesktop().getCorners().getBox(), player.getBlockPos())) {
						if (!player.isSneaking()) {
							// annoying bad code

							DoorData.DoorStateEnum state = tardis.getDoor().getDoorState();
							if (state == DoorData.DoorStateEnum.CLOSED || state == DoorData.DoorStateEnum.FIRST) {
								DoorData.useDoor(tardis, player.getServerWorld(), null, player);
								if (tardis.getDoor().isDoubleDoor()) {
									DoorData.useDoor(tardis, player.getServerWorld(), null, player);
								}
							} else {
								DoorData.useDoor(tardis, player.getServerWorld(), null, player);
							}
						} else {
							DoorData.toggleLock(tardis, player);
						}
					}
				}
		);
		ServerPlayNetworking.registerGlobalReceiver(FIND_PLAYER,
				(server, currentPlayer, handler, buf, responseSender) -> {
					UUID tardisId = buf.readUuid();
					UUID playerUuid = buf.readUuid();
					Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisId);
					ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(playerUuid);
					if (serverPlayer == null) {
						FlightUtil.playSoundAtConsole(tardis, SoundEvents.BLOCK_SCULK_SHRIEKER_BREAK, SoundCategory.BLOCKS, 3f, 1f);
						return;
					}
					tardis.getTravel().setDestination(new AbsoluteBlockPos.Directed(
									serverPlayer.getBlockX(),
									serverPlayer.getBlockY(),
									serverPlayer.getBlockZ(),
									serverPlayer.getWorld(),
									RotationPropertyHelper.fromYaw(serverPlayer.getBodyYaw())),
							PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.AUTO_LAND));
					FlightUtil.playSoundAtConsole(tardis, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 3f, 1f);
				}
		);
	}

	/**
	 * @deprecated do NOT use this method.
	 */
	@Deprecated
	public static MinecraftServer getServer() {
		return SERVER;
	}

	/**
	 * @deprecated do NOT use this method.
	 */
	@Deprecated
	public static boolean isClient() {
		return !TardisUtil.isServer();
	}

	/**
	 * @deprecated do NOT use this method.
	 */
	@Deprecated
	public static boolean isServer() {
		return SERVER != null;
	}

	public static World getTardisDimension() {
		return TARDIS_DIMENSION;
	}

	public static World getTimeVortex() {
		return TIME_VORTEX;
	}

	public static AbsoluteBlockPos.Directed createFromPlayer(PlayerEntity player) {
		return new AbsoluteBlockPos.Directed(player.getBlockPos(), player.getWorld(), RotationPropertyHelper.fromYaw(player.getBodyYaw()));
	}

	public static boolean inBox(Box box, BlockPos pos) {
		return pos.getX() <= box.maxX && pos.getX() >= box.minX &&
				pos.getZ() <= box.maxZ && pos.getZ() >= box.minZ;
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

	public static BlockPos offsetDoorPosition(AbsoluteBlockPos.Directed pos) {
		boolean bl = pos.getWorld().getBlockEntity(pos) instanceof DoorBlockEntity;
		return switch (pos.getRotation()) {
			default ->
					throw new IllegalArgumentException("Cannot adjust door position with direction: " + pos.getRotation());
			case 0 -> new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ() + (bl ? 1 : - 1));
			case 1, 2, 3 -> new BlockPos.Mutable(pos.getX() + 1, pos.getY(), pos.getZ() - 1);
			case 4 -> new BlockPos.Mutable(pos.getX() + (bl ? - 1 : 1), pos.getY(), pos.getZ());
			case 5, 6, 7 -> new BlockPos.Mutable(pos.getX() + 1, pos.getY(), pos.getZ() + 1);
			case 8 -> new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ() + (bl ? - 1 : 1));
			case 9, 10, 11 -> new BlockPos.Mutable(pos.getX() - 1, pos.getY(), pos.getZ() + 1);
			case 12 -> new BlockPos.Mutable(pos.getX() + (bl ? 1 : - 1), pos.getY(), pos.getZ());
			case 13, 14, 15 -> new BlockPos.Mutable(pos.getX() - 1, pos.getY(), pos.getZ() - 1);
		};
	}

	public static void teleportOutside(Tardis tardis, Entity entity) {
		AbsoluteBlockPos.Directed pos = tardis.getTravel().getState() == TardisTravel.State.FLIGHT ? FlightUtil.getPositionFromPercentage(tardis.position(), tardis.destination(), tardis.flight().getDurationAsPercentage()) : tardis.position();
		TardisUtil.teleportWithDoorOffset(entity, tardis.getDoor().getExteriorPos());
	}

	public static void teleportInside(Tardis tardis, Entity entity) {
		TardisUtil.teleportWithDoorOffset(entity, tardis.getDoor().getDoorPos());
		TardisDesktop tardisDesktop = tardis.getDesktop();

		tardis.getDesktop().getConsoles().forEach(console -> {
			console.findEntity().ifPresent(ConsoleBlockEntity::sync);
		});
	}

	public static void teleportToInteriorPosition(Entity entity, BlockPos pos) {
		if (entity instanceof ServerPlayerEntity player) {
			WorldOps.teleportToWorld(player, (ServerWorld) TardisUtil.getTardisDimension(), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), entity.getYaw(), player.getPitch());
			player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
		}
	}

	private static void teleportWithDoorOffset(Entity entity, AbsoluteBlockPos.Directed pos) {
		boolean bl = pos.getWorld().getBlockEntity(pos) instanceof DoorBlockEntity;
		Vec3d vec = TardisUtil.offsetDoorPosition(pos).toCenterPos();
		if(pos.getWorld() instanceof ServerWorld serverWorld) {
			SERVER.execute(() -> {
				if (DependencyChecker.hasPortals()) {
					PortalAPI.teleportEntity(entity, serverWorld, vec);
				} else {
					if (entity instanceof ServerPlayerEntity player) {
						WorldOps.teleportToWorld(player, serverWorld, vec, RotationPropertyHelper.toDegrees(pos.getRotation()) + (bl ? 0 : 180f), player.getPitch());
						player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
					} else {
						if(entity instanceof EnderDragonEntity
								|| entity instanceof WitherEntity
								|| entity instanceof WardenEntity) return;
						if (entity.getWorld().getRegistryKey() == pos.getWorld().getRegistryKey()) {
							entity.refreshPositionAndAngles(offset(vec, pos, 0.5f).x, vec.y, offset(vec, pos, 0.5f).z, RotationPropertyHelper.toDegrees(pos.getRotation()) + (bl ? 0 : 180f), entity.getPitch());
						} else {
							entity.teleport(serverWorld, offset(vec, pos, 0.5f).x, vec.y, offset(vec, pos, 0.5f).z, Set.of(), RotationPropertyHelper.toDegrees(pos.getRotation()) + (bl ? 0 : 180f), entity.getPitch());
						}
					}
				}
			});
		}
	}

	public static Vec3d offset(Vec3d vec, AbsoluteBlockPos.Directed direction, double value) {
		Vec3i vec3i = direction.getVector(direction.getRotation());
		return new Vec3d(vec.x + value * (double)vec3i.getX(), vec.y + value * (double)vec3i.getY(), vec.z + value * (double)vec3i.getZ());
	}

	public static Tardis findTardisByInterior(BlockPos pos, boolean isServer) {
		TardisManager<?> manager = TardisManager.getInstance(isServer);

		if (manager == null) {
			AITMod.LOGGER.error("TardisManager is NULL in findTardisByInterior");
			AITMod.LOGGER.error("Called server side? " + isServer);

			return null;
		}

		if (manager.getLookup() == null)
			return null;

		for (Tardis tardis : manager.getLookup().values()) {;
			if (TardisUtil.inBox(tardis.getDesktop().getCorners(), pos))
				return tardis;
		}

		return null;
	}

	public static Tardis findTardisByPosition(AbsoluteBlockPos.Directed pos) {
		Map<UUID, Tardis> matchingTardises = new HashMap<>();

		for (Map.Entry<UUID, ?> entry : TardisManager.getInstance().getLookup().entrySet()) {
			Tardis tardis = (Tardis) entry.getValue();
			if (tardis.getDoor().getExteriorPos().equals(pos)) {
				matchingTardises.put(entry.getKey(), tardis);
			}
		}

		if (matchingTardises.isEmpty()) {
			if (isClient()) {
				ClientTardisManager.getInstance().askTardis(pos);
			}
			return null;
		} else {
			// Return the first Tardis object in the Map
			return matchingTardises.values().iterator().next();
		}
	}

	public static Tardis findTardisByPosition(AbsoluteBlockPos pos) {
		Map<UUID, Tardis> matchingTardises = new HashMap<>();

		for (Map.Entry<UUID, ?> entry : TardisManager.getInstance().getLookup().entrySet()) {
			Tardis tardis = (Tardis) entry.getValue();
			if (tardis.getDoor().getExteriorPos().equals(pos)) {
				matchingTardises.put(entry.getKey(), tardis);
			}
		}

		if (matchingTardises.isEmpty()) {
			if (isClient()) {
				ClientTardisManager.getInstance().askTardis(pos);
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

	public static List<PlayerEntity> getPlayersInsideInterior(Tardis tardis) {
		List<PlayerEntity> list = new ArrayList<>();
		for (PlayerEntity player : TardisUtil.getTardisDimension().getPlayers()) {
			if (TardisUtil.inBox(tardis.getDesktop().getCorners(), player.getBlockPos()))
				list.add(player);
		}
		return list;
	}

	public static List<ServerPlayerEntity> getPlayersInInterior(Tardis tardis) {
		Tardis found;
		List<ServerPlayerEntity> list = new ArrayList<>();

		for (ServerPlayerEntity player : getServer().getPlayerManager().getPlayerList()) {
			if (player.getServerWorld() != getTardisDimension()) continue;

			found = findTardisByInterior(player.getBlockPos(), true);
			if (found == null) continue; // fixme "Cannot invoke "..getUuid()" because "found" is null ????
			if (found.getUuid().equals(tardis.getUuid())) list.add(player);
		}

		return list;
	}

	public static List<LivingEntity> getLivingEntitiesInInterior(Tardis tardis, int area) {
		return getTardisDimension().getEntitiesByClass(LivingEntity.class, new Box(tardis.getDoor().getDoorPos().north(area).east(area).up(area), tardis.getDoor().getDoorPos().south(area).west(area).down(area)), (e) -> true);
	}

	public static List<Entity> getEntitiesInInterior(Tardis tardis, int area) {
		return getTardisDimension().getEntitiesByClass(Entity.class, new Box(tardis.getDoor().getDoorPos().north(area).east(area).up(area), tardis.getDoor().getDoorPos().south(area).west(area).down(area)), (e) -> true);
	}

	public static List<LivingEntity> getLivingEntitiesInInterior(Tardis tardis) {
		return getLivingEntitiesInInterior(tardis, 20);
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
		temp = addRandomAmount(PosType.Y, temp, corners.getSecond().getY(), random);
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

	public static @NotNull List<PlayerEntity> findPlayerByTardisKey(ServerWorld world, Tardis tardis) {
		List<PlayerEntity> newList = new ArrayList<>();
		for (PlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
			if (KeyItem.isKeyInInventory(player)) {
				Collection<ItemStack> keys = KeyItem.getKeysInInventory(player);

				for (ItemStack key : keys) {
					if (KeyItem.getTardis(key) == tardis) {
						newList.add(player);
					}
				}
			}
		}
		return newList;
	}
}