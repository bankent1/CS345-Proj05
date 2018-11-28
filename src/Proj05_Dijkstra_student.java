import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Travis Banken  
 */
public class Proj05_Dijkstra_student implements Proj05_Dijkstra {
    private boolean isDigraph;
    private HashMap<String, MyGraphNode> nodeMap = new HashMap<>();

    public Proj05_Dijkstra_student (boolean isDigraph) {
        this.isDigraph = isDigraph;
    }

    public void addNode(String s) {
        MyGraphNode node = new MyGraphNode(s);
        nodeMap.put(s, node);
    }

    public void addEdge(String from, String to, int weight) {
        nodeMap.get(from).addNeighbor(to, weight);
        if (!isDigraph) {
            nodeMap.get(to).addNeighbor(from, weight);
        }
    }

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
            
            for (String neighbor : nodeMap.get(currNode).neighbors.keySet()) {
                int dist = nodeMap.get(currNode).distance + nodeMap.get(currNode).neighbors.get(neighbor);
                if (dist < nodeMap.get(neighbor).distance) {
                    //nodeMap.get(neighbor).bestPath = nodeMap.get(currNode).bestPath;
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

        HashMap<String, Boolean> seenEdge = initSeenEdge(); 
        for (String node : nodeMap.keySet()) {
            outfile.println(node + "[label=\"" + node + (nodeMap.get(node).distance == 
                            Integer.MAX_VALUE ? "" : "\\nBest: " + nodeMap.get(node).distance) +  "\"];");

            // for (String neighbor : nodeMap.get(node).allNeighbors()) {
            //     int prevIndex = nodeMap.get(neighbor).bestPath.indexOf(neighbor) - 1;
            //     String labelStr = "[label=\"" + nodeMap.get(node).neighbors.get(neighbor) + "\"";

            //     ArrayList<String> path = nodeMap.get(neighbor).bestPath;
            //     if (prevIndex != -1 && path.contains(node) && path.get(prevIndex).equals(node)) {
            //         labelStr += "color=red";
            //     }
            //     else if (/*TODO: Check if seen edge before */) {
            //         labelStr += "style=dotted";
            //     }
            //     labelStr += "];";

            //     outfile.println(node + (isDigraph ? " -> " : " -- ") + neighbor + labelStr);
                   
            // }
            
            // first iterate over edges which lie on the best path
            for (String pathNode : nodeMap.get(node).bestPath) {
                seenEdge.put(node + pathNode, true);

                int prevIndex = nodeMap.get(pathNode).bestPath.indexOf(pathNode) - 1;
                ArrayList<String> path = nodeMap.get(pathNode).bestPath;
                String labelStr = "";
                Integer dist = nodeMap.get(node).neighbors.get(pathNode);
                if (prevIndex != -1 /*&& path.get(prevIndex).equals(node)*/ && dist != null) {
                    labelStr = "[label=\"" + dist.intValue() + 
                                  "\"" + "color=red];";
                }
                if (!node.equals(pathNode)) {
                    String edgeStr = node + (isDigraph ? " -> " : " -- ") + pathNode + labelStr;
                    outfile.println(edgeStr);
                }
            }

            // now iterate over all edges and skip ones we already covered
            for (String neighbor : nodeMap.get(node).allNeighbors()) {
                if (!isDigraph && (seenEdge.get(node + neighbor) || seenEdge.get(neighbor + node))) {
                    continue;
                }
                String labelStr = "[label=\"" + nodeMap.get(node).neighbors.get(neighbor) + 
                                  "\"" + "style=dotted];";
                String edgeStr = node + (isDigraph ? " -> " : " -- ") + neighbor + labelStr;
                outfile.println(edgeStr);
            }
        }

        outfile.println("}");

        outfile.close();
       
    }

    private HashMap<String, Boolean> initSeenEdge() {
        HashMap<String, Boolean> seenEdge = new HashMap<>();
        for (String n1 : nodeMap.keySet()) {
            for (String n2 : nodeMap.keySet()) {
                seenEdge.put(n1 + n2, false);
            }
        }

        return seenEdge;
    }

    private void dubugDot() {
        PrintWriter outfile = null;
        try {
            outfile = new PrintWriter(new File("debug.dot"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (isDigraph) {
            outfile.println ("digraph {");
        } else {
            outfile.println("graph {");
        }

        for (String snode : nodeMap.keySet()) {
            outfile.println(snode + ";");
            for (String neighbor : nodeMap.get(snode).allNeighbors()) {
                outfile.println(snode + " -> " + neighbor + "[label=" + 
                    nodeMap.get(snode).neighbors.get(neighbor) + "];");
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