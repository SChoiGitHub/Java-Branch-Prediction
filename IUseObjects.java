import java.util.*;

//Test input.
public class IUseObjects {
	public static void main (String[] args) {
		Animal a = new Animal();
		int x = new Random().nextInt(6);
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
	}
}

