package it.polimi.ingsw.PSP43.client.network.networkMessages;


public class StarterResponse extends ClientMessage {

    private static final long serialVersionUID = -3850574828879541142L;

    private final String starterPlayerName;


    /**
     * Not default constructor for StarterResponse.
     * @param starterPlayerName is a String contains the nick of chosen first player
     */
    public StarterResponse(String starterPlayerName) {
        this.starterPlayerName = starterPlayerName;
    }


    /**
     * Getter method for starterPlayerName String variable.
     * @return starterPlayerName
     */
    public String getStarterPlayerName() {
        return starterPlayerName;
    }

}
