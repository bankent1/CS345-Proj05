import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implimentation of Dijkstra's min distance algorithm. The program is called on 
 * by the test driver code to run the algorithm on a given .dot file. It allows 
 * for the printing of the results to the console as well as a .dot file to show 
 * a visual representation of the solution found. Works on both directed and 
 * indirected graphs.
 * 
 * @author Travis Banken  
 */
public class Proj05_Dijkstra_student implements Proj05_Dijkstra {
    private boolean isDigraph;
    private HashMap<String, MyGraphNode> nodeMap = new HashMap<>();

    /**
     * Constructor for the algorithm.
     * 
     * @param isDigraph - determines if the input is a digraph or graph
     */
    public Proj05_Dijkstra_student (boolean isDigraph) {
        this.isDigraph = isDigraph;
    }

    /**
     * Adds a node to the graph
     * 
     * @param s - name of node to add to the graph
     */
    public void addNode(String s) {
        MyGraphNode node = new MyGraphNode(s);
        nodeMap.put(s, node);
    }

    /**
     * adds an edge to the graph
     * 
     * @param from - the node the edge connects from
     * @param to - the node the edge connects to
     * @param weight - the weight of the edge
     */
    public void addEdge(String from, String to, int weight) {
        nodeMap.get(from).addNeighbor(to, weight);
        if (!isDigraph) {
            nodeMap.get(to).addNeighbor(from, weight);
        }
    }

    /**
     * Runs dijkstra's algorithm with a given start node. 
     * Note: Does not check for invalid node start.
     * 
     * @param startNodeName - the name of the node to start from
     */
    public void runDijkstra(String startNodeName) {
        MyDijkHeap minHeap = new MyDijkHeap();
        //HashMap<String, ArrayList<String>> pathMap = new HashMap<>();

        minHeap.insert(startNodeName);
        nodeMap.get(startNodeName).distance = 0;
        nodeMap.get(startNodeName).bestPath.add(startNodeName);

        while (!minHeap.isEmpty()) {
            String currNode = (String) minHeap.removeMax();
            
            if (nodeMap.get(currNode).visited) {
                continue;
            }
            
            // look at all neighbors and if the new path to neighbor is a better 
            // distance, we update the distance and the best path meta data
            for (String neighbor : nodeMap.get(currNode).neighbors.keySet()) {
                int dist = nodeMap.get(currNode).distance + nodeMap.get(currNode).neighbors.get(neighbor);
                if (dist < nodeMap.get(neighbor).distance) {
                    nodeMap.get(neighbor).bestPath = new ArrayList<>();
                    for (String n : nodeMap.get(currNode).bestPath) {
                        nodeMap.get(neighbor).bestPath.add(n);
                    }
                    nodeMap.get(neighbor).bestPath.add(neighbor);
                    nodeMap.get(neighbor).distance = dist;
                    minHeap.bubbleUp(nodeMap.get(neighbor).heapPos);
                }
                minHeap.insert(neighbor);
            }
            nodeMap.get(currNode).visited = true;
        }
    }

    /**
     * Prints out the results to the console
     * 
     * @param startNodeName - the name of the node the algorithm started with
     */
    public void printDijkstraResults(String startNodeName) {
        for (String node : nodeMap.keySet()) {
            System.out.print(startNodeName + " -> " + node);
            if (nodeMap.get(node).distance == Integer.MAX_VALUE) {
                System.out.print(": NO PATH");
            } else {
                System.out.print(": best " +  nodeMap.get(node).distance + ": ");
            }
            String str = "";
            for (String n : nodeMap.get(node).bestPath) {
                str += n + " ";
                //System.out.print(n + " ");
            }
            System.out.println(str.trim());
        }
        //dubugDot();
    }

    /**
     * Writes a .dot file which represents a visual solution found from running 
     * the algorithm
     */
    public void writeSolutionDotFile() {
        PrintWriter outfile = null;
        try {
            outfile = new PrintWriter(new File("solution.dot"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (isDigraph) {
            outfile.println ("digraph {");
        } else {
            outfile.println("graph {");
        }

        // for every node in the graph, we look at all of the neighbors.
        // if the neighbor lies on the best path, then we color the edge red
        // otherwise we make the edge dotted
        for (String node : nodeMap.keySet()) {
            outfile.println(node + "[label=\"" + node + (nodeMap.get(node).distance == 
                            Integer.MAX_VALUE ? "" : "\\nBest: " + nodeMap.get(node).distance) +  "\"];");

            for (String neighbor : nodeMap.get(node).allNeighbors()) {
                int prevIndex = nodeMap.get(neighbor).bestPath.indexOf(neighbor) - 1;
                String labelStr = "[label=\"" + nodeMap.get(node).neighbors.get(neighbor) + "\"";

                // here we check to see if the node and the neighbor are actually 
                // adjacent on the best path. We only want to color red if this 
                // is true.
                ArrayList<String> path = nodeMap.get(neighbor).bestPath;
                if (prevIndex != -1 && path.contains(node) && path.get(prevIndex).equals(node)) {
                    labelStr += "color=red";
                }
                else {
                    labelStr += "style=dotted";
                }
                labelStr += "];";

                outfile.println(node + (isDigraph ? " -> " : " -- ") + neighbor + labelStr);
                   
            }
        }

        outfile.println("}");

        outfile.close();
       
    }

    private class MyDijkHeap {
        private boolean debug;
        private Comparable[] minHeap = new Comparable[4];
        private int trueLen = 0;
        private int bubbDownNum = 1;
        private int bubbUpNum = 1;
        
        /**
         * This method inserts an object into the heap. Inserts at end and then bubbles
         * up to new position.
         * 
         * @param object
         */
        public void insert(Comparable object) {
            trueLen++;
            if (trueLen > minHeap.length) {
                resizeArr();
            }
            
            minHeap[trueLen-1] = object;
            int currIndex = trueLen-1;
            bubbleUp(currIndex);
            
        }
        
        /**
         * This method removes the max value from the heap. First swaps max with last element.
         * Then bubbles down the new root.
         * 
         * @return max value from heap
         */
        public Comparable removeMax() {
            Comparable max = minHeap[0];
            swap(minHeap, 0, trueLen-1);
            trueLen--;
            bubbleDown(0);
            return max;
        }

        /**
         * Checks if the heap is currently empty
         * @return true if heap is empty, false otherwise
         */
        public boolean isEmpty() {
            return trueLen == 0;
        }
        
        /**
         * This method will bubble up the value at the index given until it is
         * smaller than its parent.
         * 
         * @param arr - an array of comparable objects
         * @param currIndex - index to start bubbling up
         */
        private void bubbleUp(int currIndex) {
            while(true) {
                int parent = Math.floorDiv(currIndex - 1, 2);
    
                if (parent < 0 || nodeMap.get(minHeap[parent]).compareTo(nodeMap.get(minHeap[currIndex])) < 0) {
                    break;
                }

                // if (parent < 0 || minHeap[parent].compareTo(minHeap[currIndex]) < 0) {
                //     break;
                // }
    
                swap(minHeap, currIndex, parent);
                currIndex = parent;
            }
            //  FOR DEBUG
            //	createDot("../dots/bubbleUp" + bubbUpNum + ".dot");
            //	bubbUpNum++;
    
        }
        
        /**
         * This method bubbles down the value at the given index. It will stop when 
         * its children are both smaller than itself.
         * 
         * @param currIndex - index to start bubbling down.
         */
        private void bubbleDown(int currIndex) {
            while(true) {
                int left = currIndex*2 + 1;
                int right = currIndex*2 + 2;
                int smallChild = -1;
                
                // determine smallest child
                if (right < trueLen && nodeMap.get(minHeap[left]).compareTo(nodeMap.get(minHeap[right])) > 0) {
                    smallChild = right;
                }
                else {
                    smallChild = left;
                }
                
                // if parent bigger than children
                if (smallChild >= trueLen || nodeMap.get(minHeap[currIndex]).compareTo(nodeMap.get(minHeap[smallChild])) < 0) {
                    break;
                }
                
                swap(minHeap, currIndex, smallChild);
                currIndex = smallChild;
            }
            //  FOR DEBUG
            //	createDot("../dots/bubbleDown" + bubbDownNum + ".dot");
            //	bubbDownNum++;
        }
        
        /**
         * This method will swap the values at 2 given indexes
         * @param arr - array to swap values
         * @param i1 - index 1
         * @param i2 - index 2
         */
        private void swap(Comparable[] arr, int i1, int i2) {
            int tempPos = nodeMap.get(arr[i1]).heapPos;
            nodeMap.get(arr[i1]).heapPos = nodeMap.get(arr[i2]).heapPos;
            nodeMap.get(arr[i2]).heapPos = tempPos;

            Comparable temp = arr[i1];
            arr[i1] = arr[i2];
            arr[i2] = temp;
        }
        
        /**
         * This Method will double the size of the heap array.
         */
        private void resizeArr() {
            Comparable[] tempArr = minHeap;
            minHeap = new Comparable[2*tempArr.length];
            for (int i = 0; i < tempArr.length; i++) {
                minHeap[i] = tempArr[i];
            }
        }
    }
}