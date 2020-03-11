#include "edx-io.h"
#include "stdlib.h"

#define NUMBER_BASE 512u
#define NUMBER_BASE_MAX 511u
#define NUMBER_BASE_POW 9u
#define MAX_NUM_LENGTH 4u

struct appState {
    unsigned int *arr;
    int arrLength;
};

void buildArray(struct appState *state, int firstLength, int secondLength) {
    state->arrLength = firstLength * secondLength;
    state->arr = malloc(state->arrLength * sizeof(int));

    unsigned int * firstArr = malloc(firstLength * sizeof(int));
    for (int i = 0; i < firstLength; i++) {
        firstArr[i] = edx_next_i32();
    }

    int offset = 0;
    for (int j = 0; j < secondLength; j++) {
        unsigned int secondArrValue = edx_next_i32();
        for (int i = 0; i < firstLength; i++) {
            state->arr[i + j + offset] = firstArr[i] * secondArrValue;
        }

        offset += firstLength - 1;
    }

    free(firstArr);
}

unsigned int * countSort(int arrLength, const unsigned int source[], unsigned int target[], int counters[], int digit) {
    for (int i = 0; i < NUMBER_BASE; i++) {
        counters[i] = 0;
    }

    unsigned int itemShift = NUMBER_BASE_POW * digit;
    for (int i = 0; i < arrLength; i++) {
        unsigned int countIndex = (source[i] >> itemShift) & NUMBER_BASE_MAX;
        counters[countIndex]++;
    }

    int prevCounters = counters[0];
    for (int i = 1; i < NUMBER_BASE; i++) {
        if (counters[i] > 0) {
            counters[i] += prevCounters;
            prevCounters = counters[i];
        }
    }

    for (int i = arrLength - 1; i >= 0; i--) {
        unsigned int countIndex = (source[i] >> itemShift) & NUMBER_BASE_MAX;
        target[--counters[countIndex]] = source[i];
    }

    return target;
}

void digitSort(struct appState *state) {
    unsigned int * arrBuffer = malloc(state->arrLength * sizeof(int));

    int counters[NUMBER_BASE];
    unsigned int * pResult = state->arr;
    for (int i = 0; i < MAX_NUM_LENGTH; i++) {
        pResult = countSort(state->arrLength, pResult, (pResult == state->arr) ? arrBuffer : state->arr, counters, i);
    }

    free(arrBuffer);
}

void writeOutput(struct appState *state) {
    long long tenthSum = 0;
    for (int i = 0; i < state->arrLength; i += 10) {
        tenthSum += state->arr[i];
    }

    edx_print_i64(tenthSum);
}

int main() {
    edx_open();

    int firstLength = edx_next_i32();
    int secondLength = edx_next_i32();

    struct appState state = {};
    buildArray(&state, firstLength, secondLength);
    digitSort(&state);
    writeOutput(&state);

    free(state.arr);
    edx_close();

    exit(0);
}
