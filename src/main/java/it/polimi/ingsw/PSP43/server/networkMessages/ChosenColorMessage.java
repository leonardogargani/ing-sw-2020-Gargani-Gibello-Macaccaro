package it.polimi.ingsw.PSP43.server.networkMessages;

import it.polimi.ingsw.PSP43.Color;

public class ChosenColorMessage {
    Color colorChosen;

    public ChosenColorMessage(Color colorChosen) {
        this.colorChosen = colorChosen;
    }

    public Color getColorChosen() {
        return colorChosen;
    }
}
