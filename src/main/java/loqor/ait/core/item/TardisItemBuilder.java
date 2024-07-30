package loqor.ait.core.item;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.control.impl.DirectionControl;
import loqor.ait.tardis.data.EngineHandler;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.exterior.category.CapsuleCategory;
import loqor.ait.tardis.manager.TardisBuilder;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;

public class TardisItemBuilder extends Item {

	public static final Identifier DEFAULT_INTERIOR = new Identifier(AITMod.MOD_ID, "meridian");
	public static final Identifier DEFAULT_EXTERIOR = CapsuleCategory.REFERENCE;

	private final Identifier exterior;
	private final Identifier desktop;

	public TardisItemBuilder(Settings settings, Identifier exterior, Identifier desktopId) {
		super(settings);

		this.exterior = exterior;
		this.desktop = desktopId;
	}

	public TardisItemBuilder(Settings settings, Identifier exterior) {
		this(settings, exterior, DEFAULT_INTERIOR);
	}

	public TardisItemBuilder(Settings settings) {
		this(settings, DEFAULT_EXTERIOR);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();

		if (!(player instanceof ServerPlayerEntity serverPlayer))
			return ActionResult.PASS;

        if (!(world instanceof ServerWorld serverWorld))
            return ActionResult.PASS;

        if (player.getItemCooldownManager().isCoolingDown(this))
            return ActionResult.FAIL;

        if (context.getHand() != Hand.MAIN_HAND)
			return ActionResult.SUCCESS;

        DirectedGlobalPos.Cached pos = DirectedGlobalPos.Cached.create(serverWorld, context.getBlockPos().up(),
                DirectionControl.getGeneralizedRotation(RotationPropertyHelper.fromYaw(player.getBodyYaw())));

        BlockEntity entity = world.getBlockEntity(context.getBlockPos());

		if (entity instanceof ConsoleBlockEntity consoleBlock) {
            Tardis tardis = consoleBlock.tardis().get();

            if (tardis == null)
                return ActionResult.FAIL;

            TravelHandlerBase.State state = tardis.travel().getState();

            if (!(state == TravelHandlerBase.State.LANDED || state == TravelHandlerBase.State.FLIGHT))
                return ActionResult.PASS;

			consoleBlock.killControls();
			world.removeBlock(context.getBlockPos(), false);
			world.removeBlockEntity(context.getBlockPos());
			return ActionResult.SUCCESS;
		}

		ExteriorCategorySchema category = CategoryRegistry.getInstance().get(this.exterior);

        ServerTardisManager.getInstance().create(new TardisBuilder()
                .at(pos).desktop(DesktopRegistry.getInstance().get(this.desktop)).owner(serverPlayer)
                .exterior(ExteriorVariantRegistry.getInstance().pickRandomWithParent(category))
                .<FuelData>with(TardisComponent.Id.FUEL, fuel -> fuel.setCurrentFuel(fuel.getMaxFuel()))
                .<EngineHandler>with(TardisComponent.Id.ENGINE, engine -> {
                    engine.hasEngineCore().set(true);
                    engine.enablePower();
                }).<LoyaltyHandler>with(TardisComponent.Id.LOYALTY, loyalty
						-> loyalty.set(serverPlayer, new Loyalty(Loyalty.Type.OWNER))
				)
        );

		context.getStack().decrement(1);
        player.getItemCooldownManager().set(this, 20);
		return ActionResult.SUCCESS;
	}
}