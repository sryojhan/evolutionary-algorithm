package Common;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Mutation {
	public enum Type {
		Funcional, Terminal, Permutacion

	}

	public static void Funcional(Individuo[] population, double probMutation) {

		
		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			double r = rnd.nextDouble();

			if (r > probMutation)
				continue;

			Gen gen = population[p].GetGene();

			Nodo raiz = gen.Fenotype();
			int size = gen.Size();
			
			Nodo n = null;
			int idx = 0;
			do {
			
				idx = rnd.nextInt(size);
				n = raiz.GetNodo(idx);
			} while(!n.GetValor().esFuncion());
			
			n.valor = new Valor(true);
		}

	}
	
	
public static void Terminal(Individuo[] population, double probMutation) {

		
		Random rnd = new Random();
		for (int p = 0; p < population.length; p++) {

			double r = rnd.nextDouble();

			if (r > probMutation)
				continue;

			Gen gen = population[p].GetGene();
			
			Nodo n = gen.Fenotype();

			while(n.GetValor().esFuncion()) {
				
				if(rnd.nextBoolean()) {
					n = n.izquierdo;
				}else {
					n = n.derecho;
				}
			}
			
			n.valor = new Valor(false);
		}

	}


public static void Permutacion(Individuo[] population, double probMutation) {

	
	Random rnd = new Random();
	for (int p = 0; p < population.length; p++) {

		double r = rnd.nextDouble();

		if (r > probMutation)
			continue;

		Gen gen = population[p].GetGene();
		
		int size = gen.Size();
		
		Nodo n = null;
		do {
		
			n = gen.Fenotype().GetNodo(rnd.nextInt(size));
		} while(!n.GetValor().esFuncion());
		
		
		
		Nodo aux = n.izquierdo;
		n.izquierdo = n.derecho;
		n.derecho = aux;
	}

}

}
