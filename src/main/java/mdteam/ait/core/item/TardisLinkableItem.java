package mdteam.ait.core.item;

import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.tardis.Tardis;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

/**
 * I'd prefer this to use {@link mdteam.ait.core.tardis.link.TardisLinkable} but thats not done yet @TODO
 */
public abstract class TardisLinkableItem extends Item {
    private UUID tardisUuid;

    public TardisLinkableItem(Settings settings) {
        super(settings);
    }

    protected void link(UUID uuid) {
        this.tardisUuid = uuid;
    }
    protected void link(Tardis tardis) {
        this.link(tardis.getUuid());
    }

    public boolean hasTardis() {
        return this.tardisUuid != null && this.tardis() != null;
    }
    public Tardis tardis() {
        return TardisUtil.getTardisFromUuid(this.tardisUuid);
    }

    protected void writeToNbt(NbtCompound nbt) {
        if (this.hasTardis()) {
            nbt.putUuid("tardisUuid", this.tardisUuid);
        }
    }
    protected NbtCompound writeToNbt() {
        NbtCompound nbt = new NbtCompound();
        this.writeToNbt(nbt);
        return nbt;
    }

    protected void readFromNbt(NbtCompound nbt) {
        if (nbt.contains("tardisUuid")) this.link(nbt.getUuid("tardisUuid"));
    }
}
