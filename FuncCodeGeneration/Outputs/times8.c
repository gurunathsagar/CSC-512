#include <stdio.h>
#define read(x) scanf("%d",&x)
#define write(x) printf("%d\n",x)

int sqsum(int a, int b) {
  return (a*a)+(b*b);
}

int times_eight(int a) {
  return sqsum(sqsum(sqsum(a,a),sqsum(a,a)), sqsum(sqsum(a,a),sqsum(a,a)));
}

int main() {
    int a, b;
    read(a);
    write(times_eight(a));
}
