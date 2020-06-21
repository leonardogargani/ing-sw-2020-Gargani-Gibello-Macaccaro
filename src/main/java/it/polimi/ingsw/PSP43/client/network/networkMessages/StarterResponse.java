package it.polimi.ingsw.PSP43.client.network.networkMessages;


public class StarterResponse extends ClientMessage {

    private static final long serialVersionUID = -3850574828879541142L;

    private final String starterPlayerName;


    /**
     *
     * @param starterPlayerName
     */
    public StarterResponse(String starterPlayerName) {
        this.starterPlayerName = starterPlayerName;
    }


    /**
     *
     * @return
     */
    public String getStarterPlayerName() {
        return starterPlayerName;
    }

}
