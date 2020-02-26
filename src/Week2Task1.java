import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week2Task1 {
    public static void main(String[] args) throws Exception {
        new Week2Task1().run();
    }

    private BufferedWriter writer;
    private long[] array;

    private void run() throws Exception {
        this.initOutputWriter();

        readInput();
        array = mergeSort(0, array.length - 1);

        writeOutput();
    }

    private long[] mergeSort (int boundaryStart, int boundaryEnd) throws Exception {
        if (boundaryEnd == boundaryStart) {
            return new long[]{ array[boundaryStart] };
        }

        int subLength = boundaryEnd - boundaryStart + 1;
        int middle = boundaryStart + ((subLength % 2 == 0) ? subLength / 2 : (subLength - 1) / 2);
        long[] left = mergeSort(boundaryStart, middle - 1);
        long[] right = mergeSort(middle, boundaryEnd);
        long[] target = new long[subLength];

        int indexLeft = 0;
        int indexRight = 0;
        int elements = 0;
        while (elements < subLength) {
            if (indexRight >= right.length) {
                target[elements++] = left[indexLeft++];
            } else if (indexLeft >= left.length) {
                target[elements++] = right[indexRight++];
            } else if (left[indexLeft] < right[indexRight]) {
                target[elements++] = left[indexLeft++];
            } else {
                target[elements++] = right[indexRight++];
            }
        }

        logPartSorted(boundaryStart, boundaryEnd, target[0], target[target.length - 1]);

        return target;
    }

    private void readInput() throws Exception {
        String inputString = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(inputString);
        int length = Integer.parseInt(st.nextToken());
        array = new long[length];

        for (int i = 0; i < length; i++) {
            array[i] = Integer.parseInt(st.nextToken());
        }
    }

    private void initOutputWriter() throws Exception {
        this.writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));
    }

    private void writeOutput() throws Exception {
        StringBuilder sbItems = new StringBuilder();
        for (long item : array) {
            sbItems.append(item).append(" ");
        }

        writer.write(sbItems.toString());
        writer.close();
    }

    private void logPartSorted(int boundaryStart, int boundaryEnd, long start, long end) throws Exception {
        writer.write("" + (boundaryStart + 1) + " " + (boundaryEnd + 1) + " " + start + " " + end);
        writer.newLine();
    }
}