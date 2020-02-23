import java.io.BufferedWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class TaskTwo {
    public static void main(String[] args) throws Exception {
        new TaskTwo().run();
    }

    private void run() throws Exception {
        BigInteger[] input = readInput();
        BigInteger result = input[0].add(input[1].pow(2));
        writeOutput(result.toString(10));
    }

    private BigInteger[] readInput() throws Exception {
        String input = new String(Files.readAllBytes(Paths.get("./input.txt")));
        StringTokenizer st = new StringTokenizer(input);
        return new BigInteger[]{ new BigInteger(st.nextToken()), new BigInteger(st.nextToken()) };
    }

    private void writeOutput(String result) throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"), Charset.forName("UTF-8"));
        writer.write(result);
        writer.close();
    }
}