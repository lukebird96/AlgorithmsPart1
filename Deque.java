import java.util.Iterator;
import java.util.NoSuchElementException;

// do we use linked list or do we use array?
// for linked list we will have private variables like current and next
// we also need to implement that class for a linked list?
//      assignment: your iterator implementation must support each
//      operation (including construction) in constant worst-case time
//      ---> Linked List (arrays are amortised constant time - averaged)

// remember to remove dead references so the garbage collector can reclaim memory

// the item reference is a generic class - defined outside the function
public class Deque<Item> implements Iterable<Item> {
    // for a double ended queue we need to maintain the first and last places
    private Node first = null;
    private Node last = null;
    private int size = 0;

    // our inned definition for
    private class Node {
        // don't need public declaration
        public Item item;
        public Node forwards;
        public Node backwards;
    }

    public Deque() {
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        // what if we have an empty list? - nothing to worry about, if first.next == null,
        //      our function isEmpty returns true
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null elements to deque");
        }

        // save our old first node
        Node oldFirst = first;

        // create a new first node
        first = new Node();
        first.item = item;

        // our first element always points to our next element
        first.forwards = null;
        first.backwards = oldFirst;

        // update oldFirst
        if (oldFirst != null) {
            oldFirst.forwards = first;
        }

        // update size
        size++;

        // in the case where we haven't assigned last yet, point to first
        if (last == null) {
            last = first;
        }
        // when adding a second entry - update our pointer from the other side
        // else if (last.forwards == null) { last.next = first; }


        // []
        // ["world"]            --> first: item=world, next=null, last: item=world, next=null
        // ["world", " ,"]      --> first: item=" ,", next=world, last: item=world, next=","
    }


    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null elements to deque");
        }

        // do the reverse of addFirst
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.forwards = oldLast;
        last.backwards = null;

        if (oldLast != null) {
            oldLast.backwards = last;
        }

        size++;

        // in the case where we haven't assigned first yet, point to last
        if (first == null) {
            first = last;
        }
    }

    public Item removeFirst() {
        // handle null lists
        if (isEmpty()) {
            throw new NoSuchElementException("Nothing to remove; the deque is empty");
        }

        // now we need to do the opposite to addFirst
        Item item = first.item;

        // now we need to replace first using our pointer
        first = first.backwards;

        // if the first is null, then the list is empty - make last null too
        if (first == null) {
            last = null;
        }
        else {
            first.forwards = null;
        }

        // update size
        --size;

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        // do the reverse of removeFirst
        if (isEmpty()) {
            throw new NoSuchElementException("Nothing to remove; the deque is empty");
        }

        Item item = last.item;

        // use directional pointer to step through the list
        last = last.forwards;

        // if the last is null, then the list is empty - make first null too
        if (last == null) {
            first = null;
        }
        else {
            last.backwards = null;
        }

        // update size
        --size;

        return item;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    // entirely new private class for list iteration
    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported in Deque");
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("Iterating from an empty deque");
            }
            Item item = current.item;
            current = current.backwards;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> d = new Deque<String>();
        System.out.println(d.size());

        // test throwing exceptions
        // 1. remove on empty deque
        try {
            d.removeFirst();
        }
        catch (NoSuchElementException e) {
            System.out.print("Testing remove on empty deque: ");
            System.out.println(e);
        }

        // 2. iterate on empty deque
        try {
            for (String s : d) {
                System.out.println("Shouldn't ever get here " + s);
            }
        }
        catch (NoSuchElementException e) {
            System.out.print("Testing for each deque: ");
            System.out.println(e);
        }

        // test other functionality
        d.addFirst("World");
        d.addFirst(", ");
        d.addFirst("Hello");

        for (String s : d) {
            System.out.print(s);
        }
        System.out.println();

        System.out.print("Removing last element: ");
        System.out.println(d.removeLast());
        System.out.print("Expected size is 2: ");
        System.out.println(d.size());

        // SOLVED (directional pointers)
        // BUG: when we add a new string to the end, it doesn't update the list iteration from the front
        // this bug comes from the linked list implementation
        //      the linking from the front and back are separate
        //      this means that removeLast and removeFirst don't actually remove the element from the opposite direction
        //          I think the only way to solve this elegantly is with an Array based implementation?

        // the issue with linking from both ends on a linked list is that there is no 'ground truth'
        //      if you want to remove something from one end you need to remove from both references which would be O(N)

        // alternatively you can maintain two lists, when you add something to the front you create one link
        //      when you add to the back you create the other, but then again, you can't remove something from both lists

        // what about:
        //      rather than considering only next (a static pointer, which assumes direction),
        //      we implement something which has front and back?
        //      this would give us control over our direction and allow us to maintain one list?
        d.addLast("Universe");
        for (String s : d) {
            System.out.print(s);
        }
    }
}
