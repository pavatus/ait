package dev.pavatus.lib.mixin;

import dev.pavatus.lib.item.AItem;
import dev.pavatus.lib.item.AItemSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

@Mixin(Item.class)
public class ItemMixin implements AItem {

    private ItemGroup a$group;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Item.Settings settings, CallbackInfo ci) {
        if (settings instanceof AItemSettings ais)
            this.a$group = ais.group();
    }

    @Override
    public ItemGroup a$group() {
        return this.a$group;
    }
}
