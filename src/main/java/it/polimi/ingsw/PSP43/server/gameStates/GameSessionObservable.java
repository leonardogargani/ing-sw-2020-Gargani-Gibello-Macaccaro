package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ClientMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.RegisterClientListener;
import it.polimi.ingsw.PSP43.server.Sender;
import it.polimi.ingsw.PSP43.server.model.Player;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.ChangeNickRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.PlayersListMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.ServerMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This abstract class (extended by GameSession) is the main access for all the players who want to join a game.
 */
public class GameSessionObservable implements Runnable {
    private final int idGame;
    protected int maxNumPlayers;
    private int numOfPlayers;

    private TurnState currentState;

    private final HashMap<String, ClientListener> listenersHashMap;

    public GameSessionObservable(int idGame) {
        this.idGame = idGame;
        this.listenersHashMap = new HashMap<>();
        numOfPlayers = 0;
        maxNumPlayers = 1;
    }

    public void run() {}

    /**
     * This method returns the id of the game.
     * @return the id of the game.
     */
    public int getIdGame(){
        return idGame;
    }

    /**
     * This method returns the maximum number of players allowed to enroll to the game.
     * @return the maximum number of players allowed to enroll to the game.
     */
    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    /**
     * This method returns the effective number of players enrolled to the game.
     * @return the effective number of players enrolled to the game.
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * This method sets the effective state in which the game is.
     * @param currentState the effective state in which the game is.
     */
    public void setCurrentState(TurnState currentState) {
        this.currentState = currentState;
    }

    /**
     * This method returns a map where the key is the name of a player and the value is the related ClientListener.
     * @return a map where the key is the name of a player and the value is the related ClientListener.
     */
    public HashMap<String, ClientListener> getListenersHashMap() { return listenersHashMap; }

    /**
     * This method is called by every player who wants to join the game. In particular it is called by a ClientListener,
     * when it finds that the number of max. players is still by default "1" or if the max. number of players is greater
     * than the number of players enrolled to the game.
     * @param message The message of registration arrived from the net and caught by the ClientListener.
     * @param player The ClientListener which called this method.
     * @return the id of the game.
     */
    public synchronized int registerToTheGame(RegistrationMessage message, ClientListener player) {
        if (numOfPlayers < maxNumPlayers) {
            for (String s : listenersHashMap.keySet()) {
                if (s.equals(message.getNick())) {
                    ChangeNickRequest notifyChangeNick = new ChangeNickRequest(message.getNick());
                    player.sendMessage(notifyChangeNick);
                    return -2;
                }
            }
            listenersHashMap.put(message.getNick(), player);
            numOfPlayers = listenersHashMap.size();
            currentState.executeState(message);
            return idGame;
        }
        return -1;
    }

    /**
     * This method is called at any time of the game by a player and causes the end of the game by sending to all
     * the players a EndGameMessage.
     * @param message The message which contains the reason of the end of the game (here because of connection
     *                problems or exit of a player)
     * @param player The ClientListener which called the method and so the one to which the method hasn't to send
     *               the EndGameMessage.
     */
    public synchronized void unregisterFromGame(LeaveGameMessage message, ClientListener player) {
        EndGameMessage endGameMessage = new EndGameMessage(null, EndGameMessage.EndGameHeader.PLAYER_DISCONNECTED);
        String nicknameLeft = null;
        for (String s : listenersHashMap.keySet()) {
            if (listenersHashMap.get(s).equals(player)) nicknameLeft = s;
        }
        ArrayList<String> listExcluded = new ArrayList<>();
        listExcluded.add(nicknameLeft);
        sendBroadCast(endGameMessage, listExcluded);
    }

    /**
     * This method is called when a player looses the game. It sends him a EndGameMessage and eliminates his ClientListener
     * from the HashMap in which it was saved.
     * @param playerEliminated The data of the player who lost the game.
     * @param messageToOthers The message to be sent to all other players to updated their list of players of the game.
     */
    public void eliminatePlayer(Player playerEliminated, PlayersListMessage messageToOthers) {
        EndGameMessage message = new EndGameMessage(null, EndGameMessage.EndGameHeader.LOSER);
        sendMessage(message, playerEliminated.getNickname());
        listenersHashMap.remove(playerEliminated.getNickname());
        numOfPlayers = listenersHashMap.size();

        sendBroadCast(messageToOthers);
    }

    /**
     * This method is used when tha game finishes because one of the players wins.
     * @param messageToLosers The message sent to all the losers of the game, containing the name of the winner.
     * @param messageForTheWinner The message sent to the winner of the game.
     * @param nicksExcluded The winner player which has to receive messageForTheWinner.
     */
    // TODO I can swith the arraylist in a single string
    public void sendEndingMessage(EndGameMessage messageToLosers, EndGameMessage messageForTheWinner, ArrayList<String> nicksExcluded) {
        for (String s : listenersHashMap.keySet()) {
            if (!nicksExcluded.contains(s)) {
                listenersHashMap.get(s).sendMessage(messageToLosers);
            }
        }

        ClientListener listernerLoser = listenersHashMap.get(nicksExcluded.get(0));
        if (listernerLoser != null)
            listenersHashMap.get(nicksExcluded.get(0)).sendMessage(messageForTheWinner);
        RegisterClientListener ending = new RegisterClientListener();
        ending.removeGameSession(this.idGame);
    }

    /**
     * This method is called to send to the client a generic message, which doesn't pretend an answer.
     * @param genericMessage The message to be sent.
     * @param addressee The client to which the message has to be sent.
     */
    public void sendMessage(ServerMessage genericMessage, String addressee) {
        for (String s : getListenersHashMap().keySet()) {
            if (addressee.equals(s))
                getListenersHashMap().get(s).sendMessage(genericMessage);
        }
    }

    /**
     * This method is called to send to the client the message to tell him that the game is ended (for
     * different reasons, always saved into its String field).
     * @param endGameMessage The message to be sent.
     * @param addressee The client to which the message has to be sent.
     */
    public void sendMessage(EndGameMessage endGameMessage, String addressee) {
        for (String s : getListenersHashMap().keySet()) {
            if (addressee.equals(s))
                getListenersHashMap().get(s).sendMessage(endGameMessage);
        }
    }

    /**
     * This method sends to all the players a generic message.
     * @param message The message to be sent.
     */
    public void sendBroadCast(ServerMessage message) {
        for (String s : getListenersHashMap().keySet()) {
            getListenersHashMap().get(s).sendMessage(message);
        }
    }

    /**
     * This method sends to all the players the message, excluded the ones in the list passed as parameter.
     * @param message The message to be sent.
     * @param nickExcluded The player's nickname excluded from receiving the message.
     */
    public void sendBroadCast(ServerMessage message, String nickExcluded) {
        for (String s : getListenersHashMap().keySet()) {
            if (!nickExcluded.equals(s))
                getListenersHashMap().get(s).sendMessage(message);
        }
    }

    /**
     * This method sends the EndGameMessage to all the players, telling them the end of the game.
     * All the players in the list passed as parameter are excluded.
     * @param endGameMessage The message to be sent.
     * @param nicksExcluded The list of players excluded from receiving the message.
     */
    public void sendBroadCast(EndGameMessage endGameMessage, ArrayList<String> nicksExcluded) {
        for (String s : getListenersHashMap().keySet()) {
            if (!nicksExcluded.contains(s))
                getListenersHashMap().get(s).sendMessage(endGameMessage);
        }
    }

    /**
     * This method is used to send a request to the client and to wait for a response.
     * @param message The request message sent.
     * @param addressee The player who has to receive the message.
     * @param typeExpected The type of message that the caller is expecting.
     * @return a response from the client.
     * @throws GameEndedException if the client has disconnected while he was asked by this method.
     */
    public<T extends ClientMessage> T sendRequest(ServerMessage message, String addressee, Class<T> typeExpected) throws GameEndedException {
        ClientListener listenerAddressee = listenersHashMap.get(addressee);

        Sender newSender = new Sender(listenerAddressee, message);

        while (true) {
            ClientMessage messageArrived = newSender.call();
            if (typeExpected.isInstance(messageArrived)) {
                return typeExpected.cast(messageArrived);
            }
            else if (messageArrived instanceof LeaveGameMessage) {
                throw new GameEndedException();
            }
        }
    }
}