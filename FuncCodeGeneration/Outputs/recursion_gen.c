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
top = base + 2;
mainFunc:;

    
    read(mem[base+0]);

// prologue to func
mem[top+0] = mem[base+0];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 2;
base = top+5;
top = base+10;
goto recursionsum;

label_2:;
mem[base+1]=mem[top+3];
    write(mem[base+1]);

// epilogue of main
jumpReg = mem[base - 1];
goto jumpTable;

recursionsum:;
mem[base+0]=mem[base-4-1];
mem[base+1]=mem[base+0] == 0;mem[base+2]=mem[base+1];if (mem[base+2] ) goto c0;goto c1;c0:;mem[base+3]=0;
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+3];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

c1:;mem[base+4]=mem[base+0]-1;

// prologue to func
mem[top+0] = mem[base+4];
mem[top+1] = base;
mem[top+2] = top;
mem[top+4] = 1;
base = top+5;
top = base+10;
goto recursionsum;

label_1:;
mem[base+5]=mem[top+3];mem[base+6]=mem[base+0]+mem[base+5];mem[base+7]=mem[base+6]; 
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+7];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+7];
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
 default: assert(0);
}

}