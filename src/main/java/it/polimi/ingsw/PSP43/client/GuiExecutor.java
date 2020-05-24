package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.gui.GuiStarter;
import javafx.application.Application;

public class GuiExecutor implements Runnable {
    @Override
    public void run() {
        Application.launch(GuiStarter.class);
    }
}
