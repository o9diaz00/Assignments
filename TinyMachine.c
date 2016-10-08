#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

//Function used to count the amount of lines in text file
int getProgramSize();
//Function used to parse instruction into machine code
void toMachine();

//Global variable used to store the aforementioned
int MAXPROGRAMSIZE;
//Tiny Machine variables
int programCounter = 10;
int instructionRegister = 0;
int memoryAddressRegister = 0;
int dataMemory[9];
int memoryDataRegister = 0;
int accumulator = 0;

//Creating struct
typedef struct
{
    int opCode;
    int deviceOrAddress;
}Instruction;

int main(int argc, char *argv[])
{
    //This is where we get the size of the program and define it
    MAXPROGRAMSIZE = getProgramSize(fopen(argv[argc-1], "r"));
    //This creates our struct array based on the amount of instructions
    Instruction programMemory[MAXPROGRAMSIZE];

    //Reading a file line by line
    FILE *file = fopen(argv[argc-1], "r");
    char fileBuffer[MAXPROGRAMSIZE];
    int i = 0;

    if (file == NULL)
    {
        printf("Error opening file!");
        exit(0);
    }

    //Had to do some manipulation and checks here to make sure I only store the 1st and 3rd char of every line
    while(fgets(fileBuffer, sizeof(MAXPROGRAMSIZE), file)!=NULL)
    {
        if ((*fileBuffer == '\n' || *fileBuffer != ' ') && isdigit(*fileBuffer) && (int)fileBuffer[2] != 0)
        {
            programMemory[i].opCode = atoi(&fileBuffer[0]);
            programMemory[i].deviceOrAddress = atoi(&fileBuffer[2]);
            i += 1;
        }
    }
    //Need to close the file
    fclose(file);

    //Just some text output
    printf("\n\nOtis Diaz --- CGS3269\n==============================\nTiny Machine Simulator\n==============================\n\n");
    printf("Assembling program...\n\n");
    printf("Program assembled\n\n");

    //Get the instruction from our struct and figure out what they mean by passing it into our parser function
    for (i=(programCounter/10)-1; i<sizeof(programMemory); i+=1)
    { toMachine(programMemory[i].opCode, programMemory[i].deviceOrAddress); }

    printf("\n\nProgram concluded...\n");

    return 0;
}

void toMachine(int a, int b)
{
    int i;
    //We need to parse the opcode and device/memoryaddress and see what is being asked of us to do and where
    //All I can say is.... I am not completely sure that the Tiny Machine works like this
    //These were basically conjured from the presented LOAD instructions
    //Good ole C, only way I know how to display each index of an array is to loop through it all
    switch (a)
    {
    case 1:
        //LOAD
        printf("\n>> Loading from address [%d]... <<\n", b);
        instructionRegister = b;
        memoryAddressRegister = instructionRegister;
        memoryDataRegister = dataMemory[memoryAddressRegister];
        accumulator = memoryDataRegister;
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        programCounter += 1;
        break;
    case 2:
        //ADD
        printf("\n>> Adding accumulator and value obtain from address [%d]... <<\n", b);
        instructionRegister = b;
        memoryAddressRegister = instructionRegister;
        memoryDataRegister = dataMemory[memoryAddressRegister];
        accumulator += memoryDataRegister;
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        programCounter += 1;
        break;
    case 3:
        //STORE
        printf("\n>> Storing accumulator value into memory... <<\n");
        memoryDataRegister = accumulator;
        instructionRegister = b;
        memoryAddressRegister = instructionRegister;
        dataMemory[memoryAddressRegister] = memoryDataRegister;
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        programCounter += 1;
        break;
    case 4:
        //SUB
        printf("\n>> Subtracting memory address value [%d] from accumulator... <<\n", b);
        instructionRegister = b;
        memoryAddressRegister = instructionRegister;
        memoryDataRegister = dataMemory[memoryAddressRegister];
        accumulator -= memoryDataRegister;
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        programCounter += 1;
        break;
    case 5:
        //GET INPUT
        printf("\nPlease input a number: ");
        scanf("%d", &accumulator);
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        programCounter += 1;
        break;
    case 6:
        //OUTPUT TO SCREEN
        printf("\n>> Accumulator current value = %d <<\n", accumulator);
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        programCounter += 1;
        break;
    case 7:
        //END PROGRAM
        printf("\nProgram concluded...\n");
        exit(1);
    case 8:
        //JMP
        // *Jump to address
        printf("\n>> Setting program counter to %d... <<\n", b);
        programCounter = b;
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        break;
    case 9:
        //SKIPZ
        // *Check if acc is 0, if it is skip next instruction, else proceed as normal
        printf("\n>> Skipping the next instruction... <<\n");
        if (accumulator == 0)
        { programCounter += 2; }
        else
        { programCounter += 1; }
        printf("\nPC: %d | A: %d | MEM: [", programCounter, accumulator);
        for (i=0; i<9; i+=1)
        {
            printf("%d,", dataMemory[i]);
        }
        printf("]\n");
        break;
    default:
        printf("\nThere was an error parsing that opcode!  Exiting program\n");
        exit(0);
    }
}

/*
 * Don't think we're told how many lines/size of input file
 * Just going to use some extra space for the time being
 * Get the amount of lines in the file and use that for determining
 * The amount of structs to create
 * Is this foolproof?  Hell if I know, seems to work well with my test file
*/
int getProgramSize(FILE *file)
{
    char fileBuffer[100];
    int length = 0;
    if (file == NULL)
    {
        printf("Error opening file!");
        exit(0);
    }

    //This is where I kinda have to buck down and choose an arbitrary number... 1000 might be overkill but w/e
    while(fgets(fileBuffer, 1000 ,file)!=NULL)
    { length += 1; }

    fclose(file);

    return length;
}
