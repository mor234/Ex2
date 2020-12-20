package gameClient;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent
 */

public class CL_Agent implements Runnable {
    public static final double EPS = 0.0001;
    private static int _count = 0;
    private static int _seed = 3331;
    private int _id;
    //	private long _key;
    private geo_location _pos;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private directed_weighted_graph _gg;



    private CL_Pokemon _curr_fruit;
    private long _sg_dt;
    private double _value;

    private ArrayList<CL_Pokemon> option_for_next;///////***
    private game_service gameS;

    /**
     *
     * @param _gg
     */
    public void set_gg(directed_weighted_graph _gg) {
        this._gg = _gg;
    }

    /**
     *
     * @param gameS
     */

    public void setGameS(game_service gameS) {
        this.gameS = gameS;
    }

    /**
     *
     * @param option_for_next
     */

    public void setOption_for_next(ArrayList<CL_Pokemon> option_for_next) {
        this.option_for_next = option_for_next;
    }




    public CL_Agent(directed_weighted_graph g, int start_node) {
        _gg = g;
        setMoney(0);
        this._curr_node = _gg.getNode(start_node);
        _pos = _curr_node.getLocation();
        _id = -1;
        setSpeed(0);
    }

    /**
     * update this agent according to a given json string reprsnting an agent
     * @param json
     */

    public void update(String json) {
        JSONObject line;
        try {
            // "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
            line = new JSONObject(json);
            JSONObject singleJsonAgent = line.getJSONObject("Agent");
            int id = singleJsonAgent.getInt("id");
            if (id == this.getID() || this.getID() == -1) {
                if (this.getID() == -1) {
                    _id = id;
                }
                double speed = singleJsonAgent.getDouble("speed");
                String p = singleJsonAgent.getString("pos");
                Point3D pp = new Point3D(p);
                int src = singleJsonAgent.getInt("src");
                int dest = singleJsonAgent.getInt("dest");
                double value = singleJsonAgent.getDouble("value");
                this._pos = pp;
                this.setCurrNode(src);
                this.setSpeed(speed);
                this.setNextNode(dest);
                this.setMoney(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Override
    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    public String toJSON() {
        int d = this.getNextNode();
        String ans = "{\"Agent\":{"
                + "\"id\":" + this._id + ","
                + "\"value\":" + this._value + ","
                + "\"src\":" + this._curr_node.getKey() + ","
                + "\"dest\":" + d + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + _pos.toString() + "\""
                + "}"
                + "}";
        return ans;
    }

    private void setMoney(double v) {
        _value = v;
    }

    public boolean setNextNode(int dest) {
        boolean ans = false;
        int src = this._curr_node.getKey();
        this._curr_edge = _gg.getEdge(src, dest);
        if (_curr_edge != null) {
            ans = true;
        } else {
            _curr_edge = null;
        }
        return ans;
    }

    public void setCurrNode(int src) {
        this._curr_node = _gg.getNode(src);
    }

    public boolean isMoving() {
        return this._curr_edge != null;
    }

    public String toString() {
        return toJSON();
    }

    public String toString1() {
        String ans = "" + this.getID() + "," + _pos + ", " + isMoving() + "," + this.getValue();
        return ans;
    }

    /**
     * @return
     */


    public int getID() {
        // TODO Auto-generated method stub
        return this._id;
    }

    public geo_location getLocation() {
        // TODO Auto-generated method stub
        return _pos;
    }


    public double getValue() {
        // TODO Auto-generated method stub
        return this._value;
    }


    public int getNextNode() {
        int ans = -2;
        if (this._curr_edge == null) {
            ans = -1;
        } else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }

    public double getSpeed() {
        return this._speed;
    }

    public void setSpeed(double v) {
        this._speed = v;
    }

    public CL_Pokemon get_curr_fruit() {
        return _curr_fruit;
    }

    public void set_curr_fruit(CL_Pokemon curr_fruit) {
        this._curr_fruit = curr_fruit;
    }

    public void set_SDT(long ddtt) {
        long ddt = ddtt;
        if (this._curr_edge != null) {
            double w = get_curr_edge().getWeight();
            geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
            geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
            double de = src.distance(dest);
            double dist = _pos.distance(dest);
            if (this.get_curr_fruit().get_edge() == this.get_curr_edge()) {
                dist = _curr_fruit.getLocation().distance(this._pos);
            }
            double norm = dist / de;
            double dt = w * norm / this.getSpeed();
            ddt = (long) (1000.0 * dt);
        }
        this.set_sg_dt(ddt);
    }

    public edge_data get_curr_edge() {
        return this._curr_edge;
    }

    public long get_sg_dt() {
        return _sg_dt;
    }

    public void set_sg_dt(long _sg_dt) {
        this._sg_dt = _sg_dt;
    }

    @Override
    public void run() {
        DWGraph_Algo shortestPathGraph = new DWGraph_Algo();
        shortestPathGraph.init(_gg);

        //find the closest pokemon
        double minDist = -1;
        int minIndex = -1;

        ArrayList<CL_Pokemon> pokemons = this.option_for_next;
        int destNode = -1;
        int srcNode = -1;

        for (CL_Pokemon pok : pokemons) {
            Arena.updateEdge(pok, _gg);
            destNode = pok.get_edge().getDest();
            int src = this.getSrcNode();
            if (src == destNode)//if already in dest node
            {
                destNode = pok.get_edge().getSrc();//try to return so you can eat next time
            }
            List<node_data> pathToPokDest = shortestPathGraph.shortestPath(src, destNode);
            //if the path exist
            int index;
            double pathDist;
            synchronized (this) {
                index = pathToPokDest.get(1).getKey();
                pathDist = shortestPathGraph.getLatestShortestDist();
            }
            if (pathToPokDest != null) {
                if (minIndex == -1) {
                    minIndex = index;
                    minDist = pathDist;
                } else if (minDist > shortestPathGraph.getLatestShortestDist()) {
                    minIndex = index;
                    minDist = pathDist;

                }

            }
            break;

        }

        gameS.chooseNextEdge(this._id, minIndex);

    }

}
