package dev.amble.ait.registry.v2;

import com.mojang.serialization.Codec;
import dev.amble.ait.AITMod;
import dev.amble.ait.core.tardis.control.Control;
import dev.amble.ait.data.datapack.DatapackCategory;
import dev.amble.ait.data.schema.exterior.ExteriorCategorySchema;
import dev.amble.ait.registry.CategoryRegistry;
import dev.amble.lib.registry.DatapackAmbleRegistry;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ExteriorCategoryRegistry extends DatapackAmbleRegistry<ExteriorCategorySchema> {

    public ExteriorCategoryRegistry() {
        super(AITMod.id("categories"), DatapackCategory.CODEC);
    }
}
