package gameClient;

import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.game_service;
import api.node_data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains the logical parts ot the game and
 * allow  calculation of the location ot the agents during the game
 *
 * @author mor234
 */

public class GameAlgo {
    public static boolean getIsAgentOnNode() {
        return isAgentOnNode;
    }

    HashMap<Integer, dw_graph_algorithms> smallGraphs;

    private static boolean isAgentOnNode = false;


    public GameAlgo() {
        smallGraphs = new HashMap<Integer, dw_graph_algorithms>();
    }


    /**
     * choose start location for all the agent
     *
     * @param gameGraph
     * @param pokemons
     * @param numberOfAgents
     */
    public static void chooseStartLocation(game_service gameS, int numberOfAgents, dw_graph_algorithms
            gameGraph, List<CL_Pokemon> pokemons) {


        Iterator<CL_Pokemon> pokemonIterator = pokemons.iterator();

        for (int i = 0; i < numberOfAgents; i++) {
            if (pokemonIterator.hasNext()) {
                CL_Pokemon pokemon = pokemonIterator.next();
                Arena.updateEdge(pokemon, gameGraph.getGraph());

                int destNode = pokemon.get_edge().getDest();
                int srcNode = pokemon.get_edge().getSrc();

                gameS.addAgent(srcNode);
                gameS.chooseNextEdge(i, destNode);
                pokemon.setTaken(true);

            } else {
                //take random point to start
                gameS.addAgent(gameGraph.getGraph().getV().iterator().next().getKey());

            }
        }
    }


    /**
     * write a given json string into a file
     *
     * @param fileName
     * @param json
     * @return
     */

    public static boolean writeJsonToFile(String fileName, String json) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(json);
            writer.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Choose the next node for this agent, look for the closet pokemon, and sign for the other agents it's taken,
     * so they try to go towards a different one
     *
     * @param r        agent
     * @param g        the graph the game is on
     * @param gameS    the game
     * @param pokemons list of all the pokemones currently in he game
     * @return the key of the recommended next node for this agent inorder to reach a pokemon
     */

    private static int nextNode2(CL_Agent r, DWGraph_Algo g, game_service gameS, List<CL_Pokemon> pokemons) {
        int src = r.getSrcNode();

        //find the closest pokemon
        double minDist = -1;
        int minIndex = -1;
        List<node_data> pathToPokDest = new ArrayList<>();
        int destNode = -1;
        int srcNode = -1;

        CL_Pokemon chosenPok = new CL_Pokemon();


        for (CL_Pokemon pok : pokemons) {

            if (!pok.isTaken()) {

                Arena.updateEdge(pok, g.getGraph());
                destNode = pok.get_edge().getDest();

                if (src == destNode)//if already in dest node
                {
                    destNode = pok.get_edge().getSrc();//try to return so you can eat next time
                }
                pathToPokDest = g.shortestPath(src, destNode);
                //if the path exist
                if (pathToPokDest != null) {
                    if (minIndex == -1) {
                        minIndex = pathToPokDest.get(1).getKey();
                        minDist = g.getLatestShortestDist();
                        chosenPok = pok;
                    } else if (minDist > g.getLatestShortestDist()) {
                        minIndex = pathToPokDest.get(1).getKey();
                        minDist = g.getLatestShortestDist();
                        chosenPok = pok;
                    }

                }
            }
        }
//        if (pathToPokDest.size() == 2)//it's on the next edge
//            isAgentOnNode = true;//sign it's need to make a move
        chosenPok.setTaken(true);//if wasn't any available pok, it's the empty pok and nothing happened.
        return minIndex;
    }


    /**
     * define for every agent who is on an node  the next destination to go to.
     * the destination is chosen according to which pokemons it's closest to,
     * and make he next sep inorder to each it.
     * used a given example as template , but modified
     *
     * @param gameS
     * @param gg
     * @param
     */
    public static boolean moveAgants(game_service gameS, dw_graph_algorithms gg) {
        isAgentOnNode = false;

        List<CL_Agent> log = Arena.getAgents(gameS.getAgents(), gg.getGraph());
        ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(gameS.getPokemons());

        for (int i = 0; i < log.size(); i++) {
            CL_Agent r = log.get(i);
            int dest = r.getNextNode();
            int src = r.getSrcNode();
            int id = r.getID();
            if (dest == -1) {
                isAgentOnNode = true;
                int new_dest = nextNode2(r, (DWGraph_Algo) gg, gameS, pokemons);
                gameS.chooseNextEdge(id, new_dest);
                System.out.println("Agent:" + i + ") " + r + "  move to node " + new_dest);
            }
        }
        return isAgentOnNode;

    }


}

