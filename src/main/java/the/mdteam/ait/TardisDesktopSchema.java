package the.mdteam.ait;

import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.core.helper.TardisUtil;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;

import java.util.Optional;

public abstract class TardisDesktopSchema implements IDesktopSchema {

    private final Identifier id;

    public TardisDesktopSchema(Identifier id) {
        this.id = id;
    }

    @Override
    public Identifier id() {
        return id;
    }

    @Override
    public Optional<StructureTemplate> findTemplate() {
        return TardisUtil.getTardisDimension().getStructureTemplateManager().getTemplate(this.getStructureLocation());
    }

    private Identifier getStructureLocation() {
        Identifier id = this.id();

        return new Identifier(
                id.getNamespace(), "interiors/" + id.getPath()
        );
    }
}
