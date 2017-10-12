package ru.verzhbitski_vladislav.transfer;

import java.io.Serializable;

public class Message implements Serializable {
    public enum MessageType {USER_CONNECTED, USER_DISCONNECTED, MESSAGE}

    private MessageType messageType;
    private String senderName;
    private String receiverName;
    private String message;

    public Message(MessageType messageType, String senderName, String receiverName, String message) {
        this.messageType = messageType;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return senderName + ": " + message;
    }
}
