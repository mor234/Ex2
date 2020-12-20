package api;

import com.google.gson.*;
import gameClient.util.Point3D;

import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 *
 * @author mor234
 */

public class DWGraph_Algo implements dw_graph_algorithms {
    private S_DWGraph graphA;
    private double latestShortestDist = -1;

    /**
     * default constructor
     **/

    public DWGraph_Algo() {
        this.graphA = new S_DWGraph();
    }

    /**
     * copy constructor
     **/
    public DWGraph_Algo(dw_graph_algorithms g) {
        if (g != null) {
            this.graphA = new S_DWGraph((S_DWGraph) g.getGraph());
        } else
            this.graphA = new S_DWGraph();
    }


    /**
     * Init the graph on which this set of algorithms operates on.
     * shallow copy of the given graph
     *
     * @param g- the graph
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.graphA = (S_DWGraph) g;//shallow copy

    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return graphA;
    }

    /**
     * for inside use, to prevent calling to 2 functions when want both the distances and the list
     *
     * @return the shortestDist as calculated in the  from the last call to the function shortestPathDist
     */
    public double getLatestShortestDist() {
        return latestShortestDist;
    }


    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */
    @Override
    public directed_weighted_graph copy() {
        return new S_DWGraph(this.graphA);
    }

    /**
     * This method sets the tags to -1 and  Strings to ""
     * for all the nodes of the graph variable,
     * to be used in another function
     **/
    public void setTags() {
        Iterator<node_data> nodeIt = graphA.getV().iterator();
        while (nodeIt.hasNext()) {
            node_data node = nodeIt.next();
            node.setTag(-1);
        }
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node, using the bfs algorithm. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return true if the graph is strongly connected.
     */

    @Override

    public boolean isConnected() {
        /*if there aren't any nodes in the graph,
         * it counts like all of the node are connected*/
        if (this.graphA.nodeSize() == 0)
            return true;

        int numOfNodesToGo = this.graphA.nodeSize();

        node_data node = graphA.getV().iterator().next();
        ArrayDeque<node_data> help_queue = new ArrayDeque<node_data>();

        //initialise tags for the algorithm
        setTags();

        //enter the queue the first node from the graph
        help_queue.addLast(node);
        //change tag to 0- sign it's entered the queue already
        node.setTag(0);

        // create variable destKeyIterator and initialise it to contain the iterator
        // to the collection of the neighbors keys of edges out of the first node
        Iterator<Integer> destKeyIterator = ((NodeData) node).iteratorEdgesFrom();

        /*
        the loop go through all the nodes that are getting out
        of the first first node, and then all the nodes getting out ot those nodes and so on  using the Bfs algorithm.
        create a queue and for each node that comes out from the queue
        subtract one from numOfNodesToGo. if equals 0 after finishing the loop,
        it went through all the nodes and the graph is connected in the first direction of going through the graph
        */
        while (!help_queue.isEmpty()) {
            if (destKeyIterator.hasNext()) {
                node = graphA.getNode(destKeyIterator.next());
                if (node.getTag() == -1)//this node hasn't been visited
                {
                    help_queue.addLast(node);
                    //change tag to 0- sign it's entered the queue already
                    node.setTag(0);
                }

            } else {
                help_queue.remove();
                numOfNodesToGo--;
                if (!help_queue.isEmpty())
                    //update the iterator to be the iterator of the neighbors of the first node currently in the queue
                    destKeyIterator = ((NodeData) help_queue.peekFirst()).iteratorEdgesFrom();

            }
        }
        if (numOfNodesToGo != 0)
            return false;

        //do everything again, for the opposite direction edges.
        // will be done by using the iterator for the collection of the edes in the other direction.

        numOfNodesToGo = this.graphA.nodeSize();
        node = graphA.getV().iterator().next();

        //initialise tags for the algorithm
        setTags();

        //enter the queue the first node from the graph
        help_queue.addLast(node);
        //change tag to 0- sign it's entered the queue already
        node.setTag(0);

        // create variable destKeyIterator and initialise it to contain the iterator
        // to the collection of the neighbors keys of edges getting to the first node
        Iterator<Integer> srcKeyIterator = ((NodeData) node).iteratorEdgesTo();

        /*
        the loop go through all the nodes that are getting
        to the first node, and then to the nodes getting to them and so on- using the Bfs algorithm,
        create a queue and for each node that comes out from the queue
        subtract one from numOfNodesToGo. if equals 0 after finishing the loop,
        it went through all the nodes in the opposite direction and the graph is connected
        */
        while (!help_queue.isEmpty()) {
            if (srcKeyIterator.hasNext()) {
                node = graphA.getNode(srcKeyIterator.next());
                if (node.getTag() == -1)//this node hasn't been visited
                {
                    help_queue.addLast(node);
                    //change tag to 0- sign it's entered the queue already
                    node.setTag(0);
                }

            } else {
                help_queue.remove();
                numOfNodesToGo--;
                if (!help_queue.isEmpty())
                    //update the iterator to be the iterator of the neighbors getting to the first node currently in the queue
                    srcKeyIterator = ((NodeData) help_queue.peekFirst()).iteratorEdgesTo();

            }
        }
        return numOfNodesToGo == 0;


    }

    /**
     * This method sets the tags to -1 and  Strings to ""
     * for all the nodes of the graph variable,
     * to be used in another function
     **/
    private void setNodesWeight() {
        Iterator<node_data> nodeIt = graphA.getV().iterator();
        while (nodeIt.hasNext()) {
            node_data node = nodeIt.next();
            node.setWeight(-1);
        }
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {


        //if the graph is empty
        if (this.graphA.nodeSize() == 0)
            return -1;
        node_data node = graphA.getNode(src);
        //if  the src node isn't in the graph
        if (node == null)
            return -1;
        //if the src node exist in the graph and is the same the dest node
        if (src == dest) {
            latestShortestDist = 0;
            return 0;
        }

        //use nodes weight to keep the accumulated weight of the edges I went through so far
        //initialize all the nodes weights to -1 -sign for infinity
        setNodesWeight();


        //create priority queue of node_data which priorities according to
        // the weight variable which will contain the the distance from src so far
        PriorityQueue<NodeData> helpQueue = new PriorityQueue<NodeData>();

        //initialise tags to -1
        // -1 Symbolizes not visited for the algorithm
        setTags();

        helpQueue.add((NodeData) node);
        //change weight to 0: sign it's distance 0 from src
        node.setWeight(0);

        Iterator<Integer> neighborsIterator = ((NodeData) node).iteratorEdgesFrom();

        int keyCurrNode = src;
        node_data currNode = node;


        while (!helpQueue.isEmpty()) {
            if (neighborsIterator.hasNext()) {
                int nodeKey = neighborsIterator.next();
                node = this.graphA.getNode(nodeKey);
                double weightEdge = graphA.getEdge(keyCurrNode, nodeKey).getWeight();

                //if this node hasn't been visited and add to the queue- like infinity in the algorithm
                if (node.getWeight() == -1) {
                    node.setWeight(currNode.getWeight() + weightEdge);
                    helpQueue.add((NodeData) node);

                } else if (node.getTag() == -1)//the node is already in the queue, and wasn't visited
                {
                    //if the new distance is smaller, update the tag
                    if (currNode.getWeight() + weightEdge < node.getWeight())
                        node.setWeight(currNode.getWeight() + weightEdge);


                }

            } else //finished going through the neighbors of the current first node in the queue
            {
                currNode.setTag(0);//sign it been visited.
                helpQueue.remove();
                if (!helpQueue.isEmpty()) {
                    currNode = helpQueue.peek();
                    keyCurrNode = currNode.getKey();
                    //got to the dest node -finish
                    if (keyCurrNode == dest) {
                        latestShortestDist = graphA.getNode(dest).getWeight();
                        return latestShortestDist;
                    }

                    //update the iterator to neighbors of the first node in the queue
                    neighborsIterator = ((NodeData) currNode).iteratorEdgesFrom();
                }

            }
        }
        //if there is not connection between src node and dest dest.

        return -1;
    }

    /**
     * This method receives two boolean numbers and return whether the
     * two numbers are equal or not by checking
     * if the difference between them is smaller then epsilon
     *
     * @param d1
     * @param d2
     * @return
     */
    private boolean doubleEquals(double d1, double d2) {
        final double Eps = 0.0001;
        return (Math.abs(d1 - d2)) < Eps;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
         /*call to method shortestPathDist. the method return the distance and
        changes the tags in the graph to be the distance from src*/
        double dist = shortestPathDist(src, dest);

        /*if the function returned -1, it means there isn't a connection between src and dest*/
        if (Double.compare(dist, -1) == 0)
            return null;

        LinkedList<node_data> pathList = new LinkedList<node_data>();
        node_data currNode = graphA.getNode(dest);
        pathList.add(currNode);

        if (dist == 0) {
            return pathList;
        }
        //update the iterator to be the iterator of a list containing the key of the nodes getting to node dest
        Iterator<Integer> neighborsKeysIterator = ((NodeData) currNode).iteratorEdgesTo();

      /*loop that go through the graph backward, from dest to the src,
       every time finds the node that his distance from src is:
       the current node distance from src minus the weight of the edge connecting them,
       add him to the start of the list and look through this node neighbors and does the same, until reaching the src node;
       */

        while (neighborsKeysIterator.hasNext()) {

            node_data neighborNode = this.graphA.getNode(neighborsKeysIterator.next());
            if (neighborNode.getWeight() == 0)
                break;
            double edgeWeight = graphA.getEdge(neighborNode.getKey(), currNode.getKey()).getWeight();
            if (doubleEquals(neighborNode.getWeight(), dist - edgeWeight)) {
                //add this node to the path
                pathList.addFirst(neighborNode);
                //this node becomes the currNode
                currNode = neighborNode;
                dist = currNode.getWeight();
                neighborsKeysIterator = ((NodeData) currNode).iteratorEdgesTo();
            }
        }
        pathList.addFirst(graphA.getNode(src));//add the first node to the list

        return pathList;
    }


    /**
     * Deserialization of json Object to the graph of this class.
     * used Gson library
     * I relayed on: https://www.youtube.com/watch?v=HSuVtkdej8Q
     */
    public void deSerializeJson(JsonObject jsonObject) {


        S_DWGraph graph = new S_DWGraph();
        JsonArray jsonArrayOfNodes = jsonObject.get("Nodes").getAsJsonArray();

        for (JsonElement nodeElement : jsonArrayOfNodes) {
            JsonObject nodeObject = nodeElement.getAsJsonObject();
            int nodeKey = nodeObject.get("id").getAsInt();
            NodeData newNode = new NodeData(nodeKey);
            if (nodeObject.has("pos")) {
                String pos = nodeObject.get("pos").getAsString();
                String[] arrOfXYZ = pos.split(",");

                double x = Double.parseDouble(arrOfXYZ[0]);
                double y = Double.parseDouble(arrOfXYZ[1]);
                double z = Double.parseDouble(arrOfXYZ[2]);

                newNode.setLocation(new Point3D(x, y, z));
            }
            graph.addNode(newNode);

        }
        JsonArray jsonArrayOfEdges = jsonObject.get("Edges").getAsJsonArray();

        for (JsonElement edgeElement : jsonArrayOfEdges) {
            JsonObject edgeObject = edgeElement.getAsJsonObject();
            int src = edgeObject.get("src").getAsInt();
            int dest = edgeObject.get("dest").getAsInt();
            double weight = edgeObject.get("w").getAsDouble();
            graph.connect(src, dest, weight);

        }

        this.init(graph);


    }


    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(file));

            // // convert graph in format containing edges and nodes in a collection format to JSON format and write to file
            String jsonStr = gson.toJson(this.graphA.serializationFormat());
            jsonStr = jsonStr.replaceAll("\\{\"_x\":", "\"");
            jsonStr = jsonStr.replaceAll("\"_y\":", "");
            jsonStr = jsonStr.replaceAll("\"_z\":", "");
            jsonStr = jsonStr.replaceAll("},\"id\"", "\",\"id\"");

            writer.write(jsonStr);
            // close writer
            writer.close();
            return true;


        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {

        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(file));
            JsonObject graphObject = fileElement.getAsJsonObject();
            deSerializeJson(graphObject);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}