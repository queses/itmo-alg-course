import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class Week1Task1 {
    public static void main(String[] args) throws Exception {
        new Week1Task1().run();
    }

    private void run() throws Exception {
        int[] input = readInput();
        int result = input[0] + input[1];
        writeOutput(Integer.toString(result));
    }

    private int[] readInput() throws Exception {
        String input = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(input);

        return new int[]{ Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()) };
    }

    private void writeOutput(String result) throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));
        writer.write(result);
        writer.close();
    }
}