package mdteam.ait.tardis.data;

import mdteam.ait.core.item.KeyItem;
import mdteam.ait.tardis.Tardis;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.UUID;

public class CloakData extends TardisLink {
    private boolean isCloaked;

    // @TODO its been a minute since ive had to server to client logic bullshit so duzo you do it while i do components
    /*private float alphaBasedOnDistance = 1.0F;*/

    public CloakData(Tardis tardis) {
        super(tardis, "cloak");
    }

    public void enable() {
        this.isCloaked = true;
    }

    public void disable() {
        this.isCloaked = false;
    }

    public boolean isEnabled() {
        return this.isCloaked = true;
    }

    public void toggle() {
        if (isEnabled()) disable();
        else enable();
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);

        if (getTardis().isEmpty() || this.isEnabled() && !getTardis().get().hasPower()) {
            this.disable();
            return;
        }

        if(this.getTardis().get().getExterior().getExteriorPos() == null) return;
        List<PlayerEntity> players = this.getTardis().get().getTravel().getPosition().getWorld().getEntitiesByClass(PlayerEntity.class,
                new Box(getTardis().get().getExterior().getExteriorPos()).expand(3), EntityPredicates.EXCEPT_SPECTATOR);
        for (PlayerEntity player : players) {
            ItemStack stack = KeyItem.getFirstKeyStackInInventory(player);
            if (stack != null && stack.getItem() instanceof KeyItem) {
                NbtCompound tag = stack.getOrCreateNbt();
                if (!tag.contains("tardis")) {
                    return;
                }
                if(UUID.fromString(tag.getString("tardis")).equals(this.getTardis().get().getUuid())) {
                    //this.setAlphaBasedOnDistance(0.45f);
                    return;
                }/* else {
                    this.setAlphaBasedOnDistance(0.105f);
                }*/
            }
        }

        if (!this.isEnabled()) return;

        this.getTardis().get().removeFuel(2); // idle drain of 2 fuel per tick
    }
}
