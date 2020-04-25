package it.polimi.ingsw.PSP43.server.modelHandlers;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.*;
import it.polimi.ingsw.PSP43.server.model.card.decorators.SwapIfPossibleDecorator;
import it.polimi.ingsw.PSP43.server.model.card.decorators.SwapMoveDecorator;
import it.polimi.ingsw.PSP43.server.model.card.decorators.UnconditionedDomeBuildDecorator;
import it.polimi.ingsw.PSP43.server.model.card.decorators.WinConditionDecorator;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CardsHandlerTest {
    private JAXBContext contextOut;
    private JAXBContext contextIn;
    private ArrayList<AbstractGodCard> deckComplete;
    private CardsHandler cardsHandlerIn;

    @Before
    public void setUp() throws Exception {
        deckComplete = new ArrayList<>();
        deckComplete.add(new SwapMoveDecorator(new BasicGodCard("Apollo", "God Of Music", "Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.", null, null)));
        deckComplete.add(new BasicGodCard("Artemis", "Goddes of the Hunt", "Your Move: Your Worker may move one additional time, but not back to its initial space.", new DoubleMoveBehaviour(), null));
        deckComplete.add(new BasicGodCard("Athena", "Goddes of Wisdom", "Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.", new BlockOpponentRiseBehaviour(), null));
        deckComplete.add(new UnconditionedDomeBuildDecorator(new BasicGodCard("Atlas", "Titan Shouldering the Heavens", "Your Build: Your Worker may build a dome at any level.", null, null)));
        deckComplete.add(new BasicGodCard("Demeter", "Goddess of the Harvest", "Your Build: Your Worker may build one additional time, but not on the same space.", null, new DoubleDifferentSpaceBehaviour()));
        deckComplete.add(new BasicGodCard("Hephaestus", "God of Blacksmiths", "Your Build: Your Worker may build one additional block (not dome) on top of your first block.", null, new DoubleSameSpaceBehaviour()));
        deckComplete.add(new SwapIfPossibleDecorator(new SwapMoveDecorator(new BasicGodCard("Minotaur", "Bull-headed Monster", "Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.", null, null))));
        deckComplete.add(new WinConditionDecorator(new BasicGodCard("Pan", "God of the Wild", "Win Condition: You also win if your Worker moves down two or more levels.", null, null)));
        deckComplete.add(new BasicGodCard("Prometheus", "Titan Benefactor of Mankind", "Your Turn: If your Worker does not move up, it may build both before and after moving.", new BuildBeforeMoveBehaviour(), null));
    }

    @Test
    public void serialization() throws JAXBException {
        CardsHandler cardsHandlerOut = new CardsHandler();
        cardsHandlerOut.setDeckOfAbstractGodCards(deckComplete);
        contextOut = JAXBContext.newInstance(cardsHandlerOut.getClass());
        Marshaller marshaller = contextOut.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        File file;
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource("xmlFiles/deckInit.xml");
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            file = new File(resource.getFile());
        }
        marshaller.marshal(cardsHandlerOut, file);
    }

    @Test
    public void deserialization() throws JAXBException {
        JAXBContext contextIn = JAXBContext.newInstance( CardsHandler.class );
        Unmarshaller unmarshaller = contextIn.createUnmarshaller();
        File file;
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource("xmlFiles/deckInit.xml");
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            file = new File(resource.getFile());
        }

        CardsHandler cardsHandlerIn = (CardsHandler) unmarshaller.unmarshal(file);
        cardsHandlerIn.print();
    }

    @Test
    public void getDeckOfAbstractGodCards() {
    }

    @Test
    public void getMapOwnerCard() {
    }

    @Test
    public void setCardToPlayer() {
    }

    @Test
    public void removeCardToPlayer() {
    }

    @Test
    public void getCardOwned() {
    }

    @Test
    public void print() {
    }
}