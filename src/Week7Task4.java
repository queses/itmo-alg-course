import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Week7Task4 {
    public static void main(String[] args) throws Exception {
        new Week7Task4().run();
    }

    private Tree tree;
    private int keyToRemove;
    int[][] ioArray;
    int outputNextIndex = 0;

    private void run() throws Exception {
        readInput();
        tree.remove(keyToRemove);
        writeOutput();
    }

    private void readInput() throws Exception {
        BufferedReader reader = Files.newBufferedReader(Paths.get("./input.txt"));
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int operationsAmount = (int) tokenizer.nval;
        ioArray = new int[operationsAmount][3];
        for (int i = 0; i < operationsAmount; i++) {
            tokenizer.nextToken();
            ioArray[i][0] = (int) tokenizer.nval;
            tokenizer.nextToken();
            ioArray[i][1] = (int) tokenizer.nval;
            tokenizer.nextToken();
            ioArray[i][2] = (int) tokenizer.nval;
        }

        tokenizer.nextToken();
        keyToRemove = (int) tokenizer.nval;
        reader.close();

        tree = new Tree(buildTreeFromInput(0));
    }

    void writeOutput() throws Exception {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"));
        fillOutputArray(tree.root);

        writer.write(Integer.toString(outputNextIndex));
        writer.newLine();

        for (int i = 0; i < outputNextIndex; i++) {
            writer.write("" + ioArray[i][0] + ' ' + ioArray[i][1] + ' ' + ioArray[i][2]);
            writer.newLine();
        }

        writer.close();
    }

    private TreeNode buildTreeFromInput(int nodeIndex) {
        TreeNode node = new TreeNode(ioArray[nodeIndex][0]);

        int leftIndex = ioArray[nodeIndex][1] - 1;
        if (leftIndex >= 0) {
            node.left = buildTreeFromInput(leftIndex);
        }

        int rightIndex = ioArray[nodeIndex][2] - 1;
        if (rightIndex >= 0) {
            node.right = buildTreeFromInput(rightIndex);
        }

        return node;
    }

    private void fillOutputArray(TreeNode node) {
        int currentIndex = outputNextIndex++;
        ioArray[currentIndex][0] = node.key;

        if (node.hasLeft()) {
            ioArray[currentIndex][1] = outputNextIndex + 1;
            fillOutputArray(node.left);
        } else {
            ioArray[currentIndex][1] = 0;
        }

        if (node.hasRight()) {
            ioArray[currentIndex][2] = outputNextIndex + 1;
            fillOutputArray(node.right);
        } else {
            ioArray[currentIndex][2] = 0;
        }
    }

    private static class Tree {
        TreeNode root;

        Tree(TreeNode node) {
            this.root = node;
        }

        void insert(int key) {
            root = insert(key, root);
        }

        public void remove(int key) {
            root = remove(key, root);
        }

        public boolean has(int key) {
            return (root != null) && has(key, root);
        }

        public int treeBalance() {
            return (root != null) ? root.getBalance() : 0;
        }

        private boolean has(int key, TreeNode node) {
            if (key < node.key && node.hasLeft()) {
                return has(key, node.left);
            } else if (key > node.key && node.hasRight()) {
                return has(key, node.right);
            } else {
                return (key == node.key);
            }
        }

        private TreeNode insert(int key, TreeNode node) {
            if (node == null) {
                return new TreeNode(key);
            } else if (key == node.key) {
                return node;
            }

            if (key < node.key) {
                node.left = insert(key, node.left);
            } else {
                node.right = insert(key, node.right);
            }

            return balanceNode(node);
        }

        private TreeNode remove(int key, TreeNode node) {
            if (node == null) {
                return null;
            } else if (key < node.key) {
                node.left = remove(key, node.left);
            } else if (key > node.key) {
                node.right = remove(key, node.right);
            } else if (!node.hasLeft()) {
                return node.right;
            } else {
                TreeNode rightestInLeft = findRightestNode(node.left);
                node.key = rightestInLeft.key;
                node.left = remove(rightestInLeft.key, node.left);
            }

            node.fixHeight();
            return balanceNode(node);
        }

        private TreeNode findRightestNode (TreeNode node) {
            return node.hasRight() ? findRightestNode(node.right) : node;
        }

        private TreeNode makeSmallRightTurn(TreeNode node) {
            TreeNode child = node.left;
            node.left = child.right;
            child.right = node;
            node.fixHeight();
            child.fixHeight();

            return child;
        }

        private TreeNode makeSmallLeftTurn(TreeNode node) {
            TreeNode child = node.right;
            node.right = child.left;
            child.left = node;
            node.fixHeight();
            child.fixHeight();

            return child;
        }

        private TreeNode balanceNode(TreeNode node) {
            node.fixHeight();
            int balance = node.getBalance();
            if (balance > 1) {
                if (node.right.getBalance() < 0) {
                    node.right = makeSmallRightTurn(node.right);
                }
                return makeSmallLeftTurn(node);
            } else if (balance < -1) {
                if (node.left.getBalance() > 0) {
                    node.left = makeSmallLeftTurn(node.left);
                }
                return makeSmallRightTurn(node);
                } else {
                return node;
            }
        }
    }

    private static class TreeNode {
        int key;
        TreeNode left;
        TreeNode right;
        int height;

        TreeNode(int key) {
            this(key, null, null);
        }

        TreeNode(int key, TreeNode left, TreeNode right) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.height = 1;
        }

        boolean hasRight() {
            return (right != null);
        }

        boolean hasLeft() {
            return (left != null);
        }

        int getBalance() {
            return heightOf(right) - heightOf(left);
        }

        void fixHeight() {
            this.height = Math.max(heightOf(left), heightOf(right)) + 1;
        }

        private static int heightOf(TreeNode node) {
            return (node != null) ? node.height : 0;
        }
    }
}
