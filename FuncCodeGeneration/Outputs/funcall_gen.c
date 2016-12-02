#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
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

    

// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 7;
base = top+4;
top = base+3;
goto a;

label_7:;
mem[base+1]=mem[top+2];mem[base+0] = mem[base+1];

    print("I calculate the answer to be: ");
    write(mem[base+0]);

// epilogue of main
jumpReg = mem[base - 1];
goto jumpTable;

a:;


// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 6;
base = top+4;
top = base+3;
goto b;

label_6:;
mem[base+0]=mem[top+2];mem[base+1]=mem[base+0]+1;mem[base+2]=mem[base+1]; 
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

b:;


// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 5;
base = top+4;
top = base+3;
goto c;

label_5:;
mem[base+0]=mem[top+2];mem[base+1]=mem[base+0]+1;mem[base+2]=mem[base+1]; 
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

c:;


// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 4;
base = top+4;
top = base+3;
goto d;

label_4:;
mem[base+0]=mem[top+2];mem[base+1]=mem[base+0]+1;mem[base+2]=mem[base+1]; 
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

d:;


// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 3;
base = top+4;
top = base+3;
goto e;

label_3:;
mem[base+0]=mem[top+2];mem[base+1]=mem[base+0]+1;mem[base+2]=mem[base+1]; 
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

e:;


// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 2;
base = top+4;
top = base+3;
goto f;

label_2:;
mem[base+0]=mem[top+2];mem[base+1]=mem[base+0]+1;mem[base+2]=mem[base+1]; 
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

f:;


// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 1;
base = top+4;
top = base+1;
goto g;

label_1:;
mem[base+0]=mem[top+2];mem[base+1]=mem[base+0]+1;mem[base+2]=mem[base+1]; 
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+2];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

g:;
mem[base+0]=1;
// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+0];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;



// epilogue of Func
top = mem[base-3];
mem[base-2] = mem[base+0];
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
 default: assert(0);
}

}