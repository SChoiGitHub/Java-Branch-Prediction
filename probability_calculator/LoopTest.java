import java.util.*;


class Dummy{
	int num;
	
	public Dummy(int x){
		num = x;
	}
}




public class LoopTest{
	public static void main(String[] args){
		for(int x = 0; x < 1000; x++){
			
			int y = rollTheDice();
			
			Dummy d;
			
			if(y >= 4){
				System.out.println("Hello World!");
				d = new Dummy(y);
			}else{
				System.out.println("Hi World!");
				d = null;
			}
			
			
			
			if(d != null){
				System.out.println("We have a dummy.");
			}else{
				System.out.println("We do not have a dummy");
			}
			
			
			selfConflicting(y);
			fib(10);
			
			
			System.out.println(a1(rollTheDice()));
		}
	}

	public static int rollTheDice(){
		Random r = new Random();
		return r.nextInt(6) + 1;
	}
	
	public static void selfConflicting(int i){
		if(i - 4 != 0){
			System.out.println("continue!");
		}else{
			return;
		}
	}
	
	public static int fib(int i){
		if(i > 1){
			System.out.println(i);
			return i;
		}else{
			System.out.println(fib(i-1) + fib(i-2));
			return fib(i-1) + fib(i-2);
		}
	}
	
	public static int a1(int i){
		System.out.println("A1");
		if(i > 0){
			return 1 + a2(i-1);
		}else{
			return 1;
		}
	}
	public static int a2(int i){
		System.out.println("A2");
		return a3(i) + 2;
	}
	public static int a3(int i){
		System.out.println("A3");
		return a1(i) + 3;
	}
}


