import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Week8Task3 {
    private static final long POW_10_15 = 1_000_000_000_000_000L;

    private final LongHashSet hashTable = new LongHashSet();
    private long numX;
    private int numA;
    private int numAC;
    private int numAD;
    private long numB;
    private long numBD;
    private long numBC;
    private long operationsAmount;

    public static void main(String[] args) throws Exception {
        new Week8Task3().run();
    }

    private void run() throws Exception {
        readInput();

        for (int i = 0; i < operationsAmount; i++) {
            runIteration();
        }

        writeOutput();
    }

    private void runIteration() {
        if (hashTable.has(numX)) {
            numA = (numA + numAC) % 1000;
            numB = (numB + numBC) % POW_10_15;
        } else {
            hashTable.add(numX);
            numA = (numA + numAD) % 1000;
            numB = (numB + numBD) % POW_10_15;
        }

        numX = (numX * numA + numB) % POW_10_15;
    }

    private void readInput() throws Exception {
        BufferedReader reader = Files.newBufferedReader(Paths.get("./input.txt"));
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        operationsAmount = (int) tokenizer.nval;
        tokenizer.nextToken();
        numX = (long) tokenizer.nval;
        tokenizer.nextToken();
        numA = (int) tokenizer.nval;
        tokenizer.nextToken();
        numB = (long) tokenizer.nval;
        tokenizer.nextToken();
        numAC = (int) tokenizer.nval;
        tokenizer.nextToken();
        numBC = (long) tokenizer.nval;
        tokenizer.nextToken();
        numAD = (int) tokenizer.nval;
        tokenizer.nextToken();
        numBD = (long) tokenizer.nval;

        reader.close();
    }


    private void writeOutput() throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"));
        writer.write("" + numX + ' ' + numA + ' ' + numB);
        writer.close();
    }

    private static class LongHashSet {
        static final int TABLE_SIZE = 30000049;
        static final int EXTRA_HASH = TABLE_SIZE - 1;

        private final long[] hashPool = new long[TABLE_SIZE];

        void add(long key) {
            long keyNonZero = key + 1;

            int hashIndex = (int) (keyNonZero % TABLE_SIZE);
            int extraHashMod = (int) (keyNonZero % EXTRA_HASH) + 1;
            while (hashPool[hashIndex] != 0) {
                if (hashPool[hashIndex] == keyNonZero) {
                    return;
                }

                hashIndex = (hashIndex + extraHashMod) % TABLE_SIZE;
            }

            hashPool[hashIndex] = keyNonZero;
        }

        boolean has(long key) {
            long keyNonZero = key + 1;

            int hashIndex = (int) (keyNonZero % TABLE_SIZE);
            int extraHashMod = (int) (keyNonZero % EXTRA_HASH) + 1;
            while (hashPool[hashIndex] != 0) {
                if (hashPool[hashIndex] == keyNonZero) {
                    return true;
                }

                hashIndex = (hashIndex + extraHashMod) % TABLE_SIZE;
            }

            return false;
        }
    }
}
