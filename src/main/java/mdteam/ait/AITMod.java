package mdteam.ait;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import mdteam.ait.core.*;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.components.block.radio.RadioNBTComponent;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mdteam.ait.tardis.TardisManager;

public class AITMod implements ModInitializer {
	public static final String MOD_ID = "ait";
	public static final Logger LOGGER = LoggerFactory.getLogger("ait");
	public static final OwoItemGroup AIT_ITEM_GROUP = OwoItemGroup.builder(new Identifier(AITMod.MOD_ID, "item_group"),
			() -> Icon.of(AITItems.TARDIS_ITEM.getDefaultStack())).build();
	public static final ComponentKey<RadioNBTComponent> RADIONBT =
			ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "radionbt"), RadioNBTComponent.class);
	@Override
	public void onInitialize() {
		AITDesktops.init();
		FieldRegistrationHandler.register(AITItems.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlocks.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITSounds.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlockEntityTypes.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITEntityTypes.class, MOD_ID, false);
		AIT_ITEM_GROUP.initialize();
		PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
			if(!world.isClient()) {
				if (world.getRegistryKey().getRegistry() == AITDimensions.TARDIS_DIM_WORLD.getRegistry()) {
					return !(world.getBlockEntity(pos) instanceof ConsoleBlockEntity);
				}
			}
			return !(state.getBlock() instanceof ConsoleBlock || state.getBlock() instanceof ExteriorBlock);
		}));
		TardisUtil.init();
		TardisManager.getInstance();
		TardisManager.init();
	}
}