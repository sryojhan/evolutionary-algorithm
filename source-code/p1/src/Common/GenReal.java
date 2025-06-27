package Common;

import java.util.Random;

public class GenReal extends Gen<Double> {


	private double valor;
	
	public GenReal(double min, double max) {
		
		
		minValue = min;
		maxValue = max;
		
		valor = 0;
	}
	
	
	public GenReal(GenReal other) {
		
		minValue = other.minValue;
		maxValue = other.maxValue;
		
		valor = other.valor;	
	}
	
	
	@Override
	public void Randomize() {
		
		Random random = new Random();
		
		valor = random.nextDouble(minValue, maxValue);
	}
	
	public void Set(double valor) {
		
		this.valor = valor;
	}
	
	public double Get() {
		
		return valor;
	}
	
	@Override
	public GenReal Clone() {
		
		return new GenReal(this);
	}


	@Override
	public void Set(int lotus, Double alelo) {
		
		this.valor = alelo;
	}


	@Override
	public Double Get(int lotus) {
		
		return valor;
	}


	@Override
	public int Size() {
		
		return 1;
	}


	@Override
	public double Fenotype() {
		
		return valor;
	}


	@Override
	public void Mutate(int lotus) {
		
		Randomize();
		
	}
	
}
