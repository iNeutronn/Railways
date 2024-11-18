package com.railways.railways.simulator.src.hall;

public interface IGeneration {
    Client Generate();
    void Stop();
    void Resume();
}
