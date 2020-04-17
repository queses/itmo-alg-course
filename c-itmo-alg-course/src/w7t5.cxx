#include <vector>
#include "edx-io.hpp"

class TreeNode {
public:
    explicit TreeNode (int key = 0, int leftIndex = -1, int rightIndex = -1, int parentIndex = -1) {
        this->key = key;
        this->leftIndex = leftIndex;
        this->rightIndex = rightIndex;
        this->parentIndex = parentIndex;
        this->height = -1;
    }

    int key;
    int leftIndex;
    int rightIndex;
    int parentIndex;
    int height;
};

class Tree {
public:
    int nodesAmount;
    std::vector<TreeNode*> *nodes;

    explicit Tree (int nodesAmount = 0) {
        this->nodes = new std::vector<TreeNode*>();
        this->nodesAmount = nodesAmount;

        if (nodesAmount > 0) {
            this->nodes->resize(nodesAmount);
        }
    }

    ~Tree() {
        for (TreeNode* node : *nodes) {
            delete node;
        }

        delete nodes;
    }

    int pushNode(int key) {
        nodes->push_back(new TreeNode(key));
        nodesAmount++;
        return (int) nodes->size() - 1;
    }

    TreeNode *nodeAt(int index) {
        return nodes->at(index);
    }

    void insertKey(int key) {
        if (!nodesAmount) {
            nodes->clear();
            pushNode(key);
            return;
        }

        int parentIndex = findNewNodeParent(key);
        if (key == nodeAt(parentIndex)->key) {
            return;
        }

        int newIndex = pushNode(key);
        TreeNode *newNode = nodeAt(newIndex);
        newNode->parentIndex = parentIndex;

        TreeNode *parent = nodeAt(parentIndex);
        parent->height = -1;
        if (key < parent->key) {
            parent->leftIndex = newIndex;
        } else {
            parent->rightIndex = newIndex;
        }

        balanceTreeByIndex(newIndex);
    }

    void removeKey(int key, int searchFromIndex = 0) {
        if (nodesAmount <= 1) {
            if (nodesAmount == 1 && nodeAt(0)->key == key) {
                nodes->clear();
                nodesAmount = 0;
            }

            return;
        }

        int nodeIndex = findNode(key, searchFromIndex);
        if (nodeIndex < 0) {
            return;
        }

        TreeNode *node = nodeAt(nodeIndex);
        if (node->leftIndex < 0 && node->rightIndex < 0) {
            TreeNode *parent = nodeAt(node->parentIndex);
            if (key < parent->key) {
                parent->leftIndex = -1;
            } else {
                parent->rightIndex = -1;
            }
            nodesAmount--;
            parent->height = -1;
            balanceTreeByIndex(node->parentIndex);
        } else if (node->leftIndex < 0) {
            swapNodes(nodeIndex, node->rightIndex);
            TreeNode *movedNode = nodeAt(nodeIndex);
            int balanceBy = 0;
            if (movedNode->parentIndex >= 0) {
                nodeAt(movedNode->parentIndex)->height = 0;
                balanceBy = movedNode->parentIndex;
            }
            nodesAmount--;
            balanceTreeByIndex(balanceBy);
        } else {
               int lastRightIndexOfLeftSubtree = findLastRightNode(node->leftIndex);
            TreeNode *lastRightNodeOfLeftSubtree = nodeAt(lastRightIndexOfLeftSubtree);
            int lastRightNodeParentIndex = lastRightNodeOfLeftSubtree->parentIndex;
            int lastRightNodeKey = lastRightNodeOfLeftSubtree->key;
            removeNodeInner(lastRightIndexOfLeftSubtree);
            node->key = lastRightNodeKey;
            balanceTreeByIndex(lastRightNodeParentIndex);
        }
    }

    int findNode(int key, int nodeIndex = 0) {
        if (nodeIndex < 0 || nodesAmount == 0) {
            return -1;
        }

        TreeNode *node = nodeAt(nodeIndex);
        if (key < node->key && node->leftIndex >= 0) {
            return findNode(key, node->leftIndex);
        } else if (key > node->key && node->rightIndex >= 0) {
            return findNode(key, node->rightIndex);
        } else if (key == node->key) {
            return nodeIndex;
        } else {
            return -1;
        }
    }

    int findNodeBalance(int nodeIndex) {
        if (nodeIndex < 0 || nodesAmount == 0) {
            return 0;
        }

        TreeNode *node = nodeAt(nodeIndex);
        int rightHeight = (node->rightIndex >= 0) ? findNodeHeight(node->rightIndex) : 0;
        int leftHeight = (node->leftIndex >= 0) ? findNodeHeight(node->leftIndex) : 0;
        return rightHeight - leftHeight;
    }

private:
    void removeNodeInner (int nodeIndex) {
        TreeNode *node = nodeAt(nodeIndex);
        if (node->leftIndex < 0 && node->rightIndex < 0) {
            TreeNode *parent = nodeAt(node->parentIndex);
            if (node->key < parent->key) {
                parent->leftIndex = -1;
            } else {
                parent->rightIndex = -1;
            }
            nodesAmount--;
            parent->height = -1;
        } else {
            swapNodes(nodeIndex, node->leftIndex);
            nodesAmount--;
        }
    }

    int findNodeHeight(int nodeIndex) {
        TreeNode *node = nodeAt(nodeIndex);
        if (node->height > 0) {
            return node->height;
        }

        int leftHeight = 0;
        if (node->leftIndex >= 0) {
            nodeAt(node->leftIndex)->height = findNodeHeight(node->leftIndex);
            leftHeight = nodeAt(node->leftIndex)->height;
        }

        int rightHeight = 0;
        if (node->rightIndex >= 0) {
            nodeAt(node->rightIndex)->height = findNodeHeight(node->rightIndex);
            rightHeight = nodeAt(node->rightIndex)->height;
        }

        node->height = (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
        return node->height;
    }

    int findNewNodeParent(int key, int nodeIndex = 0) {
        TreeNode *node = nodeAt(nodeIndex);
        if (key < node->key && node->leftIndex >= 0) {
            return findNewNodeParent(key, node->leftIndex);
        } else if (key > node->key && node->rightIndex >= 0) {
            return findNewNodeParent(key, node->rightIndex);
        } else {
            return nodeIndex;
        }
    }

    int findLastRightNode(int nodeIndex) {
        TreeNode *node = nodeAt(nodeIndex);
        return (node->rightIndex < 0) ? nodeIndex : findLastRightNode(node->rightIndex);
    }

    void balanceTreeByIndex(int nodeIndex = 0) {
        TreeNode *node = nodeAt(nodeIndex);
        int balance = findNodeBalance(nodeIndex);
        if (balance > 1) {
            makeLeftTurn(nodeIndex);
        } else if (balance < -1) {
            makeRightTurn(nodeIndex);
        }

        if (node->parentIndex >= 0) {
            balanceTreeByIndex(node->parentIndex);
        }
    }

    void makeSmallRightTurn(int targetIndex) {
        TreeNode *sourceNode = nodes->at(targetIndex);
        int newSourceIndex = sourceNode->leftIndex;
        TreeNode *targetNode = nodeAt(newSourceIndex);
        swapNodes(targetIndex, newSourceIndex);

        if (targetNode->leftIndex >= 0) {
            TreeNode *child = nodeAt(targetNode->leftIndex);
            child->parentIndex = targetIndex;
        }

        sourceNode->leftIndex = targetNode->rightIndex;
        if (sourceNode->leftIndex >= 0) {
            TreeNode *child = nodeAt(sourceNode->leftIndex);
            child->parentIndex = newSourceIndex;
        }

        targetNode->rightIndex = newSourceIndex;
    }

    void makeSmallLeftTurn(int targetIndex) {
        TreeNode *sourceNode = nodes->at(targetIndex);
        int newSourceIndex = sourceNode->rightIndex;
        TreeNode *targetNode = nodeAt(newSourceIndex);
        swapNodes(targetIndex, newSourceIndex);

        if (targetNode->rightIndex >= 0) {
            TreeNode *child = nodeAt(targetNode->rightIndex);
            child->parentIndex = targetIndex;
        }

        sourceNode->rightIndex = targetNode->leftIndex;
        if (sourceNode->rightIndex >= 0) {
            TreeNode *child = nodeAt(sourceNode->rightIndex);
            child->parentIndex = newSourceIndex;
        }

        targetNode->leftIndex = newSourceIndex;
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

    void swapNodes(int from, int to) {
        TreeNode *nodeFrom = nodes->at(from);
        TreeNode *nodeTo = nodes->at(to);
        nodeFrom->height = -1;
        nodeTo->height = -1;
        std::swap(nodeTo->parentIndex, nodeFrom->parentIndex);

        nodes->at(from) = nodeTo;
        nodes->at(to) = nodeFrom;
    }
};

class Week7Task4 {
public:
    void run() {
        tree = new Tree();
        readInput();
        delete tree;
    }

private:
    Tree *tree;

    void readInput() {
        int operationsAmount;
        io >> operationsAmount;

        int key;
        for (int i = 0; i < operationsAmount; i++) {
            char command;
            io >> command;
            switch (command) {
                case 'A':
                    io >> key;
                    tree->insertKey(key);
                    io << tree->findNodeBalance(0) << '\n';
                    break;
                case 'D':
                    io >> key;
                    tree->removeKey(key);
                    io << tree->findNodeBalance(0) << '\n';
                    break;
                case 'C':
                    io >> key;
                    io << (tree->findNode(key) >= 0 ? 'Y' : 'N') << '\n';
                    break;
                default:
                    continue;
            }
        }
    }
};

int main() {
    Week7Task4().run();
    return 0;
}
