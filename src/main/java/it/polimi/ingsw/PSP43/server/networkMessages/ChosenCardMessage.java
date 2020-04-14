package it.polimi.ingsw.PSP43.server.networkMessages;

public class ChosenCardMessage extends GenericMessage {
    String owner;
    String nameCardChosen;

    public ChosenCardMessage(int idGameSession, String nameCardChosen, String owner) {
        super(idGameSession);
        this.owner = owner;
        this.nameCardChosen = nameCardChosen;
    }

    public String getNameCardChosen() {
        return nameCardChosen;
    }

    public String getOwner() {
        return owner;
    }
}
