package api;

import com.google.gson.annotations.SerializedName;
import gameClient.util.Point3D;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represent a node (vertex) in a directional weighted graph.
 *
 * @author mor234
 */

public class NodeData implements node_data, Comparable {
    @SerializedName("pos")
    private geo_location nodeLocation;
    @SerializedName("id")
    private int nodeKey;
    private double weight;
    private String nodeInfo;
    private int tag;

    //an array list containing the keys of all the node there is an edge from this node to them
    private ArrayList<Integer> edgesFromNode;
    //an array list containing the keys of all the node there is an edge from them to this node
    private ArrayList<Integer> edgesToNode;

    /**
     * Constructor
     *
     * @param nodeKey
     */
    public NodeData(int nodeKey) {
        this.edgesFromNode = new ArrayList<Integer>();
        this.edgesToNode = new ArrayList<Integer>();
        this.nodeKey = nodeKey;

        this.weight = -1;
        this.nodeInfo = "";
        this.tag = -1;
    }

    /**
     * Constructor
     *
     * @param node
     */

    public NodeData(NodeData node) {
        this.edgesFromNode = new ArrayList<Integer>();
        this.edgesToNode = new ArrayList<Integer>();
        if (node != null) {

            this.nodeKey = node.nodeKey;
            this.weight = node.weight;
            this.nodeLocation = null;///fix!!!!!
            this.nodeInfo = node.nodeInfo;
            this.tag = node.tag;

            Iterator<Integer> edgesIterator = node.edgesFromNode.iterator();
            while (edgesIterator.hasNext()) {
                edgesFromNode.add(edgesIterator.next());
            }

            edgesIterator = node.edgesToNode.iterator();
            while (edgesIterator.hasNext()) {
                edgesToNode.add(edgesIterator.next());

            }
        }
    }


    /**
     * Add dest to the list
     * of edges out of this node.
     * <p>
     * * Note: this method should run in O(1) time.
     *
     * @param dest - the destination of the edge from this node.
     */

    public void addEdgeFrom(int dest) {
        //add to the list of edges getting out from this node
        edgesFromNode.add(dest);
    }


    /**
     * Add edge from node src
     * to the Arraylist  of edges to this node.
     * <p>
     * * Note: this method should run in O(1) time.
     *
     * @param src - the source of the edge to this ode.
     */

    public void addEdgeTo(int src) {
        edgesToNode.add(src);
    }

    /**
     * Remove dest from the list
     * of edges out of this node.
     * <p>
     * * Note: this method should run in O(1) time.
     *
     * @param dest - the key of the destination of the edge from this node.
     */

    public void removeEdgeFrom(int dest) {
        edgesFromNode.remove((Integer) dest);
    }


    /**
     * Remove src from
     * the Arraylist of edges to this node.
     * <p>
     * * Note: this method should run in O(1) time.
     *
     * @param src - the key of the source of the edge to this node.
     */

    public void removeEdgeTo(int src) {
        edgesToNode.remove((Integer) src);
    }


    /**
     * This method returns a Iterator (shallow copy) for the
     * collection representing all the key of the nodes connected to edges getting out of
     * this node (all the edges starting (source) at this node)
     *
     * @return Iterator<Integer>
     */

    public Iterator<Integer> iteratorEdgesFrom() {
        return edgesFromNode.iterator();
    }

    /**
     * This method returns a Iterator (shallow copy) for the
     * collection representing all the key of the nodes connected to edges getting into
     * this node (all the keys of nodes their edge is ending at this node)
     *
     * @return Iterator<Integer>
     */

    public Iterator<Integer> iteratorEdgesTo() {
        return edgesToNode.iterator();
    }


    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return nodeKey;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return nodeLocation;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this.nodeLocation = new Point3D(p.x(), p.y(), p.z());
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public synchronized double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public synchronized void setWeight(double w) {
        weight = w;

    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return nodeInfo;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        nodeInfo = s;

    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public synchronized int getTag() {
        return tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public synchronized void setTag(int t) {
        tag = t;

    }

    /**
     * Compares this node with the given node for order.  Returns a
     * negative integer, zero, or a positive integer as this node weight is less
     * than, equal to, or greater than the weight the given node
     * to be used in class DWGraph_Algo (algorithms on directional weighted graphs)
     *
     * @param n the node to be compared.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */

    @Override
    public int compareTo(Object n) {
        return Double.compare(this.getWeight(), ((NodeData) n).getWeight());
    }

    /**
     * check if two nodeInfo are equal, heavily based on the Automatically generated function,
     * compare key, tag, stringInfo and edges connected the the node
     * to be used as part of equals of class S_DWGraph
     *
     * @param o
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeData nodeData = (NodeData) o;
        if (nodeLocation != null) {
            if (nodeData.nodeLocation == null)//not both not null
                return false;
            if (!(nodeKey == nodeData.nodeKey &&
                    Double.compare(getWeight(), nodeData.getWeight()) == 0))
                return false;

            return Double.compare(nodeLocation.x(), nodeData.nodeLocation.x()) == 0 &&
                    Double.compare(nodeLocation.y(), nodeData.nodeLocation.y()) == 0 &&
                    Double.compare(nodeLocation.z(), nodeData.nodeLocation.z()) == 0;
        } else if (nodeData.nodeLocation != null)
            return false;//not both null


        if (edgesFromNode.size() != nodeData.edgesFromNode.size())
            return false;
        for (Integer i : edgesFromNode) {
            if (!nodeData.edgesFromNode.contains(i))
                return false;
        }
        if (edgesToNode.size() != nodeData.edgesToNode.size())
            return false;

        for (Integer i : edgesToNode) {
            if (!nodeData.edgesToNode.contains(i))
                return false;
        }

        return true;
    }


    /**
     * to string function for NodeData, used for debugging
     */

    @Override
    public String toString() {
        String loc = null;
        if (this.nodeLocation != null) {
            geo_location gl = this.nodeLocation;
            loc = " x:" + gl.x() + " y:" + gl.y() + " z:" + gl.z();
        }
        return "node id: " + nodeKey + " pos:" + loc;
    }


}
