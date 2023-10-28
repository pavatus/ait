package mdteam.ait.api.tardis;

import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;

import java.io.Serializable;
import java.util.Optional;

/**
 * Should be immutable.
 */
public interface IDesktopSchema extends Serializable {
    Identifier id();
    Optional<StructureTemplate> findTemplate();
}
