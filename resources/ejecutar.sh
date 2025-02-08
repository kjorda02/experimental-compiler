#!/bin/bash

if [ ! -f "output.s" ]; then
    echo "Error: no se ha encontrado output.s"
    exit 1
fi

riscv64-linux-gnu-as -march=rv32im output.s -o prog.o

riscv64-linux-gnu-ld -m elf32lriscv -e _start prog.o -o prog

qemu-riscv32 ./prog
