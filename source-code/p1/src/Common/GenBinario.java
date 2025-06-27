package Common;

import java.util.Random;

public class GenBinario extends Gen<Boolean> {

	
	private boolean[] valores;
	
	public GenBinario(double min, double max, double tolerance) {
		
		
		minValue = min;
		maxValue = max;
		
		valores = new boolean[CalculateGenSize(tolerance)];
	}
	
	
	public GenBinario(GenBinario other) {
		
		minValue = other.minValue;
		maxValue = other.maxValue;
		
		valores = new boolean[other.valores.length];
		
		for(int i = 0; i< valores.length; i++) {
			valores[i] = other.valores[i];
			
		}	
	}
	
	
	
	private int CalculateGenSize(double tolerance) {
		
		return (int) (Math.log10(((maxValue - minValue) / tolerance) + 1) / Math.log10(2));
	}
	
	public void Randomize() {
		
		Random random = new Random();
		for(int i = 0; i< valores.length; i++) {
			
			valores[i] = random.nextBoolean();
		}
	}
	
	public void Set(int lotus, Boolean alelo) {
		
		valores[lotus] = alelo;
	}
	
	public Boolean Get(int lotus) {
		
		return valores[lotus];
	}
	
	public int Size() {
		return valores.length;
	}
	
	public GenBinario Clone() {
		
		
		return new GenBinario(this);
		
	}
	
	
	private int binToDec(){
		int decimal = 0;
		for(int i = 0; i < this.valores.length; i++) 
		{
			int bin=0;
			if(this.valores[i]) {
				bin=1;
			}
			decimal += Math.pow(2,i)*bin;
		}
		return decimal;
	}
	
	public double Fenotype() {
		double fenotype = minValue + (maxValue - minValue) * binToDec()/(Math.pow(2,this.valores.length) - 1);
		return fenotype;
	}


	
	@Override
	public void Mutate(int lotus) {
		
		valores[lotus] = !valores[lotus];
	}
	
}
