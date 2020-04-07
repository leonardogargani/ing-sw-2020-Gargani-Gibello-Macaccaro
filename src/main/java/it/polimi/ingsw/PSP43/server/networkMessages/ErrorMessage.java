package it.polimi.ingsw.PSP43.server.networkMessages;

public class ErrorMessage extends GenericMessage{
    String messageToTheClient;

    public ErrorMessage(int idGame, String messageToTheClient) {
        super(idGame);
        this.messageToTheClient = messageToTheClient;
    }

    public String getMessageToTheClient() {
        return messageToTheClient;
    }
}
