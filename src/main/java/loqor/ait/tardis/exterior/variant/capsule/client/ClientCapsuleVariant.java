package loqor.ait.tardis.exterior.variant.capsule.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.CapsuleExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.core.data.datapack.exterior.BiomeOverrides;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientCapsuleVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/capsule";
	protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID, CATEGORY_PATH + "/capsule.png");
	protected static final String TEXTURE_PATH = CATEGORY_PATH + "/capsule_";

	protected static final BiomeOverrides OVERRIDES = BiomeOverrides.of(
			type -> type.getTexture(CATEGORY_IDENTIFIER)
	);

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

	@Override
	public BiomeOverrides overrides() {
		return OVERRIDES;
	}
}