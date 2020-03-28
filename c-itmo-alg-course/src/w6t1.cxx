#include <vector>
#include <string>
#include <memory>
#include "edx-io.hpp"

class Week6Task2 {
public:
    void run() {
        readInput();
        writeOutput();
    }

private:
    std::unique_ptr<std::vector<int>> items;
    std::unique_ptr<std::vector<int>> itemsToFind;

    void readInput() {
        int operationsAmount;
        io >> operationsAmount;

        items = std::make_unique<std::vector<int>>(operationsAmount);
        for (int i = 0; i < operationsAmount; i++) {
            io >> items->at(i);
        }

        io >> operationsAmount;
        itemsToFind = std::make_unique<std::vector<int>>(operationsAmount);
        for (int i = 0; i < operationsAmount; i++) {
            io >> itemsToFind->at(i);
        }
    }

    void writeOutput() {
        for (int key : *itemsToFind) {
            auto pair = findFirstAndLastIndexes(key);
            io << pair.first << " " << pair.second << "\n";
        }
    }

    std::pair<int, int> findFirstAndLastIndexes(int key) {
        int left = -1;
        int right = items->size();
        int middle;
        while (right > left + 1) {
            middle = (left + right) / 2;
            if (items->at(middle) < key) {
                left = middle;
            } else {
                right = middle;
            }
        }

        if (right >= items->size() || items->at(right) != key) {
            return std::make_pair(-1, -1);
        }

        int firstFoundIndex = ++right;
        while (right < items->size() && items->at(right) == key) {
            right++;
        }

        return std::make_pair(firstFoundIndex, right);
    }
};

int main() {
    std::make_unique<Week6Task2>()->run();
}
