import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week2Task3 {
    public static void main(String[] args) throws Exception {
        new Week2Task3().run();
    }

    private BufferedWriter writer;
    private int arrayLength;
    private long[] array;

    private void run() throws Exception {
        this.initOutputWriter();

        readInput();
        buildWorstCaseArray();

        writeOutput();
    }

    private void buildWorstCaseArray () {
        array = new long[arrayLength];
        array[0] = 1;

        if (arrayLength >= 2) {
            array[1] = 2;
        }

        int i;
        for (i = 2; i < arrayLength; i++) {
            int midIndex = i / 2;
            array[i] = array[midIndex];
            array[midIndex] = i + 1;
        }
    }

    private void readInput() throws Exception {
        String inputString = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(inputString);
        arrayLength = Integer.parseInt(st.nextToken());
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
}