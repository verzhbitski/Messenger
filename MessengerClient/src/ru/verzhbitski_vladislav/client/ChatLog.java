package ru.verzhbitski_vladislav.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.verzhbitski_vladislav.transfer.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatLog {
    private Map<String, ObservableList<Message>> log;

    public ChatLog() {
        log = new HashMap<>();
    }

    public ObservableList<Message> getChatLog(String userName) {
        return log.get(userName);
    }

    public void addNewClient(String username) {
        log.putIfAbsent(username, FXCollections.observableArrayList());
    }

    public void addMessage(Message message) {
        String senderName = message.getSenderName();
        List<Message> chat = log.get(senderName);
        chat.add(message);
    }

    public void addMyMessage(Message message) {
        String receiverName = message.getReceiverName();
        List<Message> chat = log.get(receiverName);
        chat.add(message);
    }

    public void removeUser(String senderName) {
        log.remove(senderName);
    }
}
