package replication.messageObjects;

import java.io.Serializable;

/**
 * Encapsulates a messageObject in order to send it over UDP 
 */
public class MessageEnvelope implements Serializable {
	private static final long serialVersionUID = 1L;
	private OrderMessage orderMessage;
	private OrderResponseMessage orderResponseMessage;
	private RegisterRmMessage registerRmMessage;
	private SequencerResponseMessage sequencerResponseMessage;
	private UnregisterRmMessage unregisterRmMessage;
	private FailedRmMessage failedRmMessage;
	
	public MessageEnvelope(OrderMessage o) {
		orderMessage = o;
	}
	public OrderMessage getOrderMessage() {
		return orderMessage;
	}
	
	public MessageEnvelope(OrderResponseMessage o) {
		orderResponseMessage = o;
	}
	public OrderResponseMessage getOrderResponseMessage() {
		return orderResponseMessage;
	}
	
	public MessageEnvelope(RegisterRmMessage o) {
		registerRmMessage = o;
	}
	public RegisterRmMessage getRegisterRmMessage() {
		return registerRmMessage;
	}
	
	public MessageEnvelope(SequencerResponseMessage o) {
		sequencerResponseMessage = o;
	}
	public SequencerResponseMessage getSequencerResponseMessage() {
		return sequencerResponseMessage;
	}
	
	public MessageEnvelope(UnregisterRmMessage o) {
		unregisterRmMessage = o;
	}
	public UnregisterRmMessage getUnregisterRmMessage() {
		return unregisterRmMessage;
	}
	
	public MessageEnvelope(FailedRmMessage o) {
		failedRmMessage = o;
	}
	public FailedRmMessage getFailedRmMessage() {
		return failedRmMessage;
	}
	
	public MessageType getType() {
		if (orderMessage != null) return MessageType.OrderMessage;
		if (orderResponseMessage != null) return MessageType.OrderResponseMessage;
		if (registerRmMessage != null) return MessageType.RegisterRmMessage;
		if (sequencerResponseMessage != null) return MessageType.SequencerResponseMessage;
		if (unregisterRmMessage != null) return MessageType.UnregisterRmMessage;
		if (failedRmMessage != null) return MessageType.FailedRmMessage;
		return null;
	}

	@Override
	public String toString() {
		return this.getType().toString();
	}
}
