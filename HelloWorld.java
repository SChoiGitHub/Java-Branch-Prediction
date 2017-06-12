import java.util.*;

//Test input.
public class HelloWorld {	
	public static void main (String[] args) {
		System.out.println("Hello World!");
		Random r = new Random();
		int abba = r.nextInt(3)+1;
		
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
	
	public static int fib(int x){
		if(x > 1){
			return fib(x-1) + fib(x-2);
		}else{
			return x;
		}
	}
}

