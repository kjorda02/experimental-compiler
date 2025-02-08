.global _start
.text

_start:
    call main
    # Exit syscall
    li a7, 93       # exit syscall number for RV32
    li a0, 0        # exit code 0
    ecall

# main:
main:
# pmb 3
addi sp, sp, -20
sw ra, 16(sp) # -4(fp)
sw fp, 12(sp) # -8(fp)
addi fp, sp, 20
sw s1, -12(fp)
sw s2, -16(fp)
sw s3, -20(fp)
# init_params
addi sp, sp, -0
# call 2
call readNum
addi sp, sp, 0
# t0 = returnval
mv s1, a0
# a = t0
mv s2, s1
# init_params
addi sp, sp, -4
# param_s a
sw s2, 0(sp)
# call 1
call printMatrix
addi sp, sp, 4
# t1 = 10
li s3, 10
# print t1
# Start print char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
sb s3, 16(sp)
li a0, 1
addi a1, sp, 16
li a2, 1
li a7, 64
ecall
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End print char sequence
# rtn 3
lw s1, -12(fp)
lw s2, -16(fp)
lw s3, -20(fp)
lw fp, 12(sp)
lw ra, 16(sp)
addi sp, sp, 20
jr ra
# printMatrix:
printMatrix:
# pmb 1
addi sp, sp, -1674
sw ra, 1670(sp) # -4(fp)
sw fp, 1666(sp) # -8(fp)
addi fp, sp, 1674
sw s1, -12(fp)
sw s2, -16(fp)
sw s3, -20(fp)
sw s4, -24(fp)
sw s5, -28(fp)
sw s6, -32(fp)
sw s7, -36(fp)
sw s8, -40(fp)
sw s9, -44(fp)
sw s10, -48(fp)
sw s11, -52(fp)
# i = 0
li s1, 0
# e0:
e0:
# if i < max goto e2
lw a1, 0(fp)
blt s1, a1, e2
# t2 = 0
li s5, 0
# goto e3
j e3
# e2:
e2:
# t2 = -1
li s5, -1
# e3:
e3:
# if t2 == 0 goto e1
li a0, 0
beq s5, a0, e1
# j = 0
li s2, 0
# e4:
e4:
# if j < max goto e6
lw a1, 0(fp)
blt s2, a1, e6
# t3 = 0
li s6, 0
# goto e7
j e7
# e6:
e6:
# t3 = -1
li s6, -1
# e7:
e7:
# if t3 == 0 goto e5
li a0, 0
beq s6, a0, e5
# t4 = i * 3
li a1, 3
mul s7, s1, a1
# t5 = t4 + j
add s8, s7, s2
# t6 = &matrix
addi s9, fp, -1674
# t6 += 80*i
li a0, 80
mul a0, a0, s1
add s9, s9, a0
# t6 += 4*j
li a0, 4
mul a0, a0, s2
add s9, s9, a0
# t6[0] = t5
addi a0, s9, 0
sw s8, 0(a0)
# t7 = j + 1
addi s10, s2, 1
# j = t7
mv s2, s10
# goto e4
j e4
# e5:
e5:
# t8 = i + 1
addi s11, s1, 1
# i = t8
mv s1, s11
# goto e0
j e0
# e1:
e1:
# i = 0
li s3, 0
# e8:
e8:
# if i < max goto e10
lw a1, 0(fp)
blt s3, a1, e10
# t9 = 0
li a0, 0
sw a0, -53(fp)
# goto e11
j e11
# e10:
e10:
# t9 = -1
li a0, -1
sw a0, -53(fp)
# e11:
e11:
# if t9 == 0 goto e9
li a0, 0
lw a1, -53(fp)
beq a1, a0, e9
# j = 0
li s4, 0
# e12:
e12:
# if j < max goto e14
lw a1, 0(fp)
blt s4, a1, e14
# t10 = 0
li a0, 0
sw a0, -54(fp)
# goto e15
j e15
# e14:
e14:
# t10 = -1
li a0, -1
sw a0, -54(fp)
# e15:
e15:
# if t10 == 0 goto e13
li a0, 0
lw a1, -54(fp)
beq a1, a0, e13
# init_params
addi sp, sp, -0
# t11 = &matrix
addi a0, fp, -1674
sw a0, -58(fp)
# t11 += 80*i
li a0, 80
mul a0, a0, s3
lw a1, -58(fp)
add a0, a1, a0
sw a0, -58(fp)
# t11 += 4*j
li a0, 4
mul a0, a0, s4
lw a1, -58(fp)
add a0, a1, a0
sw a0, -58(fp)
# t11 = t11[0]
lw a1, -58(fp)
addi a0, a1, 0
lw a0, 0(a0)
sw a0, -58(fp)
# param_s t11
lw a0, -58(fp)
mv a1, a0
# call 0
call printNum
addi sp, sp, 0
# t12 = 9
li a0, 9
sw a0, -62(fp)
# print t12
# Start print char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
lw a0, -62(fp)
sb a0, 16(sp)
li a0, 1
addi a1, sp, 16
li a2, 1
li a7, 64
ecall
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End print char sequence
# t13 = j + 1
addi a0, s4, 1
sw a0, -66(fp)
# j = t13
lw a0, -66(fp)
mv s4, a0
# goto e12
j e12
# e13:
e13:
# t14 = 10
li a0, 10
sw a0, -70(fp)
# print t14
# Start print char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
lw a0, -70(fp)
sb a0, 16(sp)
li a0, 1
addi a1, sp, 16
li a2, 1
li a7, 64
ecall
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End print char sequence
# t15 = i + 1
addi a0, s3, 1
sw a0, -74(fp)
# i = t15
lw a0, -74(fp)
mv s3, a0
# goto e8
j e8
# e9:
e9:
# rtn 1
lw s1, -12(fp)
lw s2, -16(fp)
lw s3, -20(fp)
lw s4, -24(fp)
lw s5, -28(fp)
lw s6, -32(fp)
lw s7, -36(fp)
lw s8, -40(fp)
lw s9, -44(fp)
lw s10, -48(fp)
lw s11, -52(fp)
lw fp, 1666(sp)
lw ra, 1670(sp)
addi sp, sp, 1674
jr ra
# printNum:
printNum:
# pmb 0
addi sp, sp, -88
sw ra, 84(sp) # -4(fp)
sw fp, 80(sp) # -8(fp)
addi fp, sp, 88
sw s1, -12(fp)
sw s2, -16(fp)
sw s3, -20(fp)
sw s4, -24(fp)
sw s5, -28(fp)
sw s6, -32(fp)
sw s7, -36(fp)
sw s8, -40(fp)
sw s9, -44(fp)
# if n < 0 goto e17
li a0, 0
blt a1, a0, e17
# t16 = 0
li a5, 0
# goto e18
j e18
# e17:
e17:
# t16 = -1
li a5, -1
# e18:
e18:
# if t16 == 0 goto e16
li a0, 0
beq a5, a0, e16
# t17 = 45
li a6, 45
# print t17
# Start print char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
sb a6, 16(sp)
li a0, 1
addi a1, sp, 16
li a2, 1
li a7, 64
ecall
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End print char sequence
# t18 = 0 - n
li a0, 0
sub t0, a0, a1
# n = t18
mv a1, t0
# e16:
e16:
# if n == 0 goto e20
li a0, 0
beq a1, a0, e20
# t19 = 0
li t1, 0
# goto e21
j e21
# e20:
e20:
# t19 = -1
li t1, -1
# e21:
e21:
# if t19 == 0 goto e19
li a0, 0
beq t1, a0, e19
# t20 = 48
li t2, 48
# print t20
# Start print char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
sb t2, 16(sp)
li a0, 1
addi a1, sp, 16
li a2, 1
li a7, 64
ecall
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End print char sequence
# e19:
e19:
# temp = n
mv a2, a1
# numDigits = 0
li a3, 0
# e22:
e22:
# if temp > 0 goto e24
li a0, 0
bgt a2, a0, e24
# t21 = 0
li t3, 0
# goto e25
j e25
# e24:
e24:
# t21 = -1
li t3, -1
# e25:
e25:
# if t21 == 0 goto e23
li a0, 0
beq t3, a0, e23
# t22 = numDigits + 1
addi t4, a3, 1
# numDigits = t22
mv a3, t4
# t23 = temp / 10
li a7, 10
div t5, a2, a7
# temp = t23
mv a2, t5
# goto e22
j e22
# e23:
e23:
# i = 0
li a4, 0
# temp = n
mv a2, a1
# e26:
e26:
# if temp > 0 goto e28
li a0, 0
bgt a2, a0, e28
# t24 = 0
li t6, 0
# goto e29
j e29
# e28:
e28:
# t24 = -1
li t6, -1
# e29:
e29:
# if t24 == 0 goto e27
li a0, 0
beq t6, a0, e27
# t25 = temp / 10
li a7, 10
div s1, a2, a7
# t26 = t25 * 10
li a7, 10
mul s2, s1, a7
# t27 = temp - t26
sub s3, a2, s2
# t28 = &digits
addi s4, fp, -88
# t28 += 4*i
li a0, 4
mul a0, a0, a4
add s4, s4, a0
# t28[0] = t27
addi a0, s4, 0
sw s3, 0(a0)
# t29 = temp / 10
li a7, 10
div s5, a2, a7
# temp = t29
mv a2, s5
# t30 = i + 1
addi s6, a4, 1
# i = t30
mv a4, s6
# goto e26
j e26
# e27:
e27:
# e30:
e30:
# if numDigits > 0 goto e32
li a0, 0
bgt a3, a0, e32
# t31 = 0
li s7, 0
# goto e33
j e33
# e32:
e32:
# t31 = -1
li s7, -1
# e33:
e33:
# if t31 == 0 goto e31
li a0, 0
beq s7, a0, e31
# t32 = numDigits - 1
addi s8, a3, -1
# numDigits = t32
mv a3, s8
# t33 = &digits
addi s9, fp, -88
# t33 += 4*numDigits
li a0, 4
mul a0, a0, a3
add s9, s9, a0
# t33 = t33[0]
addi a0, s9, 0
lw s9, 0(a0)
# t34 = t33 + 48
addi a0, s9, 48
sw a0, -48(fp)
# print t34
# Start print char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
lw a0, -48(fp)
sb a0, 16(sp)
li a0, 1
addi a1, sp, 16
li a2, 1
li a7, 64
ecall
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End print char sequence
# goto e30
j e30
# e31:
e31:
# rtn 0
lw s1, -12(fp)
lw s2, -16(fp)
lw s3, -20(fp)
lw s4, -24(fp)
lw s5, -28(fp)
lw s6, -32(fp)
lw s7, -36(fp)
lw s8, -40(fp)
lw s9, -44(fp)
lw fp, 80(sp)
lw ra, 84(sp)
addi sp, sp, 88
jr ra
# readNum:
readNum:
# pmb 2
addi sp, sp, -49
sw ra, 45(sp) # -4(fp)
sw fp, 41(sp) # -8(fp)
addi fp, sp, 49
sw s1, -12(fp)
sw s2, -16(fp)
sw s3, -20(fp)
sw s4, -24(fp)
sw s5, -28(fp)
sw s6, -32(fp)
sw s7, -36(fp)
sw s8, -40(fp)
sw s9, -44(fp)
sw s10, -48(fp)
# result = 0
li a1, 0
# c = 0
li a2, 0
# isNegative = 0
li a3, 0
# readingWhitespace = -1
li a4, -1
# e34:
e34:
# if readingWhitespace == 0 goto e35
li a7, 0
beq a4, a7, e35
# t35= input 
# Start input char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
li a0, 0
addi a1, sp, 16
li a2, 1
li a7, 63
ecall
lb a5, 16(sp)
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End input char sequence
# c = t35
mv a2, a5
# if c != 32 goto e37
li a7, 32
bne a2, a7, e37
# t38 = 0
li t2, 0
# goto e38
j e38
# e37:
e37:
# t38 = -1
li t2, -1
# e38:
e38:
# if c != 9 goto e39
li a7, 9
bne a2, a7, e39
# t39 = 0
li t3, 0
# goto e40
j e40
# e39:
e39:
# t39 = -1
li t3, -1
# e40:
e40:
# t40 = t38 and t39
and t4, t2, t3
# if c != 10 goto e41
li a7, 10
bne a2, a7, e41
# t41 = 0
li t5, 0
# goto e42
j e42
# e41:
e41:
# t41 = -1
li t5, -1
# e42:
e42:
# t42 = t40 and t41
and t6, t4, t5
# if t42 == 0 goto e36
li a7, 0
beq t6, a7, e36
# readingWhitespace = 0
li a4, 0
# e36:
e36:
# goto e34
j e34
# e35:
e35:
# if c == 45 goto e44
li a7, 45
beq a2, a7, e44
# t43 = 0
li s1, 0
# goto e45
j e45
# e44:
e44:
# t43 = -1
li s1, -1
# e45:
e45:
# if t43 == 0 goto e43
li a7, 0
beq s1, a7, e43
# isNegative = -1
li a3, -1
# t36= input 
# Start input char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
li a0, 0
addi a1, sp, 16
li a2, 1
li a7, 63
ecall
lb a6, 16(sp)
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End input char sequence
# c = t36
mv a2, a6
# e43:
e43:
# readingDigits = -1
li t0, -1
# e46:
e46:
# if readingDigits == 0 goto e47
li a7, 0
beq t0, a7, e47
# if c >= 48 goto e49
li a7, 48
bge a2, a7, e49
# t44 = 0
li s2, 0
# goto e50
j e50
# e49:
e49:
# t44 = -1
li s2, -1
# e50:
e50:
# if c <= 57 goto e51
li a7, 57
ble a2, a7, e51
# t45 = 0
li s3, 0
# goto e52
j e52
# e51:
e51:
# t45 = -1
li s3, -1
# e52:
e52:
# t46 = t44 and t45
and s4, s2, s3
# if t46 == 0 goto e48
li a7, 0
beq s4, a7, e48
# t47 = result * 10
li s10, 10
mul s5, a1, s10
# t48 = c - 48
addi s6, a2, -48
# t49 = t47 + t48
add s7, s5, s6
# result = t49
mv a1, s7
# t37= input 
# Start input char sequence
addi sp, sp, -24
sw a0, 0(sp)
sw a1, 4(sp)
sw a2, 8(sp)
sw a7, 12(sp)
li a0, 0
addi a1, sp, 16
li a2, 1
li a7, 63
ecall
lb t1, 16(sp)
lw a0, 0(sp)
lw a1, 4(sp)
lw a2, 8(sp)
lw a7, 12(sp)
addi sp, sp, 24
# End input char sequence
# c = t37
mv a2, t1
# e48:
e48:
# if c < 48 goto e54
li a7, 48
blt a2, a7, e54
# t50 = 0
li s8, 0
# goto e55
j e55
# e54:
e54:
# t50 = -1
li s8, -1
# e55:
e55:
# if c > 57 goto e56
li a7, 57
bgt a2, a7, e56
# t51 = 0
li s9, 0
# goto e57
j e57
# e56:
e56:
# t51 = -1
li s9, -1
# e57:
e57:
# t52 = t50 || t51
or a7, s8, s9
sw a7, -45(fp)
# if t52 == 0 goto e53
li a7, 0
lw s10, -45(fp)
beq s10, a7, e53
# readingDigits = 0
li t0, 0
# e53:
e53:
# goto e46
j e46
# e47:
e47:
# if isNegative == 0 goto e58
li a7, 0
beq a3, a7, e58
# t53 = 0 - result
li a7, 0
sub a7, a7, a1
sw a7, -49(fp)
# result = t53
lw a7, -49(fp)
mv a1, a7
# e58:
e58:
# returnval = result
mv a0, a1
# rtn 2
lw s1, -12(fp)
lw s2, -16(fp)
lw s3, -20(fp)
lw s4, -24(fp)
lw s5, -28(fp)
lw s6, -32(fp)
lw s7, -36(fp)
lw s8, -40(fp)
lw s9, -44(fp)
lw s10, -48(fp)
lw fp, 41(sp)
lw ra, 45(sp)
addi sp, sp, 49
jr ra
