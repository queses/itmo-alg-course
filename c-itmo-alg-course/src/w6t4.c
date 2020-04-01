#include "edx-io.h"
#include "stdlib.h"

struct TreeNode {
    int key;
    int leftIndex;
    int rightIndex;
};

struct AppState {
    struct TreeNode *nodes;
    int nodesAmount;
};

int getTreeNodesAmount(struct AppState *s, int nodeIndex) {
    struct TreeNode *node = &s->nodes[nodeIndex];

    int leftNodesAmount = 0;
    if (node->leftIndex >= 0) {
        leftNodesAmount = getTreeNodesAmount(s, node->leftIndex);
    }

    int rightNodesAmount = 0;
    if (node->rightIndex >= 0) {
        rightNodesAmount = getTreeNodesAmount(s, node->rightIndex);
    }

    return 1 + leftNodesAmount + rightNodesAmount;
}

void removeNodeByKey(struct AppState *s, int key, int nodeIndex, int parentIndex) {
    if (nodeIndex < 0) {
        return;
    }

    struct TreeNode *node = &s->nodes[nodeIndex];
    if (key < node->key) {
        return removeNodeByKey(s, key, node->leftIndex, nodeIndex);
    } else if (key > node->key) {
        return removeNodeByKey(s, key, node->rightIndex, nodeIndex);
    } else {
        s->nodesAmount -= getTreeNodesAmount(s, nodeIndex);
        struct TreeNode *parent = &s->nodes[parentIndex];
        if (key < parent->key) {
            parent->leftIndex = -1;
        } else {
            parent->rightIndex = -1;
        }
    }
}

void buildTree(struct AppState *s) {
    s->nodesAmount = edx_next_i32();
    s->nodes = malloc(s->nodesAmount * sizeof(struct TreeNode));

    for (int i = 0; i < s->nodesAmount; i++) {
        s->nodes[i].key = edx_next_i32();
        s->nodes[i].leftIndex = edx_next_i32() - 1;
        s->nodes[i].rightIndex = edx_next_i32() - 1;
    }
}

void removeNodes(struct AppState *s) {
    int operationsAmount = edx_next_i32();
    for (int i = 0; i < operationsAmount; i++) {
        removeNodeByKey(s, edx_next_i32(), 0, -1);
        edx_println_i32(s->nodesAmount);
    }
}

int main() {
    edx_open();

    struct AppState s = {};
    buildTree(&s);
    removeNodes(&s);

    edx_close();
    free(s.nodes);

    return 0;
}
