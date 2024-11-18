package com.railways.railways.simulator.src.hall;

public interface IGeneration<T> {
    T Generate();
    void Stop();
    void Resume();
}
