package loqor.ait.client.registry.exterior.impl.plinth;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PlinthExteriorModel;
import loqor.ait.client.registry.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientPlinthVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/plinth/plinth_";

	protected ClientPlinthVariant(String name) {
		super(new Identifier(AITMod.MOD_ID, "exterior/plinth/" + name));

		this.name = name;
	}

	@Override
	public ExteriorModel model() {
		return new PlinthExteriorModel(PlinthExteriorModel.getTexturedModelData().createModel());
	}

	@Override
	public Identifier texture() {
		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
	}

	@Override
	public Identifier emission() {
		return null;
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.5f, 1.2f, 1.05f);
	}
}