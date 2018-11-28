import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * This class will construct a max heap allowing for calls to insert a value 
 * or retrieve the max value.
 * 
 * @author Travis Banken
 *
 * TEST SEEDS: 52560
 */

public class Proj02_MaxHeap {
	private boolean debug;
	private Comparable[] maxHeap;
	private int trueLen = 0;
	private int bubbDownNum = 1;
	private int bubbUpNum = 1;
	
	public Proj02_MaxHeap(boolean debug) {
		//clearDebugFiles(new File("dots/")); // FOR DEBUG
		this.debug = debug;
		maxHeap = new Comparable[4];
	}
	
	public Proj02_MaxHeap(boolean debug, Comparable[] arr) {
		//System.out.println(System.getProperty("user.dir")); // FOR DEBUG
		//clearDebugFiles(new File("dots/")); // FOR DEBUG
		this.debug = debug;
		trueLen = arr.length;
		maxHeap = arr;
		buildHeap();
		
		if (debug) {
			createDot("DebugDot.dot");
		}
	}
	
	/**
	 * This method inserts an object into the heap. Inserts at end and then bubbles
	 * up to new position.
	 * 
	 * @param object
	 */
	public void insert(Comparable object) {
		trueLen++;
		if (trueLen > maxHeap.length) {
			resizeArr();
		}
		
		maxHeap[trueLen-1] = object;
		int currIndex = trueLen-1;
		bubbleUp(maxHeap, currIndex);
		
	}
	
	/**
	 * This method removes the max value from the heap. First swaps max with last element.
	 * Then bubbles down the new root.
	 * 
	 * @return max value from heap
	 */
	public Comparable removeMax() {
		Comparable max = maxHeap[0];
		swap(maxHeap, 0, trueLen-1);
		trueLen--;
		bubbleDown(0);
		return max;
	}
	
	/**
	 * This method prints out the current state of the heap.
	 * 
	 * @param outfile - PrintWriter object to write to
	 */
	public void dump(PrintWriter outfile) {
		String output = "";
		for (int i = 0; i < trueLen; i++) {
			output = output + maxHeap[i] + " ";
		}
		output = output.trim();
		outfile.println(output);
	}
	
	/**
	 * This method will bubble up the value at the index given until it is
	 * smaller than its parent.
	 * 
	 * @param arr - an array of comparable objects
	 * @param currIndex - index to start bubbling up
	 */
	private void bubbleUp(Comparable[] arr, int currIndex) {
		while(true) {
			int parent = Math.floorDiv(currIndex - 1, 2);

			if (parent < 0 || maxHeap[parent].compareTo(maxHeap[currIndex]) >= 0) {
				break;
			}

			swap(maxHeap, currIndex, parent);
			currIndex = parent;
		}
		//  FOR DEBUG
		//	createDot("../dots/bubbleUp" + bubbUpNum + ".dot");
		//	bubbUpNum++;

	}
	
	/**
	 * This method builds a max heap out of an existing array.
	 */
	private void buildHeap() {
		int firstParent = Math.floorDiv(trueLen - 2, 2);
		for (int i = firstParent; i >= 0; i--) {
			int currIndex = i;
			bubbleDown(currIndex);
			
		}
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
			int bigChild = -1;
			
			// determine biggest child
			if (right < trueLen && maxHeap[left].compareTo(maxHeap[right]) < 0) {
				bigChild = right;
			}
			else {
				bigChild = left;
			}
			
			// if parent bigger than children
			if (bigChild >= trueLen || maxHeap[currIndex].compareTo(maxHeap[bigChild]) >= 0) {
				break;
			}
			
			swap(maxHeap, currIndex, bigChild);
			currIndex = bigChild;
		}
		//  FOR DEBUG
		//	createDot("../dots/bubbleDown" + bubbDownNum + ".dot");
		//	bubbDownNum++;
	}
	
	/**
	 * This method will create a dot file representing the current heap, 
	 * using the passed in name.
	 * 
	 * @param filename - name of dot file to be created
	 */
	private void createDot(String filename) {
		// create file
		PrintWriter outfile = null;
		try {
			outfile = new PrintWriter(new File(filename));
		} catch (FileNotFoundException e){
			System.err.println(e);
			e.printStackTrace();
		}
		
		// create string to print in file
		String nodes = "";
		String relations = "";
		for (int i = 0; i < trueLen; i++) {
			nodes = nodes + i + " [label=\"" + maxHeap[i] + "\"];\n";
			if (2*i + 1 < trueLen) {
				relations = relations + i + " -> " + (2*i + 1) + "\n";
			}
			if (2*i + 2 < trueLen) {
				relations = relations + i + " -> " + (2*i + 2) + "\n";
			}
		}
		String dot = "digraph\n{\n" + nodes + relations + "}";
		outfile.println(dot);
		outfile.close();
	}
	
	/**
	 * This method will swap the values at 2 given indexes
	 * @param arr - array to swap values
	 * @param i1 - index 1
	 * @param i2 - index 2
	 */
	private void swap(Comparable[] arr, int i1, int i2) {
		Comparable temp = arr[i1];
		arr[i1] = arr[i2];
		arr[i2] = temp;
	}
	
	/**
	 * This Method will double the size of the heap array.
	 */
	private void resizeArr() {
		Comparable[] tempArr = maxHeap;
		maxHeap = new Comparable[2*tempArr.length];
		for (int i = 0; i < tempArr.length; i++) {
			maxHeap[i] = tempArr[i];
		}
	}
	
	/**
	 * This method will delete all of the files the ../dot/ directory except for Makefile.
	 * 
	 * NOTE: For debug only
	 * 
	 * @param folder - directory name
	 */
	private void clearDebugFiles(File folder) {
		File[] files = folder.listFiles();
		
		if (files == null) {
			return;
		}
		
		// delete every file except Makefile
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getName().equals("Makefile")) {
				files[i].delete();
			}
		}
	}
	
}

