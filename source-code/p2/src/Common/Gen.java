package Common;

import java.util.Random;

public class Gen{
	

	private int[] valores;
			
	public Gen(int numeroElementos)
	{
		valores = new int[numeroElementos];
		
		
		for(int i = 0; i < numeroElementos; i++) {
			valores[i] = i;
		}
		
	}
	
	public Gen(Gen otro) {
		
		valores = new int[otro.valores.length];
		
		for(int i = 0; i < valores.length; i++) {
			valores[i] = otro.valores[i];
		}
		
	}
	

	public void Randomize() {
		
		
		Random rnd = new Random();
		for(int i = 0; i < valores.length; i++) {
			
			
			int r = rnd.nextInt(0, valores.length);
			
			int aux = valores[r];
			valores[r] = valores[i];
			valores[i] = aux;
			
		}
		
	}
	
	public void Set(int lotus, int alelo) {
		
		valores[lotus] = alelo;
	}
	
	public void Set(int[] cadena) {
		valores = cadena;
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
