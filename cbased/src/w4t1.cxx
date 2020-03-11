#include <memory>
#include "edx-io.hpp"

class Week4Task3 {
public:
    void run() {
        fillQueue();
    }

private:
    int stackLength = 0;
    int stackIndex = 0;
    std::unique_ptr<int[]> stack;

    void fillQueue() {
        io >> stackLength;
        stack = std::make_unique<int[]>(stackLength);

        char command;
        for (int i = 0; i < stackLength; i++) {
            io >> command;
            if (command == '-') {
                onItemRemoved(stack[--stackIndex]);
            } else {
                io >> stack[stackIndex++];
            }
        }
    }

    static void onItemRemoved(int value) {
        io << value << '\n';
    }
};

int main() {
    auto task = std::make_unique<Week4Task3>();
    task->run();
}
