package mdteam.ait.api.tardis;

public interface ArtronHolder {
    double getFuel();
    void setFuel(double var);
    default double addFuel(double var) {
        double currentFuel = this.getFuel();
        this.setFuel(this.getFuel() <= this.getMaxFuel() ? this.getFuel() + var : this.getMaxFuel());
        if(this.getFuel() == this.getMaxFuel())
            return var - (this.getMaxFuel() - currentFuel);
        return 0;
    }
    default void removeFuel(double var) {
        if (this.getFuel() - var < 0) {
            this.setFuel(0);
            return;
        }
        this.setFuel(this.getFuel() - var);
    }
    double getMaxFuel();
    default boolean isOutOfFuel() {
        return this.getFuel() <= 0;
    }

    // boolean isRefueling();
    // void setRefueling(boolean var);
}
