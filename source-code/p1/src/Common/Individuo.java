package Common;

import Common.InformacionAlgoritmo;
import java.util.Random;


public class Individuo{
	
	
	public enum Type {
		Binario, Real
	}
	
	private Gen[] genes;
	
	private InformacionAlgoritmo info;
	
	private double fitness;
	
	
	public Individuo(InformacionAlgoritmo info, double tolerance) {
		
		this.info = info;
		genes = new Gen[info.numeroDimensiones];
		
		for(int i = 0; i < info.numeroDimensiones; i++) {
			if(info.tipoGen == Type.Binario) //Creamos un tipo de gen u otro dependiendo del tipo indicado
				genes[i] = new GenBinario(info.minimos[i], info.maximos[i], tolerance);
			else
				genes[i] = new GenReal(info.minimos[i], info.maximos[i]);
		}
		
	}
	
	
	public Individuo(Individuo original) {
		
		this.info = original.info;
		this.fitness = original.fitness;
		
		int nGenes = original.genes.length;
		genes = new Gen[nGenes];
		
		
		for(int i = 0; i < nGenes; i++) {
			
			genes[i] = original.genes[i].Clone();	
		}
		
	}
	
	
	public void Randomize() {
		
		for(int i = 0; i < genes.length; i++) {
			
			genes[i].Randomize();	
		}
		
	}
		
	
	public double CalculateFitness() {
		
		double[] x = new double[genes.length];
		
		for(int i = 0; i < genes.length; i++) {
			x[i] = genes[i].Fenotype();
		}
		
		fitness =  info.fitnessFunction.CalculateFitness(x);
		
		
		return fitness;
	}
	
	
	public Individuo Clone() {
		
		
		Individuo copia = new Individuo(this);
		
		
		
		return copia;
	}
	
	
	public double Fitness() {
		return fitness;
	}
	

	public Gen GetGene(int idx) {
		return genes[idx];
	}
	
	public int GetGeneCount() {
		return genes.length;
	}
	
	public void Mutar(double p) {
		
		Random rnd = new Random();
		
		for(int i = 0; i < genes.length; i++) {
			for(int bit = 0; bit < genes[i].Size(); bit++) {
				
				double r= rnd.nextDouble();
				if(r < p) {
					genes[i].Mutate(bit);
				}
			}
		}
	}
	
	
	public void CorregirFitness(double inc) {
		fitness += inc;
	}
	
	public void InvertirFitness()
	{
		fitness = 1 / fitness;
	}
	
	public void SetFitness(double fit) {
		
		fitness = fit;
	}
	
	
}
