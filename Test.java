import java.util.*;

//Test input.
public class Test {
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
					}
					y--;
				}while(y > 1);
			}
		}
	}
}

