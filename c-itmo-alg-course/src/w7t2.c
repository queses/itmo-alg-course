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
    if (nodeIndex == -1) {
        return 0;
    } else if (s->nodesHeights[nodeIndex] > 0) {
        return s->nodesHeights[nodeIndex];
    }

    struct TreeNode *node = &s->nodes[nodeIndex];
    if (node->leftIndex < 0 && node->rightIndex < 0) {
        return s->nodesHeights[nodeIndex] = 1;
    }

    int leftHeight = s->nodesHeights[node->leftIndex] = findNodeHeight(s, node->leftIndex);
    int rightHeight = s->nodesHeights[node->rightIndex] = findNodeHeight(s, node->rightIndex);
    return s->nodesHeights[nodeIndex] = leftHeight >= rightHeight ? leftHeight + 1 : rightHeight + 1;
}

int findNodeBalance(struct AppState *s, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];
    return findNodeHeight(s, node->rightIndex) - findNodeHeight(s, node->leftIndex);
}

void makeSmallRightTurn(struct AppState *s, int nodeIndex) {
    struct TreeNode node = s->nodes[nodeIndex];
    int newNodeIndex = node.leftIndex;
    s->nodes[nodeIndex] = s->nodes[newNodeIndex];
    s->nodes[newNodeIndex] = node;

    struct TreeNode *turnedNode = &s->nodes[nodeIndex];
    (&s->nodes[newNodeIndex])->leftIndex = turnedNode->rightIndex;
    turnedNode->rightIndex = newNodeIndex;
}


void makeSmallLeftTurn(struct AppState *s, int nodeIndex) {
    struct TreeNode node = s->nodes[nodeIndex];
    int newNodeIndex = node.rightIndex;
    s->nodes[nodeIndex] = s->nodes[newNodeIndex];
    s->nodes[newNodeIndex] = node;

    struct TreeNode *turnedNode = &s->nodes[nodeIndex];
    (&s->nodes[newNodeIndex])->rightIndex = turnedNode->leftIndex;
    turnedNode->leftIndex = newNodeIndex;
}

void makeLeftTurn(struct AppState *s, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];

    int rightChildBalance = findNodeBalance(s, node->rightIndex);
    if (rightChildBalance < 0) {
        makeSmallRightTurn(s, node->rightIndex);
    }

    makeSmallLeftTurn(s, nodeIndex);
}

void readTreeInput(struct AppState *s) {
    s->nodesAmount = edx_next_i32();
    s->nodes = malloc(s->nodesAmount * sizeof(struct TreeNode));
    s->nodesHeights = malloc(s->nodesAmount * sizeof(int));

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
    makeLeftTurn(&s, 0);

    s.currentOutputIndex = 0;
    s.outputIndexes = s.nodesHeights;
    buildOutputIndexes(&s, 0);


    edx_println_i32(s.nodesAmount);
    writeTreeOutput(&s, 0);

    edx_close();
    free(s.nodes);
    free(s.outputIndexes);

    return 0;
}
