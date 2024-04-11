package loqor.ait.client.registry.exterior.impl.classic;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ClassicExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.registry.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientClassicBoxVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/classic/classic_";

	protected ClientClassicBoxVariant(String name) {
		super(new Identifier(AITMod.MOD_ID, "exterior/classic/" + name));

		this.name = name;
	}


	@Override
	public ExteriorModel model() {
		return new ClassicExteriorModel(ClassicExteriorModel.getTexturedModelData().createModel());
	}

	@Override
	public Identifier texture() {
		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
	}

	@Override
	public Identifier snowTexture() {
		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + "_snow.png");
	}

	@Override
	public Identifier emission() {
		return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + "_emission" + ".png");
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.42f, 1.125f, 1.165f);
	}
}