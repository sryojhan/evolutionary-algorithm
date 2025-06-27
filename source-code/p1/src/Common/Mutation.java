package Common;



public class Mutation {
	public enum Type{
		Uniform,
		NonUniform,
		
	}
	
	public static void mutateUniform(Individuo[] population, double probMutation) {


		for(int i=0;i<population.length;i++) {
			
			population[i].Mutar(probMutation);
			
		}
		
	}
	public static Individuo[] mutateNonUniform(Individuo[] population, double probMutation) {
		//FOR NOW ITS EMPTY
		return null;
	}
	
	public static Individuo[] mutateInsert(Individuo[] population, double probMutation) {
		return null;	
	}
	
	public static Individuo[] mutateExchanging(Individuo[] population, double probMutation) {
		return null;	
	}
	
	public static Individuo[] mutateInvert(Individuo[] population, double probMutation) {
		return null;	
	}
	public static Individuo[] mutateHeuristic(Individuo[] population, double probMutation) {
		return null;	
	}
}
