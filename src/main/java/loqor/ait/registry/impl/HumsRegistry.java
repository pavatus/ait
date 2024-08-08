package loqor.ait.registry.impl;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.sound.HumSound;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class HumsRegistry {
	public static final SimpleRegistry<HumSound> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<HumSound>ofRegistry(new Identifier(AITMod.MOD_ID, "hum"))).buildAndRegister();

	public static HumSound register(HumSound schema) {
		return Registry.register(REGISTRY, schema.id(), schema);
	}

	public static HumSound TOYOTA;
	public static HumSound CORAL;
	public static HumSound EIGHT;
	public static HumSound COPPER;
	public static HumSound EXILE;
	public static HumSound PRIME;
	public static HumSound TOKAMAK;
	public static HumSound BEACON;

	public static void init() {
		TOYOTA = register(HumSound.create(AITMod.MOD_ID, "toyota", AITSounds.TOYOTA_HUM));
		CORAL = register(HumSound.create(AITMod.MOD_ID, "coral", AITSounds.CORAL_HUM));
		EIGHT = register(HumSound.create(AITMod.MOD_ID, "eight", AITSounds.EIGHT_HUM));
		COPPER = register(HumSound.create(AITMod.MOD_ID, "copper", AITSounds.COPPER_HUM));
		EXILE = register(HumSound.create(AITMod.MOD_ID, "exile", AITSounds.EXILE_HUM));
		PRIME = register(HumSound.create(AITMod.MOD_ID, "prime", AITSounds.PRIME_HUM));
		TOKAMAK = register(HumSound.create(AITMod.MOD_ID, "tokamak", AITSounds.TOKAMAK_HUM));
		BEACON = register(HumSound.create(AITMod.MOD_ID, "beacon", SoundEvents.BLOCK_BEACON_AMBIENT));
	}
}
