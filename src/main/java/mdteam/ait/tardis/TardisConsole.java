package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.data.TardisLink;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

import static mdteam.ait.tardis.util.TardisUtil.findTardisByInterior;

// TODO - move variant and type over to here
public class TardisConsole extends TardisLink {
    private final UUID uuid;
    private AbsoluteBlockPos position;

    protected TardisConsole(Tardis tardis, AbsoluteBlockPos pos, UUID uuid) {
        super(tardis, "console");
        this.position = pos;
        this.uuid = uuid;
    }
    public TardisConsole(Tardis tardis, AbsoluteBlockPos pos) {
        this(tardis, pos, UUID.randomUUID());
    }

    public AbsoluteBlockPos position() {
        return this.position;
    }
    public UUID uuid() {
        return this.uuid;
    }

    public @Nullable ConsoleBlockEntity getEntity() {
        return (ConsoleBlockEntity) TardisUtil.getTardisDimension().getBlockEntity(this.position);
    }

    @Override
    public Optional<Tardis> getTardis() {
        if(this.tardisId == null) {
            if (!this.validate()) return Optional.empty();

            Tardis found = findTardisByInterior(this.position(), !this.getEntity().getWorld().isClient());
            if (found != null)
                this.setTardis(found);
        }
        return super.getTardis();
    }

    public boolean validate() {
        if (this.shouldRemove()) {
            AITMod.LOGGER.warn("Removing " + this.uuid() + " from desktop as it was invalid");
            this.remove();
            return false;
        }

        return true;
    }
    private boolean shouldRemove() {
        return this.getEntity() == null;
    }
    private void remove() {
        if (this.getTardis().isEmpty()) return;

        Tardis tardis = this.getTardis().get();
        tardis.getDesktop().removeConsole(this);
    }
}
