package it.polimi.ingsw.PSP43.client.graphic.cli;

import java.util.ArrayList;


public class CliMiddleMenu {

    private String content = "";


    /**
     * Method that, given some information about the players and their gods, sets the content
     * of the menu which will be displayed at the middle of the screen.
     * @param playersInfo ArrayList containing information about players, their gods and their
     *                   workers' color
     */
    public void setContentWithInfo(ArrayList<String> playersInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String playerInfo : playersInfo) {
            stringBuilder.append(playerInfo);
            stringBuilder.append("\n");
        }
        this.content = stringBuilder.toString();
    }


    /**
     * Method that prints the content attribute to the cli.
     */
    public void show() {
        System.out.println(content);
    }


}
