package mdteam.ait.tardis.advancement;

import net.minecraft.advancement.criterion.Criteria;

public class TardisCriterions {
	public static TakeOffCriterion TAKEOFF = Criteria.register(new TakeOffCriterion());
	public static CrashCriterion CRASH = Criteria.register(new CrashCriterion());
	public static BreakVegetationCriterion VEGETATION = Criteria.register(new BreakVegetationCriterion());
	public static PlaceCoralCriterion PLACE_CORAL = Criteria.register(new PlaceCoralCriterion());

	public static void init() {
	}
}
