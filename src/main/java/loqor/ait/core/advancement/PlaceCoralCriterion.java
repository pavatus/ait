package loqor.ait.core.advancement;

import com.google.gson.JsonObject;
import loqor.ait.AITMod;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlaceCoralCriterion extends AbstractCriterion<PlaceCoralCriterion.Conditions> {
	public static final Identifier ID = new Identifier(AITMod.MOD_ID, "place_coral");

	@Override
	protected PlaceCoralCriterion.Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new PlaceCoralCriterion.Conditions();
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, PlaceCoralCriterion.Conditions::requirementsMet);
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
