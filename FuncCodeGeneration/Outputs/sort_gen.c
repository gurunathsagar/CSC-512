#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)
#define print(x) printf(x)
#include <assert.h>
int const N=2000;
int mem[2000];
#define top mem[65]
#define base mem[66]
#define jumpReg mem[67]
#define membase 68

int main()
 
{
// prologue before calling main
top = membase;
mem[top] = 0;
base = top + 1;
top = base + 82;
mainFunc:;

    mem[base+1] = 16;

// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 1;
base = top+4;
top = base+64;
goto populate_arrays;

label_1:;


// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 2;
base = top+4;
top = base+22;
goto print_arrays;

label_2:;
mem[base+1] = 16;mem[base+0] = 0;c12:;mem[base+3]=mem[base+1]-1;mem[base+4]=mem[base+0] < mem[base+3];mem[base+5]=mem[base+4];if(mem[base+5]) goto c13;goto c14;c13:;  mem[base+6]=0+mem[base+0];mem[base+7]=mem[base+0]+1;mem[base+8]=0+mem[base+7];mem[base+9]=mem[mem[base+6]] > mem[mem[base+8]];mem[base+10]=mem[base+9];  if (mem[base+10] ) goto c15;goto c16;c15:;mem[base+11]=0+mem[base+0];mem[base+2] = mem[mem[base+11]];mem[base+12]=mem[base+0]+1;mem[base+13]=0+mem[base+12];mem[base+14]=0+mem[base+0];mem[mem[base+14]] = mem[mem[base+13]];mem[base+15]=mem[base+0]+1;mem[base+16]=0+mem[base+15];mem[mem[base+16]] = mem[base+2];mem[base+0] = 0;goto c12;  c16:;mem[base+17]=mem[base+0]+1;mem[base+0] = mem[base+17]; goto c12;c14:;mem[base+0] = 0;c17:;mem[base+18]=mem[base+1]-1;mem[base+19]=mem[base+0] < mem[base+18];mem[base+20]=mem[base+19];if(mem[base+20]) goto c18;goto c19;c18:;  mem[base+21]=16+mem[base+0];mem[base+22]=mem[base+0]+1;mem[base+23]=16+mem[base+22];mem[base+24]=mem[mem[base+21]] > mem[mem[base+23]];mem[base+25]=mem[base+24];  if (mem[base+25] ) goto c20;goto c21;c20:;mem[base+26]=16+mem[base+0];mem[base+2] = mem[mem[base+26]];mem[base+27]=mem[base+0]+1;mem[base+28]=16+mem[base+27];mem[base+29]=16+mem[base+0];mem[mem[base+29]] = mem[mem[base+28]];mem[base+30]=mem[base+0]+1;mem[base+31]=16+mem[base+30];mem[mem[base+31]] = mem[base+2];mem[base+0] = 0;goto c17;  c21:;mem[base+32]=mem[base+0]+1;mem[base+0] = mem[base+32]; goto c17;c19:;mem[base+0] = 0;c22:;mem[base+33]=mem[base+1]-1;mem[base+34]=mem[base+0] < mem[base+33];mem[base+35]=mem[base+34];if(mem[base+35]) goto c23;goto c24;c23:;  mem[base+36]=32+mem[base+0];mem[base+37]=mem[base+0]+1;mem[base+38]=32+mem[base+37];mem[base+39]=mem[mem[base+36]] > mem[mem[base+38]];mem[base+40]=mem[base+39];  if (mem[base+40] ) goto c25;goto c26;c25:;mem[base+41]=0+mem[base+0];mem[base+2] = mem[mem[base+41]];mem[base+42]=mem[base+0]+1;mem[base+43]=32+mem[base+42];mem[base+44]=32+mem[base+0];mem[mem[base+44]] = mem[mem[base+43]];mem[base+45]=mem[base+0]+1;mem[base+46]=32+mem[base+45];mem[mem[base+46]] = mem[base+2];mem[base+0] = 0;goto c22;  c26:;mem[base+47]=mem[base+0]+1;mem[base+0] = mem[base+47]; goto c22;c24:;mem[base+0] = 0;c27:;mem[base+48]=mem[base+1]-1;mem[base+49]=mem[base+0] < mem[base+48];mem[base+50]=mem[base+49];if(mem[base+50]) goto c28;goto c29;c28:;  mem[base+51]=48+mem[base+0];mem[base+52]=mem[base+0]+1;mem[base+53]=48+mem[base+52];mem[base+54]=mem[mem[base+51]] > mem[mem[base+53]];mem[base+55]=mem[base+54];  if (mem[base+55] ) goto c30;goto c31;c30:;mem[base+56]=48+mem[base+0];mem[base+2] = mem[mem[base+56]];mem[base+57]=mem[base+0]+1;mem[base+58]=48+mem[base+57];mem[base+59]=48+mem[base+0];mem[mem[base+59]] = mem[mem[base+58]];mem[base+60]=mem[base+0]+1;mem[base+61]=48+mem[base+60];mem[mem[base+61]] = mem[base+2];mem[base+0] = 0;goto c27;  c31:;mem[base+62]=mem[base+0]+1;mem[base+0] = mem[base+62]; goto c27;c29:;

// prologue to func
mem[top+0] = base;
mem[top+1] = top;
mem[top+3] = 3;
base = top+4;
top = base+22;
goto print_arrays;

label_3:;


// epilogue of main
jumpReg = mem[base - 1];
goto jumpTable;

populate_arrays:;
mem[base+0]=0+0;mem[mem[base+0]] = 0;mem[base+1]=16+0;mem[mem[base+1]] = 15;mem[base+2]=32+0;mem[mem[base+2]] = 5;mem[base+3]=48+0;mem[mem[base+3]] = 13;mem[base+4]=0+1;mem[mem[base+4]] = 1;mem[base+5]=16+1;mem[mem[base+5]] = 14;mem[base+6]=32+1;mem[mem[base+6]] = 5;mem[base+7]=48+1;mem[mem[base+7]] = 9;mem[base+8]=0+2;mem[mem[base+8]] = 2;mem[base+9]=16+2;mem[mem[base+9]] = 13;mem[base+10]=32+2;mem[mem[base+10]] = 5;mem[base+11]=48+2;mem[mem[base+11]] = 12;mem[base+12]=0+3;mem[mem[base+12]] = 3;mem[base+13]=16+3;mem[mem[base+13]] = 12;mem[base+14]=32+3;mem[mem[base+14]] = 5;mem[base+15]=48+3;mem[mem[base+15]] = 1;mem[base+16]=0+4;mem[mem[base+16]] = 4;mem[base+17]=16+4;mem[mem[base+17]] = 11;mem[base+18]=32+4;mem[mem[base+18]] = 5;mem[base+19]=48+4;mem[mem[base+19]] = 0;mem[base+20]=0+5;mem[mem[base+20]] = 5;mem[base+21]=16+5;mem[mem[base+21]] = 10;mem[base+22]=32+5;mem[mem[base+22]] = 5;mem[base+23]=48+5;mem[mem[base+23]] = 14;mem[base+24]=0+6;mem[mem[base+24]] = 6;mem[base+25]=16+6;mem[mem[base+25]] = 9;mem[base+26]=32+6;mem[mem[base+26]] = 5;mem[base+27]=48+6;mem[mem[base+27]] = 3;mem[base+28]=0+7;mem[mem[base+28]] = 7;mem[base+29]=16+7;mem[mem[base+29]] = 8;mem[base+30]=32+7;mem[mem[base+30]] = 5;mem[base+31]=48+7;mem[mem[base+31]] = 2;mem[base+32]=0+8;mem[mem[base+32]] = 8;mem[base+33]=16+8;mem[mem[base+33]] = 7;mem[base+34]=32+8;mem[mem[base+34]] = 5;mem[base+35]=48+8;mem[mem[base+35]] = 11;mem[base+36]=0+9;mem[mem[base+36]] = 9;mem[base+37]=16+9;mem[mem[base+37]] = 6;mem[base+38]=32+9;mem[mem[base+38]] = 5;mem[base+39]=48+9;mem[mem[base+39]] = 8;mem[base+40]=0+10;mem[mem[base+40]] = 10;mem[base+41]=16+10;mem[mem[base+41]] = 5;mem[base+42]=32+10;mem[mem[base+42]] = 5;mem[base+43]=48+10;mem[mem[base+43]] = 6;mem[base+44]=0+11;mem[mem[base+44]] = 11;mem[base+45]=16+11;mem[mem[base+45]] = 4;mem[base+46]=32+11;mem[mem[base+46]] = 5;mem[base+47]=48+11;mem[mem[base+47]] = 4;mem[base+48]=0+12;mem[mem[base+48]] = 12;mem[base+49]=16+12;mem[mem[base+49]] = 3;mem[base+50]=32+12;mem[mem[base+50]] = 5;mem[base+51]=48+12;mem[mem[base+51]] = 5;mem[base+52]=0+13;mem[mem[base+52]] = 13;mem[base+53]=16+13;mem[mem[base+53]] = 2;mem[base+54]=32+13;mem[mem[base+54]] = 5;mem[base+55]=48+13;mem[mem[base+55]] = 10;mem[base+56]=0+14;mem[mem[base+56]] = 14;mem[base+57]=16+14;mem[mem[base+57]] = 1;mem[base+58]=32+14;mem[mem[base+58]] = 5;mem[base+59]=48+14;mem[mem[base+59]] = 7;mem[base+60]=0+15;mem[mem[base+60]] = 15;mem[base+61]=16+15;mem[mem[base+61]] = 0;mem[base+62]=32+15;mem[mem[base+62]] = 5;mem[base+63]=48+15;mem[mem[base+63]] = 15;

// epilogue of Func
top = mem[base-3];
jumpReg = mem[base - 1];
base = mem[base-4];
goto jumpTable;

print_arrays:;
 mem[base+1] = 16;
    print("Array_1:\n");mem[base+0] = 0;c0:;mem[base+2]=mem[base+0] < mem[base+1];mem[base+3]=mem[base+2];if(mem[base+3]) goto c1;goto c2;c1:; mem[base+4]=0+mem[base+0];
	write(mem[mem[base+4]]);mem[base+5]=mem[base+0]+1;mem[base+0] = mem[base+5]; goto c0;c2:;

    print("\nArray_2:\n");mem[base+0] = 0;c3:;mem[base+6]=mem[base+0] < mem[base+1];mem[base+7]=mem[base+6];if(mem[base+7]) goto c4;goto c5;c4:; mem[base+8]=16+mem[base+0];
	write(mem[mem[base+8]]);mem[base+9]=mem[base+0]+1;mem[base+0] = mem[base+9]; goto c3;c5:;

    print("\nArray_3:\n");mem[base+0] = 0;c6:;mem[base+10]=mem[base+0] < mem[base+1];mem[base+11]=mem[base+10];if(mem[base+11]) goto c7;goto c8;c7:; mem[base+12]=32+mem[base+0];
	write(mem[mem[base+12]]);mem[base+13]=mem[base+0]+1;mem[base+0] = mem[base+13]; goto c6;c8:;

    print("\nArray_4:\n");mem[base+0] = 0;c9:;mem[base+14]=mem[base+0] < mem[base+1];mem[base+15]=mem[base+14];if(mem[base+15]) goto c10;goto c11;c10:; mem[base+16]=48+mem[base+0];
	write(mem[mem[base+16]]);mem[base+17]=mem[base+0]+1;mem[base+0] = mem[base+17]; goto c9;c11:;
    print("\n");

// epilogue of Func
top = mem[base-3];
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
 default: assert(0);
}

}