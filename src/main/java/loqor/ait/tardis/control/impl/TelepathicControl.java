package loqor.ait.tardis.control.impl;

import com.mojang.datafixers.util.Pair;
import loqor.ait.core.item.KeyItem;
import loqor.ait.core.item.SonicItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.link.LinkableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.NameTagItem;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;

public class TelepathicControl extends Control {
	public TelepathicControl() {
		super("telepathic_circuit");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		boolean security = PropertiesHandler.getBool(tardis.properties(), SecurityControl.SECURITY_KEY);

		if (!KeyItem.hasMatchingKeyInInventory(player, tardis) && security)
			return false;

		if (player.getMainHandStack().getItem() instanceof LinkableItem linker) {
			if (linker instanceof SonicItem)
				return false;

			linker.link(player.getMainHandStack(), tardis);
			world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			return true;
		}

		if (player.getMainHandStack().getItem() instanceof NameTagItem) {
			ItemStack hand = player.getMainHandStack();

			if (!hand.hasCustomName())
				return false;

			tardis.stats().setName(hand.getName().getString());
			world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1F, 1.0F);

			if (!player.isCreative())
				hand.decrement(1);

			this.addToControlSequence(tardis, player, console);
			return true;
		}

		Text text = Text.literal("The TARDIS is choosing.."); // todo translatable
		player.sendMessage(text, true);

		BlockPos found = locateStructureOfInterest((ServerWorld) tardis.travel().destination().getWorld(), tardis.travel().position());
		text = Text.literal("The TARDIS chose where to go.."); // todo translatable

		if (found == null) {
			text = Text.literal("The TARDIS is happy where it is"); // todo translatable
		} else {
			tardis.travel().destination(cached -> cached.pos(found.withY(75)));
			tardis.removeFuel(500 * (tardis.tardisHammerAnnoyance + 1));
		}

		player.sendMessage(text, true);
		return true;
	}

	public static BlockPos locateStructureOfInterest(ServerWorld world, BlockPos source) {
		// TODO - create a tag "TardisStructureLikesTag" to save on performance + to make this code simpler

		BlockPos found;
		int radius = 256;

		if (world.getRegistryKey() == World.NETHER) {
			found = getFortress(world, source, radius);
			if (found != null) return found;

			found = getBastion(world, source, radius);
            return found;
		} else if (world.getRegistryKey() == World.END) {
			found = getEndCity(world, source, radius);
            return found;
		} else if (world.getRegistryKey() == World.OVERWORLD) {
			found = getVillage(world, source, radius);
            return found;
		}

		return null;
	}

	public static BlockPos getVillage(ServerWorld world, BlockPos pos, int radius) {
		return world.locateStructure(StructureTags.VILLAGE, pos, radius, false);
	}

	public static BlockPos getFortress(ServerWorld world, BlockPos pos, int radius) {
		return getStructure(world, pos, radius, StructureKeys.FORTRESS);
	}

	public static BlockPos getBastion(ServerWorld world, BlockPos pos, int radius) {
		return getStructure(world, pos, radius, StructureKeys.BASTION_REMNANT);
	}

	public static BlockPos getEndCity(ServerWorld world, BlockPos pos, int radius) {
		return getStructure(world, pos, radius, StructureKeys.END_CITY);
	}

	public static BlockPos getStructure(ServerWorld world, BlockPos pos, int radius, RegistryKey<Structure> key) {
		Registry<Structure> registry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
		if (registry.getEntry(key).isEmpty()) return null;
		Pair<BlockPos, RegistryEntry<Structure>> pair = world.getChunkManager().getChunkGenerator().locateStructure(world, RegistryEntryList.of(registry.getEntry(key).get()), pos, radius, false);
		return pair != null ? pair.getFirst() : null;
	}

	@Override
	public boolean shouldFailOnNoPower() {
		return false;
	}

    @Override
	public long getDelayLength() {
		return 5 * 1000L;
	}
}
