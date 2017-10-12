package ru.verzhbitski_vladislav;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import ru.verzhbitski_vladislav.client.ChatLog;
import ru.verzhbitski_vladislav.client.Client;
import ru.verzhbitski_vladislav.transfer.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ChatController {
    private final KeyCombination sendHotKey = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_ANY);

    @FXML
    private ListView<String> clientListView;

    private ObservableList<String> clientList = FXCollections.observableArrayList();

    @FXML
    private ListView<Message> chatLog;

    @FXML
    private TextField messageField;

    private ChatLog log = new ChatLog();

    private String currentReceiver;

    private Client client = Client.getInstance();

    private String username = client.getUsername();

    @FXML
    public void initialize() {
        messageField.setOnKeyPressed(event -> {
            if (sendHotKey.match(event)) {
                sendMessage();
            }
        });
        MessageReceiver messageReceiver = new MessageReceiver(client.getInputStream());
        messageReceiver.setDaemon(true);
        messageReceiver.start();

        clientListView.setItems(clientList);

        clientListView.setOnMouseClicked(event -> {
            String selectedItem = clientListView.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            currentReceiver = clientListView.getSelectionModel().getSelectedItem();
            chatLog.setItems(log.getChatLog(currentReceiver));
        });
    }

    private class MessageReceiver extends Thread {
        ObjectInputStream inputStream;

        MessageReceiver(ObjectInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    final Message message = (Message) inputStream.readObject();
                    switch (message.getMessageType()) {
                        case USER_DISCONNECTED:
                            Platform.runLater(() -> {
                                clientList.remove(message.getSenderName());
                                chatLog.setItems(null);
                            });
                            log.removeUser(message.getSenderName());
                            break;
                        case USER_CONNECTED:
                            Platform.runLater(() -> clientList.add(message.getSenderName()));
                            log.addNewClient(message.getSenderName());
                            break;
                        case MESSAGE:
                            Platform.runLater(() -> log.addMessage(message));
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage() {
        if (currentReceiver == null)
            return;
        String messageText = messageField.getText();
        if (!messageText.equals("")) {
            String receiverName = currentReceiver;
            Message message = new Message(Message.MessageType.MESSAGE, username, receiverName, messageText);
            messageField.setText("");
            log.addMyMessage(message);
            try {
                client.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
