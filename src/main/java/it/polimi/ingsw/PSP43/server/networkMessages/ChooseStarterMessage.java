package it.polimi.ingsw.PSP43.server.networkMessages;

import java.util.List;

public class ChooseStarterMessage extends TextMessage {
    private static final long serialVersionUID = 5729136919443485349L;
    private List<String> playersList;

    /**
     * Not default constructor for ChooseStarterMessage.
     * @param message          is the string that will be shown to the recipient
     * @param positionInScreen
     */
    public ChooseStarterMessage(List<String> list, String message, PositionInScreen positionInScreen) {
        super(message, positionInScreen);
        playersList = list;
    }

    public List<String> getPlayersList() {
        return playersList;
    }
}
