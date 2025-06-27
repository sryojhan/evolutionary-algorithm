package Common;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Mutation {
	public enum Type {
		Insercion, Intercambio, Inversion, Heuristica, Yojhan

	}

	public static void Insercion(Individuo[] population, double probMutation) {

		int numeroMaximoInserciones = 20;

		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			int contadorRepeticiones = 0;
			do { // para que un individuo pueda tener varias mutaciones lo hacemos en un bucle

				double r = rnd.nextDouble();

				if (r > probMutation)
					break;

				Gen gen = population[p].GetGene();

				int[] cadena = gen.Fenotype();

				int posicion1 = rnd.nextInt(0, gen.Size());
				int posicion2 = rnd.nextInt(0, gen.Size());

				if (posicion1 == posicion2)
					continue;

				int elemento = cadena[posicion1];

				if (posicion1 > posicion2) { // Desplazar a la derecha

					for (int i = posicion1; i > posicion2; i--) {

						cadena[i] = cadena[i - 1];
					}

				} else {

					for (int i = posicion1; i < posicion2; i++) {

						cadena[i] = cadena[i + 1];
					}
				}
				cadena[posicion2] = elemento;

				contadorRepeticiones++;

			} while (contadorRepeticiones < numeroMaximoInserciones);

		}

	}

	public static void Intercambio(Individuo[] population, double probMutation) {

		int numeroMaximoInserciones = 20;

		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			int contadorRepeticiones = 0;
			do { // para que un individuo pueda tener varias mutaciones lo hacemos en un bucle

				double r = rnd.nextDouble();

				if (r > probMutation)
					break;

				Gen gen = population[p].GetGene();

				int[] A = gen.Fenotype();

				int posicionA = (int) (rnd.nextDouble() * A.length);
				int posicionB = (int) (rnd.nextDouble() * A.length);

				int aux = A[posicionA];
				A[posicionA] = A[posicionB];
				A[posicionB] = aux;

				gen.Set(A);

			} while (contadorRepeticiones < numeroMaximoInserciones);

		}

	}

	public static void Inversion(Individuo[] population, double probMutation) {

		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			double r = rnd.nextDouble();

			if (r > probMutation)
				continue;

			Gen gen = population[p].GetGene();

			int[] A = gen.Fenotype();

			int min = (int) (rnd.nextDouble() * A.length);
			int max = (int) (rnd.nextDouble() * A.length);

			if (min == max)
				continue;

			if (min > max) {
				int aux = min;
				min = max;
				max = aux;
			}

			for (int i = min, c = max; i < c; i++, c--) {

				int aux = A[i];
				A[i] = A[c];
				A[c] = aux;
			}

			gen.Set(A);

		}

	}

	private static void permutaciones_internal(ArrayList<Integer> p, int k, ArrayList<int[]> resultado) {

		for (int i = k; i < p.size(); i++) {
			java.util.Collections.swap(p, i, k);
			permutaciones_internal(p, k + 1, resultado);
			java.util.Collections.swap(p, k, i);
		}
		if (k == p.size() - 1) {
			
			
			var arr = p.toArray();
			
			int[] nuevo = new int[arr.length];
			
			for(int i = 0; i < arr.length; i++) {
				
				nuevo[i] = (int) arr[i];
			}
			
			resultado.add(nuevo);
		}
	}
	
	
	private static ArrayList<int[]> Permutaciones(int[] puntos) {
		
		
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for (int i : puntos)
		{
		    intList.add(i);
		}
		
		ArrayList<int[]> resultado = new ArrayList<>();
		
		permutaciones_internal(intList, 0, resultado);
		
		return resultado;
	}
	
	
	private static int[] ConstruirIndividuo(int[] A, int[] posiciones, int[] valores) {
		
		int[] B = A.clone();
	
		for(int i = 0; i < posiciones.length; i++) {
			
			B[posiciones[i]] = valores[i];
		}
		
		return B;
	}
	

	private static int[] Heuristica_Internal(int[] p, FitnessFunction f, int[] A, int[] posiciones) {

		var permutaciones = Permutaciones(p);
		
		int[] elMejor = null;
		
		double scoreMinima = Double.MAX_VALUE;
		
		for(int i = 0; i < permutaciones.size(); i++) {
			
			int[] indv = ConstruirIndividuo(A, posiciones, permutaciones.get(i));
			
			double value = f.CalculateFitness(indv);
			
			if(value < scoreMinima) {
				
				scoreMinima = value;
				elMejor = indv;
			}	
		}
		

		return elMejor;
	}

	public static void Heuristica(Individuo[] population, double probMutation) {

		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			double r = rnd.nextDouble();

			if (r > probMutation)
				continue;

			Gen gen = population[p].GetGene();

			int nPuntos = 4;
			int[] fenotype = gen.Fenotype();
			boolean repetition = false;
			int[] puntos = null;
			int[] posiciones = null;
			do {
				repetition = false;

				puntos = new int[nPuntos];
				posiciones = new int[nPuntos];

				for (int i = 0; i < nPuntos; i++) {

					posiciones[i] = rnd.nextInt(gen.Size());
					puntos[i] = fenotype[posiciones[i]];
					
				}

				for (int i = 0; i < nPuntos; i++) {
					for (int c = 0; c < nPuntos; c++) {
						if (i == c)
							continue;

						if (puntos[i] == puntos[c])
							repetition = true;
					}
				}
			} while (repetition);

			Arrays.sort(posiciones);
			
			
			int[] nuevo = Heuristica_Internal(puntos, population[p].GetFitnessFuncion(), fenotype, posiciones);
			
			
			gen.Set(nuevo);

		}

	}

	public static void Yojhan(Individuo[] population, double probMutation) {

		
		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			double r = rnd.nextDouble();

			if (r > probMutation)
				continue;

			Gen gen = population[p].GetGene();

			int[] A = gen.Fenotype();

			
			int size = A.length;
			for(int i = 0; i < size; i++) {
				
				A[i] = (A[i] + 1)%size;
			}
			
			gen.Set(A);

		}

	}
}
