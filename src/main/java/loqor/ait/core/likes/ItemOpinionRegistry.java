package loqor.ait.core.likes;

import java.util.Optional;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.registry.datapack.SimpleDatapackRegistry;

public class ItemOpinionRegistry extends SimpleDatapackRegistry<ItemOpinion> {
    private static final ItemOpinionRegistry instance = new ItemOpinionRegistry();


    public ItemOpinionRegistry() {
        super(ItemOpinion::fromInputStream, ItemOpinion.CODEC, "opinion_item", "opinion/item", true);
    }

    public static ItemOpinion LIKES_ENDER_EYE;

    @Override
    protected void defaults() {
        LIKES_ENDER_EYE = register(new ItemOpinion(new Identifier(AITMod.MOD_ID, "likes_ender_eye"), Items.ENDER_EYE.getDefaultStack(), 10));
    }

    @Override
    public ItemOpinion fallback() {
        return LIKES_ENDER_EYE;
    }

    public Optional<ItemOpinion> get(ItemStack stack) {
        return REGISTRY.values().stream().filter(opinion -> opinion.stack().equals(stack)).findFirst();
    }
    public Optional<ItemOpinion> get(Item item) {
        return REGISTRY.values().stream().filter(opinion -> opinion.stack().getItem().equals(item)).findFirst();
    }

    public static ItemOpinionRegistry getInstance() {
        return instance;
    }
}
