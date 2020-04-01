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
    int treeDepth;
};

int getTreeDepth(struct AppState *s, int nodeIndex, int nodeDepth) {
    struct TreeNode *record = &s->nodes[nodeIndex];

    if (record->leftIndex < 0 && record->rightIndex < 0) {
        return nodeDepth;
    }

    int leftDepth = 0;
    if (record->leftIndex >= 0) {
        leftDepth = getTreeDepth(s, record->leftIndex, nodeDepth + 1);
    }

    int rightDepth = 0;
    if (record->rightIndex >= 0) {
        rightDepth = getTreeDepth(s, record->rightIndex, nodeDepth + 1);
    }
    
    if (leftDepth >= rightDepth) {
        return leftDepth;
    } else {
        return rightDepth;
    }
}

void readTreeInput(struct AppState *s) {
    s->nodesAmount = edx_next_i32();
    s->nodes = malloc(s->nodesAmount * sizeof(struct TreeNode));

    for (int i = 0; i < s->nodesAmount; i++) {
        s->nodes[i].key = edx_next_i32();
        s->nodes[i].leftIndex = edx_next_i32() - 1;
        s->nodes[i].rightIndex = edx_next_i32() - 1;
    }
}

void writeOutput(struct AppState *s) {
    edx_print_i32(s->treeDepth);
}

int main() {
    edx_open();

    struct AppState s = {};
    readTreeInput(&s);
    s.treeDepth = (s.nodesAmount > 0) ? getTreeDepth(&s, 0, 1) : 0;
    writeOutput(&s);

    edx_close();
    free(s.nodes);

    return 0;
}
