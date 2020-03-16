#include <memory>
#include "edx-io.hpp"

class Week5Task1 {
public:
    void run() {
        readInput();
        writeIsHeap();
    }

private:
    std::unique_ptr<int[]> numbersArr;
    int numbersArrLength = 0;

    void readInput() {
        io >> numbersArrLength;

        numbersArr = std::make_unique<int[]>(numbersArrLength);
        for (int i = 0; i < numbersArrLength; i++) {
            io >> numbersArr[i];
        }
    }

    void writeIsHeap() {
        int half = numbersArrLength / 2;
        for (int i = 0; i < half; i++) {
            if (numbersArr[i] > numbersArr[(i * 2) + 1] || (i < half - 1 && numbersArr[i] > numbersArr[(i * 2) + 2])) {
                writeOutputFalse();
                return;
            }
        }

        writeOutputTrue();
    }

    static void writeOutputFalse() {
        io << "NO";
    }

    static void writeOutputTrue() {
        io << "YES";
    }
};

int main() {
    auto task = std::make_unique<Week5Task1>();
    task->run();
}
