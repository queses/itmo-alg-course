#include <memory>
#include <utility>
#include <vector>
#include <deque>
#include <map>
#include "edx-io.hpp"

#define REGISTERS_AMOUNT 26
#define CHAR_OFFSET 97
#define ASCII_SIZE 256

class AbstractCommand {
public:
    virtual void run() {}
};

struct AppState {
    std::deque<unsigned> numbers;
    std::map<std::string, int> labels;
    unsigned registers[REGISTERS_AMOUNT] = {};
    int currentCommand = 0;

    std::unique_ptr<std::vector<std::unique_ptr<AbstractCommand>>> commands =
            std::make_unique<std::vector<std::unique_ptr<AbstractCommand>>>();
};

class PutInQueueCommand : public AbstractCommand {
public:
    PutInQueueCommand (AppState *pS, int pNumber) {
        s = pS;
        number = pNumber;
    }

    void run() override {
        s->numbers.push_back(number);
    }

private:
    AppState *s;
    int number;
};

class PutInRegisterCommand : public AbstractCommand {
public:
    PutInRegisterCommand (AppState *pS, int pRegisterNum) {
        s = pS;
        registerNum = pRegisterNum;
    }

    void run() override {
        if (s->numbers.empty()) {
            s->registers[registerNum] = 0;
        } else {
            int value = s->numbers.front();
            s->numbers.pop_front();
            s->registers[registerNum] = value;
        }
    }

private:
    AppState *s;
    int registerNum;
};

class TakeFromRegisterCommand : public AbstractCommand {
public:
    TakeFromRegisterCommand (AppState *pS, int pRegisterNum) {
        s = pS;
        registerNum = pRegisterNum;
    }

    void run() override {
        unsigned value = s->registers[registerNum];
        s->numbers.push_back(value);
    }

private:
    AppState *s;
    int registerNum;
};

class GoToCommand : public AbstractCommand {
public:
    GoToCommand (AppState *pS, std::string pLabel) {
        s = pS;
        label = std::move(pLabel);
    }

    void run() override {
        s->currentCommand = s->labels.at(label) - 1;
    }

private:
    AppState *s;
    std::string label;
};

class GoToIfZeroCommand : public AbstractCommand {
public:
    GoToIfZeroCommand (AppState *pS, int pRegisterNum, std::string pLabel) {
        s = pS;
        registerNum = pRegisterNum;
        label = std::move(pLabel);
    }

    void run() override {
        unsigned value = s->registers[registerNum];
        if (value == 0) {
            s->currentCommand = s->labels.at(label) - 1;
        }
    }

private:
    AppState *s;
    int registerNum;
    std::string label;
};

class GoToIfEqualsCommand : public AbstractCommand {
public:
    GoToIfEqualsCommand (AppState *pS, int pFirstRegisterNum, int pSecondRegisterNum, std::string pLabel) {
        s = pS;
        firstRegisterNum = pFirstRegisterNum;
        secondRegisterNum = pSecondRegisterNum;
        label = std::move(pLabel);
    }

    void run() override {
        if (s->registers[firstRegisterNum] == s->registers[secondRegisterNum]) {
            s->currentCommand = s->labels.at(label) - 1;
        }
    }

private:
    AppState *s;
    int firstRegisterNum;
    int secondRegisterNum;
    std::string label;
};

class GoToIfGreaterCommand : public AbstractCommand {
public:
    GoToIfGreaterCommand (AppState *pS, int pFirstRegisterNum, int pSecondRegisterNum, std::string pLabel) {
        s = pS;
        firstRegisterNum = pFirstRegisterNum;
        secondRegisterNum = pSecondRegisterNum;
        label = std::move(pLabel);
    }

    void run() override {
        if (s->registers[firstRegisterNum] > s->registers[secondRegisterNum]) {
            s->currentCommand = s->labels.at(label) - 1;
        }
    }

private:
    AppState *s;
    int firstRegisterNum;
    int secondRegisterNum;
    std::string label;
};

class PlusElementsCommand : public AbstractCommand {
public:
    explicit PlusElementsCommand (AppState *pS) {
        s = pS;
    }

    void run() override {
        unsigned first = s->numbers.front();
        s->numbers.pop_front();
        unsigned second = s->numbers.front();
        s->numbers.pop_front();

        s->numbers.push_back((first + second) % 65536);
    }

private:
    AppState *s;
};

class MinusElementsCommand : public AbstractCommand {
public:
    explicit MinusElementsCommand (AppState *pS) {
        s = pS;
    }

    void run() override {
        unsigned first = s->numbers.front();
        s->numbers.pop_front();
        unsigned second = s->numbers.front();
        s->numbers.pop_front();

        s->numbers.push_back((first - second) % 65536);
    }

private:
    AppState *s;
};

class MultiplyElementsCommand : public AbstractCommand {
public:
    explicit MultiplyElementsCommand (AppState *pS) {
        s = pS;
    }

    void run() override {
        unsigned first = s->numbers.front();
        s->numbers.pop_front();
        unsigned second = s->numbers.front();
        s->numbers.pop_front();

        s->numbers.push_back((first * second) % 65536);
    }

private:
    AppState *s;
};

class DivElementsCommand : public AbstractCommand {
public:
    explicit DivElementsCommand (AppState *pS) {
        s = pS;
    }

    void run() override {
        unsigned first = s->numbers.front();
        s->numbers.pop_front();
        unsigned second = s->numbers.front();
        s->numbers.pop_front();

        s->numbers.push_back((second == 0) ? 0 : first / second);
    }

private:
    AppState *s;
};

class ModElementsCommand : public AbstractCommand {
public:
    explicit ModElementsCommand (AppState *pS) {
        s = pS;
    }

    void run() override {
        unsigned first = s->numbers.front();
        s->numbers.pop_front();
        unsigned second = s->numbers.front();
        s->numbers.pop_front();

        s->numbers.push_back((second == 0) ? 0 : first % second);
    }

private:
    AppState *s;
};

class PrintCommand : public AbstractCommand {
public:
    explicit PrintCommand (AppState *pS) {
        s = pS;
    }

    void run() override {
        unsigned value = s->numbers.front();
        s->numbers.pop_front();
        io << value << '\n';
    }

private:
    AppState *s;
};

class PrintCharCommand : public AbstractCommand {
public:
    explicit PrintCharCommand (AppState *pS) {
        s = pS;
    }

    void run() override {
        unsigned value = s->numbers.front();
        s->numbers.pop_front();
        io << (char) (value % 256);
    }

private:
    AppState *s;
};

class PrintRegisterCommand : public AbstractCommand {
public:
    PrintRegisterCommand (AppState *pS, int pRegisterNum) {
        s = pS;
        registerNum = pRegisterNum;
    }

    void run() override {
        io << s->registers[registerNum] << '\n';
    }

private:
    AppState *s;
    int registerNum;
};

class PrintCharRegisterCommand : public AbstractCommand {
public:
    PrintCharRegisterCommand (AppState *pS, int pRegisterNum) {
        s = pS;
        registerNum = pRegisterNum;
    }

    void run() override {
        io << (char) (s->registers[registerNum] % ASCII_SIZE);
    }

private:
    AppState *s;
    int registerNum;
};

class QuackInterpreter {
public:
    void run() {
        readCommands();
        executeCommands();
    }

private:
    AppState s;

    void executeCommands() {
        for (s.currentCommand = 0; s.currentCommand < s.commands->size(); s.currentCommand++) {
            s.commands
                ->at(s.currentCommand)
                ->run();
        }
    }

    void readCommands() {
        std::string line;
        io >> line;

        while (!line.empty()) {
            parseCommand(line);
            io >> line;
        }
    }

    void parseCommand (std::string &line) {
        switch (line.front()) {
            case '+':
                s.commands->push_back(std::make_unique<PlusElementsCommand>(&s));
                break;
            case '-':
                s.commands->push_back(std::make_unique<MinusElementsCommand>(&s));
                break;
            case '*':
                s.commands->push_back(std::make_unique<MultiplyElementsCommand>(&s));
                break;
            case '/':
                s.commands->push_back(std::make_unique<DivElementsCommand>(&s));
                break;
            case '%':
                s.commands->push_back(std::make_unique<ModElementsCommand>(&s));
                break;
            case '>':
                s.commands->push_back(std::make_unique<PutInRegisterCommand>(&s, line.at(1) - CHAR_OFFSET));
                break;
            case '<':
                s.commands->push_back(std::make_unique<TakeFromRegisterCommand>(&s, line.at(1) - CHAR_OFFSET));
                break;
            case 'P':
                if (line.size() > 1) {
                    s.commands->push_back(std::make_unique<PrintRegisterCommand>(&s, line.at(1) - CHAR_OFFSET));
                } else {
                    s.commands->push_back(std::make_unique<PrintCommand>(&s));
                }
                break;
            case 'C':
                if (line.size() > 1) {
                    s.commands->push_back(std::make_unique<PrintCharRegisterCommand>(&s, line.at(1) - CHAR_OFFSET));
                } else {
                    s.commands->push_back(std::make_unique<PrintCharCommand>(&s));
                }
                break;
            case ':':
                s.labels[line.substr(1)] = s.commands->size();
                break;
            case 'J':
                s.commands->push_back(std::make_unique<GoToCommand>(&s, line.substr(1)));
            case 'Z':
                s.commands->push_back(std::make_unique<GoToIfZeroCommand>(&s, line.at(1) - CHAR_OFFSET, line.substr(2)));
                break;
            case 'G':
                s.commands->push_back(std::make_unique<GoToIfGreaterCommand>(&s, line.at(1) - CHAR_OFFSET, line.at(2) - CHAR_OFFSET, line.substr(3)));
                break;
            case 'E':
                s.commands->push_back(std::make_unique<GoToIfEqualsCommand>(&s, line.at(1) - CHAR_OFFSET, line.at(2) - CHAR_OFFSET, line.substr(3)));
                break;
            case 'Q':
                line = "";
                break;
            default:
                s.commands->push_back(std::make_unique<PutInQueueCommand>(&s, std::stoi(line)));
        }
    }
};

int main() {
    auto task = std::make_unique<QuackInterpreter>();
    task->run();
}
