import java.util.*;


public class Test {
	public static void main (String[] args) {
		int[] hooray = new int[50];
		
		while(hooray[2] <= 20){
			for(int x = 0; x < 50; x++){
				if(x > 0){
					hooray[x] = x + hooray[x-1];
				}else{
					hooray[x] = x;
				}
			}
		}
	}
}

