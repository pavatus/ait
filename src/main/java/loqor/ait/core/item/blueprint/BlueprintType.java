package loqor.ait.core.item.blueprint;

public record BlueprintType(String name) {

    public String id() {
        return this.name;
    }

}
