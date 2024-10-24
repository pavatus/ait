package dev.pavatus.multidim.impl;

public class SimpleWorldProgressListener extends AbstractWorldProgressListener {

    private final Runnable onDone;

    public SimpleWorldProgressListener(Runnable onDone) {
        this.onDone = onDone;
    }

    @Override
    public void setDone() {
        this.onDone.run();
    }
}
