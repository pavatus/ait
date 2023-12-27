package mdteam.ait.client.screens;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.ExteriorEnum;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OwOMonitorScreen extends BaseOwoScreen<FlowLayout> {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/exterior_changer.png");
    private final UUID tardisid;
    private ExteriorEnum currentModel;
    private ExteriorVariantSchema currentVariant;

    public OwOMonitorScreen(UUID tardisid) {
        this.tardisid = tardisid;
        this.updateTardis();
    }

    protected Tardis tardis() {
        return ClientTardisManager.getInstance().getLookup().get(this.tardisid);
    }

    protected Tardis updateTardis() {
        ClientTardisManager.getInstance().ask(this.tardisid);
        return tardis();
    }

    public ExteriorEnum getCurrentModel() {
        return currentModel == null ? tardis().getExterior().getType() : currentModel;
    }

    public void setCurrentModel(ExteriorEnum currentModel) {
        this.currentModel = currentModel;
    }

    public ExteriorVariantSchema getCurrentVariant() {
        return currentVariant == null ? tardis().getExterior().getVariant() : currentVariant;
    }

    public void setCurrentVariant(ExteriorVariantSchema currentVariant) {
        this.currentVariant = currentVariant;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);
        FlowLayout container = Containers.horizontalFlow(Sizing.fixed(236), Sizing.fixed(133));
        Component panel = Components.texture(TEXTURE, 0, 133, 20, 40);
        List<Component> listing = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            listing.add(panel.id("panel_" + i));
        }
        ScrollContainer<Component> scrollContainer = Containers.verticalScroll(
                Sizing.content(), Sizing.fill(98), Components.list(
                listing, (layout) -> {
            Containers.horizontalFlow(Sizing.fill(100), Sizing.content());
            }, (s) -> Containers.stack(Sizing.content(),
                                        Sizing.content())
                        .child(Components.label(Text.literal("huh?"))).child(Components.button(Text.empty(),
                                        buttonComponent ->
                                System.out.println("hello :)"))).padding(Insets.of(10))
                                .surface(Surface.TOOLTIP)
                                .alignment(HorizontalAlignment.CENTER,VerticalAlignment.TOP)
                        /*.verticalAlignment(VerticalAlignment.TOP)
                        .horizontalAlignment(HorizontalAlignment.LEFT)*/, false));
        Component left = Components.button(Text.literal("<"), buttonComponent -> System.out.println(this.tardisid)).sizing(Sizing.fixed(15))
                .positioning(Positioning.absolute(20, 20));
        Component right = Components.button(Text.literal(">"), buttonComponent -> System.out.println(this.tardisid)).sizing(Sizing.fixed(15))
                .positioning(Positioning.absolute(25, 20));

        container.child(left);
        container.child(right);

        container.child(scrollContainer.surface(Surface.PANEL_INSET).positioning(Positioning.absolute((container.horizontalSizing().get().value / 2) - 100, 0)));
        rootComponent.child(container.surface(Surface.DARK_PANEL));
    }
}
