package api;

import com.google.gson.annotations.SerializedName;

/**
 * This class represent an directional weighted edge
 *
 * @author mor234
 */

public class Edge implements edge_data {

    @SerializedName("src")
    private int srcKey;
    @SerializedName("w")
    private double weight;
    @SerializedName("dest")
    private int dstKey;
    private String edgeInfo;
    private int tag;


    public Edge(edge_data edge) {
        if (edge != null) {
            this.srcKey = edge.getSrc();
            this.dstKey = edge.getDest();
            this.weight = edge.getWeight();
            this.edgeInfo = edge.getInfo();
            this.tag = edge.getTag();

        }
    }

    /*
     * Constructor
     */
    public Edge(int srcKey, int dstKey, double weight) {
        this.srcKey = srcKey;
        this.dstKey = dstKey;
        this.weight = weight;
        this.edgeInfo = "";
        this.tag = -1;

    }

    /**
     * The id of the source node of this edge.
     *
     * @return
     */
    @Override
    public int getSrc() {
        return srcKey;
    }

    /**
     * The id of the destination node of this edge
     *
     * @return
     */
    @Override
    public int getDest() {
        return dstKey;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return edgeInfo;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        edgeInfo = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used by algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;

    }

    /**
     * Automatically generated function. to be used us part of equals of S_DWGraph
     *
     * @param o
     * @return
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return srcKey == edge.srcKey && Double.compare(edge.weight, weight) == 0 && dstKey == edge.dstKey;
    }


}
