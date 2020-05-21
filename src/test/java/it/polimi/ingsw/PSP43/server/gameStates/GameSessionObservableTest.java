package it.polimi.ingsw.PSP43.server.gameStates;

import it.polimi.ingsw.PSP43.client.networkMessages.ActionResponse;
import it.polimi.ingsw.PSP43.client.networkMessages.LeaveGameMessage;
import it.polimi.ingsw.PSP43.client.networkMessages.RegistrationMessage;
import it.polimi.ingsw.PSP43.server.ClientListener;
import it.polimi.ingsw.PSP43.server.modelHandlersException.GameEndedException;
import it.polimi.ingsw.PSP43.server.networkMessages.ActionRequest;
import it.polimi.ingsw.PSP43.server.networkMessages.EndGameMessage;
import it.polimi.ingsw.PSP43.server.networkMessages.TextMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameSessionObservableTest {
    private GameSession spyGame;
    private ClientListener clientListenerMock;
    private final String firstNick = "Gibi";
    private ClientListener clientListenerFirstPlayer;
    private final String secondNick = "Leo";
    private ClientListener clientListenerSecondPlayer;
    private final String thirdNick = "Rob";
    private ClientListener clientListenerThirdPlayer;
    private final int IDGAME = 9;

    @Captor
    private ArgumentCaptor<ArrayList<String>> arrayListArgumentCaptor;
    @Captor
    private ArgumentCaptor<EndGameMessage> messageFirstArgumentCaptor;
    @Captor
    private ArgumentCaptor<EndGameMessage> messageSecondArgumentCaptor;
    @Captor
    private ArgumentCaptor<EndGameMessage> messageThirdArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        spyGame = spy(new GameSession(IDGAME));

        clientListenerMock = mock(ClientListener.class);
        doNothing().when(clientListenerMock).sendMessage(any());

        clientListenerFirstPlayer = new ClientListener(new Socket());
        clientListenerSecondPlayer = new ClientListener(new Socket());
        clientListenerThirdPlayer = new ClientListener(new Socket());
    }

    @Test
    public void registerToTheGameTest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());


        assertEquals(IDGAME, spyGame.registerToTheGame(new RegistrationMessage(firstNick), clientListenerFirstPlayer));
        assertTrue(spyGame.getNumOfPlayers() == 1 && clientListenerFirstPlayer.equals(spyGame.getListenersHashMap().get(firstNick)));

        spyGame.maxNumPlayers = 2;
        assertEquals(-2, spyGame.registerToTheGame(new RegistrationMessage(firstNick), clientListenerSecondPlayer));
        assertTrue(spyGame.getNumOfPlayers() == 1 && !(clientListenerSecondPlayer.equals(spyGame.getListenersHashMap().get(firstNick))));

        assertEquals(IDGAME, spyGame.registerToTheGame(new RegistrationMessage(secondNick), clientListenerSecondPlayer));
        assertTrue(spyGame.getNumOfPlayers() == 2 && clientListenerSecondPlayer.equals(spyGame.getListenersHashMap().get(secondNick)));

        assertEquals(-1, spyGame.registerToTheGame(new RegistrationMessage(thirdNick), clientListenerThirdPlayer));
        assertTrue(spyGame.getNumOfPlayers() == 2 && spyGame.getListenersHashMap().get(thirdNick) == null);
    }

    @Test
    public void unregisterFromGameTest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), clientListenerFirstPlayer);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), clientListenerSecondPlayer);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), clientListenerThirdPlayer);

        ArrayList<String> expectedNicksExcluded = new ArrayList<>();
        expectedNicksExcluded.add(firstNick);

        spyGame.unregisterFromGame(new LeaveGameMessage(), clientListenerFirstPlayer);
        verify(spyGame).sendBroadCast(any(), arrayListArgumentCaptor.capture());
        ArrayList<String> effectiveNicksExcluded = arrayListArgumentCaptor.getValue();
        assertTrue(expectedNicksExcluded.containsAll(effectiveNicksExcluded) && expectedNicksExcluded.size() == effectiveNicksExcluded.size());
    }

    @Test
    public void eliminatePlayerTest() throws IOException, SAXException, ParserConfigurationException {
        /*PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), clientListenerFirstPlayer);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), clientListenerSecondPlayer);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), clientListenerThirdPlayer);

        spyGame.eliminatePlayer(spyGame.getPlayersHandler().getPlayer(firstNick));
        assertTrue(spyGame.getNumOfPlayers() == 2 && spyGame.getListenersHashMap().get(firstNick) == null);

         */
    }

    @Test
    public void sendMessageEndTest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), clientListenerMock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), clientListenerMock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), clientListenerMock);

        spyGame.sendMessage(new EndGameMessage("", endGameHeader), firstNick);
        verify(clientListenerMock, times(1)).sendMessage(any());
    }

    @Test
    public void sendMessageTest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), clientListenerMock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), clientListenerMock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), clientListenerMock);

        TextMessage textMessage = new TextMessage("");
        spyGame.sendMessage(textMessage, firstNick);
        verify(clientListenerMock, times(1)).sendMessage(textMessage);
    }

    @Test
    public void sendBroadCastTest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        ClientListener firstMock = mock(ClientListener.class);
        ClientListener secondMock = mock(ClientListener.class);
        ClientListener thirdMock = mock(ClientListener.class);

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), firstMock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), secondMock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), thirdMock);

        TextMessage textMessage = new TextMessage("");

        spyGame.sendBroadCast(textMessage);
        verify(firstMock, times(1)).sendMessage(textMessage);
        verify(secondMock, times(1)).sendMessage(textMessage);
        verify(thirdMock, times(1)).sendMessage(textMessage);
    }

    @Test
    public void sendBroadCastWithExcludedTest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        ClientListener firstMock = mock(ClientListener.class);
        ClientListener secondMock = mock(ClientListener.class);
        ClientListener thirdMock = mock(ClientListener.class);

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), firstMock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), secondMock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), thirdMock);

        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(firstNick);
        TextMessage textMessage = new TextMessage("");
        spyGame.sendBroadCast(textMessage, nicksExcluded);
        verify(firstMock, times(0)).sendMessage(textMessage);
        verify(secondMock, times(1)).sendMessage(textMessage);
        verify(thirdMock, times(1)).sendMessage(textMessage);
    }

    @Test
    public void sendBroadCastPlayerLeftTest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        ClientListener firstMock = mock(ClientListener.class);
        ClientListener secondMock = mock(ClientListener.class);
        ClientListener thirdMock = mock(ClientListener.class);

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), firstMock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), secondMock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), thirdMock);

        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(firstNick);
        EndGameMessage endGameMessage = new EndGameMessage("", endGameHeader);
        spyGame.sendBroadCast(endGameMessage, nicksExcluded);
        verify(firstMock, times(0)).sendMessage(endGameMessage);
        verify(secondMock, times(1)).sendMessage(endGameMessage);
        verify(thirdMock, times(1)).sendMessage(endGameMessage);
    }

    @Test
    public void sendRequest() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), clientListenerMock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), clientListenerMock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), clientListenerMock);

        ActionResponse actionResponse = new ActionResponse();
        LeaveGameMessage leaveGameMessage = new LeaveGameMessage();
        doReturn(actionResponse, leaveGameMessage).when(clientListenerMock).getMessage();

        boolean caught = false;

        try {
            assertNotNull(spyGame.sendRequest(new ActionRequest("", new HashMap<>()), firstNick, ActionResponse.class));
        } catch (GameEndedException e) {
            caught = true;
        }

        try {
            spyGame.sendRequest(new ActionRequest("", new HashMap<>()), firstNick, ActionResponse.class);
        } catch (GameEndedException e) {
            caught = true;
        }

        assertTrue(caught);
    }

    @Test
    public void sendEndingGameMessage() {
        PlayerRegistrationState spyState = spy(new PlayerRegistrationState(spyGame));
        spyGame.setCurrentState(spyState);
        doNothing().when(spyState).executeState(any());

        ClientListener firstMock = mock(ClientListener.class);
        ClientListener secondMock = mock(ClientListener.class);
        ClientListener thirdMock = mock(ClientListener.class);

        spyGame.registerToTheGame(new RegistrationMessage(firstNick), firstMock);
        spyGame.maxNumPlayers = 3;
        spyGame.registerToTheGame(new RegistrationMessage(secondNick), secondMock);
        spyGame.registerToTheGame(new RegistrationMessage(thirdNick), thirdMock);

        doNothing().when(firstMock).sendMessage(messageFirstArgumentCaptor.capture());
        doNothing().when(secondMock).sendMessage(messageSecondArgumentCaptor.capture());
        doNothing().when(thirdMock).sendMessage(messageThirdArgumentCaptor.capture());


        ArrayList<String> nicksExcluded = new ArrayList<>();
        nicksExcluded.add(firstNick);
        EndGameMessage endGameMessageWinner = new EndGameMessage("", endGameHeader);
        EndGameMessage endGameMessageLoser = new EndGameMessage("", endGameHeader);
        spyGame.sendEndingMessage(endGameMessageLoser, endGameMessageWinner, nicksExcluded);
        verify(firstMock, times(1)).sendMessage(endGameMessageWinner);
        verify(secondMock, times(1)).sendMessage(endGameMessageLoser);
        verify(thirdMock, times(1)).sendMessage(endGameMessageLoser);

        assertEquals(endGameMessageWinner, messageFirstArgumentCaptor.getValue());
        assertEquals(endGameMessageLoser, messageSecondArgumentCaptor.getValue());
        assertEquals(endGameMessageLoser, messageThirdArgumentCaptor.getValue());
    }
}