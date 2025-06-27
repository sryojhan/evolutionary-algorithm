package Common;

import java.util.Random;

public class Gen {


	int[] valores;

	public Gen(int size) {

		valores = new int[size];
	}

	public Gen(Gen otro) {

		valores = new int[otro.valores.length];
				
		for(int i = 0; i < valores.length; i++) {
			valores[i] = otro.valores[i];
		}
	}

	public void Set(int lotus, int alelo) {

		valores[lotus] = alelo;
		
	}
	
	public void Set(int[] l) {

		valores = l;
	}
	
	public void Randomize() {
		
		Random rnd = new Random();
		
		for(int i = 0; i < valores.length; i++) {
			
			valores[i] = rnd.nextInt(256);
		}
	}
	

	public int Get(int lotus) {

		return valores[lotus];
	}

	public int Size() {
		
		return valores.length;
	}
	
	
	public Gen Clone() {
		
		return new Gen(this);
	}

	public int[] Fenotype() {
		
		return valores;
	}

}
