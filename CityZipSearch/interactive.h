#ifndef INTERACTIVE_H
#define INTERACTIVE_H
#include "structs.h"
#include "lib.h"

extern void doInteractive(zipTowns arrs, int length);
extern int FindZip(zipTowns arrs, int length, char *target);
extern int FindTown(zipTowns arrs, int length, char *target);

#endif // last line of file
