import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class DataStructuresTest {

    @Test
    public void testDynamicArrayAndLinkedListEdgeCases() {
        DynamicArray arr = new DynamicArray();
        MyLinkedList list = new MyLinkedList();

        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(2, 10));

        arr.add(5);
        list.add(5);
        assertEquals(5, arr.get(0));
        assertEquals(5, list.get(0));

        arr.add(5);
        list.add(5);
        assertTrue(arr.contains(5));
        assertTrue(list.contains(5));
        assertFalse(arr.contains(99));
    }

    @Test
    public void testStructuresAgainstJavaUtil() {
        DynamicArray myArr = new DynamicArray();
        MyLinkedList myList = new MyLinkedList();
        ArrayList<Integer> javaList = new ArrayList<>();
        Random rand = new Random(42);

        for (int i = 0; i < 100; i++) {
            int val = rand.nextInt(1000);
            myArr.add(val);
            myList.add(val);
            javaList.add(val);
        }

        for (int i = 0; i < 100; i++) {
            assertEquals(javaList.get(i), myArr.get(i));
            assertEquals(javaList.get(i), myList.get(i));
        }
    }

    @Test
    public void testMinHeapPropertyAndSortedOutput() {
        MinHeap heap = new MinHeap();
        PriorityQueue<Integer> javaHeap = new PriorityQueue<>();
        Random rand = new Random(42);


        assertThrows(IllegalStateException.class, heap::peekMin);
        assertThrows(IllegalStateException.class, heap::extractMin);

        int n = 1000;
        for (int i = 0; i < n; i++) {
            int val = rand.nextInt(10000);
            heap.insert(val);
            javaHeap.add(val);
        }


        int previous = -1;
        for (int i = 0; i < n; i++) {
            int current = heap.extractMin();
            int expected = javaHeap.poll();

            assertEquals(expected, current);
            assertTrue(current >= previous);
            previous = current;
        }
    }
}