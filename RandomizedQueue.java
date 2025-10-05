import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


// since we need to be able to make random accesses we should use an array implementation
//      since linked lists don't support random access
public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size = 0;
    private int arraySize = 0;
    private Item[] randomQueue;

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    private void resize(int length) {
        // we need to think carefully about this implementation
        // the issue is that we might have a sparse array because of the random nature
        // we could solve that by re-indexing at the point of dequeue-ing
        //      we need to keep a condensed array, so when we randomly dequeue, we should
        //      take our element from index 'size' and reassign the value to the lower index
        //      then take the 'size' index and update the value to null

        // To resize we need to create a new copy of the old array with the specified length
        Item[] copy = (Item[]) new Object[length]; // Java generics don't support array creation

        // iterate through old array and add elements to our new copy
        for (int i = 0; i < size; i++) {
            copy[i] = randomQueue[i];
        }
        randomQueue = copy;

        // update our size now
        arraySize = randomQueue.length;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        // adding something to an array based list works differently since we need to consider array size
        // first we need to check if there is space in the array
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null elements to deque");
        }

        if (size == 0) {
            resize(2);
        }
        else if (size == randomQueue.length) {
            resize(randomQueue.length * 2);
        }

        // now we have space, add item at size and then increment
        randomQueue[size] = item;
        size++;
    }

    // internal function for getting an index for a given size of array
    private int getRandomIndex(int sz) {
        int idx;
        if (sz > 1) {
            idx = StdRandom.uniformInt(0, sz);
        }
        else if (sz == 1) {
            idx = 0;
        }
        else {
            throw new NoSuchElementException("Cannot dequeue empty queue");
        }

        return idx;
    }

    // remove and return a random item
    public Item dequeue() {
        // get item from queue
        int randIndex = getRandomIndex(size);
        Item item = randomQueue[randIndex];

        // remove reference from the array
        // we need to condense our array into a sparse array
        //      otherwise we can't easily read and update the array size
        //      therefore: re-map our removed index to the index at 'size'
        randomQueue[randIndex] = randomQueue[size - 1];
        randomQueue[size - 1] = null;

        // reduce the size of array content
        size--;

        // check how full our array is
        double arrayFullness = (double) size / arraySize;
        if (arrayFullness <= 0.25) {
            resize(randomQueue.length / 2);
        }

        // return item
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        int randIndex = getRandomIndex(size);
        return randomQueue[randIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        // iterators should be mutually exclusive
        //      we need to iterate through the queue randomly multiple times
        //      we create queue copies and iterate through, removing seen items

        // declare variables for use throughout iterators
        Item[] queueCopy = null;
        int unseenSize = size;

        public boolean hasNext() {
            // if we are done, set queue copy to null
            if (unseenSize == 0) {
                queueCopy = null;
                return false;
            }
            else {
                return true;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "Remove operation is not supported in RandomizedQueue"
            );
        }

        public Item next() {
            if (unseenSize == 0) {
                throw new NoSuchElementException("Iterating from empty queue");
            }

            // if we have no copy, create a fresh one
            if (queueCopy == null) {
                queueCopy = getQueueCopy();
            }

            int randIndex = getRandomIndex(unseenSize);

            // extract item and replace with last element
            Item item = queueCopy[randIndex];
            queueCopy[randIndex] = queueCopy[unseenSize - 1];
            queueCopy[unseenSize - 1] = null;

            // reduce size
            unseenSize--;

            return item;
        }

        private Item[] getQueueCopy() {
            // prepare object, remembering Java can't create arrays of generics
            Item[] copy = (Item[]) new Object[randomQueue.length];

            // iterate through the existing randomQueue and copy across
            for (int i = 0; i < unseenSize; i++) {
                copy[i] = randomQueue[i];
            }

            return copy;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("Hello");
        rq.enqueue("my");
        rq.enqueue("name");
        rq.enqueue("is");
        rq.enqueue("Luke");

        // check size
        System.out.println(rq.size());

        // sample
        System.out.println(rq.sample());
        System.out.println(rq.sample());
        System.out.println(rq.sample());
        System.out.println(rq.sample());
        System.out.println(rq.sample());

        // iterator 1
        System.out.println("ITERATOR 1: ");
        for (String s : rq) {
            System.out.print(s + " ");
        }

        // iterator 2
        System.out.println("\nITERATOR 2: ");
        for (String s : rq) {
            System.out.print(s + " ");
        }
    }
}
