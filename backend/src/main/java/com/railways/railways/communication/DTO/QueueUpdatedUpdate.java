package com.railways.railways.communication.DTO;

import com.railways.railways.domain.station.QueueUpdate;

import java.util.List;

/**
 * Represents an update triggered when the state of a queue has been updated.
 *
 * This class extends {@link GenerationUpdateDTO} and provides details about
 * the queue update event. The type of the update is defined as
 * {@link GenerationUpdateTypes#QueueUpdated}, indicating that there has been
 * a change in the queue's state. The data associated with this update is of type
 * {@link QueueUpdate}, which contains information about the queue's new state.
 *
 * @see GenerationUpdateDTO
 * @see GenerationUpdateTypes
 * @see QueueUpdate
 */
public class QueueUpdatedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating an instance of {@code QueueUpdatedUpdate}.
     *
     * @param queueUpdate the {@link QueueUpdate} object associated with this update,
     *                    containing details about the queue's new state after the update.
     */
    public QueueUpdatedUpdate(QueueUpdate queueUpdate) {
        super(GenerationUpdateTypes.QueueUpdated, queueUpdate);
    }
}
