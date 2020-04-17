#include <vector>
#include "edx-io.hpp"

typedef unsigned long long t_key;


class OpenHashSet {
public:
    void add(t_key key) {
        t_key keyNonZero = key + 1;

        unsigned int hashIndex = keyNonZero % TABLE_SIZE;
        unsigned int extraHashMod = (keyNonZero % EXTRA_HASH) + 1;
        while ((*hashPool)[hashIndex] != 0) {
            if ((*hashPool)[hashIndex] == keyNonZero) {
                return;
            }

            hashIndex = (hashIndex + extraHashMod) % TABLE_SIZE;
        }

        (*hashPool)[hashIndex] = keyNonZero;
    }

    bool has(t_key key) {
        t_key keyNonZero = key + 1;

        unsigned int hashIndex = keyNonZero % TABLE_SIZE;
        unsigned int extraHashMod = (keyNonZero % EXTRA_HASH) + 1;
        while ((*hashPool)[hashIndex] != 0) {
            if ((*hashPool)[hashIndex] == keyNonZero) {
                return true;
            }

            hashIndex = (hashIndex + extraHashMod) % TABLE_SIZE;
        }

        return false;
    }

    ~OpenHashSet() {
        delete hashPool;
    }

private:
    static const unsigned int TABLE_SIZE = 30000049;
    static const unsigned int EXTRA_HASH = TABLE_SIZE - 1;

    std::vector<t_key> *hashPool = new std::vector<t_key>(TABLE_SIZE);
};

class Week8Task3 {
public:
    void run() {
        readInput();

        hashTable = new OpenHashSet();
        for (int i = 0; i < numN; i++) {
            runIteration();
        }

        writeOutput();
        delete hashTable;
    }

private:
    static const long long POW_10_15 = 1000000000000000L;

    OpenHashSet *hashTable;
    unsigned int numN;
    t_key numX;
    unsigned int numA;
    unsigned int numAC;
    unsigned int numAD;
    t_key numB;
    t_key numBD;
    t_key numBC;

    void runIteration() {
        if (hashTable->has(numX)) {
            numA = (numA + numAC) % 1000;
            numB = (numB + numBC) % POW_10_15;
        } else {
            hashTable->add(numX);
            numA = (numA + numAD) % 1000;
            numB = (numB + numBD) % POW_10_15;
        }

        numX = (numX * numA + numB) % POW_10_15;
    }

    void readInput() {
        int readN, readA, readAC, readAD;
        long long readX, readB, readBC, readBD;

        io >> readN;
        io >> readX;
        io >> readA;
        io >> readB;
        io >> readAC;
        io >> readBC;
        io >> readAD;
        io >> readBD;

        numN = readN;
        numX = readX;
        numA = readA;
        numAC = readAC;
        numAD = readAD;
        numB = readB;
        numBC = readBC;
        numBD = readBD;
    }

    void writeOutput() {
        io << std::to_string(numX) << ' ' << std::to_string(numA) << ' ' << std::to_string(numB);
    }
};

int main() {
    Week8Task3().run();
    return 0;
}
