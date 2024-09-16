package loqor.ait.core.world.planets;

// @TODO: could potentially just be an interface that DimensionOptions implements
public abstract class Planet {
    public boolean hasCustomSkybox() {
        return false;
    }

    public boolean hasWeather() {
        return false;
    }

    public enum WeatherTypes {
        ACID_RAIN,
        SANDSTORM,
        STATIC_STORM
    }
}
