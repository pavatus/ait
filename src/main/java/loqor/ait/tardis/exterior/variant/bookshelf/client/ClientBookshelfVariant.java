package loqor.ait.tardis.exterior.variant.bookshelf.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.BookshelfExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.tardis.data.BiomeHandler;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public abstract class ClientBookshelfVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String CATEGORY_PATH = "textures/blockentities/exteriors/bookshelf";
	protected static final Identifier CATEGORY_IDENTIFIER = new Identifier(AITMod.MOD_ID, CATEGORY_PATH + "/bookshelf.png");
	protected static final String TEXTURE_PATH = CATEGORY_PATH + "/bookshelf_";

	protected ClientBookshelfVariant(String name) {
		super(new Identifier(AITMod.MOD_ID, "exterior/bookshelf/" + name));

		this.name = name;
	}


	@Override
	public ExteriorModel model() {
		return new BookshelfExteriorModel(BookshelfExteriorModel.getTexturedModelData().createModel());
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
		return new Vector3f(0.56f, 1.2f, 1.2f);
	}

	@Override
	public Identifier getBiomeTexture(BiomeHandler.BiomeType biomeType) {
		return switch(biomeType) {
			case DEFAULT -> BiomeHandler.BiomeType.DEFAULT.getTexture(null);
			case SNOWY -> BiomeHandler.BiomeType.SNOWY.getTexture(CATEGORY_IDENTIFIER);
			case SCULK -> BiomeHandler.BiomeType.SCULK.getTexture(CATEGORY_IDENTIFIER);
			case SANDY -> BiomeHandler.BiomeType.SANDY.getTexture(CATEGORY_IDENTIFIER);
			case RED_SANDY -> BiomeHandler.BiomeType.RED_SANDY.getTexture(CATEGORY_IDENTIFIER);
			case MUDDY -> BiomeHandler.BiomeType.MUDDY.getTexture(CATEGORY_IDENTIFIER);
			case CHORUS -> BiomeHandler.BiomeType.CHORUS.getTexture(CATEGORY_IDENTIFIER);
			case CHERRY -> BiomeHandler.BiomeType.CHERRY.getTexture(CATEGORY_IDENTIFIER);
		};
	}
}