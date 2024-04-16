package loqor.ait.client.registry.exterior.impl.capsule;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.CapsuleExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.registry.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientCapsuleVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/capsule/capsule_";

	protected ClientCapsuleVariant(String name) {
		super(new Identifier(AITMod.MOD_ID, "exterior/capsule/" + name));

		this.name = name;
	}


	@Override
	public ExteriorModel model() {
		return new CapsuleExteriorModel(CapsuleExteriorModel.getTexturedModelData().createModel());
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
		return new Vector3f(0.5f, 1.2f, 1.15f);
	}
}