#include <capstone.h>

void foo()
{
    // cs_arch test = CS_ARCH_AARCH64;
    // char check[sizeof(cs_arch) >= CS_ARCH_AARCH64 ? 1 : -1];
    (void)sizeof(enum { CS_ARCH_CHECK = 1 / (int)(CS_ARCH_AARCH64 >= 0) });
}