package mdteam.ait.registry;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.console.type.*;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class ConsoleRegistry {
	public static final SimpleRegistry<ConsoleTypeSchema> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<ConsoleTypeSchema>ofRegistry(new Identifier(AITMod.MOD_ID, "console"))).buildAndRegister();

	public static ConsoleTypeSchema register(ConsoleTypeSchema schema) {
		return Registry.register(REGISTRY, schema.id(), schema);
	}

	public static ConsoleTypeSchema CORAL;
	public static ConsoleTypeSchema HARTNELL;
	public static ConsoleTypeSchema COPPER;
	public static ConsoleTypeSchema TOYOTA;
	public static ConsoleTypeSchema ALNICO;
	public static ConsoleTypeSchema STEAM;

	public static void init() {
		HARTNELL = register(new HartnellType());
		CORAL = register(new CoralType());
		//COPPER = register(new CopperConsole());
		TOYOTA = register(new ToyotaType());
		ALNICO = register(new AlnicoType());
		STEAM = register(new SteamType());
	}
}
