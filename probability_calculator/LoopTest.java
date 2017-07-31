import java.util.*;


class Dummy{
	int num;
	
	public Dummy(int x){
		num = x;
	}
}




public class LoopTest{
	
	static Random r = new Random();
	
	
	public static void main(String[] args){
		for(int x = 0; x < 1000; x+=1){
			
			int y = rollTheDice();
			for(int z = -100; z < 0; z+=rollTheDice()*2){
				y += rollTheDice() - 3;
			}
			
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
			System.out.println(b1(rollTheDice()));
			System.out.println(c1());
			System.out.println(d2() + d1());
			System.out.println(e1());
		}
	}

	public static int rollTheDice(){
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
		return a3(i) + 2;
	}
	public static int a3(int i){
		return a1(i) + 3;
	}	
	public static int b1(int i){
		if(i > 0){
			return b2(i-1) + 1;
		}else if(i < 0){
			return b3(i+1) + 1;
		}else{
			return 1;
		}
	}
	public static int b2(int i){
		return b1(i*-1) + 12;
	}
	public static int b3(int i){
		return b1(i*-1) + 3;
	}
	
	
	
	public static int c1(){
		if(rollTheDice() <= 1){
			return 1 + c2();
		}else{
			return 0;
		}
	}
	public static int c2(){
		if(rollTheDice() <= 2){
			return 2 + c3();
		}else{
			return c1();
		}
	}
	public static int c3(){
		if(rollTheDice() <= 3){
			return 4 + c4();
		}else{
			return c1();
		}
	}
	public static int c4(){
		if(rollTheDice() <= 4){
			return 8 + c5();
		}else{
			return c1();
		}
	}
	public static int c5(){
		if(rollTheDice() <= 5){
			return 16 + c6();
		}else{
			return c1();
		}
	}
	public static int c6(){
		return 32;
	}
	
	
	public static int d1(){
		if(rollTheDice() == 1){
			return 1 + d1();
		}
		if(rollTheDice() == 2){
			return 1 + d2();
		}
		if(rollTheDice() == 3){
			return 1 + d3();
		}
		return 1;
	}
	public static int d2(){
		if(rollTheDice() == 1){
			return 2 + d1();
		}
		if(rollTheDice() == 2){
			return 2 + d2();
		}
		if(rollTheDice() == 3){
			return 2 + d3();
		}
		return 2;
	}
	public static int d3(){
		if(rollTheDice() == 1){
			return 3 + d1();
		}
		if(rollTheDice() == 2){
			return 3 + d2();
		}
		if(rollTheDice() == 3){
			return 3 + d3();
		}
		return 3;
	}
	
	
	
	public static int e1(){
		if(rollTheDice() <= 3){
			return 1 + e1();
		}else{
			return e2();
		}
	}
	public static int e2(){
		if(rollTheDice() <= 3){
			return 2 + e1();
		}else{
			return e3();
		}
	}
	public static int e3(){
		if(rollTheDice() <= 3){
			return 3 + e1();
		}else{
			return 0;
		}
	}
	
	
	
	
	
	
	
	
	
}


