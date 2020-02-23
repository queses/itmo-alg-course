import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week1Task4 {
    public static void main(String[] args) throws Exception {
        new Week1Task4().run();
    }

    private void run() throws Exception {
        double[] input = readInput();

        int[] ids = new int[input.length];
        ids[0] = 0;
        for (int j = 1; j < input.length; j++) {
            ids[j] = j;

            int i = j;
            while (i > 0) {
                if (input[i] < input[i - 1]) {
                    swapNumbersInArray(input, i, i - 1);
                    swapNumbersInArray(ids, i, i - 1);
                }
                i--;
            }
        }

        writeOutput(new int[]{ ids[0], ids[(input.length - 1) / 2], ids[input.length - 1] });
    }

    private void swapNumbersInArray(double[] array, int from, int to) {
        double temp = array[from];
        array[from] = array[to];
        array[to] = temp;
    }

    private void swapNumbersInArray(int[] array, int from, int to) {
        int temp = array[from];
        array[from] = array[to];
        array[to] = temp;
    }

    private double[] readInput() throws Exception {
        String input = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(input);
        int length = Integer.parseInt(st.nextToken());
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = Double.parseDouble(st.nextToken());
        }

        return result;
    }

    private void writeOutput(int[] items) throws Exception {
        StringBuilder sbItems = new StringBuilder();
        for (int item : items) {
            sbItems.append(item + 1).append(" ");
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));
        writer.write(sbItems.toString());
        writer.close();
    }
}