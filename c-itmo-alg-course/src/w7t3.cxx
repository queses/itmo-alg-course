#include <memory>
#include <vector>
#include "edx-io.hpp"

class TreeNode {
public:
    explicit TreeNode (int key, int leftIndex = -1, int rightIndex = -1) {
        this->key = key;
        this->leftIndex = leftIndex;
        this->rightIndex = rightIndex;
    }

    int key;
    int leftIndex;
    int rightIndex;
};

class Tree {
public:
    void insertKey(int key) {
        if (nodes->empty()) {
            pushNode(TreeNode(key));
            return;
        }

        int parentIndex = findNodeParent(key);
        TreeNode *parent = nodeAt(parentIndex);
        if (key == parent->key) {
            return;
        }

        int newIndex = nodes->size();
        if (key < parent->key) {
            parent->leftIndex = newIndex;
        } else {
            parent->rightIndex = newIndex;
        }

        pushNode(TreeNode(key));
        balanceTreeByKey(key);
    }

    void pushNode(TreeNode node) {
        nodes->push_back(node);
        nodesHeights->push_back(0);
    }

    int nodesAmount() {
        return nodes->size();
    }

    TreeNode* nodeAt(int index) {
        return &nodes->at(index);
    }

private:
    std::unique_ptr<std::vector<TreeNode>> nodes = std::make_unique<std::vector<TreeNode>>();
    std::unique_ptr<std::vector<int>> nodesHeights = std::make_unique<std::vector<int>>();

    int findNodeHeight(int nodeIndex) {
        if (nodesHeights->at(nodeIndex) > 0) {
            return nodesHeights->at(nodeIndex);
        }

        TreeNode *node = nodeAt(nodeIndex);
        int leftHeight = 0;
        if (node->leftIndex >= 0) {
            leftHeight = nodesHeights->at(node->leftIndex) = findNodeHeight(node->leftIndex);
        }

        int rightHeight = 0;
        if (node->rightIndex >= 0) {
            rightHeight = nodesHeights->at(node->rightIndex) = findNodeHeight(node->rightIndex);
        }

        return nodesHeights->at(nodeIndex) = leftHeight > rightHeight ? leftHeight + 1 : rightHeight + 1;
    }

    int findNodeBalance(int nodeIndex) {
        if (nodeIndex < 0) {
            return 0;
        }

        TreeNode *node = nodeAt(nodeIndex);
        int rightHeight = (node->rightIndex >= 0) ? findNodeHeight(node->rightIndex) : 0;
        int leftHeight = (node->leftIndex >= 0) ? findNodeHeight(node->leftIndex) : 0;
        return rightHeight - leftHeight;
    }

    int findNodeParent(int key, int nodeIndex = 0) {
        TreeNode *node = nodeAt(nodeIndex);

        if (key < node->key && node->leftIndex >= 0) {
            return findNodeParent(key, node->leftIndex);
        } else if (key > node->key && node->rightIndex >= 0) {
            return findNodeParent(key, node->rightIndex);
        } else {
            return nodeIndex;
        }
    }

    void makeSmallRightTurn(int nodeIndex) {
        TreeNode node = nodes->at(nodeIndex);
        int newNodeIndex = node.leftIndex;
        nodes->at(nodeIndex) = nodes->at(newNodeIndex);
        nodes->at(newNodeIndex) = node;

        TreeNode *turnedNode = nodeAt(nodeIndex);
        nodeAt(newNodeIndex)->leftIndex = turnedNode->rightIndex;
        turnedNode->rightIndex = newNodeIndex;

        nodesHeights->at(newNodeIndex) = -1;
        nodesHeights->at(nodeIndex) = -1;
    }

    void makeSmallLeftTurn(int nodeIndex) {
        TreeNode node = nodes->at(nodeIndex);
        int newNodeIndex = node.rightIndex;
        nodes->at(nodeIndex) = nodes->at(newNodeIndex);
        nodes->at(newNodeIndex) = node;

        TreeNode *turnedNode = nodeAt(nodeIndex);
        nodeAt(newNodeIndex)->rightIndex = turnedNode->leftIndex;
        turnedNode->leftIndex = newNodeIndex;

        nodesHeights->at(newNodeIndex) = -1;
        nodesHeights->at(nodeIndex) = -1;
    }

    void makeLeftTurn(int nodeIndex) {
        TreeNode *node = nodeAt(nodeIndex);

        int rightChildBalance = findNodeBalance(node->rightIndex);
        if (rightChildBalance < 0) {
            makeSmallRightTurn(node->rightIndex);
        }

        makeSmallLeftTurn(nodeIndex);
    }

    void makeRightTurn(int nodeIndex) {
        TreeNode *node = nodeAt(nodeIndex);

        int rightChildBalance = findNodeBalance(node->leftIndex);
        if (rightChildBalance > 0) {
            makeSmallLeftTurn(node->leftIndex);
        }

        makeSmallRightTurn(nodeIndex);
    }

    void balanceTreeByKey(int key, int nodeIndex = 0) {
        TreeNode *node = nodeAt(nodeIndex);
        int childIndex = (key < node->key) ? node->leftIndex : node->rightIndex;

        TreeNode *child = nodeAt(childIndex);
        if (child->key == key) {
            return;
        }

        balanceTreeByKey(key, childIndex);

        int balance = findNodeBalance(nodeIndex);
        if (balance > 1) {
            makeLeftTurn(nodeIndex);
        } else if (balance < -1) {
            makeRightTurn(nodeIndex);
        }
    }
};

class Week7Task3 {
public:
    void run() {
        readTreeInput();

        int keyToInsert;
        io >> keyToInsert;
        tree->insertKey(keyToInsert);

        writeOutput();
    }

private:
    std::unique_ptr<Tree> tree;
    std::unique_ptr<std::vector<int>> outputIndexes;
    int currentOutputIndex = 0;

    void readTreeInput() {
        int nodesAmount;
        io >> nodesAmount;

        tree = std::make_unique<Tree>();
        for (int i = 0; i < nodesAmount; i++) {
            int key, leftIndex, rightIndex;
            io >> key;
            io >> leftIndex;
            io >> rightIndex;
            tree->pushNode(TreeNode(key, leftIndex - 1, rightIndex - 1));
        }
    }

    void writeOutput() {
        io << tree->nodesAmount() << '\n';
        outputIndexes = std::make_unique<std::vector<int>>(tree->nodesAmount());
        fillOutputIndexes();
        writeTreeOutput();
    }

    void fillOutputIndexes(int nodeIndex = 0) {
        outputIndexes->at(nodeIndex) = currentOutputIndex++;
        TreeNode *node = tree->nodeAt(nodeIndex);

        if (node->leftIndex >= 0) {
            fillOutputIndexes(node->leftIndex);
        }

        if (node->rightIndex >= 0) {
            fillOutputIndexes(node->rightIndex);
        }
    }

    void writeTreeOutput(int nodeIndex = 0) {
        TreeNode *node = tree->nodeAt(nodeIndex);

        io << node->key << ' ';
        io << (node->leftIndex >= 0 ? outputIndexes->at(node->leftIndex) + 1 : 0) << ' ';
        io << (node->rightIndex >= 0 ? outputIndexes->at(node->rightIndex) + 1 : 0) << '\n';

        if (node->leftIndex >= 0) {
            writeTreeOutput(node->leftIndex);
        }

        if (node->rightIndex >= 0) {
            writeTreeOutput(node->rightIndex);
        }
    }
};

int main() {
    std::make_unique<Week7Task3>()->run();
    return 0;
}
