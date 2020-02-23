import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week1Task3 {
    public static void main(String[] args) throws Exception {
        new Week1Task3().run();
    }

    private void run() throws Exception {
        long[] input = readInput();
        long[] indexes = new long[input.length];

        indexes[0] = 0;
        for (int j = 1; j < input.length; j++) {
            int i = j;
            indexes[j] = i;
            while (i > 0) {
                if (input[i] < input[i - 1]) {
                    swapNumbersInArray(input, i, i - 1);
                    indexes[j] = i - 1;
                }
                i--;
            }
        }

        writeOutput(input, indexes);
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

    private void writeOutput(long[] items, long[] indexes) throws Exception {
        StringBuilder sbItems = new StringBuilder();
        StringBuilder sbIndexes = new StringBuilder();

        for (int i = 0; i < items.length; i++) {
            sbItems.append(items[i]).append(" ");
            sbIndexes.append(indexes[i] + 1).append(" ");
        }


        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));
        writer.write(sbIndexes.toString());
        writer.newLine();
        writer.write(sbItems.toString());
        writer.close();
    }
}