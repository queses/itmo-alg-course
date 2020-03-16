#include <memory>
#include <deque>
#include "edx-io.hpp"

class Week4Task6 {
public:
    void run() {
        readCommands();
    }

private:
    std::deque<int> deque;

    void readCommands() {
        int operatorsAmount = 0;
        io >> operatorsAmount;

        std::string token;
        for (int i = 0; i < operatorsAmount; i++) {
            io >> token;
            switch (token.front()) {
                case '+':
                    add();
                    break;
                case '-':
                    minus();
                    break;
                case '*':
                    multiply();
                    break;
                default:
                    deque.push_back(std::stoi(token));
            }
        }

        io << deque.back();
    }

    void add() {
        int second = deque.back();
        deque.pop_back();
        int first = deque.back();
        deque.pop_back();
        deque.push_back(first + second);
    }

    void minus() {
        int second = deque.back();
        deque.pop_back();
        int first = deque.back();
        deque.pop_back();
        deque.push_back(first - second);
    }

    void multiply() {
        int second = deque.back();
        deque.pop_back();
        int first = deque.back();
        deque.pop_back();
        deque.push_back(first * second);
    }
};

int main() {
    auto task = std::make_unique<Week4Task6>();
    task->run();
}
