package mdteam.ait.client.registry.exterior.impl.doom;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.DoomExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.exteriors.DoomConstants;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientDoomVariant extends ClientExteriorVariantSchema {
	public ClientDoomVariant() {
		super(new Identifier(AITMod.MOD_ID, "exterior/doom"));
	}


	@Override
	public ExteriorModel model() {
		return new DoomExteriorModel(DoomExteriorModel.getTexturedModelData().createModel());
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.5f, 1.5f, 0f);
	}

	@Override
	public Identifier texture() {
		return DoomConstants.DOOM_FRONT_BACK;
	}

	@Override
	public Identifier emission() {
		return DoomConstants.DOOM_TEXTURE_EMISSION;
	}
}