package Common;

import java.util.Random;

public class Crossing {
	
	public enum Type{
		Mono,  
		Uniform, 
		Aritmetic,
		BLX
		
	}
	
	public static void MonopointCrossOver(Individuo[] population, double probCruce) {
	
		Random rnd= new Random();
		
		Individuo A = null;
		
		int longitud = 0;
		
		for(int g = 0; g < population[0].GetGeneCount(); g++) {
			
			longitud += population[0].GetGene(g).Size();
		}
		
		if(longitud <= 1)
			return;
		
		for(int p=0;p<population.length;p++) {
			
			double r= rnd.nextDouble();
			
			if(r < probCruce) {
				
				if(A == null)
					A = population[p];
				
				else { //Cruzar
					
					
					int puntoCorte = longitud == 2 ? 1 : 1 + rnd.nextInt(longitud - 2);
					
					
					Individuo B = population[p];

					int cont = 0;
					
					for(int g = 0; g < A.GetGeneCount() && cont < puntoCorte; g++) {
						
						for(int bit = 0; bit < A.GetGene(g).Size() && cont < puntoCorte; bit++) {
							
							
							Object a = A.GetGene(g).Get(bit);
							Object b = B.GetGene(g).Get(bit);
							
							A.GetGene(g).Set(bit, b);
							B.GetGene(g).Set(bit, a);
							
							cont++;
						}
						
						
					}
					
					A = null;
					B = null;

				}
			}
			
			
		}
		
	
	}

	public static void UniformCrossOver(Individuo[] population, double probCruce) {
		
		Random rnd= new Random();
		Individuo A = null;
		
		for(int i = 0; i < population.length; i++) {
						
			
			double r = rnd.nextDouble();
			
			if(r < probCruce)
			{
				
				if(A == null)
					A = population[i];
				
				else {
					Individuo B = population[i];

					for(int g = 0; g < population[i].GetGeneCount(); g++) {
						
						for(int bit = 0; bit < population[i].GetGene(g).Size(); bit++) {
							
							
							double probIntercambio = rnd.nextDouble();
							
							if(probIntercambio > probCruce) continue;

							Object a = A.GetGene(g).Get(bit);
							Object b = B.GetGene(g).Get(bit);
							
							A.GetGene(g).Set(bit, b);
							B.GetGene(g).Set(bit, a);
							
							
						}
					}
					
				}
				
				
			}
			
			
		}
		
		
		
	}
	
	public static void AritmeticCrossOver(Individuo[] population, double probCruce) {
		
		double alpha=0.6;

		Random rnd= new Random();
		Individuo A = null;
		
		for(int i = 0; i < population.length; i++) {
						
			
			double r = rnd.nextDouble();
			
			if(r < probCruce)
			{
				
				if(A == null)
					A = population[i];
				
				else {
					Individuo B = population[i];

					for(int g = 0; g < population[i].GetGeneCount(); g++) {
						
						for(int bit = 0; bit < population[i].GetGene(g).Size(); bit++) {
							
							
							double probIntercambio = rnd.nextDouble();
							
							if(probIntercambio > probCruce) continue;

							GenReal AGen = ((GenReal)A.GetGene(g));
							GenReal BGen = ((GenReal)B.GetGene(g));
							
							double a = AGen.Get(bit);
							double b = BGen.Get(bit);
							
						
							AGen.Set(bit, alpha * a + (1 - alpha) * b);
							BGen.Set(bit, alpha * b + (1 - alpha) * a);
							
							
						}
					}
					
				}
				
				
			}
			
			
		}
		
	}
	

	public static void BLXCrossOver(Individuo[] population, double probCruce) {
		
		double alpha=0.5;

		Random rnd= new Random();
		Individuo A = null;
		
		for(int i = 0; i < population.length; i++) {
						
			
			double r = rnd.nextDouble();
			
			if(r < probCruce)
			{
				
				if(A == null)
					A = population[i];
				
				else {
					Individuo B = population[i];

					for(int g = 0; g < population[i].GetGeneCount(); g++) {
						
						for(int bit = 0; bit < population[i].GetGene(g).Size(); bit++) {
							
							
							double probIntercambio = rnd.nextDouble();
							
							if(probIntercambio > probCruce) continue;

							GenReal AGen = ((GenReal)A.GetGene(g));
							GenReal BGen = ((GenReal)B.GetGene(g));
							
							

							double a = AGen.Get(bit);
							double b = BGen.Get(bit);
							
							double dif = Math.abs(a - b);
							
							if(dif == 0) {
								continue;
							}
							
							double minValue = Math.min(a, b) - alpha * dif;
							double maxValue = Math.max(a, b) + alpha * dif;
						
							AGen.Set(bit, rnd.nextDouble(minValue, maxValue));
							BGen.Set(bit, rnd.nextDouble(minValue, maxValue));
							
							
						}
					}
					
				}
				
				
			}
			
			
		}
		
	}


}
