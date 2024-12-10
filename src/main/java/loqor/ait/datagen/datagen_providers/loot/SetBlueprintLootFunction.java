/*
package loqor.ait.datagen.datagen_providers.loot;


public class SetBlueprintLootFunction extends ConditionalLootFunction {
    final BlueprintType blueprint;
    public Random random = AITMod.RANDOM;

    SetBlueprintLootFunction(LootCondition[] conditions, BlueprintType blueprint) {
        super(conditions);
        this.blueprint = blueprint;
    }

    @Override
    public LootFunctionType getType() {
        return BlueprintRegistry.BLUEPRINT_TYPE;
    }

    @Override
    public ItemStack process(ItemStack stack, LootContext context) {
        BlueprintRegistry.setBlueprint(stack, this.blueprint);
        return stack;
    }

    public static ConditionalLootFunction.Builder<?> builder(BlueprintType blueprint) {
        return SetBlueprintLootFunction
                .builder((LootCondition[] conditions) -> new SetBlueprintLootFunction(conditions, blueprint));
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<SetBlueprintLootFunction> {
        @Override
        public void toJson(JsonObject jsonObject, SetBlueprintLootFunction setBlueprintLootFunction,
                JsonSerializationContext jsonSerializationContext) {
            super.toJson(jsonObject, setBlueprintLootFunction, jsonSerializationContext);
            jsonObject.addProperty("id",
                    BlueprintRegistry.REGISTRY.getId(setBlueprintLootFunction.blueprint).toString());
        }

        @Override
        public SetBlueprintLootFunction fromJson(JsonObject jsonObject,
                JsonDeserializationContext jsonDeserializationContext, LootCondition[] lootConditions) {
            String string = JsonHelper.getString(jsonObject, "id");
            BlueprintType blueprint = BlueprintRegistry.REGISTRY.getOrEmpty(Identifier.tryParse(string))
                    .orElseThrow(() -> new JsonSyntaxException("Unknown blueprint '" + string + "'"));
            return new SetBlueprintLootFunction(lootConditions, blueprint);
        }
    }
}
*/
