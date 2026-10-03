import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int WARMUP = 1;
    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        new File("results").mkdirs();
        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                System.out.println("Processing n = " + n + "...");
                runW1(writer, n);
                runW2(writer, n);
                runW3(writer, n, "head");
                runW3(writer, n, "middle");
                runW4(writer, n);
            }
        }
        System.out.println("Benchmark finished! Check results/results.csv");
    }

    private static void runW1(PrintWriter w, int n) {
        measure(w, "W1", "-", "DynamicArray", n, () -> {
            DynamicArray arr = new DynamicArray();
            Random rand = new Random(42);
            for (int i = 0; i < n; i++) arr.add(rand.nextInt());
            arr.resetMetrics();
            for (int i = 0; i < 10000; i++) arr.get(rand.nextInt(n));
            return new long[]{arr.steps, arr.moves, arr.comparisons};
        });

        measure(w, "W1", "-", "MyLinkedList", n, () -> {
            MyLinkedList list = new MyLinkedList();
            Random rand = new Random(42);
            for (int i = 0; i < n; i++) list.add(rand.nextInt());
            list.resetMetrics();
            for (int i = 0; i < 10000; i++) list.get(rand.nextInt(n));
            return new long[]{list.steps, list.moves, list.comparisons};
        });
    }

    private static void runW2(PrintWriter w, int n) {
        measure(w, "W2", "-", "DynamicArray", n, () -> {
            DynamicArray arr = new DynamicArray();
            Random rand = new Random(42);
            for (int i = 0; i < n; i++) arr.add(rand.nextInt());
            arr.resetMetrics();
            for (int i = 0; i < 1000; i++) {
                if (i % 2 == 0) arr.contains(arr.get(rand.nextInt(n)));
                else arr.contains(rand.nextInt());
            }
            return new long[]{arr.steps, arr.moves, arr.comparisons};
        });

        measure(w, "W2", "-", "MyLinkedList", n, () -> {
            MyLinkedList list = new MyLinkedList();
            Random rand = new Random(42);
            for (int i = 0; i < n; i++) list.add(rand.nextInt());
            list.resetMetrics();
            for (int i = 0; i < 1000; i++) {
                if (i % 2 == 0) list.contains(list.get(rand.nextInt(n)));
                else list.contains(rand.nextInt());
            }
            return new long[]{list.steps, list.moves, list.comparisons};
        });
    }

    private static void runW3(PrintWriter w, int n, String variant) {
        int index = variant.equals("head") ? 0 : n / 2;

        measure(w, "W3", variant, "DynamicArray", n, () -> {
            DynamicArray arr = new DynamicArray();
            Random rand = new Random(42);
            for (int i = 0; i < n; i++) arr.add(rand.nextInt());
            arr.resetMetrics();
            for (int i = 0; i < 1000; i++) arr.add(index, rand.nextInt());
            for (int i = 0; i < 1000; i++) arr.remove(index);
            return new long[]{arr.steps, arr.moves, arr.comparisons};
        });

        measure(w, "W3", variant, "MyLinkedList", n, () -> {
            MyLinkedList list = new MyLinkedList();
            Random rand = new Random(42);
            for (int i = 0; i < n; i++) list.add(rand.nextInt());
            list.resetMetrics();
            for (int i = 0; i < 1000; i++) list.add(index, rand.nextInt());
            for (int i = 0; i < 1000; i++) list.remove(index);
            return new long[]{list.steps, list.moves, list.comparisons};
        });
    }

    private static void runW4(PrintWriter w, int n) {
        measure(w, "W4", "-", "MinHeap", n, () -> {
            MinHeap heap = new MinHeap();
            Random rand = new Random(42);
            heap.resetMetrics();
            for (int i = 0; i < n; i++) heap.insert(rand.nextInt());
            for (int i = 0; i < n; i++) heap.extractMin();
            return new long[]{heap.steps, heap.moves, heap.comparisons};
        });
    }

    private static void measure(PrintWriter w, String workload, String variant, String structure, int n, Task task) {
        long[] times = new long[RUNS];
        long[] metrics = new long[3];

        for (int i = 0; i < WARMUP + RUNS; i++) {
            long start = System.currentTimeMillis();
            long[] currentMetrics = task.execute();
            long end = System.currentTimeMillis();

            if (i >= WARMUP) {
                times[i - WARMUP] = (end - start);
                metrics = currentMetrics;
            }
        }

        Arrays.sort(times);
        long medianTime = times[RUNS / 2];

        w.printf("%s,%s,%s,%d,%d,%d,%d,%d%n",
                workload, variant, structure, n, medianTime, metrics[0], metrics[1], metrics[2]);
    }

    interface Task {
        long[] execute();
    }
}