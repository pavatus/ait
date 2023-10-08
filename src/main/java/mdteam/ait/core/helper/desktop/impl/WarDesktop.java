package mdteam.ait.core.helper.desktop.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mdteam.ait.core.helper.desktop.DesktopSchema;
import mdteam.ait.core.helper.structures.DesktopStructure;
import net.minecraft.util.Identifier;

public class WarDesktop extends DesktopSchema {
    public WarDesktop(String structureName) {
        super(structureName);
    }
    public WarDesktop(Identifier location, String structureName) {
        super(location, structureName);
    }
    public WarDesktop() {
        this("war");
    }

    private static final Codec<DesktopStructure> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Identifier.CODEC.fieldOf("structure").forGetter(DesktopStructure::getLocation),
                Codec.STRING.orElse("Placeholder").fieldOf("name_component").forGetter(DesktopStructure::getStructureName)
        ).apply(instance, WarDesktop::new);
    });
}