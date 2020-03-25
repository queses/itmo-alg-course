#include <memory>
#include "edx-io.hpp"

class Week4Task2 {
public:
    void run() {
        fillQueue();
    }

private:
    int queueLength = 0;
    int queueHead = 0;
    int queueTail = 0;
    std::unique_ptr<int[]> queue;

    void fillQueue() {
        io >> queueLength;
        queue = std::make_unique<int[]>(queueLength);

        char command;
        for (int i = 0; i < queueLength; i++) {
            io >> command;
            if (command == '-') {
                onItemRemoved(queue[queueHead++]);
            } else {
                io >> queue[queueTail++];
            }
        }
    }

    static void onItemRemoved(int value) {
        io << value << '\n';
    }
};

int main() {
    std::make_unique<Week4Task2>()->run();
}
