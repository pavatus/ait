package loqor.ait.tardis.control.impl;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.TravelHandlerV2;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DimensionControl extends Control {

	public DimensionControl() {
		super("dimension");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		TravelHandlerV2 travel = tardis.travel2();
		DirectedGlobalPos.Cached dest = travel.destination();

		List<ServerWorld> dims = getDimensions(world.getServer());
		int current = dims.indexOf(dest.getWorld() == null ? World.OVERWORLD : dest.getWorld());

		int next;
		if (!player.isSneaking()) {
			next = (current + 1) > dims.size() - 1 ? 0 : current + 1;
		} else {
			next = (current - 1) < 0 ? dims.size() - 1 : current - 1;
		}

		// FIXME we should make it so that once the ender dragon is defeated, the end is unlocked; also make that a config option as well for the server. - Loqor

		ServerWorld destWorld = dims.get(next);

		travel.destination(cached -> cached.world(destWorld));
		messagePlayer(player, destWorld);

		return true;
	}

	private void messagePlayer(ServerPlayerEntity player, ServerWorld world) {
		Text message = Text.translatable("message.ait.tardis.control.dimension.info").append(
				dimensionText(world.getRegistryKey())
		);

		player.sendMessage(message, true); // fixme translatable is preferred
	}

	public static Text dimensionText(RegistryKey<World> key) {
		return Text.translatable(key.getValue().toTranslationKey("dimension"));
	}

	// @TODO for some reason in the dev env, this method tends to not like doing anything sometimes. idk, it works or it doesnt, but in builds, it always works. funny what a lil spaghetti man can tell you at 3 am
	public static String convertWorldValueToModified(String value) {

		// Split the string into words
		String[] words = value.split("_");

		// Capitalize the first letter of each word
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
		}

		// Join the words back together with spaces
		return String.join(" ", words);
	}

	public static List<ServerWorld> getDimensions(MinecraftServer server) {
		List<ServerWorld> dims = new ArrayList<>();
		Iterable<ServerWorld> allDims = server.getWorlds();

		// fixme this is easiest/stupidest way to do this without letting them get to the tardis dim :p - Loqor
		allDims.forEach(dim -> {
			if (dim.getRegistryKey() != TardisUtil.getTardisDimension().getRegistryKey())
				dims.add(dim);
		});

		return dims;
	}
}
