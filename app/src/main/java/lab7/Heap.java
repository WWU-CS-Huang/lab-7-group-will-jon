
package lab7;
/*
 * Author: Jon Wick 
 * Date: 20250527
 * Purpose: Custome heap class
 */
import java.util.NoSuchElementException;

/** An instance is a min-heap of distinct values of type V with
 *  priorities of type P. Since it's a min-heap, the value
 *  with the smallest priority is at the root of the heap. */
public final class Heap<V, P extends Comparable<P>> {

    // TODO 1.0: Read and understand the class invariants given in the
    // following comment:

    /**
     * The contents of c represent a complete binary tree. We use square-bracket
     * shorthand to denote indexing into the AList (which is actually
     * accomplished using its get method. In the complete tree,
     * c[0] is the root; c[2i+1] is the left child of c[i] and c[2i+2] is the
     * right child of i.  If c[i] is not the root, then c[(i-1)/2] (using
     * integer division) is the parent of c[i].
     *
     * Class Invariants:
     *
     *   The tree is complete:
     *     1. `c[0..c.size()-1]` are non-null
     *
     *   The tree satisfies the heap property:
     *     2. if `c[i]` has a parent, then `c[i]`'s parent's priority
     *        is smaller than `c[i]`'s priority
     *
     *   In Phase 3, the following class invariant also must be maintained:
     *     3. The tree cannot contain duplicate *values*; note that dupliate
     *        *priorities* are still allowed.
     *     4. map contains one entry for each element of the heap, so
     *        map.size() == c.size()
     *     5. For each value v in the heap, its map entry contains in the
     *        the index of v in c. Thus: map.get(c[i]) = i.
     */
    protected AList<Entry> c;
    protected HashTable<V, Integer> map;

    /** Constructor: an empty heap with capacity 10. */
    public Heap() {
        c = new AList<Entry>(10);
        map = new HashTable<V, Integer>();
    }

    /** An Entry contains a value and a priority. */
    class Entry {
        public V value;
        public P priority;

        /** An Entry with value v and priority p*/
        Entry(V v, P p) {
            value = v;
            priority = p;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    /** Add v with priority p to the heap.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap. Precondition: p is not null.
     *  In Phase 3 only:
     *  @throws IllegalArgumentException if v is already in the heap.*/
    public void add(V v, P p) throws IllegalArgumentException {
      //throw exception if there is already a value in heap
      if (contains(v)) throw new IllegalArgumentException();
        //add to heap and adjust if needed
        c.append(new Entry(v,p));
        map.put( v, c.size() - 1);
        bubbleUp(c.size()-1);
        // TODO 1.1: Write this whole method. Note that bubbleUp is not implemented,
        // so calling it will have no effect. The first tests of add, using
        // test100Add, ensure that this method maintains the class invariant in
        // cases where no bubbling up is needed.
        // When done, this should pass test100Add.
        //
        // TODO 3.1: Update this method to maintain class invariants 3-5.
        // (delete the following line after completing TODO 1.1)
        // throw new UnsupportedOperationException();
    }

    /** Return the number of values in this heap.
     *  This operation takes constant time. */
    public int size() {
        return c.size();
    }

    /** Swap c[h] and c[k].
     *  precondition: h and k are >= 0 and < c.size() */
    protected void swap(int h, int k) {
        //creates temp variable and swaps them over
        Entry temp = c.get(k);
        map.put(c.get(h).value, k);
        c.put(k, c.get(h));
        c.put(h, temp);
        map.put(temp.value, h);

        //TODO 1.2: When bubbling values up and down (later on), two values,
        // c[h] and c[k], will have to be swapped. In order to always get this right,
        // write this helper method to perform the swap.
        // When done, this should pass test110Swap.
        //
        // TODO 3.2 Change this method to additionally maintain class
        // invariants 3-5 by updating the map field.
        // throw new UnsupportedOperationException();
    }

    /** Bubble c[k] up in heap to its right place.
     *  Precondition: Priority of every c[i] >= its parent's priority
     *                except perhaps for c[k] */
    protected void bubbleUp(int k) {
        if (k == 0) return;
        // TODO 1.3 As you know, this method should be called within add in order
        // to bubble a value up to its proper place, based on its priority.
        // When done, this should pass test115Add_BubbleUp
        if (c.get(k).priority.compareTo( c.get((k-1)/2).priority) < 0){
          swap(k, (k-1)/2);
        }
        bubbleUp((k-1)/2);
        // throw new UnsupportedOperationException();
    }

    /** Return the value of this heap with lowest priority. Do not
     *  change the heap. This operation takes constant time.
     *  @throws NoSuchElementException if the heap is empty. */
    public V peek() throws NoSuchElementException {

        //if its empty throw exception else return first value
        if (c.size() == 0){
          throw new NoSuchElementException();
        }

        return c.get(0).value;
        // TODO 1.4: Do peek. This is an easy one.
        //         test120Peek will not find errors if this is correct.
        // throw new UnsupportedOperationException();
    }

    /** Remove and return the element of this heap with lowest priority.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws NoSuchElementException if the heap is empty. */
    public V poll() throws NoSuchElementException {
        if (c.size() == 0){
          throw new NoSuchElementException();
        }
        //stores values to be removed and then swapped to begging
        V temp =  c.get(0).value;
        Entry last = c.pop();
        c.put(0, last);
        map.put(last.value, 0);
        map.remove(temp);
        bubbleDown(0);
        return temp;
          
        // TODO 1.5: Do poll (1.5) and bubbleDown (1.6) together. When they
        // are written correctly, testing procedures
        // test30Poll_BubbleDown_NoDups and test140testDuplicatePriorities
        // should pass. The second of these checks that entries with equal
        // priority are not swapped.
        //
        // TODO 3.3: Update poll() to maintain class invariants 3-5.
        // throw new UnsupportedOperationException();
    }

    /** Bubble c[k] down in heap until it finds the right place.
     *  If there is a choice to bubble down to both the left and
     *  right children (because their priorities are equal), choose
     *  the right child.
     *  Precondition: Each c[i]'s priority <= its childrens' priorities
     *                except perhaps for c[k] */
    protected void bubbleDown(int k) {
        //swap if needed and continue to bubble down
        if ((k * 2 + 1 >= c.size())) return;
        if (c.get(k).priority.compareTo( c.get(k * 2 + 1).priority) > 0){
          swap(k, k * 2 + 1);
          bubbleDown(k * 2 + 1);
        } 
        if ((k * 2 + 2 >= c.size())) return; 
        if (c.get(k).priority.compareTo( c.get(k * 2 + 2).priority) > 0){
          swap(k, k * 2 + 2);
          bubbleDown(k * 2 + 2);
        }
        
        // TODO 1.6: Do poll (1.5) and bubbleDown together.  We also suggest
        //         implementing and using smallerChild, though you don't
        //         have to.
        // throw new UnsupportedOperationException();
    }

    /** Return true if the value v is in the heap, false otherwise.
     *  The average case runtime is O(1).  */
    public boolean contains(V v) {
      //checks if the map contains a value
      return map.containsKey(v);
    }

    /** Change the priority of value v to p.
     *  The expected time is logarithmic and the worst-case time is linear
     *  in the size of the heap.
     *  @throws IllegalArgumentException if v is not in the heap. */
    public void changePriority(V v, P p) throws IllegalArgumentException {
        //changes priority value then calls bubbleUp and bubbleDown to ensure heap is correct
        Integer index = map.get(v);
        if ( index == null) throw new IllegalArgumentException();
        c.put(index, new Entry(v, p));
        bubbleUp(index);
        bubbleDown(index);
        // TODO 3.5: Implement this method to change the priority of node in
        // the heap.
    }

    // Recommended helper method spec:
    /* Return the index of the child of k with smaller priority.
     * if only one child exists, return that child's index
     * Precondition: at least one child exists.*/
    private int smallerChild(int k) {
        //did not need 
      throw new UnsupportedOperationException();
    }

}
