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

int main() {
    edx_open();

    struct AppState s = {};
    readTreeInput(&s);
    for (int i = 0; i < s.nodesAmount; i++) {
        edx_println_i32(findNodeBalance(&s, i));
    }

    edx_close();
    free(s.nodes);
    free(s.nodesHeights);

    return 0;
}
