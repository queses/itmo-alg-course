import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week2Task2 {
    public static void main(String[] args) throws Exception {
        new Week2Task2().run();
    }

    private BufferedWriter writer;
    private long[] input;
    private long inversionCounter = 0;

    private void run() throws Exception {
        this.initOutputWriter();

        readInput();
        input = mergeSort();

        writeOutput();
    }

    private long[] mergeSort () {
        long[] buffer = new long[input.length];

        long[] source = buffer;
        long[] target = input;

        int partLength;
        for (partLength = 2; partLength < input.length; partLength *= 2) {
            if (source == input) {
                source = buffer;
                target = input;
            } else {
                source = input;
                target = buffer;
            }

            int index;
            for (index = 0; index <= input.length - partLength; index += partLength) {
                merge(source, target, index, index + partLength / 2, partLength);
            }

            if (index != input.length) {
                int length = input.length - index;
                if (length > (partLength / 2)) {
                    merge(source, target, index, index + partLength / 2, length);
                } else {
                    System.arraycopy(source, index, target, index, length);
                }
            }
        }


        return merge(target, source, 0, partLength / 2, input.length);
    }

    private long[] merge (long[] source, long[] target, int start, int middle, int length) {
        if (length == 1) {
            target[start] = source[start];
            return target;
        }

        int end = start + length - 1;
        int indexLeft = start;
        int indexRight = middle;
        int targetIndex = start;
        while (targetIndex <= end) {
            if (indexRight > end) {
                target[targetIndex++] = source[indexLeft++];
            } else if (indexLeft >= middle) {
                target[targetIndex++] = source[indexRight++];
            } else if (source[indexLeft] <= source[indexRight]) {
                target[targetIndex++] = source[indexLeft++];
            } else {
                inversionCounter += indexRight - targetIndex;
                target[targetIndex++] = source[indexRight++];
            }
        }

        return target;
    }

    private void readInput() throws Exception {
        String inputString = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(inputString);
        int length = Integer.parseInt(st.nextToken());
        input = new long[length];

        for (int i = 0; i < length; i++) {
            input[i] = Integer.parseInt(st.nextToken());
        }
    }

    private void initOutputWriter() throws Exception {
        this.writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));
    }

    private void writeOutput() throws Exception {
        writer.write(Long.toString(inversionCounter));
        writer.close();
    }
}