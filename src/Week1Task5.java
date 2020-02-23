import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week1Task5 {
    public static void main(String[] args) throws Exception {
        new Week1Task5().run();
    }

    private BufferedWriter writer;

    private void run() throws Exception {
        this.initOutputWriter();

        long[] input = readInput();
        for (int j = 1; j < input.length; j++) {
            int i = j;
            while (i > 0) {
                if (input[i] < input[i - 1]) {
                    swapNumbersInArray(input, i, i - 1);
                    logSwap(i, i - 1);
                }
                i--;
            }
        }

        logEnd();
        writeOutput(input);
    }

    private void swapNumbersInArray (long[] array, int from, int to) {
        long temp = array[from];
        array[from] = array[to];
        array[to] = temp;
    }

    private long[] readInput() throws Exception {
        String input = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(input);
        int length = Integer.parseInt(st.nextToken());
        long[] result = new long[length];

        for (int i = 0; i < length; i++) {
            result[i] = Integer.parseInt(st.nextToken());
        }

        return result;
    }

    private void initOutputWriter() throws Exception {
        this.writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));
    }

    private void writeOutput(long[] items) throws Exception {
        StringBuilder sbItems = new StringBuilder();
        for (long item : items) {
            sbItems.append(item).append(" ");
        }

        writer.write(sbItems.toString());
        writer.close();
    }

    private void logSwap (int from, int to) throws Exception {
        writer.write("Swap elements at indices " + (to + 1) + " and " + (from + 1) + ".");
        writer.newLine();
    }

    private void logEnd () throws Exception {
        writer.write("No more swaps needed.");
        writer.newLine();
    }
}