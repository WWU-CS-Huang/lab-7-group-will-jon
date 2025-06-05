package lab7;
import java.lang.Math;

/** A hash table modeled after java.util.Map. It uses chaining for collision
 * resolution and grows its underlying storage by a factor of 2 when the load
 * factor exceeds 0.8. */
public class HashTable<K,V> {

    protected Pair[] buckets; // array of list nodes that store K,V pairs
    protected int size; // how many items currently in the map


    /** class Pair stores a key-value pair and a next pointer for chaining
     * multiple values together in the same bucket, linked-list style*/
    public class Pair {
        protected K key;
        protected V value;
        protected Pair next;

        /** constructor: sets key and value */
        public Pair(K k, V v) {
            key = k;
            value = v;
            next = null;
        }

        /** constructor: sets key, value, and next */
        public Pair(K k, V v, Pair nxt) {
            key = k;
            value = v;
            next = nxt;
        }

        /** returns (k, v) String representation of the pair */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }
    }

    /** constructor: initialize with default capacity 17 */
    public HashTable() {
        this(17);
    }

    /** constructor: initialize the given capacity */
    public HashTable(int capacity) {
        buckets = createBucketArray(capacity);
    }

    /** Return the size of the map (the number of key-value mappings in the
     * table) */
    public int getSize() {
        return size;
    }

    /** Return the current capacity of the table (the size of the buckets
     * array) */
    public int getCapacity() {
        return buckets.length;
    }

    /** Return the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * Runtime: average case O(1); worst case O(size) */
    public V get(K key) {
      Pair pair = getPair(key);
      return pair == null ? null : pair.value;
    }

    public Pair getPair(K key){

      int hash = Math.abs(key.hashCode()) % buckets.length;
      Pair cur = buckets[hash];

      while (cur!= null){
      if (cur.key.equals(key)) return cur;
      cur = cur.next; 
    }
    return null;
  }

    /** Associate the specified value with the specified key in this map. If
     * the map previously contained a mapping for the key, the old value is
     * replaced. Return the previous value associated with key, or null if
     * there was no mapping for key. If the load factor exceeds 0.8 after this
     * insertion, grow the array by a factor of two and rehash.
     * Precondition: val is not null.
     * Runtime: average case O(1); worst case O(size + a.length)*/
    public V put(K key, V val) {
      Pair prevPair = getPair(key);
      if (prevPair == null){

        int hash = Math.abs(key.hashCode()) % buckets.length;
        buckets[hash] = new Pair(key,val, buckets[hash]);
        size++;
        growIfNeeded();
        return null;
      }
      V temp = prevPair.value;
      prevPair.value = val;
      return temp;
    }

    /** Return true if this map contains a mapping for the specified key.
     *  Runtime: average case O(1); worst case O(size) */
    public boolean containsKey(K key) {
        if (get(key) == null) return false;
        return true;

    }

    /** Remove the mapping for the specified key from this map if present.
     *  Return the previous value associated with key, or null if there was no
     *  mapping for key.
     *  Runtime: average case O(1); worst case O(size)*/
    public V remove(K key) {
          int hash = Math.abs(key.hashCode()) % buckets.length;
          Pair cur = buckets[hash];
          if (cur.key == key) {
            V temp = cur.value;
            buckets[hash] = cur.next;
            size--;
            return temp;
          }

          while (cur.next != null){
            if (cur.next.key == key) {

              V temp = cur.next.value;
              cur.next = cur.next.next;
              size--;
              return temp;
            }
          cur = cur.next; 
        }
        return null;
    }


    // suggested helper method:
    /* check the load factor; if it exceeds 0.8, double the capacity 
     * and rehash values from the old array to the new array */
    private void growIfNeeded() {
      
      double load = (double)size / buckets.length;
      if (load <= 0.8) return;
      Pair[] temp = createBucketArray(buckets.length);
      for (int i = 0; i < buckets.length; i++){
        temp[i] = buckets[i];
      }
      buckets = createBucketArray(buckets.length * 2);
      Pair cur;
      size = 0;
      for (Pair i : temp){
        cur = i;
        while (cur != null){
          put(cur.key, cur.value);
          cur = cur.next;

        }
      }
    

        
      // for (Pair i : buckets)

      
      
      // throw new UnsupportedOperationException();
    }

    /* useful method for debugging - prints a representation of the current
     * state of the hash table by traversing each bucket and printing the
     * key-value pairs in linked-list representation */
    protected void dump() {
        System.out.println("Table size: " + getSize() + " capacity: " +
                getCapacity());
        for (int i = 0; i < buckets.length; i++) {
            System.out.print(i + ": --");
            Pair node = buckets[i];
            while (node != null) {
                System.out.print(">" + node + "--");
                node = node.next;

            }
            System.out.println("|");
        }
    }

    /*  Create and return a bucket array with the specified size, initializing
     *  each element of the bucket array to be an empty LinkedList of Pairs.
     *  The casting and warning suppression is necessary because generics and
     *  arrays don't play well together.*/
    @SuppressWarnings("unchecked")
    protected Pair[] createBucketArray(int size) {
        return (Pair[]) new HashTable<?,?>.Pair[size];
    }
}
