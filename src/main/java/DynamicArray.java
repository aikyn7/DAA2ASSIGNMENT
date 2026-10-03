public class DynamicArray {
    private int[] data;
    private int size;
    public long steps = 0;
    public long moves = 0;
    public long comparisons = 0;

    public DynamicArray() {
        this.data = new int[10];
        this.size = 0;
    }

    public void add(int x) {
        if (size == data.length) resize();
        data[size++] = x;
        moves++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
        if (size == data.length) resize();
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            steps++; moves++;
        }
        data[index] = x;
        moves++; size++;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            steps++; moves++;
        }
        size--;
    }

    public int get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index);
        steps++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            steps++; comparisons++;
            if (data[i] == x) return true;
        }
        return false;
    }

    private void resize() {
        int[] newData = new int[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
            steps++; moves++;
        }
        data = newData;
    }

    public void resetMetrics() { steps = 0; moves = 0; comparisons = 0; }
    public int getSize() { return size; }
}