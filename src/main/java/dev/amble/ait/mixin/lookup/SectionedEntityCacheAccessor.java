package dev.amble.ait.mixin.lookup;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.entity.EntityLike;
import net.minecraft.world.entity.EntityTrackingSection;
import net.minecraft.world.entity.SectionedEntityCache;

@Mixin(SectionedEntityCache.class)
public interface SectionedEntityCacheAccessor<T extends EntityLike> {

    @Accessor("trackingSections")
    Long2ObjectMap<EntityTrackingSection<T>> getTrackingSections();

    @Accessor("trackedPositions")
    LongSortedSet getTrackedPositions();
}
