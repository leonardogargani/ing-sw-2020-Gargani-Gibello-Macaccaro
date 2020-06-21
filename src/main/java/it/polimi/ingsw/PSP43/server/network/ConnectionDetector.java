package it.polimi.ingsw.PSP43.server.network;

import it.polimi.ingsw.PSP43.client.network.networkMessages.PingMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Connection detector is the class that sends ping messages during the match to keep the connection active.
 */
public class ConnectionDetector implements Runnable {
    private static final int timeout = 20000;
    private final ClientListener clientListener;

    /**
     * Not-default constructor for ConnectionDetector class. It sets a socket timeout.
     * @param clientSocket      It is the socket opened between this client and the server.
     * @param clientListener    It is the network handler class that has got the sendMessage method that the connection
     *                          detector will use to send ping messages.
     * @throws IOException  exception thrown if for some reason the socket is closed and we are trying to send a ping
     *                      message.
     */
    public ConnectionDetector(Socket clientSocket, ClientListener clientListener) throws IOException {
        this.clientListener = clientListener;
        clientSocket.setSoTimeout(timeout);
    }

    /**
     * Override of the run method. It will send a ping message during all the duration of the connection to the client.
     */
    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(10);
                if (clientListener.isDisconnected())
                    break;
                clientListener.sendMessage(new PingMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
