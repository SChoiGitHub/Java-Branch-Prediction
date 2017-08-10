import java.util.*;



public class LoopTest{
	public static void main(String[] args){
		for(int xyz = 0; xyz < 500; xyz++){
			//Call Heuristic?
			if(rollTheDice() > 3){
				System.out.println("Rolled higher than 3");
				hi(rollTheDice());
				bye(rollTheDice());
			}else{
				
			}
			
			
			
			
			
			int x = 0;
			//Loop Heuristics
			while(rollTheDice() == 6 && rollTheDice() == 5 && rollTheDice() == 4){
				System.out.println("I did not roll a 6, 5, and 4!");
				
				
				if(rollTheDice() == 3){
					hi(x);
					x/=2;
				}
				if(rollTheDice() == 4){
					x/=2;
					bye(x);
				}
				
				//Opcode Heuristic?
				if(x == 15){
					System.out.println("My fib number is too big!");
					
				}else{
					System.out.println("My fib number: " + fib(x++));
				}
			}
			
			int asdf = tooManyCalls();
			if(asdf > 12){
				System.out.println(asdf);
				System.out.println(fib(12));
			}else{
				System.out.println(bye(hi(asdf%20)%20));
			}
			
			System.out.println(bye(hi(asdf%10)%25));
			
			
		}
	}
	
	
	public static int tooManyCalls(){
		return hi(rollTheDice() * rollTheDice()) + bye(rollTheDice() * rollTheDice());
	}
	
	
	public static int hi(int x){
		System.out.println("Hi");
		if(x > 0){
			return bye(x-1) + 1;
		}else{
			return 100;
		}
	}
	
	public static int bye(int x){
		System.out.println("Bye");
		if(x > 0){
			return hi(x-1) + 5;
		}else{
			return 10;
		}
	}
	
	public static int fib(int x){
		if(x == 0){
			return 0;
		}
		
		if(x == 1){
			return 1;
		}
		
		return fib(x-1) + fib(x-2);
	}
	
	
	public static int rollTheDice(){
		Random r = new Random();
		return r.nextInt(6) + 1;
	}

}


