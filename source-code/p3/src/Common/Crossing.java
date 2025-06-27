package Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Node;

public class Crossing {

	public enum Type {
		CruceRamas

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

	private static void InsertarHijo(Nodo original, Nodo nuevo) {

		if (original.padre.izquierdo == original) {

			original.padre.izquierdo = nuevo;
		} else {

			original.padre.derecho = nuevo;
		}

	}

	public static void Intercambio(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int size = Math.min(A.Size(), B.Size());

			Nodo a = A.Fenotype().GetNodo(1 + (int) (Math.random() * (size - 1)));
			Nodo b = A.Fenotype().GetNodo(1 + (int) (Math.random() * (size - 1)));

			InsertarHijo(a, Nodo.Clone(b));
			InsertarHijo(b, Nodo.Clone(a));

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

}
