package it.polimi.ingsw.PSP43.server;

import it.polimi.ingsw.PSP43.client.networkMessages.PingMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Connection detector is the class that sends ping messages during the match to keep the connection active
 */
public class ConnectionDetector implements Runnable {
    private Socket clientSocket;
    private static final int timeout = 20000;
    private ClientListener clientListener;

    /**
     * Not default constructor for ConnectionDetector class.It sets a socket timeout
     * @param clientSocket is the socket opened between this client and the server
     * @param clientListener is the network handler class that has got the sendMessage method that the connection detector
     * will use to send ping messages
     * @throws IOException exception thrown if for some reason the socket is closed and we are trying to send a ping
     * message
     */
    public ConnectionDetector(Socket clientSocket, ClientListener clientListener) throws IOException {
        this.clientSocket = clientSocket;
        this.clientListener = clientListener;
        clientSocket.setSoTimeout(timeout);
    }

    /**
     * Override of the run method, it will send a ping message during all the match
     */
    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(10);
                if(clientListener.isDisconnected())
                    break;
                try {
                    clientListener.sendMessage(new PingMessage());
                } catch (IOException e) {
                    clientListener.handleDisconnection();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
