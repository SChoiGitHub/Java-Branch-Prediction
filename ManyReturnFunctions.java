import java.util.*;

//Test input.
public class ManyReturnFunctions {
	public static void main (String[] args) {
		int nobody = who(4);
		System.out.println(rollTheDice());
	}
	public static int who(int x){
		int a = 0;
		while(x > 0){
			a += x * x;
			x--;
		}
		return(a);
	}
	
	public static int returnNoMatterWhat(int x, int y){
		if(x > y){
			return x + y;
		}
		return y - x;
	}
	
	public static int rollTheDice(){
		Random r = new Random();
		boolean previous_six = false;
		int rolls = 0;
		int t = 0;
		while(true){
			t = r.nextInt(6);
			switch(t){
				case 5:
					if(previous_six){
						return rolls;
					}else{
						previous_six = true;
					}
					rolls++;
					break;
				default:
					previous_six = false;
					rolls++;
					break;
			}
		}
	}
}

