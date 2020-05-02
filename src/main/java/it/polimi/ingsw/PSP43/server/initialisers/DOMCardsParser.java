package it.polimi.ingsw.PSP43.server.initialisers;

import it.polimi.ingsw.PSP43.server.model.card.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.model.card.BasicGodCard;
import it.polimi.ingsw.PSP43.server.model.card.behaviours.*;
import it.polimi.ingsw.PSP43.server.model.card.decorators.SwapIfPossibleDecorator;
import it.polimi.ingsw.PSP43.server.model.card.decorators.SwapMoveDecorator;
import it.polimi.ingsw.PSP43.server.model.card.decorators.UnconditionedDomeBuildDecorator;
import it.polimi.ingsw.PSP43.server.model.card.decorators.WinConditionDecorator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DOMCardsParser {

    public DOMCardsParser() {
    }

    public static ArrayList<AbstractGodCard> buildDeck() throws ParserConfigurationException,
            IOException, org.xml.sax.SAXException, ClassNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ClassLoader classLoader = DOMCardsParser.class.getClassLoader();
        URL resource = classLoader.getResource("configurationFiles/xmlFiles/deckOfAbstractGodCards.xml");
        File file;
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            file = new File(resource.getFile());
        }
        Document document = builder.parse(file);
        Element deck = document.getDocumentElement();
        NodeList cards = deck.getChildNodes();
        ArrayList<AbstractGodCard> effectiveDeck = buildCompleteDeck(cards);
        return effectiveDeck;
    }

    public static ArrayList<AbstractGodCard> buildCompleteDeck(NodeList list) throws ClassNotFoundException {
        ArrayList<AbstractGodCard> deck = new ArrayList<>();
        for (int i = 0; i<list.getLength(); i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                AbstractGodCard card = buildComponent((Element) list.item(i));
                deck.add(card);
            }
        }
        return deck;
    }

    public static AbstractGodCard buildComponent(Element element) throws ClassNotFoundException {
        String nodeName = element.getNodeName().substring(0,1).toUpperCase() + element.getNodeName().substring(1);
        if (nodeName.equals("BasicGodCard") && element.getNodeType() == Node.ELEMENT_NODE) {
            String godName = element.getElementsByTagName("godName").item(0).getTextContent();
            String godDescription = element.getElementsByTagName("godDescription").item(0).getTextContent();
            String godPower = element.getElementsByTagName("godPower").item(0).getTextContent();
            MoveBehavior moveBehavior = null;
            BuildBlockBehaviour buildBlockBehaviour = null;
            if (element.getElementsByTagName("moveBehaviour").item(0) != null)
                moveBehavior = buildMoveBehaviour(element.getElementsByTagName("moveBehaviour").item(0).getNodeName());
            if (element.getElementsByTagName("buildBlockBehaviour").item(0) != null)
                buildBlockBehaviour = buildBuildBlockBehaviour(element.getElementsByTagName("buildBlockBehaviour").item(0).getNodeName());
            return new BasicGodCard(godName, godDescription, godPower, moveBehavior, buildBlockBehaviour);
        } else {
            AbstractGodCard component = null;
            for (int i = 0; i<element.getChildNodes().getLength(); i++) {
                if (element.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
                    component = buildComponent((Element) element.getChildNodes().item(i));
                }
            if (nodeName.equals("SwapIfPossibleDecorator")) {
                return new SwapIfPossibleDecorator(component);
            } else if (nodeName.equals("SwapMoveDecorator")) {
                return new SwapMoveDecorator(component);
            } else if (nodeName.equals("UnconditionedDomeBuildDecorator")) {
                return new UnconditionedDomeBuildDecorator(component);
            } else {
                return new WinConditionDecorator(component);
            }
        }
    }

    public static MoveBehavior buildMoveBehaviour(String nameMoveBehaviour) throws ClassNotFoundException {
        if (nameMoveBehaviour.equals("BlockOpponentRiseBehaviour")) {
            return new BlockOpponentRiseBehaviour();
        }
        else if (nameMoveBehaviour.equals("BuildBeforeMoveBehaviour")) {
            return new BuildBeforeMoveBehaviour();
        }
        else if (nameMoveBehaviour.equals("DoubleMoveBehaviour")) {
            return new DoubleMoveBehaviour();
        }
        else return null;
    }

    public static BuildBlockBehaviour buildBuildBlockBehaviour(String nameBlockBehaviour) throws ClassNotFoundException {
        if (nameBlockBehaviour.equals("DoubleSameSpaceBehaviour")) {
            return new DoubleSameSpaceBehaviour();
        }
        else if (nameBlockBehaviour.equals("DoubleDifferentSpaceBehaviour")) {
            return new DoubleDifferentSpaceBehaviour();
        }
        else return null;
    }
}
