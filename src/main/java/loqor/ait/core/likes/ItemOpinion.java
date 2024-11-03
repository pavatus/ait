package loqor.ait.core.likes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import loqor.ait.AITMod;
import loqor.ait.api.Identifiable;
import loqor.ait.core.tardis.ServerTardis;

public record ItemOpinion(Identifier id, ItemStack stack, int cost, int loyalty) implements Identifiable, Opinion {
    public static final Codec<ItemOpinion> CODEC = Codecs.exceptionCatching(RecordCodecBuilder.create(instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(ItemOpinion::id),
                    ItemStack.CODEC.fieldOf("stack").forGetter(ItemOpinion::stack),
                    Codec.INT.fieldOf("cost").forGetter(ItemOpinion::cost),
                    Codec.INT.fieldOf("loyalty").forGetter(ItemOpinion::loyalty))
            .apply(instance, ItemOpinion::new)));


    public ItemOpinion {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        if (loyalty < 0) {
            throw new IllegalArgumentException("Loyalty cannot be negative");
        }
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    @Override
    public void apply(ServerTardis tardis, ServerPlayerEntity target) {
        Opinion.super.apply(tardis, target);

        target.getInventory().getMainHandStack().decrement(this.stack().getCount()); // assume its in the main hand
        target.addExperience(-this.cost);
    }

    @Override
    public Type type() {
        return Type.ITEM;
    }

    @Override
    public String toString() {
        return "ItemOpinion{" +
                "id=" + id +
                ", stack=" + stack +
                ", cost=" + cost +
                ", loyalty=" + loyalty +
                '}';
    }

    public static ItemOpinion fromInputStream(InputStream stream) {
        return fromJson(JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject());
    }

    public static ItemOpinion fromJson(JsonObject json) {
        AtomicReference<ItemOpinion> created = new AtomicReference<>();

        CODEC.decode(JsonOps.INSTANCE, json).get().ifLeft(var -> created.set(var.getFirst())).ifRight(err -> {
            created.set(null);
            AITMod.LOGGER.error("Error decoding datapack item opinion: {}", err);
        });

        return created.get();
    }
}
