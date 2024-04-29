package loqor.ait.tardis.exterior.variant.renegade.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.RenegadeExteriorModel;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientRenegadeVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/renegade/renegade_";

	protected ClientRenegadeVariant(String name) {
		super(new Identifier(AITMod.MOD_ID, "exterior/renegade/" + name));

		this.name = name;
	}

	@Override
	public ExteriorModel model() {
		return new RenegadeExteriorModel(RenegadeExteriorModel.getTexturedModelData().createModel());
	}

	@Override
	public Identifier texture() {
		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
	}

	@Override
	public Identifier emission() {
		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + "_emission" + ".png");
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.875f, 1.16f, 0.975f);
	}
}