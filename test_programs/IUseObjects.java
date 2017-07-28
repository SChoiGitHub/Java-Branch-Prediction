import java.util.*;

//Test input.
public class IUseObjects {
	public static void main (String[] args) {
		Animal a = new Animal();
		int x = new Random().nextInt(6);
		
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
		
		
		
		
		section();
	}
	
	public static void section(){
		int what = new Random().nextInt(6);
		Animal b = new Animal();
		
		//Dummy Test for call heuristic
		
		//call heuristic Predicts untaken?
		if(what > 2){
			what++;
			what++;
			what++;
			b.talk(); //This is why its untaken.
		}
		what++;
		what++;
		
		
		//call heuristic predicts unknown?
		if(what > 2){
			what++;
			what++;
			what++;
		}
		what++;
		what++;
		
	}
	
}

