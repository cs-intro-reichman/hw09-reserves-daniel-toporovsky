/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {
    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;

    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the first element in the list */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
       Node tmp = first;
       first = new Node(new CharData(chr), tmp);
       size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        ListIterator iterator = listIterator(0);
        StringBuilder str = new StringBuilder("(");
        while (iterator.hasNext()) {
            str.append(iterator.next());
            str.append(" ");
        }
        str.deleteCharAt(str.length()-1);
        return str.append(")") + "";
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        if (size == 0) return -1;
        CharData[] arr = toArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].chr == chr) return i;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        int c = indexOf(chr);
        if (c == -1) { addFirst(chr); }
        else {
            CharData[] arr = toArray();
            arr[c].count++;
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        int c = indexOf(chr);
        if (c == -1) return false;
        else if (c == 0) {
            first = first.next;
            size--;
        } else {
            int i = 0;
            Node prev = null;
            Node current = first;
            while (i != c) {
                prev = current;
                current = current.next;
                i++;
            }
            prev.next = current.next;
            size--;
        }
        return true;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if ((index < 0) || (index >= size)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        else {
            CharData[] arr = toArray();
            return arr[index];
        }
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
        int i = 0;
	    ListIterator itr = listIterator(i);
        while (itr.hasNext()) {
            arr[i] = itr.next();
            i++;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }

    public static void space() {
        System.out.println();
    }
}
