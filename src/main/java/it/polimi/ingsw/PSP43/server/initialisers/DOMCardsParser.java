package it.polimi.ingsw.PSP43.server.initialisers;

import it.polimi.ingsw.PSP43.server.controllers.AbstractGodCard;
import it.polimi.ingsw.PSP43.server.controllers.BasicGodCard;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours.BasicBuildBehaviour;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours.DoubleDifferentSpaceBehaviour;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours.DoubleSameSpaceBehaviour;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.buildBehaviours.RemoveBlockFromNeighbourBehaviour;
import it.polimi.ingsw.PSP43.server.controllers.behaviours.moveBehaviours.*;
import it.polimi.ingsw.PSP43.server.controllers.decorators.*;
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

/**
 * This class is used to parse the XML configuration file and to initialise the deck of cards of the game.
 */
public class DOMCardsParser {

    /**
     * This method is called by the GameSession to initialise all the cards of the game which can be chosen
     * by a player.
     * @return a deck of cards used during the game.
     */
    public static ArrayList<AbstractGodCard> buildDeck() throws ParserConfigurationException,
            IOException, org.xml.sax.SAXException {
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
        return buildCompleteDeck(cards);
    }

    private static ArrayList<AbstractGodCard> buildCompleteDeck(NodeList list) {
        ArrayList<AbstractGodCard> deck = new ArrayList<>();
        for (int i = 0; i<list.getLength(); i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                AbstractGodCard card = buildComponent((Element) list.item(i));
                deck.add(card);
            }
        }
        return deck;
    }

    private static AbstractGodCard buildComponent(Element element) {
        String nodeName = element.getNodeName().substring(0,1).toUpperCase() + element.getNodeName().substring(1);
        if (nodeName.equals("BasicGodCard") && element.getNodeType() == Node.ELEMENT_NODE) {
            String godName = element.getElementsByTagName("godName").item(0).getTextContent();
            String godDescription = element.getElementsByTagName("godDescription").item(0).getTextContent();
            String godPower = element.getElementsByTagName("godPower").item(0).getTextContent();

            BasicMoveBehaviour moveBehavior = null;
            BasicBuildBehaviour buildBehaviour = null;
            NodeList nodeListMoveBehaviour = element.getElementsByTagName("moveBehaviours");
            NodeList nodeListBuildBlockBehaviour = element.getElementsByTagName("buildBehaviour");

            for (int i=0; i<nodeListMoveBehaviour.getLength(); i++) {
                if (nodeListMoveBehaviour.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    NodeList effectiveNodeListMoveBehaviour = nodeListMoveBehaviour.item(i).getChildNodes();
                    for (int j=0; j<effectiveNodeListMoveBehaviour.getLength(); j++) {
                        if (effectiveNodeListMoveBehaviour.item(j).getNodeType() == Node.ELEMENT_NODE)
                            moveBehavior = buildMoveBehaviour(((Element) effectiveNodeListMoveBehaviour.item(j)).getTagName());
                    }
                }
            }
            for (int i=0; i<nodeListBuildBlockBehaviour.getLength(); i++) {
                if (nodeListBuildBlockBehaviour.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    NodeList effectiveNodeListBuildBlockBehaviour = nodeListBuildBlockBehaviour.item(i).getChildNodes();
                    for (int j=0; j<effectiveNodeListBuildBlockBehaviour.getLength(); j++) {
                        if (effectiveNodeListBuildBlockBehaviour.item(j).getNodeType() == Node.ELEMENT_NODE)
                            buildBehaviour = buildBuildBlockBehaviour(((Element) effectiveNodeListBuildBlockBehaviour.item(j)).getTagName());
                    }
                }
            }

            return new BasicGodCard(godName, godDescription, godPower, moveBehavior, buildBehaviour);
        } else {
            AbstractGodCard component = null;
            for (int i = 0; i<element.getChildNodes().getLength(); i++) {
                if (element.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
                    component = buildComponent((Element) element.getChildNodes().item(i));
                }
            switch (nodeName) {
                case "SwapIfPossibleDecorator":
                    return new SwapIfPossibleDecorator(component);
                case "SwapMoveDecorator":
                    return new SwapMoveDecorator(component);
                case "UnconditionedBuildDomeDecorator":
                    return new UnconditionedDomeBuildDecorator(component);
                case "WinFiveDomeDecorator":
                    return new WinFiveDomeDecorator(component);
                case "BuildUnderFeetDecorator":
                    return new BuildUnderFeetDecorator(component);
                default:
                    return new WinTwoFloorsFallDecorator(component);
            }
        }
    }

    private static BasicMoveBehaviour buildMoveBehaviour(String nameMoveBehaviour) {
        String moveBehavior = nameMoveBehaviour.substring(0,1).toUpperCase() + nameMoveBehaviour.substring(1);
        switch (moveBehavior) {
            case "BasicMoveBehaviour":
                return new BasicMoveBehaviour();
            case "BlockOpponentRiseBehaviour":
                return new BlockOpponentRiseBehaviour();
            case "BuildBeforeMoveBehaviour":
                return new BuildBeforeMoveBehaviour();
            case "DoubleMoveBehaviour":
                return new DoubleMoveBehaviour();
            case "ForceToOppSideBehaviour":
                return new ForceToOppSideBehaviour();
            case "InfiniteMovesOnPerimeterBehaviour":
                return new InfiniteMovesOnPerimeterBehaviour();
            default:
                return null;
        }
    }

    private static BasicBuildBehaviour buildBuildBlockBehaviour(String nameBlockBehaviour) {
        String buildBlockBehaviour = nameBlockBehaviour.substring(0,1).toUpperCase() + nameBlockBehaviour.substring(1);
        switch (buildBlockBehaviour) {
            case "BasicBuildBehaviour":
                return new BasicBuildBehaviour();
            case "DoubleSameSpaceBehaviour":
                return new DoubleSameSpaceBehaviour();
            case "DoubleDifferentSpaceBehaviour":
                return new DoubleDifferentSpaceBehaviour();
            case "RemoveBlockFromNeighbourBehaviour":
                return new RemoveBlockFromNeighbourBehaviour();
            default:
                return null;
        }
    }
}
