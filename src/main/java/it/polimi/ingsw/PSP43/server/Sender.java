package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.io.*;
import java.util.concurrent.Callable;

public class Sender implements Callable<ClientMessage> {
    private ClientListener clientListener;
    private ServerMessage messageToSend;

    public Sender(ClientListener clientListener, ServerMessage serverMessage) {
        this.clientListener = clientListener;
        this.messageToSend = serverMessage;
    }

    @Override
    public ClientMessage call() throws IOException, InterruptedException {
        clientListener.sendMessage(messageToSend);
        ClientMessage response;

        response = clientListener.getMessage();

        clientListener.popMessageFromStack(response);

        return response;
    }
}
