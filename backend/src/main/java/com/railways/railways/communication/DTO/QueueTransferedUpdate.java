package com.railways.railways.communication.DTO;
import com.railways.railways.domain.station.QueueUpdate;

/**
 * Represents an update triggered when a client has been transferred to a different queue.
 *
 * This class extends {@link GenerationUpdateDTO} and provides details about
 * the queue transfer event. The type of the update is defined as
 * {@link GenerationUpdateTypes#QueueTransfered}, indicating that a client
 * has been moved to a different queue. The data associated with this update
 * is of type {@link QueueUpdate}, which contains information about the
 * queue transfer.
 *
 * @see GenerationUpdateDTO
 * @see GenerationUpdateTypes
 * @see QueueUpdate
 */
public class QueueTransferedUpdate extends GenerationUpdateDTO{
    /**
     * Constructor for creating an instance of {@code QueueTransferedUpdate}.
     *
     * @param queueUpdate the {@link QueueUpdate} object associated with this update,
     *                    containing details about the queue transfer event.
     */
    public QueueTransferedUpdate(QueueUpdate queueUpdate) {
        super(GenerationUpdateTypes.QueueTransfered, queueUpdate);
    }
}
