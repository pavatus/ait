package loqor.ait.tardis.wrapper.server;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.TardisTickable;
import loqor.ait.tardis.exterior.category.ExteriorCategorySchema;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTardisExterior extends TardisExterior implements TardisTickable {

	public ServerTardisExterior(Tardis tardis, ExteriorCategorySchema exterior, ExteriorVariantSchema variant) {
		super(tardis, exterior, variant);
	}

	@Override
	public void setType(ExteriorCategorySchema exterior) {
		super.setType(exterior);
		this.sync();
	}

	@Override
	public void setVariant(ExteriorVariantSchema variant) {
		super.setVariant(variant);

		this.sync();
	}

	@Override
	public void startTick(MinecraftServer server) {
	}

	@Override
	public void tick(MinecraftServer server) {
	}

	@Override
	public void tick(ServerWorld world) {
	}

	@Override
	public void tick(MinecraftClient client) {
	}
}
