package mdteam.ait;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import mdteam.ait.core.*;
import mdteam.ait.core.components.block.exterior.ExteriorNBTComponent;
import mdteam.ait.core.components.block.interior_door.InteriorDoorNBTComponent;
import mdteam.ait.core.components.block.radio.RadioNBTComponent;
import mdteam.ait.core.helper.TardisUtil;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import the.mdteam.ait.TardisManager;

public class AITMod implements ModInitializer {

	public static final ComponentKey<RadioNBTComponent> RADIONBT =
			ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "radionbt"), RadioNBTComponent.class);

	public static final ComponentKey<ExteriorNBTComponent> EXTERIORNBT =
			ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "exteriornbt"), ExteriorNBTComponent.class);

	public static final ComponentKey<InteriorDoorNBTComponent> INTERIORDOORNBT =
			ComponentRegistry.getOrCreate(new Identifier(AITMod.MOD_ID, "interiordoornbt"), InteriorDoorNBTComponent.class);

	public static final OwoItemGroup AIT_ITEM_GROUP = OwoItemGroup.builder(new Identifier(AITMod.MOD_ID, "item_group"),
			() -> Icon.of(AITItems.TARDIS_ITEM.getDefaultStack())).build();

	public static final String MOD_ID = "ait";

    public static final Logger LOGGER = LoggerFactory.getLogger("ait");

	@Override
	public void onInitialize() {
		AITItems.createExteriorItems();
		AITDesktops.init();
		AITItems.createInteriorItems();

		FieldRegistrationHandler.register(AITItems.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlocks.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITSounds.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITBlockEntityTypes.class, MOD_ID, false);
		FieldRegistrationHandler.register(AITEntityTypes.class, MOD_ID, false);

		AIT_ITEM_GROUP.initialize();
		// AITDesktops.init();
		TardisUtil.init();

		// makes sure the initialization for tardis managers runs
		TardisManager.getInstance();
		//System.out.println(TardisManager.getInstance());
	}
}