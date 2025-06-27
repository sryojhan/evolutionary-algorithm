package Common;

import Common.InformacionAlgoritmo;
import java.util.Random;


public class Individuo{
	
	
	private Gen gen;
	
	private InformacionAlgoritmo info;
	
	private double fitness;
	
	
	public Individuo(InformacionAlgoritmo info) {
		
		this.info = info;

		gen = new Gen();
	}
	
	
	public Individuo(Individuo original) {
		
		this.info = original.info;
		this.fitness = original.fitness;
		
		
		gen = original.gen.Clone();
	}
	
	public double CalculateFitness() {
		
		fitness =  info.fitnessFunction.CalculateFitness(gen.Fenotype());
		
		return fitness;
	}
	
	
	public Individuo Clone() {
		
		
		Individuo copia = new Individuo(this);
		
		
		
		return copia;
	}
	
	
	public double Fitness() {
		return fitness;
	}
	

	public Gen GetGene() {
		return gen;
	}
	
	public int GetGeneCount() {
		return 1;
	}

	
	public void CorregirFitness(double inc) {
		fitness += inc;
	}
	
	public void SetFitness(double fit) {
		
		fitness = fit;
	}
	
	public FitnessFunction GetFitnessFuncion() {
		return info.fitnessFunction;
	}
	
}
