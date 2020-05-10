package it.polimi.ingsw.PSP43.client;

import it.polimi.ingsw.PSP43.client.cli.CliGraphicHandler;
import it.polimi.ingsw.PSP43.client.cli.InputHandler;
import it.polimi.ingsw.PSP43.client.cli.QuitPlayerException;
import it.polimi.ingsw.PSP43.client.cli.Screens;
import it.polimi.ingsw.PSP43.client.gui.GuiGraphicHandler;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.PlayersNumberResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.gameStates.PlayerRegistrationState;
import it.polimi.ingsw.PSP43.server.networkMessages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;


public class ClientManager implements Runnable{
    private final int chosenInterface;
    private GraphicHandler graphicHandler;
    private ClientBG clientBG;
    private boolean isActive;
    private final ArrayList<ServerMessage> messageBox;

    public ClientManager(int chosenInterface){
        this.chosenInterface = chosenInterface;
        this.isActive = true;
        this.messageBox = new ArrayList<>();
    }

    @Override
    public void run() {
        clientBG = new ClientBG(this);
        Thread clientBGThread = new Thread(clientBG);
        clientBGThread.start();

        if(this.chosenInterface == 1)
            graphicHandler = new CliGraphicHandler(clientBG);
        else
            graphicHandler = new GuiGraphicHandler(clientBG);

        while (isActive){
            try {
                handleEvent();
            } catch (QuitPlayerException | IOException e) {
                // TODO implement the handling of a QuitPlayerException when a player writes "quit" in the cli
                try {
                    clientBG.sendMessage(new LeaveGameMessage());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public void handleEvent() throws QuitPlayerException, IOException {
        if( messageBox.size() >= 1)
        {
            if(messageBox.get(0) instanceof CellMessage)
            {getGraphicHandler().updateBoardChange((CellMessage)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof ActionRequest)
            {getGraphicHandler().updateMenuChange((ActionRequest)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof CardRequest)
            {getGraphicHandler().updateMenuChange((CardRequest)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof ChangeNickRequest)
            {getGraphicHandler().updateMenuChange((ChangeNickRequest)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof EndGameMessage)
            {getGraphicHandler().updateMenuChange((EndGameMessage)messageBox.get(0));
                clientBG.setDisconnect(true);
                clientBG.closer();
                this.isActive = false;
            }

            else if(messageBox.get(0) instanceof InitialCardsRequest)
            {getGraphicHandler().updateMenuChange((InitialCardsRequest)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof PlayersNumberRequest)
            {getGraphicHandler().updateMenuChange((PlayersNumberRequest)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof PlayersListMessage)
            {getGraphicHandler().updateMenuChange((PlayersListMessage)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof RequestMessage)
            {getGraphicHandler().updateMenuChange((RequestMessage)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof StartGameMessage)
            {getGraphicHandler().updateMenuChange((StartGameMessage)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof WorkersColorRequest)
            {getGraphicHandler().updateMenuChange((WorkersColorRequest)messageBox.get(0));
                messageBox.remove(0);}
            else if(messageBox.get(0) instanceof TextMessage)
            {getGraphicHandler().updateMenuChange((TextMessage) messageBox.get(0));
                messageBox.remove(0);}
        }
    }


    public GraphicHandler getGraphicHandler() {
        return graphicHandler;
    }


    public ArrayList<ServerMessage> getMessageBox() {
        return messageBox;
    }


    public ClientBG getClientBG() {
        return clientBG;
    }


    public void execute() {
        System.out.println(Screens.WELCOME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("Choose a nickname:\n");
            InputHandler inputHandler = new InputHandler();
            String nickname = inputHandler.requestInput();
            RegistrationMessage message = new RegistrationMessage(nickname);
            clientBG.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (QuitPlayerException e) {
            // TODO implement the handling of a QuitPlayerException when a player writes "quit" in the cli
        }

    }

    public boolean isActive() {
        return isActive;
    }

}
