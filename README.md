### University of Central Florida
### Department of Computer Science
### CGS 3269   Computer Architecture Concepts   Summer 2016

### Programming Project 3

***
<b><u>The Problem</b></u>

Using C programming language write a program that simulates the Tiny Machine Architecture. Your code must implement the basic instruction set that the Tiny Machine Architecture adheres to (LOAD[1], ADD[2], STORE[3], SUB[4], IN[5], OUT[6], END[7], JMP[8], SKIPZ[9]). Each piece of the architecture must be accurately represented in your code (Instruction Register, Program Counter, Memory Address Register, Data Memory, Memory Data Register, and Accumulator). Data Memory will be represented by a 0-9 array. Your Program Counter will begin at 10. 

For the sake of simplicity Program Memory and Data Memory may be implemented as separate arrays. 

Hint: Implementing a struct for your Instructions and an array of these structs as your Program Memory greatly simplifies this program.

<b>Example</b>:
```
typedef struct {
   int opCode, deviceOrAddress;  
} Instruction;  
Instruction programMemory[MAXPROGRAMSIZE];
```

<u><b>Input Specifications</b></u>

Your simulator must run from the command line with a single input file as a parameter to main. This file
will contain a sequence of instructions for your simulator to assemble (store in “program memory”) and
then run via the fetch/execute cycle.

<b>Example:</b>
```
 1 0  //LOAD 0
 4 1  //SUB 1
 3 0  //STORE 0
 6 7  //OUT 7
 1 1  //LOAD 1
 6 7  //OUT 7
 7 0  //END
```

<b><u>Output Specifications</u></b>

Your simulator should provide output according to the input file. Along with this output your program should provide status messages identifying details on the workings of your simulator. Output text does not have to reflect my example word-for-word, but please provide detail on the program as it runs in a readable format that does not conflict with the actual output of your simulator. After each instruction print the current state of the Program Counter, Accumulator, and Data Memory. The INPUT instruction is the only one that should prompt an interaction from the user.

<b>Example:</b>

```
Assembling Program…
Program Assembled.

Run.

PC = 10 | A = NULL | MEM = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

/* input value */
X
PC = 11 | A = X | MEM = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

/* outputting accumulator to screen */
X
PC = 12 | A = X | MEM = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

/* storing accumulator to memory location 0 */
PC = 13 | A = X | MEM = [X, 0, 0, 0, 0, 0, 0, 0, 0, 0]

… etc
 
 Program complete.
```

<b><u>Grading</u></b>

Your simulator will be graded on the above criteria. Your program should compile and run from the command line with one input file parameter. Please note that your program will not just be graded on whether or not it runs successfully; accurate simulation and a thorough demonstration of your understanding on the workings of this architecture will constitute a large portion of this grade. As that is the case it is in your best interest to comment your program in a concise and readable way. However, if your program does not run or compile the maximum points possible will be 50 (up to 25 may be recovered by debugging and demonstrating an understanding of your errors during TA’s office hours). 

<b>For instance, to implement instruction LOAD you must implement each step:</b>
```
/* PC <- PC + 1 */
/* MAR <- IR.ADDR */
/* MDR <- MEM[MAR] */
/* A <- MDR */ 
```
