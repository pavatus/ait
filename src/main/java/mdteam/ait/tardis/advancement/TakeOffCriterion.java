package mdteam.ait.tardis.advancement;

import com.google.gson.JsonObject;
import mdteam.ait.AITMod;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TakeOffCriterion extends AbstractCriterion<TakeOffCriterion.Conditions> {
	public static final Identifier ID = new Identifier(AITMod.MOD_ID, "tardis_takeoff");

	@Override
	protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions();
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, Conditions::requirementsMet);
	}

	public static class Conditions extends AbstractCriterionConditions {
		public Conditions() {
			super(ID, LootContextPredicate.EMPTY);
		}

		boolean requirementsMet() {
			return true;
		}
	}
}
