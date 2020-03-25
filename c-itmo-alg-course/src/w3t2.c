#include "edx-io.h"
#include "stdlib.h"

#define ALPHABET_OFFSET 97
#define ALPHABET_SIZE 26

struct appState {
    char *inputArr;
    int *lineOrderArr;
    int lineLength;
    int linesAmount;
    int sortIterations;
};

void countSort(struct appState *state, char source[], char target[], int counters[], int lineOrderTarget[], int digit) {
    for (int i = 0; i < ALPHABET_SIZE; i++) {
        counters[i] = 0;
    }

    int inputOffset = state->linesAmount * (state->lineLength - 1 - digit);
    for (int i = 0; i < state->linesAmount; i++) {
        char value = state->inputArr[inputOffset + state->lineOrderArr[i]];
        source[i] = value;
        counters[value]++;
    }

    int prevCounters = counters[0];
    for (int i = 1; i < ALPHABET_SIZE; i++) {
        if (counters[i] > 0) {
            counters[i] += prevCounters;
            prevCounters = counters[i];
        }
    }

    for (int i = state->linesAmount - 1; i >= 0; i--) {
        int newIndex = --counters[source[i]];
        target[newIndex] = source[i];
        lineOrderTarget[newIndex] = state->lineOrderArr[i];
    }
}

void digitSort(struct appState *state) {
    char *sourceArr = malloc(state->linesAmount * sizeof(char));
    char *targetArr = malloc(state->linesAmount * sizeof(char));
    int *lineOrderTargetArr = malloc(state->linesAmount * sizeof(int));

    int countersArr[ALPHABET_SIZE];
    for (int digit = 0; digit < state->sortIterations; digit++) {
        countSort(state, sourceArr, targetArr, countersArr, lineOrderTargetArr, digit);

        char *arrTmp = sourceArr;
        sourceArr = targetArr;
        targetArr = arrTmp;

        int *lineOrderTmp = state->lineOrderArr;
        state->lineOrderArr = lineOrderTargetArr;
        lineOrderTargetArr = lineOrderTmp;
    }

    free(sourceArr);
    free(targetArr);
    free(lineOrderTargetArr);
}

void writeOutput(const struct appState *state) {
    for (int i = 0; i < state->linesAmount; i++) {
        edx_printf("%d ", state->lineOrderArr[i] + 1);
    }
}

int main() {
    edx_open();

    struct appState state = {};
    state.linesAmount = edx_next_i32();
    state.lineLength = edx_next_i32();
    state.sortIterations = edx_next_i32();

    int inputLength = state.linesAmount * state.lineLength;
    state.inputArr = malloc(inputLength * sizeof(char));
    for (int j = 0; j < state.lineLength; j++) {
        int offset = j * state.linesAmount;
        char *inputLine = edx_next_unbounded();
        for (int i = 0; i < state.linesAmount; i++) {
            state.inputArr[i + offset] = (char) (inputLine[i] - ALPHABET_OFFSET);
        }

        free(inputLine);
    }

    state.lineOrderArr = malloc(state.linesAmount * sizeof(int));
    for (int i = 0; i < state.linesAmount; i++) {
        state.lineOrderArr[i] = i;
    }

    digitSort(&state);
    writeOutput(&state);

    edx_close();
    free(state.inputArr);
    free(state.lineOrderArr);

    return 0;
}
