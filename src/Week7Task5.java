import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StreamTokenizer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Week7Task5 {
    public static void main(String[] args) throws Exception {
        new Week7Task5().run();
    }

    private void run() throws Exception {
        Tree tree = new Tree();

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output.txt"));
        BufferedReader reader = Files.newBufferedReader(Paths.get("./input.txt"));
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int operationsAmount = (int) tokenizer.nval;
        for (int i = 0; i < operationsAmount; i++) {
            tokenizer.nextToken();
            switch (tokenizer.sval.charAt(0)) {
                case 'A':
                    tokenizer.nextToken();
                    tree.insert((int) tokenizer.nval);
                    writer.write("" + tree.findNodeBalance(0) + System.lineSeparator());
                    break;
                case 'D':
                    tokenizer.nextToken();
                    tree.removeKey((int) tokenizer.nval);
                    writer.write("" + tree.findNodeBalance(0) + System.lineSeparator());
                    break;
                case 'C':
                    tokenizer.nextToken();
                    writer.write((tree.findNode((int) tokenizer.nval) < 0 ? 'N' : 'Y') + System.lineSeparator());
            }
        }

        writer.close();
        reader.close();
    }

    private static class Tree {
        int nodesAmount;
        List<TreeNode> nodes;

        Tree() {
            this.nodes = new ArrayList<>(nodesAmount);
        }

        Tree(int nodesAmount) {
            this.nodes = new ArrayList<>(nodesAmount);
        }

        TreeNode nodeAt(int nodeIndex) {
            return this.nodes.get(nodeIndex);
        }

        void insert(int key) {
            if (nodesAmount == 0) {
                nodes.clear();
                pushNode(key);
                return;
            }

            int parentIndex = findNewNodeParent(key);
            if (key == nodeAt(parentIndex).key) {
                return;
            }

            int newIndex = pushNode(key);
            TreeNode newNode = nodeAt(newIndex);
            newNode.parentIndex = parentIndex;

            TreeNode parent = nodeAt(parentIndex);
            parent.height = -1;
            if (key < parent.key) {
                parent.leftIndex = newIndex;
            } else {
                parent.rightIndex = newIndex;
            }

            balanceTreeByIndex(newIndex);
        }

        void removeKey(int key) {
            removeKey(key, 0);
        }

        void removeKey(int key, int searchFromIndex) {
            if (nodesAmount <= 1) {
                if (nodesAmount == 1 && nodeAt(0).key == key) {
                    nodes.clear();
                    nodesAmount = 0;
                }

                return;
            }

            int nodeIndex = findNode(key, searchFromIndex);
            if (nodeIndex < 0) {
                return;
            }

            boolean isInner = searchFromIndex > 0;
            TreeNode node = nodeAt(nodeIndex);
            if (node.leftIndex < 0 && node.rightIndex < 0) {
                TreeNode parent = nodeAt(node.parentIndex);
                if (key < parent.key) {
                    parent.leftIndex = -1;
                } else {
                    parent.rightIndex = -1;
                }
                parent.height = -1;
                deleteNode(nodeIndex);
                if (!isInner) {
                    balanceTreeByIndex(node.parentIndex);
                }
            } else if (node.leftIndex < 0) {
                swapNodes(nodeIndex, node.rightIndex);
                TreeNode movedNode = nodeAt(nodeIndex);

                if (movedNode.leftIndex >= 0) {
                    nodeAt(movedNode.leftIndex).parentIndex = nodeIndex;
                }
                if (movedNode.rightIndex >= 0) {
                    nodeAt(movedNode.rightIndex).parentIndex = nodeIndex;
                }

                deleteNode(node.rightIndex);
                if (!isInner) {
                    balanceTreeByIndex(Math.max(movedNode.parentIndex, 0));
                }
            } else {
                int leftSubtreeRightestIndex = findLastRightNode(node.leftIndex);
                TreeNode leftSubtreeRightestNode = nodeAt(leftSubtreeRightestIndex);
                int leftSubtreeRightestParent = leftSubtreeRightestNode.parentIndex;
                int leftSubtreeRightestKey = leftSubtreeRightestNode.key;
                removeKey(leftSubtreeRightestKey, leftSubtreeRightestParent);
                node.key = leftSubtreeRightestKey;
                balanceTreeByIndex(leftSubtreeRightestParent);
            }
        }

        int findNodeBalance(int nodeIndex) {
            if (nodeIndex < 0 || nodesAmount == 0 || nodes.isEmpty() || nodeAt(nodeIndex) == null) {
                return 0;
            }

            TreeNode node = nodeAt(nodeIndex);
            int rightHeight = (node.rightIndex >= 0) ? findNodeHeight(node.rightIndex) : 0;
            int leftHeight = (node.leftIndex >= 0) ? findNodeHeight(node.leftIndex) : 0;
            return rightHeight - leftHeight;
        }

        int findNode(int key) {
            return findNode(key, 0);
        }

        int findNode(int key, int nodeIndex) {
            if (nodeIndex < 0 || nodesAmount == 0 || nodes.isEmpty() || nodeAt(nodeIndex) == null) {
                return -1;
            }

            TreeNode node = nodeAt(nodeIndex);
            if (key < node.key && node.leftIndex >= 0) {
                return findNode(key, node.leftIndex);
            } else if (key > node.key && node.rightIndex >= 0) {
                return findNode(key, node.rightIndex);
            } else if (key == node.key) {
                return nodeIndex;
            } else {
                return -1;
            }
        }

        private int pushNode(int key) {
            nodes.add(new TreeNode(key));
            nodesAmount++;
            return nodes.size() - 1;
        }

        private void deleteNode(int nodeIndex) {
            nodes.set(nodeIndex, null);
            nodesAmount--;
        }

        private int findNodeHeight(int nodeIndex) {
            TreeNode node = nodeAt(nodeIndex);
            if (node.height > 0) {
                return node.height;
            }

            int leftHeight = 0;
            if (node.leftIndex >= 0) {
                nodeAt(node.leftIndex).height = findNodeHeight(node.leftIndex);
                leftHeight = nodeAt(node.leftIndex).height;
            }

            int rightHeight = 0;
            if (node.rightIndex >= 0) {
                nodeAt(node.rightIndex).height = findNodeHeight(node.rightIndex);
                rightHeight = nodeAt(node.rightIndex).height;
            }

            node.height = Math.max(leftHeight, rightHeight) + 1;
            return node.height;
        }

        private int findNewNodeParent(int key) {
            return findNewNodeParent(key, 0);
        }

        private int findNewNodeParent(int key, int nodeIndex) {
            TreeNode node = nodeAt(nodeIndex);
            if (key < node.key && node.leftIndex >= 0) {
                return findNewNodeParent(key, node.leftIndex);
            } else if (key > node.key && node.rightIndex >= 0) {
                return findNewNodeParent(key, node.rightIndex);
            } else {
                return nodeIndex;
            }
        }

        private int findLastRightNode(int nodeIndex) {
            TreeNode node = nodeAt(nodeIndex);
            return (node.rightIndex < 0) ? nodeIndex : findLastRightNode(node.rightIndex);
        }

        private void balanceTreeByIndex() {
            balanceTreeByIndex(0);
        }

        private void balanceTreeByIndex(int nodeIndex) {
            TreeNode node = nodeAt(nodeIndex);
            int balance = findNodeBalance(nodeIndex);
            if (balance > 1) {
                makeLeftTurn(nodeIndex);
            } else if (balance < -1) {
                makeRightTurn(nodeIndex);
            }

            if (node.parentIndex >= 0) {
                balanceTreeByIndex(node.parentIndex);
            }
        }

        private void makeLeftTurn(int nodeIndex) {
            TreeNode node = nodeAt(nodeIndex);

            int rightChildBalance = findNodeBalance(node.rightIndex);
            if (rightChildBalance < 0) {
                makeSmallRightTurn(node.rightIndex);
            }

            makeSmallLeftTurn(nodeIndex);
        }

        private void makeRightTurn(int nodeIndex) {
            TreeNode node = nodeAt(nodeIndex);

            int rightChildBalance = findNodeBalance(node.leftIndex);
            if (rightChildBalance > 0) {
                makeSmallLeftTurn(node.leftIndex);
            }

            makeSmallRightTurn(nodeIndex);
        }

        void makeSmallLeftTurn(int targetIndex) {
            TreeNode sourceNode = nodeAt(targetIndex);
            int newSourceIndex = sourceNode.rightIndex;
            TreeNode targetNode = nodeAt(newSourceIndex);
            swapNodes(targetIndex, newSourceIndex);

            if (targetNode.rightIndex >= 0) {
                nodeAt(targetNode.rightIndex).parentIndex = targetIndex;
            }

            sourceNode.rightIndex = targetNode.leftIndex;
            if (sourceNode.rightIndex >= 0) {
                nodeAt(sourceNode.rightIndex).parentIndex = newSourceIndex;
            }

            targetNode.leftIndex = newSourceIndex;
        }

        void makeSmallRightTurn(int targetIndex) {
            TreeNode sourceNode = nodeAt(targetIndex);
            int newSourceIndex = sourceNode.leftIndex;
            TreeNode targetNode = nodeAt(newSourceIndex);
            swapNodes(targetIndex, newSourceIndex);

            if (targetNode.leftIndex >= 0) {
                nodeAt(targetNode.leftIndex).parentIndex = targetIndex;
            }

            sourceNode.leftIndex = targetNode.rightIndex;
            if (sourceNode.leftIndex >= 0) {
                nodeAt(sourceNode.leftIndex).parentIndex = newSourceIndex;
            }

            targetNode.rightIndex = newSourceIndex;
        }

        private void swapNodes(int parentIndex, int childIndex) {
            TreeNode nodeParent = nodeAt(parentIndex);
            TreeNode nodeChild = nodeAt(childIndex);

            nodeParent.height = -1;
            nodeChild.height = -1;
            if (nodeParent.parentIndex >= 0) {
                nodeAt(nodeParent.parentIndex).height = -1;
            }

            nodeChild.parentIndex = nodeParent.parentIndex;
            nodeParent.parentIndex = parentIndex;

            nodes.set(parentIndex, nodeChild);
            nodes.set(childIndex, nodeParent);
        }
    }

    private static class TreeNode {
        int key;
        int leftIndex;
        int rightIndex;
        int parentIndex;
        int height;

        TreeNode(int key) {
            this(key, -1, -1, -1);
        }

        TreeNode(int key, int leftIndex, int rightIndex, int parentIndex) {
            this.key = key;
            this.leftIndex = leftIndex;
            this.rightIndex = rightIndex;
            this.parentIndex = parentIndex;
            this.height = -1;
        }
    }
}
