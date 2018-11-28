import java.util.ArrayList;
import java.util.HashMap;

/**
 * A node class to be used in the dijkstra algorithm. This class will also carry 
 * meta data containing the edges it is incident to along with the coresponding 
 * edge weights.
 * 
 * @author Travis Banken
 */
public class MyGraphNode implements Comparable {
    String value;
    int distance;
    HashMap<String, Integer> neighbors;
    int heapPos;
    boolean visited = false;
    ArrayList<String> bestPath = new ArrayList<>();

    /**
     * Creates a new node in the graph
     * @param value - the name of the node
     */
    public MyGraphNode(String value) {
        this.value = value;
        distance = Integer.MAX_VALUE;
        neighbors = new HashMap<>();
    }

    /**
     * Adds a neighbor to the node
     * @param neighbor - name of node to add
     * @param weight - weight of edge between them
     */
    public void addNeighbor(String neighbor, int weight) {
        neighbors.put(neighbor, weight);
    }

    /**
     * Returns an ArrayList of all of the neighbors for this node
     * @return - ArrayList of all neighbors
     */
    public ArrayList<String> allNeighbors() {
        ArrayList<String> allNeigh = new ArrayList<>();
        for (String neigh : neighbors.keySet()) {
            allNeigh.add(neigh);
        }
        return allNeigh;
    }

    /**
     * Allows for the comparison of two node objects. It will compare the current 
     * distances from the source node. This is meant for use in a min heap and 
     * dijkstras specifically
     * @param other - another node object
     * @return positive if larger, neg if smaller, 0 if equal
     */
    public int compareTo(MyGraphNode other) {
        if (this.distance > other.distance) 
            return 1;
        if (this.distance == other.distance)
            return 0;
        else
            return -1;
    }

    public int compareTo(Object other) {
        return 0;
    }
}