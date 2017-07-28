public class Animal{
	int age;
	String sound;
	Animal(){
		age = 2;
		sound = "bark";
	}
	public void talk(){
		for(int x = 0; x < 2; x++){
			for(int y = 0; y < 2; y++){
				System.out.println("bark");
			}
			System.out.println("mew");
		}
	}
}
