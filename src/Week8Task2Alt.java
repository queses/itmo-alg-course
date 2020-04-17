import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Week8Task2Alt {
    private final HashMap<String, OrderedMapItem> hashMap = new HashMap<>();
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
        if (!hashMap.containsKey(key)) {
            hashMap.put(key, new OrderedMapItem(value, itemsOrder.size()));
            itemsOrder.add(key);
        } else {
            hashMap.get(key).value = value;
        }
    }


    private void deleteFrom (String key) {
        if (!hashMap.containsKey(key)) {
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
        if (!hashMap.containsKey(key)) {
            return null;
        }

        String nextItemHash = null;
        int order = hashMap.get(key).order;
        while (nextItemHash == null && ++order < itemsOrder.size()) {
            nextItemHash = itemsOrder.get(order);
        }

        if (nextItemHash != null && hashMap.containsKey(nextItemHash)) {
            return hashMap.get(itemsOrder.get(order));
        } else {
            return null;
        }
    }

    private OrderedMapItem getPrev (String key) {
        if (!hashMap.containsKey(key)) {
            return null;
        }

        String nextItemHash = null;
        int order = hashMap.get(key).order;
        while (nextItemHash == null && --order >= 0) {
            nextItemHash = itemsOrder.get(order);
        }

        if (nextItemHash != null && hashMap.containsKey(nextItemHash)) {
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
}
