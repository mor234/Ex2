package test;

import api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {
    static S_DWGraph stronglyConnectedGraph;
    static directed_weighted_graph pathCheckGraph;


    @BeforeAll
    static void createGraphs() {
        stronglyConnectedGraph = new S_DWGraph();
        for (int i = 0; i < 9; i++) {
            node_data nodeC = new NodeData(i);
            stronglyConnectedGraph.addNode(nodeC);

        }
        stronglyConnectedGraph.connect(0, 1, 0.5);
        stronglyConnectedGraph.connect(0, 2, 0.5);
        stronglyConnectedGraph.connect(2, 3, 0.5);
        stronglyConnectedGraph.connect(3, 5, 0.5);
        stronglyConnectedGraph.connect(5, 0, 0.5);
        stronglyConnectedGraph.connect(1, 4, 0.5);
        stronglyConnectedGraph.connect(4, 1, 0.5);
        stronglyConnectedGraph.connect(4, 6, 0.5);
        stronglyConnectedGraph.connect(6, 7, 0.5);
        stronglyConnectedGraph.connect(7, 8, 0.5);
        stronglyConnectedGraph.connect(8, 5, 0.5);

        pathCheckGraph = new S_DWGraph();
        for (int i = 1; i < 7; i++) {
            pathCheckGraph.addNode(new NodeData(i));
        }
        pathCheckGraph.connect(1, 2, 3);
        pathCheckGraph.connect(1, 4, 4);

        pathCheckGraph.connect(2, 4, 6);
        pathCheckGraph.connect(2, 5, 7);
        pathCheckGraph.connect(2, 3, 2);

        pathCheckGraph.connect(3, 5, 1);
        pathCheckGraph.connect(3, 6, 8);

        pathCheckGraph.connect(4, 5, 5);

        pathCheckGraph.connect(5, 6, 4);

    }


    @Test
    void init() {
        dw_graph_algorithms g1 = new DWGraph_Algo();

        g1.init(stronglyConnectedGraph);
        assertEquals(stronglyConnectedGraph, g1.getGraph(), "equals not working, not working  before change in graph");
        stronglyConnectedGraph.removeNode(1);
        assertNull(g1.getGraph().getNode(1), "return a node after removed");
        assertEquals(stronglyConnectedGraph, g1.getGraph(), "not both graphs changed at the same. time not a shallow copy");
        //return to normal for other function use

        stronglyConnectedGraph.addNode(new NodeData(1));

        stronglyConnectedGraph.connect(0, 1, 0.5);
        stronglyConnectedGraph.connect(1, 4, 0.5);
        stronglyConnectedGraph.connect(4, 1, 0.5);
    }


    @Test
    void copy() {
        dw_graph_algorithms g1 = new DWGraph_Algo();
        dw_graph_algorithms g2 = new DWGraph_Algo();
        g1.init(stronglyConnectedGraph);
        g2.init(g1.copy());//g2 suppose to contain a deep copy of the graph of g1
        assertEquals(g2.getGraph(), g1.getGraph(), "equals not working, not working before  change in graph");
        g1.getGraph().removeNode(1);
        assertNotEquals(g2.getGraph(), g1.getGraph(), "only one graph suppose to change- not suppose to be equal!");

        //return to normal for other function use

        stronglyConnectedGraph.addNode(new NodeData(1));
        stronglyConnectedGraph.connect(0, 1, 0.5);
        stronglyConnectedGraph.connect(1, 4, 0.5);
        stronglyConnectedGraph.connect(4, 1, 0.5);

    }

    @Test
    void isConnected() {
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.load("data/A0");
        assertTrue(g1.isConnected(), "the given graphs are strongly connected");

        g1.init(stronglyConnectedGraph);
        assertTrue(g1.isConnected(), "the graph is Strongly connected");
        g1.getGraph().removeEdge(4, 6);
        assertFalse(g1.isConnected(), "the graph is not suppose to be connected");
        g1.init(new S_DWGraph());//init with empty graph
        assertTrue(g1.isConnected(), "Empty graph suppose to counts as connected");

        //add for other function use
        stronglyConnectedGraph.connect(4, 6, 0.5);


    }

    @Test
        //check on general example graph
    void shortestPathDist1() {
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(pathCheckGraph);
        assertEquals(g1.shortestPathDist(1, 6), 10);
        assertEquals(g1.shortestPathDist(1, 3), 5);
        assertEquals(g1.shortestPathDist(1, 5), 6);
        assertEquals(g1.shortestPathDist(2, 5), 3);
    }

    @Test
        //check on a very simple graph that return the shortest path, and not the first arrived solution
    void shortestPathDist2() {
        dw_graph_algorithms gA = new DWGraph_Algo();
        directed_weighted_graph g = new S_DWGraph();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));
        g.addNode(new NodeData(3));
        g.connect(0, 1, 5);
        g.connect(0, 3, 1);
        g.connect(1, 2, 1);
        g.connect(3, 2, 10);
        gA.init(g);
        assertEquals(gA.shortestPathDist(0, 2), 6, "not works when the first arrived solution is not the smallest one");


    }

    //check on general example graph, when the node to find distance to is not connected
    @Test
    void shortestPathDist3() {
        directed_weighted_graph notConnectedGraph5_6 = new S_DWGraph(stronglyConnectedGraph);
        notConnectedGraph5_6.removeEdge(4, 6);//now the graph is not strongly connected and there isn't path from 5 to 6
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(notConnectedGraph5_6);
        assertEquals(g1.shortestPathDist(5, 6), -1, "the nodes are not connected. suppose to return -1");

    }

    //check on general example graph the distance to itself
    @Test
    void shortestPathDist4() {
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(stronglyConnectedGraph);
        assertEquals(g1.shortestPathDist(2, 2), 0, "the distance to itself suppose to be 0");
    }

    @Test
        //check on a general graph that returned the right path
    void shortestPath1() {
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(pathCheckGraph);

        Collection<node_data> path = g1.shortestPath(1, 6);
        Iterator<node_data> iterator = path.iterator();
        assertEquals(iterator.next().getKey(), 1, "didn't give the correct first node in the path");
        assertEquals(iterator.next().getKey(), 2, "didn't give the correct second node in the path");
        assertEquals(iterator.next().getKey(), 3, "didn't give the correct third node in the path");
        assertEquals(iterator.next().getKey(), 5, "didn't give the correct forth node in the path");
        assertEquals(iterator.next().getKey(), 6, "didn't give the correct fifth node in the path");
        assertEquals(path.size(), 5, "the size of the path isn't right");

    }

    @Test
        //check return the right path to itself
    void shortestPath2() {
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(pathCheckGraph);
        Collection<node_data> path = g1.shortestPath(1, 1);
        Iterator<node_data> iterator = path.iterator();
        assertEquals(iterator.next().getKey(), 1, "didn't give the correct first node in the path");
        assertEquals(path.size(), 1, "the size of the path isn't right");

    }

    @Test
        //check return empty collection when path doesn't exist
    void shortestPath3() {
        directed_weighted_graph notConnectedGraph5_6 = new S_DWGraph(stronglyConnectedGraph);
        notConnectedGraph5_6.removeEdge(4, 6);//now the graph is not strongly connected and there isn't path from 5 to 6
        dw_graph_algorithms g1 = new DWGraph_Algo();
        g1.init(notConnectedGraph5_6);
        assertNull(g1.shortestPath(5, 6), "didn't return null when weren't connected");

    }

    /*copied from example test, with a few changes
    check I can save and load a graph I made.
     */
    @Test
    void save_load() {
        directed_weighted_graph g0 = graph_creator(10, 30, 1);
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        String str = "data/myGraph";

        if (!ag0.save(str))
            fail();

        directed_weighted_graph g1 = graph_creator(10, 30, 1);
        assertEquals(ag0.getGraph(), g1, "equals doesn't return true for 2 equal graphs ");
        g0.removeNode(0);
        assertNotEquals(ag0.getGraph(), g1, "equals doesn't return false for graphs after change in one of them ");
        if (!ag0.load(str))
            fail();
        assertEquals(ag0.getGraph(), g1, "didn't load the old graph correctly");
    }
    /*
     check I can load and save a graph from file.
     */

    @Test
    void load_save() {

        String str = "data/A0";

        dw_graph_algorithms ag0 = new DWGraph_Algo();

        if (!ag0.load(str))
            fail();

        ag0.save("data/saveA0");
        dw_graph_algorithms ag1 = new DWGraph_Algo();
        if (!ag1.load("data/saveA0"))
            fail();
        assertEquals(ag1.getGraph(),ag0.getGraph(),"didn't save and load correctly");

    }

    @Test
    void runTimeTest1() {
        long start = new Date().getTime();
        graph_creator(1000000, 1000000, 1);
        long end = new Date().getTime();
        double dt = (end - start) / 1000.0;
        //System.out.println(dt);
        assertTrue(dt < 5, "the build takes " + dt + " seconds, longer than 5 seconds");

    }

    ///////////////////////////////////
    /**
     * Generate a random graph with v_size nodes and e_size edges -given as part of example test
     *
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     * @author boaz_benmoshe
     */
    private static Random _rnd = null;

    public static directed_weighted_graph graph_creator(int v_size, int e_size, int seed) {
        directed_weighted_graph g = new S_DWGraph();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(new NodeData(i));
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0 + min, (double) max);
        int ans = (int) v;
        return ans;
    }

    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max - min;
        double ans = d * dx + min;
        return ans;
    }

    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     *
     * @param g
     * @return
     */
    private static int[] nodes(directed_weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodes = new node_data[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for (int i = 0; i < size; i++) {
            ans[i] = nodes[i].getKey();
        }
        Arrays.sort(ans);
        return ans;
    }


}