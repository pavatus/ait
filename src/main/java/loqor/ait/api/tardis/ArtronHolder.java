package loqor.ait.api.tardis;

public interface ArtronHolder {
	double getCurrentFuel();

	void setCurrentFuel(double var);

	default void addFuel(double var) {
        double toAdd = var + this.getCurrentFuel();

		if (toAdd >= this.getMaxFuel()) {
			this.setCurrentFuel(this.getMaxFuel());
			return;
		}

		this.setCurrentFuel(toAdd);
	}

	default void removeFuel(double var) {
		double toRemove = this.getCurrentFuel() - var;

		if (toRemove < 0) {
			this.setCurrentFuel(0);
			return;
		}

		this.setCurrentFuel(toRemove);
	}

	double getMaxFuel();

	default boolean isOutOfFuel() {
		return this.getCurrentFuel() <= 0;
	}

	// boolean isRefueling();
	// void setRefueling(boolean var);
}
