import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week2Task5 {
    public static void main(String[] args) throws Exception {
        new Week2Task5().run();
    }

    private long[] array;
    private int stepLength;
    private boolean canBeSorted = true;

    private void run() throws Exception {
        readInput();

        if (stepLength > 1) {
            setCanBeSorted();
        }

        writeOutput();
    }


    private void readInput() throws Exception {
        String inputString = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(inputString);
        int length = Integer.parseInt(st.nextToken());
        stepLength = Integer.parseInt(st.nextToken());

        array = new long[length];
        for (int i = 0; i < length; i++) {
            array[i] = Long.parseLong(st.nextToken());
        }
    }

    private void setCanBeSorted() {
        long[] prevPart = sortSteppedPart(0);
        for (int offset = 1; offset < stepLength; offset++) {
            long[] currentPart = sortSteppedPart(offset);
            for (int i = 0; i < currentPart.length; i++) {
                if (prevPart[i] > currentPart[i] || (i < prevPart.length - 1 && prevPart[i + 1] < currentPart[i])) {
                    canBeSorted = false;
                    break;
                }
            }

            prevPart = currentPart;
        }
    }

    private long[] sortSteppedPart(int offset) {
        int partLength = (array.length % stepLength > 0)
            ? (int) Math.ceil((double) (array.length - offset) / stepLength)
            : array.length / stepLength;

        long[] part = new long[partLength];
        for (int i = 0; i < partLength; i++) {
            part[i] = array[i * stepLength + offset];
        }

        quickSort(part, 0, partLength - 1);

        return part;
    }

    private void quickSort(long[] array, int leftIndex, int rightIndex) {
        int middle = leftIndex + (rightIndex - leftIndex) / 2;
        long support = (middle > leftIndex)
                ? getTripleMedianSupport(new long[]{ array[leftIndex], array[middle], array[rightIndex] })
                : array[rightIndex];

        int j = splitItemsBySupport(array, support, leftIndex, rightIndex);

        if (leftIndex < j) {
            quickSort(array, leftIndex, j);
        }

        if (rightIndex > j + 1) {
            quickSort(array,j + 1, rightIndex);
        }
    }

    private int splitItemsBySupport(long[] array, long support, int leftIndex, int rightIndex) {
        int i = leftIndex;
        int j = rightIndex;
        while (i <= j) {
            while (array[i] < support) {
                i++;
            }

            while (array[j] > support) {
                j--;
            }

            if (i <= j) {
                long temp = array[i];
                array[i] = array[j];
                array[j] = temp;

                i++;
                j--;
            }
        }

        return j;
    }

    private long getTripleMedianSupport(long[] values) {
        if (values[0] > values[1]) {
            long temp = values[0];
            values[0] = values[1];
            values[1] = temp;
        }

        if (values[2] >= values[1]) {
            return values[1];
        } else if (values[2] < values[0]) {
            return values[0];
        } else {
            return values[2];
        }
    }

    private void writeOutput() throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));

        writer.write(canBeSorted ? "YES" : "NO");
        writer.close();
    }
}