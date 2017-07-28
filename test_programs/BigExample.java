import java.util.*;

//Test input.
public class BigExample {	
	public static void main (String[] args) {		
		int[] hooray = new int[50];
		while(hooray[2] <= 20){
			hooray[2]+=3;
			for(int x = 0; x < 10; x++){
				int y = x + 1;
				do{
					if(x > 0){
						hooray[x] = x + hooray[x-1];
					}else{
						hooray[x]++;
						System.out.println("Hello World!");
						Random r = new Random();
						int abba = r.nextInt(3)+1;
						Animal a = new Animal();
						
						//The if statements are negated then used as forward branches!
						//How should the opcode heuristic operate?
						if(a != null){
							a.talk();
						}
						
						if(new Animal() == a){
							a.talk();
						}

						if(x == 2){
							a.talk();
						}
						if(x == 4){
							a.talk();
						}
						
						if(x <= 0){
							a.talk();
						}
						if(x < 0){
							a.talk();
						}
						if(0 >= x){
							a.talk();
						}
						if(0 > x){
							a.talk();
						}
						
						
						if(x >= 0){
							a.talk();
						}
						if(x > 0){
							a.talk();
						}
						if(0 <= x){
							a.talk();
						}
						if(0 < x){
							a.talk();
						}
						switch(abba){
							case 3:
								System.out.println("THREE!");
								break;
							case 2:
								System.out.println("TWO!");
								break;
							case 1:
								System.out.println("ONE!");
								break;
						}
						
						if(abba == 3){
							System.out.println("HELLO WORLD!");
						}else if(abba == 2){
							System.out.println("Hello world.");
						}else if(abba == 1){
							System.out.println("hello world...");
						}
						System.out.println(fib(21));
						
						int j = 1;
						while(fib(j) < 20){
							j += 1;
						}
						
						int on = 4;
						while(fib(on) > 0){
							on--;
						}
					}
					y--;
				}while(y > 1);
			}
		}
	}
	
	public static int fib(int x){
		if(x > 1){
			return fib(x-1) + fib(x-2);
		}else{
			return x;
		}
	}
}

