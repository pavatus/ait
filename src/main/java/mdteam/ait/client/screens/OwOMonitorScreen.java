package mdteam.ait.client.screens;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.component.SlimSliderComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class OwOMonitorScreen extends BaseOwoScreen<FlowLayout> {
    private static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/gui/tardis/consoles/monitors/exterior_changer.png");
    private final UUID tardisid;
    private ExteriorEnum currentModel;
    private VariantEnum currentVariant;

    public OwOMonitorScreen(UUID tardisid) {
        this.tardisid = tardisid;
        this.updateTardis();
    }

    /*@Override
    public void renderBackgroundTexture(DrawContext context) {
        context.drawTexture(TEXTURE, 0, 0, 0, 0, 236, 133);
    }*/

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

    public VariantEnum getCurrentVariant() {
        return currentVariant == null ? tardis().getExterior().getVariant() : currentVariant;
    }

    public void setCurrentVariant(VariantEnum currentVariant) {
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

        /*rootComponent.child(container.surface((context, component) -> {
            context.drawTexture(TEXTURE, container.x(), container.y(), 0, 0, 236, 133);
        }));*/
    }
}
