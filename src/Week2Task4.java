import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week2Task4 {
    public static void main(String[] args) throws Exception {
        new Week2Task4().run();
    }

    private int[] array;
    private int arrayLength;
    private int ratioA;
    private int ratioB;
    private int ratioC;
    private int leftNeedleIndex;
    private int rightNeedleIndex;
    private int prevArrayItem;
    private int currentArrayItem;

    private void run() throws Exception {
        readInput();
        createArray();

        quickSort(0, arrayLength - 1);

        writeOutput();
    }

    private void readInput() throws Exception {
        String inputString = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(inputString);
        arrayLength = Integer.parseInt(st.nextToken());
        leftNeedleIndex = Integer.parseInt(st.nextToken()) - 1;
        rightNeedleIndex = Integer.parseInt(st.nextToken()) - 1;
        ratioA = Integer.parseInt(st.nextToken());
        ratioB = Integer.parseInt(st.nextToken());
        ratioC = Integer.parseInt(st.nextToken());
        prevArrayItem = Integer.parseInt(st.nextToken());
        currentArrayItem = Integer.parseInt(st.nextToken());
    }

    private void createArray() {
        array = new int[arrayLength];
        array[0] = prevArrayItem;
        array[1] = currentArrayItem;

        for (int index = 2; index < arrayLength; index++) {
            int nextItem = ratioA * prevArrayItem + ratioB * currentArrayItem + ratioC;
            prevArrayItem = currentArrayItem;
            currentArrayItem = nextItem;
            array[index] = nextItem;
        }
    }

    private void quickSort(int leftIndex, int rightIndex) {
        int middle = leftIndex + (rightIndex - leftIndex) / 2;
        int support = (middle > leftIndex)
            ? getTripleMedianSupport(new int[]{ array[leftIndex], array[middle], array[rightIndex] })
            : array[rightIndex];

        int j = splitItemsBySupport(support, leftIndex, rightIndex);

        if (leftIndex < j && j >= leftNeedleIndex) {
            quickSort(leftIndex, j);
        }

        if (rightIndex > j + 1 && j <= rightNeedleIndex) {
            quickSort(j + 1, rightIndex);
        }
    }

    private int splitItemsBySupport(int support, int leftIndex, int rightIndex) {
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
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }

        return j;
    }

    private int getTripleMedianSupport(int[] values) {
        if (values[0] > values[1]) {
            int temp = values[0];
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

        StringBuilder sb = new StringBuilder();
        for (int i = leftNeedleIndex; i <= rightNeedleIndex; i++) {
            sb.append(array[i]).append(' ');
        }

        writer.write(sb.toString());
        writer.close();
    }
}