package loqor.ait.core.tardis.util;

import java.util.Objects;

import org.apache.commons.lang3.NotImplementedException;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.core.tardis.handler.ServerHumHandler;
import loqor.ait.core.tardis.manager.TardisBuilder;
import loqor.ait.data.hum.Hum;
import loqor.ait.data.schema.console.ConsoleVariantSchema;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.HumRegistry;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

public enum DefaultThemes {
    CORAL("coral", "exterior/police_box/coral", "coral", "console/coral"),
    PRIME("accursed", "exterior/capsule/default", "prime", "console/hartnell"),
    TOYOTA("toyota", "exterior/police_box/default", "toyota", "console/toyota"),
    RENAISSANCE("renaissance", "exterior/police_box/renaissance", "renaissance", "todo"),
    WAR(AITMod.id("war"), new Identifier("frooploof", "coral_war"), AITMod.id("coral"), AITMod.id("console/coral")),
    CRYSTALLINE("crystalline", "exterior/police_box/renaissance", "renaissance", "console/crystalline");
    private final Identifier desktop;
    private final Identifier exterior;
    private final Identifier hum;
    private final Identifier console;


    DefaultThemes(String desktop, String exterior, String hum, String console) {
        this(AITMod.id(desktop), AITMod.id(exterior), AITMod.id(hum), AITMod.id(console));
    }
    DefaultThemes(Identifier desktop, Identifier exterior, Identifier hum, Identifier console) {
        this.desktop = desktop;
        this.exterior = exterior;
        this.hum = hum;
        this.console = console;
    }

    public TardisDesktopSchema desktop() {
        return DesktopRegistry.getInstance().get(desktop);
    }
    public ExteriorVariantSchema exterior() {
        return ExteriorVariantRegistry.getInstance().get(exterior);
    }
    public Hum hum() {
        return HumRegistry.getInstance().get(hum);
    }
    public ConsoleVariantSchema console() { // todo
        if (Objects.equals(console.getPath(), "todo")) throw new NotImplementedException("DefaultThemes.console() is not implemented for " + this);

        return ConsoleVariantRegistry.getInstance().get(console);
    }
    public TardisBuilder apply(TardisBuilder builder) {
        builder.exterior(exterior());
        builder.desktop(desktop());
        builder.<ServerHumHandler>with(TardisComponent.Id.HUM, handler -> handler.setHum(this.hum()));

        return builder;
    }

    public static DefaultThemes getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
