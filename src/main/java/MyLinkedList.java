public class MyLinkedList {
    private static class Node {
        int value;
        Node next, prev;
        Node(int value) { this.value = value; }
    }

    private Node head, tail;
    private int size;
    public long steps = 0, moves = 0, comparisons = 0;

    public void add(int x) {
        Node newNode = new Node(x);
        if (size == 0) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            moves += 2;
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (index == size) { add(x); return; }

        Node newNode = new Node(x);
        if (index == 0) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            moves += 2;
        } else {
            Node current = getNode(index);
            newNode.prev = current.prev;
            newNode.next = current;
            current.prev.next = newNode;
            current.prev = newNode;
            moves += 4;
        }
        size++;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        Node current = getNode(index);

        if (current.prev != null) {
            current.prev.next = current.next;
            moves++;
        } else { head = current.next; }

        if (current.next != null) {
            current.next.prev = current.prev;
            moves++;
        } else { tail = current.prev; }

        size--;
    }

    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        return getNode(index).value;
    }

    public boolean contains(int x) {
        Node current = head;
        while (current != null) {
            steps++; comparisons++;
            if (current.value == x) return true;
            current = current.next;
        }
        return false;
    }

    private Node getNode(int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
            steps++;
        }
        return current;
    }

    public void resetMetrics() { steps = 0; moves = 0; comparisons = 0; }
    public int getSize() { return size; }
}