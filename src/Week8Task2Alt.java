import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Week8Task2Alt {
    private final StringHashMap<OrderedMapItem> hashMap = new StringHashMap<>();
    private final List<String> itemsOrder = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new Week8Task2Alt().run();
    }

    private void run() throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"));
        BufferedReader reader = Files.newBufferedReader(Paths.get("./input.txt"));
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int operationsAmount = (int) tokenizer.nval;
        String key;
        OrderedMapItem item;
        for (int i = 0; i < operationsAmount; i++) {
            tokenizer.nextToken();
            switch (tokenizer.sval) {
                case "put":
                    tokenizer.nextToken();
                    key = tokenizer.sval;
                    tokenizer.nextToken();
                    putInto(key, tokenizer.sval);
                    break;
                case "delete":
                    tokenizer.nextToken();
                    deleteFrom(tokenizer.sval);
                    break;
                case "get":
                    tokenizer.nextToken();
                    item = getFrom(tokenizer.sval);
                    if (item != null) {
                        writer.write(item.value);
                    } else {
                        writer.write("<none>");
                    }
                    writer.newLine();
                    break;
                case "next":
                    tokenizer.nextToken();
                    item = getNext(tokenizer.sval);
                    if (item != null) {
                        writer.write(item.value);
                    } else {
                        writer.write("<none>");
                    }
                    writer.newLine();
                    break;
                case "prev":
                    tokenizer.nextToken();
                    item = getPrev(tokenizer.sval);
                    if (item != null) {
                        writer.write(item.value);
                    } else {
                        writer.write("<none>");
                    }
                    writer.newLine();
                    break;
            }
        }

        writer.close();
        reader.close();
    }

    private void putInto (String key, String value) {
        if (!hashMap.has(key)) {
            hashMap.add(key, new OrderedMapItem(value, itemsOrder.size()));
            itemsOrder.add(key);
        } else {
            hashMap.get(key).value = value;
        }
    }


    private void deleteFrom (String key) {
        if (!hashMap.has(key)) {
            return;
        }

        OrderedMapItem item = hashMap.get(key);
        itemsOrder.set(item.order, null);
        hashMap.remove(key);
    }

    private OrderedMapItem getFrom (String key) {
        return hashMap.get(key);
    }

    private OrderedMapItem getNext (String key) {
        if (!hashMap.has(key)) {
            return null;
        }

        String nextItemHash = null;
        int order = hashMap.get(key).order;
        while (nextItemHash == null && ++order < itemsOrder.size()) {
            nextItemHash = itemsOrder.get(order);
        }

        if (nextItemHash != null && hashMap.has(nextItemHash)) {
            return hashMap.get(itemsOrder.get(order));
        } else {
            return null;
        }
    }

    private OrderedMapItem getPrev (String key) {
        if (!hashMap.has(key)) {
            return null;
        }

        String nextItemHash = null;
        int order = hashMap.get(key).order;
        while (nextItemHash == null && --order >= 0) {
            nextItemHash = itemsOrder.get(order);
        }

        if (nextItemHash != null && hashMap.has(nextItemHash)) {
            return hashMap.get(itemsOrder.get(order));
        } else {
            return null;
        }
    }

    private static class OrderedMapItem {
        String value;
        int order;

        OrderedMapItem (String value, int order) {
            this.value = value;
            this.order = order;
        }
    }

    private static class StringHashMap<T> {
        private static class Item<VT> {
            String key;
            VT value;

            Item(String key, VT value) {
                this.key = key;
                this.value = value;
            }

        }

        private static final int KEY_ALPHABET_SIZE = 58; // 57 + 1
        private static final int KEY_ALPHABET_OFFSET = 65; // Index of "A" in ASCII is 67
        private static final int HASH_SIZE = 64007;

        private final List<List<Item<T>>> hashPool;

        StringHashMap() {
            hashPool = new ArrayList<>(StringHashMap.HASH_SIZE);
        }

        void add(String key, T value) {
            int hashIndex = hashKey(key);
            for (int i = hashPool.size(); i <= hashIndex; i++) {
                hashPool.add(null);
            }

            List<Item<T>> list = hashPool.get(hashIndex);
            if (list == null) {
                list = new LinkedList<>();
                hashPool.set(hashIndex, list);
            }

            list.add(new Item<>(key, value));
        }

        void remove(String key) {
            int hashIndex = hashKey(key);
            if (hashIndex >= hashPool.size()) {
                return;
            }

            List<Item<T>> list = hashPool.get(hashIndex);
            if (list != null) {
                list.removeIf(item -> key.equals(item.key));
            }
        }

        T get (String key) {
            int hashIndex = hashKey(key);
            if (hashIndex >= hashPool.size()) {
                return null;
            }

            List<Item<T>> list = hashPool.get(hashIndex);
            if (list != null) {
                for (Item<T> item : list) {
                    if (key.equals(item.key)) {
                        return item.value;
                    }
                }
            }

            return null;
        }

        boolean has(String key) {
            return (get(key) != null);
        }

        private int hashKey(String key) {
            long asInt = 0;
            int keyLen = key.length();

            int multiplier = 1;
            for (int i = 0; i < keyLen; i++) {
                asInt += multiplier * (key.charAt(i) - KEY_ALPHABET_OFFSET);
                multiplier *= KEY_ALPHABET_SIZE;
            }

            return (int) (asInt % HASH_SIZE);
        }
    }
}
