#include "edx-io.h"
#include "stdlib.h"

struct TreeNode {
    int key;
    int leftIndex;
    int rightIndex;
};

struct AppState {
    struct TreeNode *nodes;
    int *nodesHeights;
    int nodesAmount;
    int *outputIndexes;
    int currentOutputIndex;
};

int findNodeHeight(struct AppState *s, int nodeIndex) {
    if (s->nodesHeights[nodeIndex] > 0) {
        return s->nodesHeights[nodeIndex];
    }

    struct TreeNode *node = &s->nodes[nodeIndex];
    int leftHeight = 0;
    if (node->leftIndex >= 0) {
        leftHeight = s->nodesHeights[node->leftIndex] = findNodeHeight(s, node->leftIndex);
    }

    int rightHeight = 0;
    if (node->rightIndex >= 0) {
        rightHeight = s->nodesHeights[node->rightIndex] = findNodeHeight(s, node->rightIndex);
    }

    return s->nodesHeights[nodeIndex] = leftHeight >= rightHeight ? leftHeight + 1 : rightHeight + 1;
}

int findNodeBalance(struct AppState *s, int nodeIndex) {
    if (nodeIndex < 0) {
        return 0;
    }

    struct TreeNode *node = &s->nodes[nodeIndex];
    int rightHeight = (node->rightIndex >= 0) ? findNodeHeight(s, node->rightIndex) : 0;
    int leftHeight = (node->leftIndex >= 0) ? findNodeHeight(s, node->leftIndex) : 0;
    return rightHeight - leftHeight;
}

int findNodeParent(struct AppState *s, int key, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];

    if (key < node->key && node->leftIndex >= 0) {
        return findNodeParent(s, key, node->leftIndex);
    } else if (key > node->key && node->rightIndex >= 0) {
        return findNodeParent(s, key, node->rightIndex);
    } else {
        return nodeIndex;
    }
}

void makeSmallRightTurn(struct AppState *s, int nodeIndex) {
    struct TreeNode node = s->nodes[nodeIndex];
    int newNodeIndex = node.leftIndex;
    s->nodes[nodeIndex] = s->nodes[newNodeIndex];
    s->nodes[newNodeIndex] = node;

    struct TreeNode *turnedNode = &s->nodes[nodeIndex];
    (&s->nodes[newNodeIndex])->leftIndex = turnedNode->rightIndex;
    turnedNode->rightIndex = newNodeIndex;

    s->nodesHeights[newNodeIndex] = -1;
    s->nodesHeights[nodeIndex] = -1;
}


void makeSmallLeftTurn(struct AppState *s, int nodeIndex) {
    struct TreeNode node = s->nodes[nodeIndex];
    int newNodeIndex = node.rightIndex;
    s->nodes[nodeIndex] = s->nodes[newNodeIndex];
    s->nodes[newNodeIndex] = node;

    struct TreeNode *turnedNode = &s->nodes[nodeIndex];
    (&s->nodes[newNodeIndex])->rightIndex = turnedNode->leftIndex;
    turnedNode->leftIndex = newNodeIndex;

    s->nodesHeights[newNodeIndex] = -1;
    s->nodesHeights[nodeIndex] = -1;
}

void makeLeftTurn(struct AppState *s, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];

    int rightChildBalance = findNodeBalance(s, node->rightIndex);
    if (rightChildBalance < 0) {
        makeSmallRightTurn(s, node->rightIndex);
    }

    makeSmallLeftTurn(s, nodeIndex);
}

void makeRightTurn(struct AppState *s, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];

    int rightChildBalance = findNodeBalance(s, node->leftIndex);
    if (rightChildBalance > 0) {
        makeSmallLeftTurn(s, node->leftIndex);
    }

    makeSmallRightTurn(s, nodeIndex);
}

void balanceTreeByKey(struct AppState *s, int key, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];
    int childIndex = (key < node->key) ? node->leftIndex : node->rightIndex;

    struct TreeNode *child = &s->nodes[childIndex];
    if (child->key == key) {
        return;
    }

    balanceTreeByKey(s, key, childIndex);

    int balance = findNodeBalance(s, nodeIndex);
    if (balance > 1) {
        makeLeftTurn(s, nodeIndex);
    } else if (balance < -1) {
        makeRightTurn(s, nodeIndex);
    }
}

void insertElement(struct AppState *s, int key) {
    if (!s->nodesAmount) {
        struct TreeNode node = { key, -1, -1 };
        s->nodes[0] = node;
        s->nodesAmount++;
        return;
    }

    int parentIndex = findNodeParent(s, key, 0);
    struct TreeNode *parent = &s->nodes[parentIndex];
    if (key == parent->key) {
        return;
    }

    struct TreeNode node = { key, -1, -1 };
    int newIndex = s->nodesAmount++;
    s->nodes[newIndex] = node;

    if (key < parent->key) {
        parent->leftIndex = newIndex;
    } else {
        parent->rightIndex = newIndex;
    }

    balanceTreeByKey(s, key, 0);
}

void readTreeInput(struct AppState *s) {
    s->nodesAmount = edx_next_i32();
    s->nodes = malloc((s->nodesAmount + 1) * sizeof(struct TreeNode));
    s->nodesHeights = malloc((s->nodesAmount + 1) * sizeof(int));
    s->outputIndexes = malloc((s->nodesAmount + 1) * sizeof(int));

    for (int i = 0; i < s->nodesAmount; i++) {
        s->nodes[i].key = edx_next_i32();
        s->nodes[i].leftIndex = edx_next_i32() - 1;
        s->nodes[i].rightIndex = edx_next_i32() - 1;
        s->nodesHeights[i] = 0;
    }
}

void writeTreeOutput(struct AppState *s, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];
    edx_printf(
            "%d %d %d\n",
            node->key,
            (node->leftIndex >= 0) ? s->outputIndexes[node->leftIndex] + 1 : 0,
            (node->rightIndex >= 0) ? s->outputIndexes[node->rightIndex] + 1 : 0
    );

    if (node->leftIndex >= 0) {
        writeTreeOutput(s, node->leftIndex);
    }

    if (node->rightIndex >= 0) {
        writeTreeOutput(s, node->rightIndex);
    }
}

void buildOutputIndexes(struct AppState *s, int nodeIndex) {
    s->outputIndexes[nodeIndex] = s->currentOutputIndex++;
    struct TreeNode *node = &s->nodes[nodeIndex];

    if (node->leftIndex >= 0) {
        buildOutputIndexes(s, node->leftIndex);
    }

    if (node->rightIndex >= 0) {
        buildOutputIndexes(s, node->rightIndex);
    }
}

int main() {
    edx_open();

    struct AppState s = {};

    readTreeInput(&s);
    int keyToInsert = edx_next_i32();
    insertElement(&s, keyToInsert);

    s.currentOutputIndex = 0;
    buildOutputIndexes(&s, 0);

    edx_println_i32(s.nodesAmount);
    writeTreeOutput(&s, 0);

    edx_close();
    free(s.nodes);
    free(s.nodesHeights);
    free(s.outputIndexes);

    return 0;
}
