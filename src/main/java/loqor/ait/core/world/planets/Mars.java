package loqor.ait.core.world.planets;

public class Mars extends Planet {
    @Override
    public boolean hasWeather() {
        return true;
    }

    @Override
    public boolean hasCustomSkybox() {
        return true;
    }
}
