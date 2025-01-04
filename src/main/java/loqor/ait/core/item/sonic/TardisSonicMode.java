package loqor.ait.core.item.sonic;

import loqor.ait.data.schema.sonic.SonicSchema;
import net.minecraft.util.Identifier;

public class TardisSonicMode extends SonicMode {

    protected TardisSonicMode(int index) {
        super(index);
    }

    @Override
    public int maxTime() {
        return 2 * 20;
    }

    @Override
    public Identifier model(SonicSchema.Models models) {
        return models.tardis();
    }
}
