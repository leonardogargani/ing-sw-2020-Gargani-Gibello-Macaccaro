package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.util.concurrent.Callable;

/**
 * This is a class used to send request it implements Callable which means that something will comeback, and that
 * will be the response for the request
 */
public class Sender implements Callable<ClientMessage> {
    private ClientListener clientListener;
    private ServerMessage messageToSend;

    /**
     * Not default constructor for Sender class
     *
     * @param clientListener that has got the send method
     * @param serverMessage  that is the message that contains the request
     */
    public Sender(ClientListener clientListener, ServerMessage serverMessage) {
        this.clientListener = clientListener;
        this.messageToSend = serverMessage;
    }

    /**
     * Override of the call method, that sends a request and after that it takes the response from the clientListener
     *
     * @return response
     */
    @Override
    public ClientMessage call() {
        clientListener.sendMessage(messageToSend);
        ClientMessage response;

        response = clientListener.getMessage();

        clientListener.popMessageFromStack(response);

        return response;
    }
}
