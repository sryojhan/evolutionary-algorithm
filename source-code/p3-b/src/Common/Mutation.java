package Common;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Mutation {
	public enum Type{
		Uniform,
		NonUniform
		
	}
	
	public static void Uniform(Individuo[] population, double probMutation) {


		int numeroMaximoMutacion = (int)Math.ceil(population[0].GetGene().Size() * 0.9);

		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			int contadorRepeticiones = 0;
			do { // para que un individuo pueda tener varias mutaciones lo hacemos en un bucle

				double r = rnd.nextDouble();

				if (r > probMutation)
					break;

				Gen gen = population[p].GetGene();

				int[] A = gen.Fenotype();

				int pos = (int) (rnd.nextDouble() * A.length);
				
				A[pos] = rnd.nextInt(256);
				
				gen.Set(A);

			} while (contadorRepeticiones < numeroMaximoMutacion);

		}
		
	}
	
	
	public static void NonUniform(Individuo[] population, double probMutation) {


		int numeroMaximoMutacion = (int)Math.ceil(population[0].GetGene().Size() * 0.9);

		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			int contadorRepeticiones = 0;
			do { // para que un individuo pueda tener varias mutaciones lo hacemos en un bucle

				double r = rnd.nextDouble();

				if (r > probMutation)
					break;

				Gen gen = population[p].GetGene();

				int[] A = gen.Fenotype();

				int pos = (int) (rnd.nextDouble() * A.length);
				
				A[pos] = rnd.nextInt(256);
				
				gen.Set(A);

			} while (contadorRepeticiones < numeroMaximoMutacion);

		}
		
	}
	

}
