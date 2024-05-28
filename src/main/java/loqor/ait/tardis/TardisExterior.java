package loqor.ait.tardis;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.base.TardisLink;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import net.minecraft.block.entity.BlockEntity;

import java.util.Optional;

public class TardisExterior extends TardisLink {
	private ExteriorCategorySchema exterior;
	private ExteriorVariantSchema variant;

	public TardisExterior(ExteriorVariantSchema variant) {
		super(Id.EXTERIOR);
		this.exterior = variant.category();
		this.variant = variant;
	}

	public ExteriorCategorySchema getCategory() {
		if (exterior == null) {
			AITMod.LOGGER.error("Exterior Category was null! Changing to a random one...");
			this.setType(CategoryRegistry.getInstance().getRandom());
		}

		return exterior;
	}

	public ExteriorVariantSchema getVariant() {
		if (variant == null) {
			AITMod.LOGGER.error("Variant was null! Changing to a random one...");
			this.setVariant(ExteriorVariantRegistry.getInstance().getRandom(this.tardis()));
		}

		return variant;
	}

	public void setType(ExteriorCategorySchema exterior) {
		this.exterior = exterior;

		if (exterior != getVariant().category()) {
			AITMod.LOGGER.error("Force changing exterior variant to a random one to ensure it matches!");
			setVariant(ExteriorVariantRegistry.getInstance().pickRandomWithParent(exterior));
		}

		this.sync();
	}

	public void setVariant(ExteriorVariantSchema variant) {
		this.variant = variant;
		this.sync();
	}

	public Optional<ExteriorBlockEntity> findExteriorBlock() {
		BlockEntity found = this.getExteriorPos().getWorld().getBlockEntity(this.getExteriorPos());

		if (!(found instanceof ExteriorBlockEntity))
			return Optional.empty();

		return Optional.of((ExteriorBlockEntity) found);
	}
}
