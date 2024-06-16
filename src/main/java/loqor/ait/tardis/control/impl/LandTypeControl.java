package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class LandTypeControl extends Control {
	public LandTypeControl() {
		super("land_type");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		PropertiesHandler.set(tardis, PropertiesHandler.FIND_GROUND, !PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.FIND_GROUND));
		messagePlayer(player, PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.FIND_GROUND));

		return false;
	}

	public void messagePlayer(ServerPlayerEntity player, boolean var) {
		Text on = Text.translatable("tardis.message.control.landtype.on");
		Text off = Text.translatable("tardis.message.control.landtype.off");
		player.sendMessage(var ? on : off, true);
	}
}
