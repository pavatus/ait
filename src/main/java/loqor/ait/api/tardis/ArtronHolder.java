package loqor.ait.api.tardis;

public interface ArtronHolder {
	double getCurrentFuel();

	void setCurrentFuel(double var);

	default double addFuel(double var) {
		double currentFuel = this.getCurrentFuel();
		this.setCurrentFuel(this.getCurrentFuel() <= this.getMaxFuel() ? this.getCurrentFuel() + var : this.getMaxFuel());
		if (this.getCurrentFuel() == this.getMaxFuel())
			return var - (this.getMaxFuel() - currentFuel);
		return 0;
	}

	default void removeFuel(double var) {
		if (this.getCurrentFuel() - var < 0) {
			this.setCurrentFuel(0);
			return;
		}
		this.setCurrentFuel(this.getCurrentFuel() - var);
	}

	double getMaxFuel();

	default boolean isOutOfFuel() {
		return this.getCurrentFuel() <= 0;
	}

	// boolean isRefueling();
	// void setRefueling(boolean var);
}
