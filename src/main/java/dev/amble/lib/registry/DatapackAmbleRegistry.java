package dev.amble.lib.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.impl.registry.sync.DynamicRegistriesImpl;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DatapackAmbleRegistry<T> extends DynamicAmbleRegistry<T> {

    public DatapackAmbleRegistry(Identifier id, Codec<T> codec, boolean sync) {
        this(id, codec, codec, sync);
    }

    public DatapackAmbleRegistry(Identifier id, Codec<T> codec) {
        this(id, codec, codec, true);
    }

    public DatapackAmbleRegistry(Identifier id, Codec<T> codec, Codec<T> networkCodec, boolean shouldSync) {
        super(id);

        if (shouldSync) {
            DynamicRegistries.registerSynced(this.getKey(), codec, networkCodec, DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY);
        } else {
            DynamicRegistries.register(this.getKey(), codec);
        }
    }

    public DatapackAmbleRegistry(Identifier id, Codec<T> codec, Codec<T> networkCodec) {
        this(id, codec, networkCodec, true);
    }
}
