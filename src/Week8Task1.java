import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Week8Task1 {
    public static void main(String[] args) throws Exception {
        new Week8Task1().run();
    }

    private void run() throws Exception {
        IntHashMap hashMap = new IntHashMap();

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"));
        BufferedReader reader = Files.newBufferedReader(Paths.get("./input.txt"));
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int operationsAmount = (int) tokenizer.nval;
        for (int i = 0; i < operationsAmount; i++) {
            tokenizer.nextToken();
            if (tokenizer.ttype == '?')  {
                tokenizer.nextToken();
                boolean has = hashMap.has((long) tokenizer.nval);
                writer.write(has ? 'Y' : 'N');
                writer.newLine();
                continue;
            }

            switch (tokenizer.sval.charAt(0)) {
                case 'A':
                    tokenizer.nextToken();
                    hashMap.add((long) tokenizer.nval);
                    break;
                case 'D':
                    tokenizer.nextToken();
                    hashMap.remove((long) tokenizer.nval);
                    break;
            }
        }

        writer.close();
        reader.close();
    }

    private static class IntHashMap {
        private static final int HASH_SIZE = 64007;

        private final List<List<Long>> hashPool = new ArrayList<>(HASH_SIZE);

        void add(long key) {
            int hashIndex = hashKey(key);
            for (int i = hashPool.size(); i <= hashIndex; i++) {
                hashPool.add(null);
            }

            List<Long> list = hashPool.get(hashIndex);
            if (list == null) {
                list = new LinkedList<>();
                hashPool.set(hashIndex, list);
            }

            list.add(key);
        }

        void remove(long key) {
            int hashIndex = hashKey(key);
            if (hashIndex >= hashPool.size()) {
                return;
            }

            List<Long> list = hashPool.get(hashIndex);
            if (list != null) {
                list.removeIf(item -> item == key);
            }
        }

        boolean has(long key) {
            int hashIndex = hashKey(key);
            if (hashIndex >= hashPool.size()) {
                return false;
            }

            List<Long> list = hashPool.get(hashIndex);
            if (list != null) {
                for (long item : list) {
                    if (item == key) {
                        return true;
                    }
                }
            }

            return false;
        }

        int hashKey(long key) {
            return Math.abs((int) (key % HASH_SIZE));
        }
    }
}
