package loqor.ait.tardis.data.loyalty;

import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;
import loqor.ait.registry.ConsoleVariantRegistry;
import loqor.ait.registry.DesktopRegistry;
import loqor.ait.registry.ExteriorVariantRegistry;
import loqor.ait.registry.datapack.Nameable;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.function.Function;

public class LoyaltyHandler extends TardisLink {
    private final Map<UUID, Loyalty> data;

    public LoyaltyHandler(Tardis tardis, HashMap<UUID, Loyalty> data) {
        super(tardis, TypeId.LOYALTY);
        this.data = data;
    }

    public LoyaltyHandler(Tardis tardis) {
        this(tardis, new HashMap<>());
    }

    public Map<UUID, Loyalty> data() {
        return this.data;
    }

    public Loyalty get(PlayerEntity player) {
        return this.data.getOrDefault(player.getUuid(), new Loyalty(Loyalty.Type.NEUTRAL));
    }

    public Loyalty set(PlayerEntity player, Loyalty loyalty) {
        this.data.put(player.getUuid(), loyalty);
        this.unlockInteriorViaLoyalty((ServerPlayerEntity) player, loyalty); // safe cast because this should only be called on server anyway

        return loyalty;
    }

    @Override
    public void tick(ServerWorld world) {
        super.tick(world);
        if(world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
            Optional<Tardis> tardis = this.findTardis();
            if(tardis.isEmpty()) return;
            List<ServerPlayerEntity> list = TardisUtil.getPlayersInInterior(tardis.get());
            for(ServerPlayerEntity player : list) {
                this.addLevel(player, (this.get(player).level() >= Loyalty.Type.NEUTRAL.level &&
                        this.get(player).level() < Loyalty.Type.COMPANION.level &&
                        AITMod.RANDOM.nextInt(0, 40) == 14) ? 1 : 0);
            }
        }
    }

    public void update(ServerPlayerEntity player, Function<Loyalty, Loyalty> consumer) {
        Loyalty current = this.get(player);
        current = consumer.apply(current);

        this.unlockInteriorViaLoyalty(player, current);
        this.set(player, current);
    }

    public void unlockInteriorViaLoyalty(ServerPlayerEntity player, Loyalty loyalty) {
        Optional<Tardis> tardis = this.findTardis();

        if (tardis.isEmpty() || !(tardis.get() instanceof ServerTardis serverTardis))
            return;

        ConsoleVariantRegistry.getInstance().unlock(serverTardis, loyalty, schema -> this.playUnlockEffects(player, schema));
        DesktopRegistry.getInstance().unlock(serverTardis, loyalty, schema -> this.playUnlockEffects(player, schema));
        ExteriorVariantRegistry.getInstance().unlock(serverTardis, loyalty, schema -> this.playUnlockEffects(player, schema));
    }

    private void playUnlockEffects(ServerPlayerEntity player, Nameable nameable) {
        player.getServerWorld().playSound(null, player.getBlockPos(),
                SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 0.2F, 1.0F);

        player.sendMessage(Text.literal(nameable.name() + " unlocked!")
                .formatted(Formatting.BOLD, Formatting.ITALIC, Formatting.GOLD), false);
    }

    public void addLevel(ServerPlayerEntity player, int level) {
        this.update(player, loyalty -> loyalty.add(level));
    }

    public void subLevel(ServerPlayerEntity player, int level) {
        this.addLevel(player, -level);
    }
}
