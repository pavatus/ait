package loqor.ait.tardis.exterior.variant.bookshelf.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.BookshelfExteriorModel;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.PoliceBoxModel;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public abstract class ClientBookshelfVariant extends ClientExteriorVariantSchema {
	private final String name;
	protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/bookshelf/bookshelf_";

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
}