public class MinHeap {
    private int[] data;
    private int size;
    public long steps = 0, moves = 0, comparisons = 0;

    public MinHeap() {
        this.data = new int[10];
        this.size = 0;
    }

    public void insert(int x) {
        if (size == data.length) resize();
        data[size] = x;
        moves++;
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        steps++;
        return data[0];
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException("Heap is empty");
        int min = data[0];
        steps++;
        data[0] = data[size - 1];
        moves++;
        size--;
        bubbleDown(0);
        return min;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            comparisons++; steps += 2;
            if (data[index] >= data[parent]) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (2 * index + 1 < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = left;

            steps++; comparisons++;
            if (right < size && data[right] < data[left]) {
                smallest = right;
                steps++;
            }

            comparisons++; steps += 2;
            if (data[index] <= data[smallest]) break;

            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        moves += 2; steps += 2;
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