import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Week8Task4 {
    public static void main(String[] args) throws Exception {
        new Week8Task4().run();
    }
    
    private void run() throws Exception {
        int linesAmount = readInput();
        writeOutput(Arrays.copyOfRange(generateStrings(), 0, linesAmount));
    }

    private String[] generateStrings() {
        final String[] thueMorseChunks = { thueMorse(0, 128), thueMorse(128, 256) };
        final int allLinesAmount = 1 << 15;
        final int[] parts = { 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1 };
        final String[] strings = new String[allLinesAmount / 2];

        for (int i = 0; i < allLinesAmount; i++) {
            int iMod2 = i % 2;
            if (iMod2 != 0) {
                continue;
            }

            StringBuilder builder = new StringBuilder();
            for (int part : parts) {
                builder.append(thueMorseChunks[(i / part) % 2]);
            }

            strings[(i / 2) + iMod2] = builder.toString();
        }

        return strings;
    }

    private int readInput() throws Exception {
        BufferedReader reader = Files.newBufferedReader(Paths.get("./input.txt"));
        int stringsToGenerate = Integer.parseInt(reader.readLine());

        reader.close();
        return stringsToGenerate;
    }

    private void writeOutput(String[] strings) throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"));
        for (String str : strings) {
            writer.write(str);
            writer.newLine();
        }

        writer.close();
    }

    private void test() {
        int multiplier = 2;
        String[] strings = generateStrings();
        assert hashOf(strings[0], multiplier) == hashOf(strings[1], multiplier);
        assert !strings[0].equals(strings[1]);

        multiplier = 257;

        assert hashOf(strings[0], multiplier) == hashOf(strings[1], multiplier);
        assert hashOf(strings[0], multiplier) == hashOf(strings[9999], multiplier);
        assert hashOf(strings[511], multiplier) == hashOf(strings[997], multiplier);
        assert hashOf(strings[510], 258) == hashOf(strings[432], 258);
        assert hashOf(strings[0], multiplier + 2) == hashOf(strings[999], multiplier + 2);
        assert hashOf(strings[511], 2) == hashOf(strings[997], 2);

        assert secureHashOf(strings[0], multiplier) != secureHashOf(strings[1], multiplier);
        assert secureHashOf(strings[0], multiplier) != secureHashOf(strings[9999], multiplier);
        assert secureHashOf(strings[511], multiplier) != secureHashOf(strings[997], multiplier);
        assert secureHashOf(strings[510], 258) != secureHashOf(strings[432], 258);
        assert secureHashOf(strings[0], multiplier + 2) != secureHashOf(strings[999], multiplier + 2);
        assert secureHashOf(strings[511], 2) != secureHashOf(strings[997], 2);
    }

    private static int hashOf(String source, int multiplier) {
        int hash = 0;
        for (int i = 0; i < source.length(); ++i) {
            hash = multiplier * hash + source.charAt(i);
        }

        return hash;
    }

    private static int secureHashOf(String source, int multiplier) {
        final int mod = (1 << 30) - 1;

        int hash = 0;
        for (int i = 0; i < source.length(); ++i) {
            hash = (multiplier * hash + source.charAt(i)) % mod;
        }

        return hash % mod;
    }

    private static String thueMorse(int from, int to) {
        StringBuilder builder = new StringBuilder();
        while (from < to) {
            builder.append((char) ('a' + Integer.bitCount(from++) % 2));
        }

        return builder.toString();
    }
}
