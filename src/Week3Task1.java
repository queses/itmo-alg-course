import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week3Task1 {
    public static void main(String[] args) throws Exception {
        new Week3Task1().run();
    }

    private final static int base = 512;
    private final static int baseMax = 511;
    private final static int basePow = 9;
    private final static int maxNumLength = 4;

    private int[] firstSourceArray;
    private int[] secondSourceArray;
    private int[] array;
    private long tenthItemsSum = 0;

    private void run() throws Exception {
        readInput();
        buildArray();
        digitSort();
        countTenthItemsSum();
        writeOutput();
    }

    private void buildArray() {
        array = new int[firstSourceArray.length * secondSourceArray.length];

        int offset = 0;
        for (int j = 0; j < secondSourceArray.length; j++) {
            for (int i = 0; i < firstSourceArray.length; i++) {
                int value = firstSourceArray[i] * secondSourceArray[j];

                array[i + j + offset] = value;
            }

            offset += firstSourceArray.length - 1;
        }

        firstSourceArray = null;
        secondSourceArray = null;
    }

    private void digitSort() {
        int[] bufferArray = new int[array.length];

        int[] result = array;
        for (int i = 0; i < maxNumLength; i++) {
            result = (result == array) ? countingSort(array, bufferArray, i) : countingSort(bufferArray, array, i);
        }
    }

    private int[] countingSort(int[] source, int[] target, int digit) {
        int[] countsArray = new int[base];
        int itemShift = basePow * digit;

        for (int item : source) {
            int countIndex = ((item >> itemShift) & baseMax);
            countsArray[countIndex]++;
        }

        int prevCounter = countsArray[0];
        for (int i = 1; i < countsArray.length; i++) {
            if (countsArray[i] > 0) {
                countsArray[i] += prevCounter;
                prevCounter = countsArray[i];
            }
        }

        for (int i = source.length - 1; i >= 0; i--) {
            int countIndex = ((source[i] >> itemShift) & baseMax);
            target[--countsArray[countIndex]] = source[i];
        }

        return target;
    }

    private void countTenthItemsSum() {
        for (int i = 0; i < array.length; i += 10) {
            tenthItemsSum += array[i];
        }
    }

    private void readInput() throws Exception {
        String inputString = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(inputString);
        int firstLength = Integer.parseInt(st.nextToken());
        int secondLength = Integer.parseInt(st.nextToken());

        firstSourceArray = new int[firstLength];
        for (int i = 0; i < firstLength; i++) {
            firstSourceArray[i] = Integer.parseInt(st.nextToken());
        }

        secondSourceArray = new int[secondLength];
        for (int i = 0; i < secondLength; i++) {
            secondSourceArray[i] = Integer.parseInt(st.nextToken());
        }
    }

    private void writeOutput() throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));

        writer.write(Long.toString(tenthItemsSum));
        writer.close();
    }
}