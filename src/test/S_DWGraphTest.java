package test;

import api.*;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class S_DWGraphTest {

    static S_DWGraph g;


    @Test
    void getNode() {
        directed_weighted_graph g = new S_DWGraph();
        g.addNode((node_data) (new NodeData(0)));
        g.addNode((node_data) (new NodeData(1)));
        g.addNode((node_data) (new NodeData(2)));
        assertNull(g.getNode(5), "suppose to return null if the node doesnt exist in the graph");
        assertEquals(g.getNode(2).getKey(), 2, "didn't get the right node");


    }

    @Test
    void getEdge() {
        directed_weighted_graph g = new S_DWGraph();
        g.addNode((node_data) (new NodeData(0)));
        g.addNode((node_data) (new NodeData(1)));
        g.addNode((node_data) (new NodeData(2)));
        g.connect(0, 1, 0.76);
        assertNull(g.getEdge(1, 0), "opposite direction edge not suppose to be created, suppose to return null");
        assertEquals(g.getEdge(0, 1).getWeight(), 0.76, "didn't create the edge");


    }

    @Test
    void connect() {
        directed_weighted_graph g1 = new S_DWGraph();
        g1.addNode((node_data) (new NodeData(1)));
        g1.addNode((node_data) (new NodeData(2)));
        g1.addNode((node_data) (new NodeData(3)));
        g1.connect(1, 2, 1.5);
        g1.connect(2, 1, 3);
        g1.connect(1, 3, 6);


        assertEquals(g1.getEdge(1, 2).getWeight(), 1.5, "not connected edge correctly");
        assertEquals(g1.getEdge(2, 1).getWeight(), 3, "not connected edge in the other direction correctly");
        assertNull(g1.getEdge(3, 1), "created edge in the opposite direction");
        g1.connect(1, 3, 7);
        assertEquals(g1.getEdge(1, 3).getWeight(), 7, "didn't update the weight");

    }

    @Test
    void getV() {
        directed_weighted_graph g1 = new S_DWGraph();
        g1.addNode((node_data) (new NodeData(1)));
        g1.getNode(1).setInfo("hello1");
        g1.addNode((node_data) (new NodeData(2)));
        g1.getNode(2).setInfo("hello2");
        g1.addNode((node_data) (new NodeData(3)));
        g1.getNode(3).setInfo("hello3");


        Collection<node_data> nodes = g1.getV();
        assertEquals(nodes.size(), 3, "didn't get the right collection");
        int indexCounter[] = new int[3];

        for (node_data node : nodes) {
            indexCounter[node.getKey() - 1]++;
            assertEquals("hello" + node.getKey(), node.getInfo(), "not got the node correctly");
        }
        assertEquals(indexCounter[0], 1, "not found node 1");
        assertEquals(indexCounter[1], 1, "not found node 2");
        assertEquals(indexCounter[2], 1, "not found node 3");

    }

    @Test
    void getE() {
        directed_weighted_graph g1 = new S_DWGraph();
        g1.addNode((node_data) (new NodeData(1)));
        g1.addNode((node_data) (new NodeData(2)));
        g1.addNode((node_data) (new NodeData(3)));
        g1.connect(1, 2, 1.5);
        g1.connect(2, 1, 3);
        g1.connect(1, 3, 6);

        Edge e1_2 = (Edge) g1.getEdge(1, 2);
        Edge e2_1 = (Edge) g1.getEdge(2, 1);
        Edge e1_3 = (Edge) g1.getEdge(1, 3);

        Collection<edge_data> edges = g1.getE(1);
        assertEquals(edges.size(), 2, "didn't get the right collection");

        assertTrue(edges.contains(e1_2), "not found edge 1_2");
        assertFalse(edges.contains(e2_1), "contain edge in the opposite direction 2_1");
        assertTrue(edges.contains(e1_3), "not found edge 1_3");

    }

    @Test
    void removeNode() {
        directed_weighted_graph g1 = new S_DWGraph();
        g1.addNode((node_data) (new NodeData(1)));
        g1.addNode((node_data) (new NodeData(2)));
        g1.addNode((node_data) (new NodeData(3)));
        g1.connect(1, 2, 1.5);
        g1.connect(2, 1, 3);
        g1.connect(1, 3, 6);
        int mc = g1.getMC();
        g1.removeNode(1);
        assertNull(g1.getNode(1), "didn't delete node 1");
        assertNull(g1.getEdge(1, 2), "didn't delete edge ");
        assertNull(g1.getEdge(2, 1), "didn't delete edge in the oposite direction.");
        assertNull(g1.getE(1), "didn't delete edges");
        assertTrue(mc + 4 <= g1.getMC(), "didn't update mc correctly");


    }

    @Test
    void removeEdge() {
        directed_weighted_graph g1 = new S_DWGraph();
        g1.addNode((node_data) (new NodeData(1)));
        g1.addNode((node_data) (new NodeData(2)));
        g1.addNode((node_data) (new NodeData(3)));
        g1.connect(1, 2, 1.5);
        g1.connect(2, 1, 3);
        g1.connect(1, 3, 6);


        g1.removeEdge(1, 2);


        assertEquals(g1.edgeSize(), 2, "not updated num of edges");

        assertNull(g1.getEdge(1, 2), "didn't delete edge 1-2");
        assertEquals(g1.getEdge(2, 1).getWeight(), 3, "deleted the opposite edge: 2-1");


        g1.removeEdge(1, 2);
        assertEquals(g1.edgeSize(), 2, "num of edges not suppose to change, delete a deleted edge ");
        assertEquals(g1.nodeSize(), 3, "nodeSize not suppose to change when deleting edges");

    }

    @Test
    void nodeSize() {
        directed_weighted_graph g = new S_DWGraph();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(3));
        g.addNode(new NodeData(4));
        g.addNode(new NodeData(1));//not suppose to be added
        g.addNode(new NodeData(4));//not suppose to be added
        g.addNode(new NodeData(-5));

        assertEquals(g.nodeSize(), 5, "not added nodes correctly");
    }

    @Test
    void edgeSize() {
        directed_weighted_graph g = new S_DWGraph();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));


        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(1, 2, 2.7);
        g.connect(2, 0, 7);//num of edges suppose to change- a different node


        g.connect(0, 1, 3);//not suppose to change the count
        g.connect(1, 1, 1);//not suppose to connect

        assertEquals(g.edgeSize(), 4, "didn't count edges correctly ");

    }

    @Test
    void getMC() {
        directed_weighted_graph g = new S_DWGraph();
        for (int i = 0; i < 9; i++) {
            node_data nodeC = new NodeData(i);
            g.addNode(nodeC);
        }
        g.connect(0, 1, 0.5);
        g.connect(0, 2, 0.5);
        g.connect(2, 3, 0.5);
        g.connect(3, 5, 0.5);
        g.connect(5, 0, 0.5);
        g.connect(1, 4, 0.5);
        g.connect(4, 1, 0.5);
        g.connect(4, 6, 0.5);
        g.connect(6, 7, 0.5);
        g.connect(7, 8, 0.5);
        g.connect(8, 5, 0.5);
        int mcAfterCreating = g.getMC();
        g.connect(7, 8, 2.5);
        int mcConnectEdge = g.getMC();
        g.removeNode(1);
        int mcRemoveNode = g.getMC();
        g.removeEdge(7, 8);
        int mcRemoveEdge = g.getMC();

        assertTrue(mcAfterCreating < mcConnectEdge, "not updated mc after connecting edge");
        assertTrue(mcConnectEdge < mcRemoveNode, "not updated mc after removing node");
        assertTrue(mcRemoveNode < mcRemoveEdge, "not updated mc after removing edge");
        g.removeNode(1);//already happened
        g.removeEdge(7, 8);//already happend
        g.getNode(3);
        g.edgeSize();
        assertEquals(mcRemoveEdge, g.getMC(), "mc suppose to remain the same, the graph hasn't changed");

    }

    @Test
    void equals() {
        directed_weighted_graph g = new S_DWGraph();
        directed_weighted_graph g1 = new S_DWGraph();
        g.addNode(new NodeData(0));
        g.addNode(new NodeData(1));
        g.addNode(new NodeData(2));

        g1.addNode(new NodeData(0));
        g1.addNode(new NodeData(1));
        g1.addNode(new NodeData(2));

        g.connect(0, 1, 1);
        g.connect(0, 2, 2);
        g.connect(1, 2, 2.7);
        g.connect(2, 0, 7);

        g1.connect(0, 1, 1);
        g1.connect(0, 2, 2);
        g1.connect(1, 2, 2.7);
        g1.connect(2, 0, 7);

        assertEquals(g, g, "a graph not equals to itself!");
        assertEquals(g, g1, "suppose to be equal");

        g1.connect(2, 0, 6);
        assertNotEquals(g, g1, "edge weight is different, not suppose to be equal");
        g1.connect(2, 0, 7);
        assertEquals(g, g1, "edge weight are the same, suppose to be equal");
        g1.removeNode(1);
        assertNotEquals(g, g1, "removed node, , not suppose to be equal");
        g1.addNode(new NodeData(1));
        g1.connect(0, 1, 1);
        g1.connect(1, 2, 2.7);
        assertEquals(g, g1, "added everything again, suppose to be equal");


    }
}