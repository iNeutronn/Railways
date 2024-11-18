package com.railways.railways.simulator.src.hall;

public abstract class ClientGenerator<T> implements IGeneration<T> {
    protected boolean isStopped;
    protected int lastGeneratedAt;

    @Override
    public void Stop() {
        isStopped = true;
    }

    @Override
    public void Resume() {
        isStopped = false;
    }
}
