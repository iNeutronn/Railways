package com.railways.railways.communication.DTO;

import com.railways.railways.domain.station.QueueUpdate;

import java.util.List;

public class QueueUpdatedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating a QueueUpdatedUpdate.
     *
     *
     * @param queueUpdate the queue associated with the update
     */
    public QueueUpdatedUpdate(QueueUpdate queueUpdate) {
        super(GenerationUpdateTypes.QueueUpdated, queueUpdate);
    }
}
