#include <memory>
#include <vector>
#include "edx-io.hpp"

class Week7Task3 {
public:
    void run() {
        readInput();
        fillHeights();
        writeOutput();
    }

private:
    std::unique_ptr<std::vector<double>> heights;
    double pointsAmount = 0;

    void fillHeights() {
        double left = 0;
        double right = heights->front();
        while (right - left > 0.000000001) {
            heights->at(1) = (left + right) / 2;
            bool isBelowZero = false;
            for (int i = 2; i < pointsAmount; i++) {
                heights->at(i) = 2 * heights->at(i - 1) - heights->at(i - 2) + 2;
                if (heights->at(i) < 0) {
                    isBelowZero = true;
                    break;
                }
            }

            if (isBelowZero) {
                left = (left + right) / 2;
            } else {
                right = (left + right) / 2;
            }
        }
    }

    void readInput() {
        io >> pointsAmount;
        heights = std::make_unique<std::vector<double>>(pointsAmount);
        io >> heights->at(0);
    }

    void writeOutput() {
        io << heights->back();
    }
};

int main() {
    std::make_unique<Week7Task3>()->run();
    return 0;
}
