package com.railways.railways.communication.DTO;
import com.railways.railways.domain.station.QueueUpdate;

public class QueueTransferedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating a QueueTransferedUpdate.
     *
     *
     * @param queueUpdate the queue associated with the update
     */
    public QueueTransferedUpdate(QueueUpdate queueUpdate) {
        super(GenerationUpdateTypes.QueueTransfered, queueUpdate);
    }
}
