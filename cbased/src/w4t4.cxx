#include <memory>
#include <list>
#include <vector>
#include "edx-io.hpp"

class Week4Task2 {
public:
    void run() {
        fillQueue();
    }

private:
    std::unique_ptr<std::vector<std::pair<int, int>>> inputStack, outputStack;

    void fillQueue() {
        int operationsAmount;
        io >> operationsAmount;

        inputStack = std::make_unique<std::vector<std::pair<int, int>>>();
        outputStack = std::make_unique<std::vector<std::pair<int, int>>>();

        char command;
        for (int i = 0; i < operationsAmount; i++) {
            io >> command;
            if (command == '-') {
                removeElement();
            } else if (command == '?') {
                io << getCurrentMin() << '\n';
            } else {
                int number;
                io >> number;
                addElement(number);
            }
        }
    }

    void removeElement() {
        if (outputStack->empty()) {
            while (!inputStack->empty()) {
                int element = inputStack->back().first;
                inputStack->pop_back();
                int min = outputStack->empty() ? element : std::min(element, outputStack->back().second);
                outputStack->push_back(std::make_pair(element, min));
            }
        }

        outputStack->pop_back();
    }

    void addElement(int element) {
        int min = inputStack->empty() ? element : std::min(element, inputStack->back().second);
        inputStack->push_back(std::make_pair(element, min));
    }

    int getCurrentMin() {
        if (inputStack->empty()) {
            return outputStack->back().second;
        } else if (outputStack->empty()) {
            return inputStack->back().second;
        } else {
            return std::min(inputStack->back().second, outputStack->back().second);
        }
    }
};

int main() {
    auto task = std::make_unique<Week4Task2>();
    task->run();
}
