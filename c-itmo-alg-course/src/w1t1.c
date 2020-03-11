#include "edx-io.h"

int main() {
    edx_open();
    edx_println_i32(edx_next_i32() + edx_next_i32());
    edx_close();

    return 0;
}
