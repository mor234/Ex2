package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.dw_graph_algorithms;
import api.game_service;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gameClient.gui.MyFrame;
import gameClient.gui.StartPanel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Ex2 {

    public static Id_Level id_level = new Id_Level();
    private static MyFrame _win;


    /**
     * The main program, the controller, open the graphical interfaces, call to logical functions
     * and run the game.
     *
     * @param args
     */

    public static void main(String args[]) throws InterruptedException {

        StartPanel p;

        if (args.length > 0) {

            if (!id_level.setID_Level(args[0], args[1])) ;//need to check for errors
            System.exit(-1);


        } else {
            p = new StartPanel(id_level);
            p.startScreen();

            while (p.isVisible())
                Thread.sleep(100);
        }
        //after this, id_level contains id and level number

        game_service gameS = Game_Server_Ex2.getServer(id_level.getLevel()); // you have [0,23] games
        gameS.login(id_level.getId());


        //initialize
        GameInfo gameInfo = new GameInfo();
        gameInfo.load(getGsonObjectFromString(gameS.toString()));
        //load the graph
        dw_graph_algorithms gameGraph = new DWGraph_Algo();
        gameGraph.load(gameInfo.graphFileLoc);
        //initialize view
        Arena _ar = new Arena();
        _ar.setGraph(gameGraph.getGraph());
        _ar.setPokemons(Arena.json2Pokemons(gameS.getPokemons()));
        //place the agents
        GameAlgo.chooseStartLocation(gameS, gameInfo.numberOfAgent, gameGraph, _ar.getPokemons());
        _ar.setAgents(Arena.getAgents(gameS.getAgents(), gameGraph.getGraph()));


        _win = new MyFrame("Ex2 ");
        _win.setSize(1000, 700);

        _win.update(_ar);
        _win.setVisible(true);

        gameS.startGame();
        _ar.setTimeToEnd(gameS.timeToEnd());
        gameS.move();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution). level: " + gameInfo.gameLevel);

        long tlastMove = gameS.timeToEnd();
        int ind = 0;
        long dt = 100;

        while (gameS.isRunning()) {

            GameAlgo.moveAgants(gameS, gameGraph);
            try {
                if (ind % 2 == 0) {
                    _ar.setGraph(gameGraph.getGraph());
                    _ar.setPokemons(Arena.json2Pokemons(gameS.getPokemons()));
                    _ar.setAgents(Arena.getAgents(gameS.getAgents(), gameGraph.getGraph()));
                    _ar.setTimeToEnd(gameS.timeToEnd());
                    gameInfo.load(getGsonObjectFromString(gameS.toString()));
                    _ar.setMovesSoFar(gameInfo.moves);
                    _win.update(_ar);
                    _win.repaint();
                }

                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            // if (GameAlgo2.getIsAgentOnNode())
            gameS.move();
//            tlastMove=gameS.timeToEnd();
        }

        gameInfo.load(getGsonObjectFromString(gameS.toString()));
        _ar.setMovesSoFar(gameInfo.moves);
        _ar.setGraph(gameGraph.getGraph());
        _ar.setPokemons(Arena.json2Pokemons(gameS.getPokemons()));
        _ar.setAgents(Arena.getAgents(gameS.getAgents(), gameGraph.getGraph()));
        _ar.setTimeToEnd(gameS.timeToEnd());
        _win.update(_ar);
        _win.repaint();

        Thread.sleep(5000);
        String res = gameS.toString();
        _win.dispose();
        System.out.println(res);
        System.exit(0);
    }


    /**
     * convert string to json Object
     *
     * @param string
     * @return JsonObject o the given string
     */
    public static JsonObject getGsonObjectFromString(String string) {
        return new JsonParser().parse(string).getAsJsonObject();

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

}

