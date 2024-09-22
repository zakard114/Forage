import java.util.Comparator;

class PowerOfTwoMaxHeap<E> {
    // Field declarations
    private Object[] array;
    private int size; // Heap size
    private int childrenExponent; // Exponent of children nodes
    private Comparator<? super E> comparator;
    private static final int INITIAL_CAPACITY = 16; // Initial capacity of array

    // Constructor Type 1: no initial capacity allocation
    public PowerOfTwoMaxHeap(int childrenExponent) {
        this(childrenExponent, null);
    }

    public PowerOfTwoMaxHeap(int childrenExponent, Comparator<? super E> comparator){
        // Throw exception if childrenExponent is negative
        if(childrenExponent < 0){
            throw new IllegalArgumentException("Exponent must be non-negative");
        }
        // Initialize fields
        this.array = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.childrenExponent = childrenExponent;
        this.comparator = comparator;
    }

    // Constructor Type2: With initial capacity allocation
    public PowerOfTwoMaxHeap(int childrenExponent, int capacity) {
        this(childrenExponent, capacity, null);
    }

    public PowerOfTwoMaxHeap(int childrenExponent, int capacity, Comparator<? super E> comparator){
        // Throw exception if childrenExponent is negative
        if(childrenExponent < 0) {
            throw new IllegalArgumentException("Exponent must be non-negative");
        }
        // Initialize fields
        this.array = new Object[capacity];
        this.size = 0;
        this.childrenExponent = childrenExponent;
        this.comparator = comparator;
    }

    // Method to insert an element
    public void insert(E value) {
        // Resize array into double if it is full
        if(size + 1 == array.length){
            resize(array.length * 2);
        }
        size++; // Increment heap-size
        siftUp(size, value); // Rearrange the heap structure in an upward direction
    }

    // Method to remove and return the maximum node
    public E popMax(){
        // Throw exception if heap is empty
        if(size == 0){
            throw new IllegalArgumentException("Heap is empty");
        }
        E max = (E) array[1]; // Store the maximum value
        E last = (E) array[size]; // Store the last node
        array[size] = null; // Remove the last node
        size--; // Decrement size
        if(size > 0){
            array[1] = last; // Place the last node at the root
            siftDown(1); // Rearrange the heap structure in a downward direction
        }
        return max; // Return the maximum value
    }

    private void resize(int newCapacity){
        Object[] newArray = new Object[newCapacity]; // Create a new array
        System.arraycopy(array, 0, newArray, 0, size + 1); // Copy the existing array

        this.array = newArray; // Update the array
    }

    private void siftUp(int idx, E target) {
        // If a comparator exists, pass comparator as an argument.
        if (comparator != null){
            siftUpComparator(idx, target, comparator); // Call with comparator if exists
        } else {
            siftUpComparable(idx, target); // Call without comparator
        }
    }

    @SuppressWarnings("unchecked")
    // Method for sift-up operation with comparator
    private void siftUpComparator(int idx, E target, Comparator<? super E> comp){
        // Search only until it is larger than the root node.
        while (idx > 1){
            int parent = (idx - 1) / (int) Math.pow(2, childrenExponent); // Calculate parent index
            E parentVal = (E) array[parent + 1]; // Get parent value
            if(parentVal != null && comp.compare(target, parentVal) <= 0){
                break; // Exit if condition satisfies
            }
            array[idx] = parentVal; // Move parent value to current position
            idx = parent + 1; // Update index
        }
        array[idx] = target; // Insert at final position
    }


    @SuppressWarnings("unchecked")
    // Sift-up without comparator but using Comparable of object to be inserted
    private void siftUpComparable(int idx, E target){
        Comparable<? super E> comp = (Comparable<? super E>) target; // Typecast to comparable type
        while (idx > 1){
            int parent = (idx - 1) / (int) Math.pow(2, childrenExponent); // Calculate parent index
            E parentVal = (E) array[parent + 1]; // Get parent value
            if(parentVal != null && comp.compareTo(parentVal) <= 0){
                break; // Exit if condition satisfies
            }
            array[idx] = parentVal; // Move parent value to current position
            idx = parent + 1; // Update index
        }
        array[idx] = target; // Insert at final position
    }

    // Method to swap values at two indices
    private void swap(int i, int j){
        E temp = (E) array[i]; // Temporary storage
        array[i] = array[j]; // Swap values
        array[j] = temp; // Update with stored value
    }

    // Method for sift-down operation
    private void siftDown(int idx){
        while(true){
            int maxChildIdx = getMaxChildIdx(idx); // Get index of maximum child
            if(maxChildIdx == -1){
                break;
            }
            E current = (E) array[idx];
            E maxChild = (E) array[maxChildIdx];

            boolean shouldSwap = (comparator != null) ?
                    comparator.compare(current, maxChild) < 0 :
                    ((Comparable<? super E>) current).compareTo(maxChild) < 0;

            if(!shouldSwap) {
                break;
            }

            swap(idx, maxChildIdx);
            idx = maxChildIdx;
        }
    }

    // Method to get index of maximum child
    private int getMaxChildIdx(int idx) {

        int start = idx * (int) Math.pow(2, childrenExponent) - (int) Math.pow(2, childrenExponent - 1);
        int end = Math.min(size + 1, start + (int) Math.pow(2, childrenExponent));
        if(start >= size + 1) {
            return -1;
        }

        int maxChildIdx = start;
        for(int i = start + 1; i < end; i++){
            E currentChild = (E) array[i];
            E maxChild = (E) array[maxChildIdx];

            boolean isGreater = (comparator != null) ?
                    comparator.compare(currentChild, maxChild) > 0 :
                    ((Comparable<? super E>) currentChild).compareTo(maxChild) > 0;

            if (isGreater) {
                maxChildIdx = i;
            }
        }

        return maxChildIdx;
    }
}

public class Main {
    public static void main(String[] args) {
        PowerOfTwoMaxHeap<Integer> heap = new PowerOfTwoMaxHeap<>(2, Comparator.naturalOrder());
        heap.insert(2);
        heap.insert(11);
        heap.insert(7);
        heap.insert(92);
        heap.insert(4);
        System.out.println(heap.popMax());
        System.out.println(heap.popMax());
    }
}

/*

The parent is at index (i-1)/2 (using integer division).
The left child is at index 2*i + 1.
The right child is at index 2*i + 2.
https://www.cs.dartmouth.edu/~cs10/notes14.html
https://en.wikipedia.org/wiki/Heap_(data_structure)

*/
