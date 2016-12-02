#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#include <assert.h>
int const N=2000;
int mem[2000];
#define top mem[1]
#define base mem[2]
#define jumpReg mem[3]
#define membase 4

int main()
 {
// prologue before calling main
top = membase;
mem[top] = 0;
base = top + 1;
top = base + 3;
mainFunc:;

    
    read(mem[base+0]);

// prologue to func
mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 8;
base = top+5;
top = base+16;
goto times_eight;

label_8:;
mem[base+2]=mem[top+3];
    write(mem[base+2]);

// epilogue of main
jumpReg = mem[base - 1];
goto jumpTable;

sqsum:;
mem[base+0]=mem[base-4-1];
mem[base+1]=mem[base+0]*mem[base+0];mem[base+2]=mem[base-4-2];
mem[base+3]=mem[base+2]*mem[base+2];mem[base+4]=mem[base+1]+mem[base+3];mem[base+5]=mem[base+4];
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+5];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+5];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

times_eight:;
mem[base+0]=mem[base-4-1];


// prologue to func
mem[top+0] = mem[base+0];
mem[top+1] = mem[base+0];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 1;
base = top+6;
top = base+12;
goto sqsum;

label_1:;
mem[base+1]=mem[top+4];

// prologue to func
mem[top+0] = mem[base+0];
mem[top+1] = mem[base+0];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 2;
base = top+6;
top = base+12;
goto sqsum;

label_2:;
mem[base+2]=mem[top+4];

// prologue to func
mem[top+0] = mem[base+2];
mem[top+1] = mem[base+1];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 3;
base = top+6;
top = base+12;
goto sqsum;

label_3:;
mem[base+3]=mem[top+4];

// prologue to func
mem[top+0] = mem[base+0];
mem[top+1] = mem[base+0];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 4;
base = top+6;
top = base+12;
goto sqsum;

label_4:;
mem[base+4]=mem[top+4];

// prologue to func
mem[top+0] = mem[base+0];
mem[top+1] = mem[base+0];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 5;
base = top+6;
top = base+12;
goto sqsum;

label_5:;
mem[base+5]=mem[top+4];

// prologue to func
mem[top+0] = mem[base+5];
mem[top+1] = mem[base+4];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 6;
base = top+6;
top = base+12;
goto sqsum;

label_6:;
mem[base+6]=mem[top+4];

// prologue to func
mem[top+0] = mem[base+6];
mem[top+1] = mem[base+3];
mem[top+2] = base;
mem[top+3] = top;
mem[top+5] = 7;
base = top+6;
top = base+12;
goto sqsum;

label_7:;
mem[base+7]=mem[top+4];mem[base+8]=mem[base+7];
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+8];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+8];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;


// Jump table
jumpTable:
 switch(jumpReg) {
 case 0: return(0);
 case 1:
   goto label_1;
 case 2:
   goto label_2;
 case 3:
   goto label_3;
 case 4:
   goto label_4;
 case 5:
   goto label_5;
 case 6:
   goto label_6;
 case 7:
   goto label_7;
 case 8:
   goto label_8;
 default: assert(0);
}

}