#include <memory>
#include <vector>
#include "edx-io.hpp"

class Week4Task3 {
public:
    void run() {
        io >> linesAmount;

        line = std::make_unique<std::string>();
        stack = std::make_unique<std::vector<char>>();

        for (int i = 0; i < linesAmount; i++) {
            io << (isLineCorrect() ? "YES" : "NO") << '\n';
        }
    }

private:
    int linesAmount = 0;
    std::unique_ptr<std::vector<char>> stack;
    std::unique_ptr<std::string> line;

    bool isLineCorrect() {
        io >> *line;
        stack->clear();
        for (char &c : *line) {
            if (c == '(' || c == '[') {
                stack->push_back(c);
            } else if (c == ')') {
                if (stack->empty() || stack->back() != '(') {
                    return false;
                } else {
                    stack->pop_back();
                }
            } else if (c == ']') {
                if (stack->empty() || stack->back() != '[') {
                    return false;
                } else {
                    stack->pop_back();
                }
            }
        }

        return stack->empty();
    }
};

int main() {
    auto task = std::make_unique<Week4Task3>();
    task->run();
}
