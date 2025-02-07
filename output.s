; fib:
fib:
; pmb 0
addi sp, sp, -32
sw ra, 28(sp) ; -4(fp)
sw fp, 24(sp) ; -8(fp)
addi fp, sp, 32
sw s1, -12(fp)
sw s2, -16(fp)
sw s3, -20(fp)
sw s4, -24(fp)
sw s5, -28(fp)
sw s6, -32(fp)
; if n <= 1 goto e1
lw a1, 0(fp)
ble a1, 1, e1
; t0 = 0
li s1, 0
; goto e2
j e2
; e1:
e1:
; t0 = -1
li s1, -1
; e2:
e2:
; if t0 == 0 goto e0
beq s1, 0, e0
; returnval = n
lw a1, 0(fp)
mv a0, a1
; rtn 0
lw s1, -12(fp)
lw s2, -16(fp)
lw s3, -20(fp)
lw s4, -24(fp)
lw s5, -28(fp)
lw s6, -32(fp)
lw fp, 24(sp)
lw ra, 28(sp)
addi sp, sp, 32
jr ra
; e0:
e0:
; init_params
addi sp, sp, -4
; t2 = n - 1
lw a1, 0(fp)
addi s3, a1, -1
; param_s t2
sw s3, 0(sp)
; call 0
call fib; t1 = returnval
mv s2, a0
; init_params
addi sp, sp, -4
; t4 = n - 2
lw a1, 0(fp)
addi s5, a1, -2
; param_s t4
sw s5, 0(sp)
; call 0
call fib; t3 = returnval
mv s4, a0
; t5 = t1 + t3
add s6, s2, s4
; returnval = t5
mv a0, s6
; rtn 0
lw s1, -12(fp)
lw s2, -16(fp)
lw s3, -20(fp)
lw s4, -24(fp)
lw s5, -28(fp)
lw s6, -32(fp)
lw fp, 24(sp)
lw ra, 28(sp)
addi sp, sp, 32
jr ra
; rtn 0
lw s1, -12(fp)
lw s2, -16(fp)
lw s3, -20(fp)
lw s4, -24(fp)
lw s5, -28(fp)
lw s6, -32(fp)
lw fp, 24(sp)
lw ra, 28(sp)
addi sp, sp, 32
jr ra
; main:
main:
; pmb 1
addi sp, sp, -8
sw ra, 4(sp) ; -4(fp)
sw fp, 0(sp) ; -8(fp)
addi fp, sp, 8
; init_params
addi sp, sp, -4
; t7 = 42
li a4, 42
; param_s t7
sw a4, 0(sp)
; call 0
call fib; t6 = returnval
mv a3, a0
; rtn 1
lw fp, 0(sp)
lw ra, 4(sp)
addi sp, sp, 8
jr ra
