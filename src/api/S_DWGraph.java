package api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represent
 * a directional weighted graph.
 * The class implement interface that has  has a road-system or communication network in mind -
 * and should support a large number of nodes (over 100,000).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 *
 * @author mor234
 */
public class S_DWGraph implements directed_weighted_graph {
    private HashMap<String, edge_data> edges;
    private HashMap<Integer, node_data> nodes;

    private int mc = 0;

    /**
     * @param src
     * @param dest
     * @return
     */

    private String edgeStringKey(int src, int dest) {
        return src + "_" + dest;
    }

    /**
     * constructor
     */
    public S_DWGraph() {

        this.nodes = new HashMap<Integer, node_data>();
        this.edges = new HashMap<String, edge_data>();
        mc++;
    }

    /**
     * copy constructor
     * update mc only once for initializing the graph
     *
     * @param graphToCopy
     */
    public S_DWGraph(S_DWGraph graphToCopy) {

        this();

        if (graphToCopy != null) {
            Iterator<node_data> nodesIterator = graphToCopy.nodes.values().iterator();
            //copy each node using NodeData copy constructor
            while (nodesIterator.hasNext()) {
                node_data node = nodesIterator.next();
                this.nodes.put(node.getKey(), new NodeData((NodeData) node));
            }
            Iterator<edge_data> edgesIterator = graphToCopy.edges.values().iterator();
            //copy each edge using Edge copy constructor
            while (edgesIterator.hasNext()) {
                edge_data edge = edgesIterator.next();
                this.edges.put(edgeStringKey(edge.getSrc(), edge.getDest()), new Edge((Edge) edge));
            }

        }
    }


    /**
     * returns the node_data by the node_id,
     *
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public  node_data getNode(int key) {

        return nodes.get(key);
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        return this.edges.get(edgeStringKey(src, dest));
    }

    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (n != null) {
            //assumes has no edge connected to it.
            //edges need to be added after adding the node
            nodes.put(n.getKey(), n);
            mc++;
        }

    }

    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
       public void connect(int src, int dest, double w) {

        //if the weight is positive
        if (w > 0) {
            /*can't be any edge between the node to itself*/
            if (src == dest) {
                return;
            }
            NodeData srcN = (NodeData) this.getNode(src);
            NodeData destN = (NodeData) this.getNode(dest);
            /*if one of the nodes doesn't exist in the graph- don't do any thing*/
            if (srcN == null || destN == null) {
                return;
            }

            /*if the edge exist- update the edge*/
            if (edges.containsKey(edgeStringKey(src, dest))) {
                if (edges.get(edgeStringKey(src, dest)).getWeight() == w)//if the same weight
                    return;
                edges.replace(edgeStringKey(src, dest), new Edge(src, dest, w));

            }
            /*if the edge doesn't exist- create it*/
            else {
                Edge newEdge = new Edge(src, dest, w);
                edges.put(edgeStringKey(src, dest), newEdge);
                //add to list of edges getting out of node src
                ((NodeData) getNode(src)).addEdgeFrom(dest);
                //add to list of edges getting to node dest
                ((NodeData) getNode(dest)).addEdgeTo(src);
                //keep the indexes of the place in the array in the nodes, in order to remove in O(1)

            }
            mc++;//update changes count only if the weight is different/ created a new edge.
        }

    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        NodeData n = (NodeData) getNode(node_id);
        Collection<edge_data> ansEdges = new ArrayList<edge_data>();
        if (n != null) {
            Iterator<Integer> iteratorDest = n.iteratorEdgesFrom();
            while (iteratorDest.hasNext()) {
                ansEdges.add(this.edges.get(edgeStringKey(node_id, iteratorDest.next())));
            }
            return ansEdges;
        }
        return null;//if the node doesn't exist in the graph- return null.
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        NodeData node = (NodeData) this.nodes.get(key);
        if (node != null) {
            mc++;//update changes count;
            //remove all edges getting out of this node.
            Iterator<Integer> indexConnectedNode = node.iteratorEdgesFrom();
            while (indexConnectedNode.hasNext()) {
                removeEdge(key, indexConnectedNode.next());
                //after removing, update the iterator
                indexConnectedNode = node.iteratorEdgesFrom();
            }
            //remove all edges getting to this node.
            indexConnectedNode = node.iteratorEdgesTo();
            while (indexConnectedNode.hasNext()) {
                removeEdge(indexConnectedNode.next(), key);
                //after removing, update the iterator
                indexConnectedNode = node.iteratorEdgesTo();
            }
            //remove the node
            this.nodes.remove(key);


        }
        return node;
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {


        Edge removedEdge = (Edge) edges.remove(edgeStringKey(src, dest));
        if (removedEdge != null) {
            mc++;
            //remove from list of edges getting out of node src
            ((NodeData) getNode(src)).removeEdgeFrom(dest);
            //remove from list of edges getting to node dest
            ((NodeData) getNode(dest)).removeEdgeTo(src);
        }

        return removedEdge;
    }


    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return nodes.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return edges.size();
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     * ToString function- convert the graph to String
     * mainly used for debugging
     *
     * @return String representing the graph
     */
    @Override

    public String toString() {
        String graphStr = "";
        Iterator<node_data> i = nodes.values().iterator();
        graphStr += "nodes: " + nodeSize() + " edges: " + this.edgeSize();
        while (i.hasNext()) {
            NodeData n = (NodeData) i.next();
            graphStr += "\n" + n.toString();

            Collection neighbors = getE(n.getKey());

            if (!neighbors.isEmpty()) {
                Iterator<edge_data> EdgesIterator = neighbors.iterator();
                graphStr += "\n";
                graphStr += "the " + neighbors.size() + " neighbors are: ";
                while (EdgesIterator.hasNext()) {
                    edge_data e = EdgesIterator.next();
                    graphStr += "dest id: " + e.getDest() + " Edge weight: " + e.getWeight() + " ";
                    if (EdgesIterator.hasNext())
                        graphStr += ",";
                }
            }
        }
        return graphStr;
    }

    /**
     * used for serialization, I want to keep only the values from the hashmap.
     */
    public class GraphForSerlization {
        @SerializedName("Nodes")
        Collection<node_data> nodes;
        @SerializedName("Edges")
        Collection<edge_data> edges;

        public GraphForSerlization(Collection<node_data> nodes, Collection<edge_data> edges) {
            this.nodes = nodes;
            this.edges = edges;
        }
    }

    /**
     * This methode return a class containing only the Collectiones values of the edges and nodes hash maps
     * inorder to match the required files in Json.
     * @return
     */

    public GraphForSerlization serializationFormat() {
        return new GraphForSerlization(nodes.values(), edges.values());

    }

    /**
     * Return whether or not two S_DWGraph are equal.
     * to be used in tests.
     *
     * @param o
     * @return
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        S_DWGraph s_dwGraph = (S_DWGraph) o;
        if (s_dwGraph.nodeSize() != this.nodeSize())
            return false;
        Collection<node_data> oNodes = s_dwGraph.getV();
        for (node_data node : oNodes) {
            if (!(this.getV().contains((NodeData) node)))//uses the override equals in NodeData
                return false;
        }
        if (s_dwGraph.edgeSize() != this.edgeSize())
            return false;
        for (edge_data edge : edges.values()) {
            if (!(((Edge) edge).equals(s_dwGraph.getEdge(edge.getSrc(), edge.getDest()))))//uses the override equals in Edge
                return false;
        }

        return true;
    }


}
