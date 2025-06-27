package Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Crossing {

	public enum Type {
		PMX, OX, OX_PP, CX, ERX, CO, Yojhan

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

	private static boolean GenContieneValor(int[] g, int valor) {

		for (int i = 0; i < g.length; i++) {

			if (g[i] == valor)
				return true;
		}

		return false;
	}

	private static boolean ContieneGen(int[] array, int value) {
		for (int i : array) {
			if (i == value) {
				return true;
			}
		}
		return false;
	}

	private static int IndiceValor(int[] array, int value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value) {
				return i;
			}
		}
		return -1;
	}

	private static void PMX_Internal(int[] A, int[] B, int puntoMin, int puntoMax) {

		if (puntoMin > puntoMax) {

			int aux = puntoMin;
			puntoMin = puntoMax;
			puntoMax = aux;
		}

		int size = A.length;

		int[] nuevoA = new int[size];
		int[] nuevoB = new int[size];

		for (int i = 0; i < size; i++) {

			nuevoA[i] = -1;
			nuevoB[i] = -1;
		}

		for (int i = puntoMin; i <= puntoMax; i++) {

			nuevoA[i] = B[i];
			nuevoB[i] = A[i];
		}

		for (int i = 0; i < size; i++) {

			if (i >= puntoMin && i <= puntoMax)
				continue;

			int value1 = A[i];
			int value2 = B[i];

			while (ContieneGen(nuevoA, value1)) {

				int indice = IndiceValor(B, value1);
				value1 = A[indice];

			}
			nuevoA[i] = value1;

			while (ContieneGen(nuevoB, value2)) {

				int indice = IndiceValor(A, value2);
				value2 = B[indice];

			}
			nuevoB[i] = value2;

		}

		for (int i = 0; i < size; i++) {

			A[i] = nuevoA[i];
			B[i] = nuevoB[i];

		}
	}

	public static void PMX(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			int puntoA = (int) (Math.random() * Afen.length);
			int puntoB = (int) (Math.random() * Afen.length);

			while (puntoA == puntoB) {

				puntoB = (int) (Math.random() * Afen.length);
			}

			PMX_Internal(Afen, Bfen, puntoA, puntoB);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

	private static int Next(int idx, int size) {

		int i = idx + 1;

		if (i == size)
			i = 0;

		return i;
	}

	private static void OX_Internal(int[] A, int[] B, int puntoMin, int puntoMax) {

		if (puntoMin > puntoMax) {

			int aux = puntoMin;
			puntoMin = puntoMax;
			puntoMax = aux;
		}

		int size = A.length;

		int[] nuevoA = new int[size];
		int[] nuevoB = new int[size];

		for (int i = 0; i < size; i++) {

			nuevoA[i] = -1;
			nuevoB[i] = -1;
		}

		for (int i = puntoMin; i <= puntoMax; i++) {

			nuevoA[i] = B[i];
			nuevoB[i] = A[i];
		}

		int siguiente = Next(puntoMax, size);

		int indiceAux = siguiente;

		while (siguiente != puntoMin) {

			while (ContieneGen(nuevoA, A[indiceAux])) {

				indiceAux = Next(indiceAux, size);
			}

			nuevoA[siguiente] = A[indiceAux];
			siguiente = Next(siguiente, size);

		}

		siguiente = Next(puntoMax, size);

		indiceAux = siguiente;

		while (siguiente != puntoMin) {

			while (ContieneGen(nuevoB, B[indiceAux])) {

				indiceAux = Next(indiceAux, size);
			}

			nuevoB[siguiente] = B[indiceAux];
			siguiente = Next(siguiente, size);

		}

		for (int i = 0; i < size; i++) {

			A[i] = nuevoA[i];
			B[i] = nuevoB[i];

		}

	}

	public static void OX(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			int puntoA = (int) (Math.random() * Afen.length);
			int puntoB = (int) (Math.random() * Afen.length);

			while (puntoA == puntoB) {

				puntoB = (int) (Math.random() * Afen.length);
			}

			OX_Internal(Afen, Bfen, puntoA, puntoB);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

	private static void OX_PP_Internal(int[] A, int[] B, int puntoA, int puntoB) {

		if (puntoA > puntoB) {

			int aux = puntoA;
			puntoA = puntoB;
			puntoB = aux;
		}

		int size = A.length;

		int[] nuevoA = new int[size];
		int[] nuevoB = new int[size];

		for (int i = 0; i < size; i++) {

			nuevoA[i] = -1;
			nuevoB[i] = -1;
		}

		nuevoA[puntoA] = B[puntoA];
		nuevoA[puntoB] = B[puntoB];
		nuevoB[puntoA] = A[puntoA];
		nuevoB[puntoB] = A[puntoB];

		int siguiente = Next(puntoB, size);

		int indiceAux = siguiente;

		while (siguiente != puntoB) {

			if (siguiente == puntoA) {

				siguiente = Next(siguiente, size);
				continue;
			}

			while (ContieneGen(nuevoA, A[indiceAux])) {

				indiceAux = Next(indiceAux, size);
			}

			nuevoA[siguiente] = A[indiceAux];
			siguiente = Next(siguiente, size);

		}

		siguiente = Next(puntoB, size);

		indiceAux = siguiente;

		while (siguiente != puntoB) {

			if (siguiente == puntoA) {

				siguiente = Next(siguiente, size);
				continue;
			}

			while (ContieneGen(nuevoB, B[indiceAux])) {

				indiceAux = Next(indiceAux, size);
			}

			nuevoB[siguiente] = B[indiceAux];
			siguiente = Next(siguiente, size);

		}

		for (int i = 0; i < size; i++) {

			A[i] = nuevoA[i];
			B[i] = nuevoB[i];

		}

	}

	public static void OX_PP(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			int puntoA = (int) (Math.random() * Afen.length);
			int puntoB = (int) (Math.random() * Afen.length);

			while (puntoA == puntoB) {

				puntoB = (int) (Math.random() * Afen.length);
			}

			OX_PP_Internal(Afen, Bfen, puntoA, puntoB);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

	private static int[] CX_Descendant(int[] A, int[] B) {

		int size = A.length;
		int[] nuevoA = new int[size];

		for (int i = 0; i < size; i++) {

			nuevoA[i] = -1;
		}

		int siguiente = 0;

		while (!ContieneGen(nuevoA, A[siguiente])) {

			nuevoA[siguiente] = A[siguiente];
			siguiente = IndiceValor(B, A[siguiente]);
		}

		for (int i = 0; i < nuevoA.length; i++) {

			if (nuevoA[i] < 0)
				nuevoA[i] = B[i];
		}

		return nuevoA;
	}

	private static void CX_Internal(int[] A, int[] B) {

		int[] hijo1 = CX_Descendant(A, B);
		int[] hijo2 = CX_Descendant(B, A);

		for (int i = 0; i < A.length; i++) {
			A[i] = hijo1[i];
			B[i] = hijo2[i];
		}

	}

	public static void CX(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			CX_Internal(Afen, Bfen);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

	private static int ERX_Siguiente(int current, int[] nuevo, int[][] M) {

		Random rnd = new Random();
		int menor = Integer.MAX_VALUE;

		int seleccionado = -1;

		for (int i = 1; i <= M[current][0]; i++) {

			int siguiente = M[M[current][i]][0];

			if (siguiente == menor) {

				if (!rnd.nextBoolean()) {
					continue;
				}
			} else if (siguiente > menor) {
				continue;
			}

			if (ContieneGen(nuevo, M[current][i]))
				continue;

			menor = siguiente;
			seleccionado = M[current][i];

		}

		return seleccionado;

	}

	private static int[] ERX_Cross(int[] A, int[] B, int[][] M) {

		int size = A.length;
		int[] nuevoA = new int[size];

		for (int i = 0; i < size; i++)
			nuevoA[i] = -1;

		nuevoA[0] = B[0];

		boolean necesarioRellenar = false;

		int i = 1;

		for (; i < size; i++) {

			nuevoA[i] = ERX_Siguiente(nuevoA[i - 1], nuevoA, M);

			if (nuevoA[i] == -1) {
				necesarioRellenar = true;
				break;
			}

		}

		if (necesarioRellenar) {

			int aux = 0;

			for (; i < size; i++) {

				while (ContieneGen(nuevoA, aux))
					aux++;
				nuevoA[i] = aux;

			}

			// System.out.println("Ha sido necesario rellenar");

		}

		return nuevoA;

	}

	private static boolean ERX_AlreadyAddedInMatrix(int[][] M, int m, int n) {

		for (int i = 0; i < M[m].length; i++) {

			if (M[m][i] == n)
				return true;
		}

		return false;

	}

	private static void ERX_InsertConnectionInMatrix(int[][] M, int m, int n) {

		if (ERX_AlreadyAddedInMatrix(M, m, n))
			return;

		M[m][0]++;
		M[n][0]++;

		M[m][M[m][0]] = n;
		M[n][M[n][0]] = m;
	}

	private static void ERX_Internal(int[] A, int[] B) {

		int size = A.length; // TODO temporal el +1
		int[][] M = new int[size][size]; // Aprovechando que una ciudad no tiene camino hacia si misma, usaremos el
											// indice 0 de cada matriz para establecer cuantos caminos hay a esa ciudad

		for (int i = 0; i < size; i++)
			for (int c = 1; c < size; c++)
				M[i][c] = -1;

		for (int i = 0; i < size - 1; i++) {

			ERX_InsertConnectionInMatrix(M, A[i], A[i + 1]);
			ERX_InsertConnectionInMatrix(M, B[i], B[i + 1]);

		}

		ERX_InsertConnectionInMatrix(M, A[0], A[size - 1]);
		ERX_InsertConnectionInMatrix(M, B[0], B[size - 1]);

		int[] hijo1 = ERX_Cross(A, B, M);
		int[] hijo2 = ERX_Cross(B, A, M);

		for (int i = 0; i < size; i++) {

			A[i] = hijo1[i];
			B[i] = hijo2[i];

		}

	}

	public static void ERX(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			ERX_Internal(Afen, Bfen);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}

	private static List<Integer> CO_GenerarLista(int size) {

		List<Integer> l = new ArrayList<Integer>();

		for (int i = 0; i < size; i++) {
			l.add(i);
		}

		return l;
	}

	private static int[] CO_Codificar(int[] A, List<Integer> list) {

		int size = A.length;
		int[] nuevo = new int[size];

		for (int i = 0; i < size; i++) {

			int indice = list.indexOf(A[i]);
			nuevo[i] = indice;
			list.remove(indice);

		}

		return nuevo;

	}

	private static int[] CO_Decodificar(int[] A, List<Integer> list) {

		int size = A.length;
		int[] nuevo = new int[size];

		for (int i = 0; i < size; i++) {

			nuevo[i] = list.get(A[i]);
			list.remove(A[i]);

		}

		return nuevo;

	}

	private static void CO_Internal(int[] A, int[] B, int puntoCorte) {

		int size = A.length;

		int[] Acodificado = CO_Codificar(A, CO_GenerarLista(size));
		int[] Bcodificado = CO_Codificar(B, CO_GenerarLista(size));

		for (int i = 0; i <= puntoCorte; i++) {

			int aux = Acodificado[i];
			Acodificado[i] = Bcodificado[i];
			Bcodificado[i] = aux;
		}

		int[] Adecodificado = CO_Decodificar(Acodificado, CO_GenerarLista(size));
		int[] Bdecodificado = CO_Decodificar(Bcodificado, CO_GenerarLista(size));

		for (int i = 0; i < size; i++) {
			A[i] = Adecodificado[i];
			B[i] = Bdecodificado[i];
		}

	}

	public static void CO(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			int puntoCorte = (int) (Math.random() * (Afen.length - 1));

			CO_Internal(Afen, Bfen, puntoCorte);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}
	
	
	private static void Shift(int[] A) {
		
		int size = A.length;
		
		int cero = A[0];
		
		for(int i = 1; i < size; i++) {
			
			A[i - 1] = A[i];
			
		}
		
		A[size - 1] =cero;
	}

	private static void Yojhan_Internal(int[] A, int[] B, double probCruce) {
		
		
		int size = A.length;
		
		
		int posicionRandom = (int) (Math.random() * size);
		
		
		while(A[posicionRandom] != B[posicionRandom]) {
			
			Shift(B);
		}
		
		
	}
	
	public static void Yojhan(Individuo[] poblacion, double probabilidadCruce) {

		Seleccion selec = SeleccionarCruces(poblacion, probabilidadCruce, 0);

		while (selec.idx < poblacion.length) {

			Gen A = selec.A.GetGene();
			Gen B = selec.B.GetGene();

			int[] Afen = A.Fenotype();
			int[] Bfen = B.Fenotype();

			Yojhan_Internal(Afen, Bfen, probabilidadCruce);

			A.Set(Afen);
			B.Set(Bfen);

			selec = SeleccionarCruces(poblacion, probabilidadCruce, selec.idx);
		}

	}
	
}
