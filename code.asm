addi $t7, $gp, 64
addi $t0, $0, 127
addi $t3, $0, 200
sw $t0, 20($t7)
sw $t3, 24($t7)
lw $t1, 20($t7)
lw $t2, 24($t7)
add $t4, $t3, $t1