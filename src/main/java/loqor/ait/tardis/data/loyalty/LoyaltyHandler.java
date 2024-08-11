package loqor.ait.tardis.data.loyalty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;

import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.data.base.Nameable;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.SonicRegistry;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public class LoyaltyHandler extends TardisComponent implements TardisTickable {
    private final Map<UUID, Loyalty> data;

    public LoyaltyHandler(HashMap<UUID, Loyalty> data) {
        super(Id.LOYALTY);
        this.data = data;
    }

    public LoyaltyHandler() {
        this(new HashMap<>());
    }

    public Map<UUID, Loyalty> data() {
        return this.data;
    }

    public Loyalty get(PlayerEntity player) {
        return this.data.getOrDefault(player.getUuid(), new Loyalty(Loyalty.Type.NEUTRAL));
    }

    public Loyalty set(ServerPlayerEntity player, Loyalty loyalty) {
        this.data.put(player.getUuid(), loyalty);
        this.unlock(player, loyalty);

        this.sync();
        return loyalty;
    }

    @Override
    public void tick(ServerWorld world) {
        if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return;

        if (world.getServer().getTicks() % 20 != 0)
            return;

        Tardis tardis = this.tardis();
        List<ServerPlayerEntity> list = TardisUtil.getPlayersInsideInterior(tardis);

        for (ServerPlayerEntity player : list) {
            this.addLevel(player,
                    (this.get(player).level() >= Loyalty.Type.NEUTRAL.level
                            && this.get(player).level() < Loyalty.Type.COMPANION.level
                            && AITMod.RANDOM.nextInt(0, 20) == 14) ? 1 : 0);
        }
    }

    public void update(ServerPlayerEntity player, Function<Loyalty, Loyalty> consumer) {
        Loyalty current = this.get(player);
        current = consumer.apply(current);

        this.set(player, current);
    }

    public void unlock(ServerPlayerEntity player, Loyalty loyalty) {
        ServerTardis tardis = (ServerTardis) this.tardis();

        boolean playSound = ConsoleVariantRegistry.getInstance().tryUnlock(tardis, loyalty,
                schema -> this.playUnlockEffects(player, schema));
        playSound = DesktopRegistry.getInstance().tryUnlock(tardis, loyalty,
                schema -> this.playUnlockEffects(player, schema)) || playSound;
        playSound = ExteriorVariantRegistry.getInstance().tryUnlock(tardis, loyalty,
                schema -> this.playUnlockEffects(player, schema)) || playSound;
        playSound = SonicRegistry.getInstance().tryUnlock(tardis, loyalty,
                schema -> this.playUnlockEffects(player, schema)) || playSound;

        if (playSound)
            player.getServerWorld().playSound(null, player.getBlockPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                    SoundCategory.PLAYERS, 0.2F, 1.0F);
    }

    private void playUnlockEffects(ServerPlayerEntity player, Nameable nameable) {
        player.sendMessage(nameable.text().copy().append(" unlocked!").formatted(Formatting.BOLD, Formatting.ITALIC,
                Formatting.GOLD), false);
    }

    public void addLevel(ServerPlayerEntity player, int level) {
        this.update(player, loyalty -> loyalty.add(level));
    }

    public void subLevel(ServerPlayerEntity player, int level) {
        this.addLevel(player, -level);
    }
}
