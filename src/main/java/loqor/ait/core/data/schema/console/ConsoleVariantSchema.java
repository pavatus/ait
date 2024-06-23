package loqor.ait.core.data.schema.console;

import com.google.gson.*;
import loqor.ait.AITMod;
import loqor.ait.core.data.BasicSchema;
import loqor.ait.registry.impl.console.ConsoleRegistry;
import loqor.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import loqor.ait.registry.impl.console.variant.ConsoleVariantRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.data.loyalty.Loyalty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.lang.reflect.Type;

/**
 * This class is for variants of a {@link ConsoleTypeSchema} and can be changed in game to the players desires
 * <br><br>
 * It's information should be final and set once on its creation during registration
 * <br><br>
 * It should be registered in {@link ConsoleVariantRegistry#REGISTRY} otherwise it wont show up in-game
 * <br><br>
 * It should only be gotten from {@link ConsoleVariantRegistry#REGISTRY#get(Identifier)} using its {@link #id} and only created once
 * <br><br>
 *
 * @author duzo
 * @see ConsoleVariantRegistry#REGISTRY
 */
public abstract class ConsoleVariantSchema extends BasicSchema implements Unlockable {

	private final Identifier parent;
	private final Identifier id;
	private final Loyalty loyalty;

	@Environment(EnvType.CLIENT)
	private ClientConsoleVariantSchema cachedSchema;

	protected ConsoleVariantSchema(Identifier parent, Identifier id, Loyalty loyalty) {
		this.parent = parent;
		this.id = id;
		this.loyalty = loyalty;
	}

	protected ConsoleVariantSchema(Identifier parent, Identifier id) {
		this(parent, id, Loyalty.MIN);
	}

	@Override
	public Identifier id() {
		return id;
	}

	@Override
	public Loyalty getRequirement() {
		return loyalty;
	}

	@Override
	public UnlockType unlockType() {
		return UnlockType.CONSOLE;
	}

	public Identifier parentId() {
		return this.parent;
	}

	public ConsoleTypeSchema parent() {
		return ConsoleRegistry.REGISTRY.get(this.parentId());
	}

	@Environment(EnvType.CLIENT)
	public ClientConsoleVariantSchema getClient() {
		if (this.cachedSchema == null)
			this.cachedSchema = ClientConsoleVariantRegistry.withParent(this);

		return cachedSchema;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		return o instanceof ConsoleVariantSchema other
				&& id.equals(other.id);
	}

	public static Object serializer() {
		return new Serializer();
	}

	private static class Serializer implements JsonSerializer<ConsoleVariantSchema>, JsonDeserializer<ConsoleVariantSchema> {

		@Override
		public ConsoleVariantSchema deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			Identifier id;

			try {
				id = new Identifier(json.getAsJsonPrimitive().getAsString());
			} catch (InvalidIdentifierException e) {
				id = new Identifier(AITMod.MOD_ID, "console/borealis");
			}

			return ConsoleVariantRegistry.getInstance().get(id);
		}

		@Override
		public JsonElement serialize(ConsoleVariantSchema src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.id().toString());
		}
	}
}