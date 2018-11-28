// public class MyDijkHeap {
//     private boolean debug;
// 	private Comparable[] minHeap = new Comparable[4];
// 	private int trueLen = 0;
// 	private int bubbDownNum = 1;
// 	private int bubbUpNum = 1;
	
// 	/**
// 	 * This method inserts an object into the heap. Inserts at end and then bubbles
// 	 * up to new position.
// 	 * 
// 	 * @param object
// 	 */
// 	public void insert(Comparable object) {
// 		trueLen++;
// 		if (trueLen > minHeap.length) {
// 			resizeArr();
// 		}
		
// 		minHeap[trueLen-1] = object;
// 		int currIndex = trueLen-1;
// 		bubbleUp(minHeap, currIndex);
		
// 	}
	
// 	/**
// 	 * This method removes the max value from the heap. First swaps max with last element.
// 	 * Then bubbles down the new root.
// 	 * 
// 	 * @return max value from heap
// 	 */
// 	public Comparable removeMax() {
// 		Comparable max = minHeap[0];
// 		swap(minHeap, 0, trueLen-1);
// 		trueLen--;
// 		bubbleDown(0);
// 		return max;
// 	}
	
// 	/**
// 	 * This method will bubble up the value at the index given until it is
// 	 * smaller than its parent.
// 	 * 
// 	 * @param arr - an array of comparable objects
// 	 * @param currIndex - index to start bubbling up
// 	 */
// 	private void bubbleUp(Comparable[] arr, int currIndex) {
// 		while(true) {
// 			int parent = Math.floorDiv(currIndex - 1, 2);

// 			if (parent < 0 || minHeap[parent].compareTo(minHeap[currIndex]) < 0) {
// 				break;
// 			}

// 			swap(minHeap, currIndex, parent);
// 			currIndex = parent;
// 		}
// 		//  FOR DEBUG
// 		//	createDot("../dots/bubbleUp" + bubbUpNum + ".dot");
// 		//	bubbUpNum++;

// 	}
	
// 	/**
// 	 * This method bubbles down the value at the given index. It will stop when 
// 	 * its children are both smaller than itself.
// 	 * 
// 	 * @param currIndex - index to start bubbling down.
// 	 */
// 	private void bubbleDown(int currIndex) {
// 		while(true) {
// 			int left = currIndex*2 + 1;
// 			int right = currIndex*2 + 2;
// 			int smallChild = -1;
			
// 			// determine smallest child
// 			if (right < trueLen && minHeap[left].compareTo(minHeap[right]) > 0) {
// 				smallChild = right;
// 			}
// 			else {
// 				smallChild = left;
// 			}
			
// 			// if parent bigger than children
// 			if (smallChild >= trueLen || minHeap[currIndex].compareTo(minHeap[smallChild]) < 0) {
// 				break;
// 			}
			
// 			swap(minHeap, currIndex, smallChild);
// 			currIndex = smallChild;
// 		}
// 		//  FOR DEBUG
// 		//	createDot("../dots/bubbleDown" + bubbDownNum + ".dot");
// 		//	bubbDownNum++;
//     }
    
// 	/**
// 	 * This method will swap the values at 2 given indexes
// 	 * @param arr - array to swap values
// 	 * @param i1 - index 1
// 	 * @param i2 - index 2
// 	 */
// 	private void swap(Comparable[] arr, int i1, int i2) {
// 		Comparable temp = arr[i1];
// 		arr[i1] = arr[i2];
// 		arr[i2] = temp;
// 	}
	
// 	/**
// 	 * This Method will double the size of the heap array.
// 	 */
// 	private void resizeArr() {
// 		Comparable[] tempArr = minHeap;
// 		minHeap = new Comparable[2*tempArr.length];
// 		for (int i = 0; i < tempArr.length; i++) {
// 			minHeap[i] = tempArr[i];
// 		}
// 	}
// }