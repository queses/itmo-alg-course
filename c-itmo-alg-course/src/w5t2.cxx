#include <vector>
#include <string>
#include <memory>
#include "edx-io.hpp"

class Week7Task3 {
public:
    void run() {
        readInput();
    }

private:
    std::unique_ptr<std::vector<std::pair<int, int>>> pQueue;
    std::unique_ptr<std::vector<unsigned int>> linesIndexes;

    void readInput() {
        int operationsAmount;
        io >> operationsAmount;
        pQueue = std::make_unique<std::vector<std::pair<int, int>>>();
        linesIndexes = std::make_unique<std::vector<unsigned int>>(operationsAmount);

        std::string command;
        int number, lineNumber;
        for (int line = 0; line < operationsAmount; line++) {
            io >> command;
            switch (command.at(0)) {
                case 'A':
                    io >> number;
                    addItem(number, line);
                    break;
                case 'X':
                    popAndPrintItem();
                    break;
                case 'D':
                    io >> lineNumber;
                    io >> number;
                    decreaseItemAddedAt(lineNumber - 1, number);
            }
        }
    }

    void addItem(int number, int lineNumber) {
        linesIndexes->at(lineNumber) = pQueue->size();
        pQueue->push_back(std::make_pair(number, lineNumber));
        decreaseItem(pQueue->size() - 1, number);
    }

    void popAndPrintItem() {
        if (pQueue->empty()) {
            io << "*" << '\n';
        } else {
            io << pQueue->front().first << '\n';
            linesIndexes->at(pQueue->back().second) = 0;
            pQueue->front() = pQueue->back();
            pQueue->pop_back();
            heapify(0);
        }
    }

    void decreaseItemAddedAt(int lineNumber, int value) {
        decreaseItem(linesIndexes->at(lineNumber), value);
    }

    void decreaseItem(unsigned int index, int value) {
        if (value > pQueue->at(index).first) {
            throw std::invalid_argument("Error while decreasing item: new key is bigger than old key");
        }

        pQueue->at(index).first = value;

        unsigned int parentIndex = parentIndexOf(index);
        while (index > 0 && pQueue->at(parentIndex) > pQueue->at(index)) {
            swapItems(index, parentIndex);
            index = parentIndex;
            parentIndex = parentIndexOf(index);
        }
    }

    void heapify(unsigned int index) {
        unsigned int leftChildIndex, rightChildIndex, minIndex;
        while (true) {
            leftChildIndex = leftChildIndexOf(index);
            rightChildIndex = leftChildIndex + 1;

            minIndex = (leftChildIndex < pQueue->size() && pQueue->at(leftChildIndex) < pQueue->at(index))
                       ? leftChildIndex
                       : index;

            if (rightChildIndex < pQueue->size() && pQueue->at(rightChildIndex) < pQueue->at(minIndex)) {
                minIndex = rightChildIndex;
            }

            if (minIndex != index) {
                swapItems(index, minIndex);
                index = minIndex;
            } else {
                break;
            }
        }
    }

    void swapItems(unsigned int from, unsigned int to) {
        linesIndexes->at(pQueue->at(from).second) = to;
        linesIndexes->at(pQueue->at(to).second) = from;
        std::swap(pQueue->at(from), pQueue->at(to));
    }

    static unsigned int parentIndexOf(unsigned int index) {
        return (index - 1u) >> 1u;
    }

    static unsigned int leftChildIndexOf(unsigned int index) {
        return (index << 1u) + 1u;
    }
};

int main() {
    std::make_unique<Week7Task3>()->run();
}
