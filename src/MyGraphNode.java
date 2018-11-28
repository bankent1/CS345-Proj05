import java.util.ArrayList;
import java.util.HashMap;

public class MyGraphNode implements Comparable {
    String value;
    int distance;
    HashMap<String, Integer> neighbors;
    int heapPos;
    boolean visited = false;
    ArrayList<String> bestPath = new ArrayList<>();

    public MyGraphNode(String value) {
        this.value = value;
        distance = Integer.MAX_VALUE;
        neighbors = new HashMap<>();
    }

    public void addNeighbor(String neighbor, int weight) {
        neighbors.put(neighbor, weight);
    }

    public ArrayList<String> allNeighbors() {
        ArrayList<String> allNeigh = new ArrayList<>();
        for (String neigh : neighbors.keySet()) {
            allNeigh.add(neigh);
        }
        return allNeigh;
    }

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