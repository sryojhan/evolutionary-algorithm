package Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Node;

public class Crossing {

	public enum Type {
		Mono, Uniform
	}

	private static class Seleccion {

		public Individuo A;
		public Individuo B;

		public int idx;

	}

	private static Seleccion SeleccionarCruces(Individuo[] poblacion, double probabilidadCruce, int idx) {

		Seleccion selec = new Seleccion();
		for (int i = idx; i < poblacion.length; i++) {

			double r = Math.random();

			if (r > probabilidadCruce)
				continue;

			if (selec.A == null) {
				selec.A = poblacion[i];
			} else {
				selec.B = poblacion[i];
				selec.idx = i + 1;
				return selec;
			}

		}

		selec.idx = poblacion.length;
		return selec;

	}

	public static void Monopunto_Internal(int[] A, int[] B, int punto) {

		for (int i = 0; i <= punto; i++) {
			int aux = A[i];
			A[i] = B[i];
			B[i] = aux;
		}

	}

	public static void Monopunto(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			int punto = (int) (Math.random() * (Afen.length - 1));

			Monopunto_Internal(Afen, Bfen, punto);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

	public static void Uniforme_Internal(int[] A, int B[], double p) {

		for(int i = 0; i < A.length; i++) {
			if(Math.random() < p) {
				int aux = A[i];
				A[i] = B[i];
				B[i] = aux;
			}
		}
	}

	public static void Uniforme(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			int punto = (int) (Math.random() * (Afen.length - 1));

			Uniforme_Internal(Afen, Bfen, punto);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

}
