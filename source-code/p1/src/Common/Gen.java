package Common;

import java.util.Random;

public abstract class Gen<T>{
	
	protected double minValue;
	protected double maxValue;
	

	public abstract void Randomize();
	
	public abstract void Set(int lotus, T alelo);
	
	public abstract T Get(int lotus);
	
	public abstract int Size();
	
	public abstract Gen<T> Clone();
	
	
	public abstract double Fenotype();
	
	public abstract void Mutate(int lotus);
	
}
