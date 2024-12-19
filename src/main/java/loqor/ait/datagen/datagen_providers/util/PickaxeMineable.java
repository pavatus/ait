package loqor.ait.datagen.datagen_providers.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.block.Block;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PickaxeMineable {
    Tool tool() default Tool.NONE;

    enum Tool {
        NONE(null),
        STONE(BlockTags.NEEDS_STONE_TOOL),
        IRON(BlockTags.NEEDS_IRON_TOOL),
        DIAMOND(BlockTags.NEEDS_DIAMOND_TOOL),;

        public final TagKey<Block> tag;

        Tool(TagKey<Block> tag) {
            this.tag = tag;
        }
    }
}
