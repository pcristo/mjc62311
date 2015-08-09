package replication.messageObjects;

/**
 * Enumerates the various message types that can be found inside
 * a Message Envelope
 * @author Patrick
 */
public enum MessageType {
	OrderMessage,
	OrderResponseMessage,
	RegisterRmMessage,
	SequencerResponseMessage,
	UnregisterRmMessage,
	FailedRmMessage
}
