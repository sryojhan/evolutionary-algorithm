package Common;

import Common.InformacionAlgoritmo;

public class Algoritmo {

	public Individuo[] poblacion;
	private int numeroGeneraciones; // Poblacion y numero de generaciones

	private InformacionAlgoritmo info; // Informacion del algoritmo

	private double tolerance; // Tolerancia para los genes binarios

	private double[] fitnessMedio; // Fitness medio de cada generacion
	private double[] fitnessMejor; // Fitness mejor de cada generacion
	private double[] fitnessAbsoluto; // El mejor fitness encontrado hasta el momento

	private int[] puntoSolucion; // El mejor recorrido encontrado

	public double[][] allFitness; // Fitness de cada individuo en cada generacion para la grafica completa

	private boolean elitismo; // Hay elitismo
	private double porcentajeElite; // Porcentaje de elitismo

	private Selection.Type tipoSeleccion; // tipo de seleccion
	private int tamTorneo; // Tamaño del torneo

	private Mutation.Type tipoMutacion; // tipo de mutacion
	public double probabilidadMutacion; // Probabilidad de que mute cada gen

	private Crossing.Type tipoCruce; // tipo de cruce
	public double probabilidadCruce; // Probabilidad de cruce

	private boolean escaladoLineal = false;

	public Algoritmo(int tamPoblacion, int numeroGeneraciones, InformacionAlgoritmo info) {

		this.info = info;

		this.numeroGeneraciones = numeroGeneraciones;
		poblacion = new Individuo[tamPoblacion];

		fitnessMedio = new double[numeroGeneraciones];
		fitnessMejor = new double[numeroGeneraciones];
		fitnessAbsoluto = new double[numeroGeneraciones];

		puntoSolucion = new int[info.numeroElementos];

		allFitness = new double[tamPoblacion][numeroGeneraciones];

	}

	public double run() {

		IniciarPoblacion();

		for (int i = 0; i < numeroGeneraciones; i++) {

			if (escaladoLineal)
				EscaladoLineal(i);
			else
				CorregirFitness(); // Hacer que los fitness no sean negativos, y ajusta el fitness en caso de que
									// sea un ejercicio de minimizacion

			Individuo[] elite = null;
			if (elitismo)
				elite = SeleccionarElite(); // Seleccion de la elite

			Individuo[] nuevaPoblacion = ClonarPoblacion(); // Clonamos la poblacion para que no haya problemas de
															// punteros

			nuevaPoblacion = Seleccion(poblacion);
			Cruce(nuevaPoblacion);
			Mutacion(nuevaPoblacion);

			poblacion = nuevaPoblacion; // Actualizamos el valor de la poblacion

			if (elitismo) {
				IntroducirElite(elite); // Introducir la elite de vuelta a la poblacion
			}

			ActualizarFitness(i); // Actualizamos la informacion de fitness para las graficas
		}

		return this.MejorResultado(); // Devolvemos el mejor fitness encontrado

	}

	private void EscaladoLineal(int idx) {

		if (idx == 0) {
			CorregirFitness();
			return;
		}

		double mejor = fitnessMejor[idx - 1];
		double media = fitnessMedio[idx - 1];

		double a = media / (media - mejor);
		double b = (1 - a) * media;

		for (int i = 0; i < poblacion.length; i++) {

			poblacion[i].SetFitness(a * poblacion[i].CalculateFitness() + b);
		}

		CorregirFitness();
	}

	private Individuo[] Seleccion(Individuo[] poblacion) {

		Individuo[] seleccionados = poblacion;

		switch (tipoSeleccion) {

		case Proporcional:
			seleccionados = Selection.Proporcional(poblacion);
			break;
		case MuestreoUniversalEstoclastico:
			seleccionados = Selection.MuestreoUniversalEstoclastico(poblacion);
			break;
		case Truncamiento:
			seleccionados = Selection.Truncamiento(poblacion);
			break;
		case TorneoDeterministico:

			seleccionados = Selection.TorneoDeterministico(poblacion, tamTorneo);
			break;
		case TorneoProbabilistico:

			double p = 0.5;
			seleccionados = Selection.TorneoProbabilistico(poblacion, tamTorneo, p);
			break;

		case Restos:
			seleccionados = Selection.Restos(poblacion);
			break;

		case Ranking:
			seleccionados = Selection.Ranking(poblacion, 2);
			break;
		}

		return seleccionados;

	}

	private void Mutacion(Individuo[] pob) {

		switch (tipoMutacion) {

		case Insercion:

			Mutation.Insercion(pob, probabilidadMutacion);
			break;
		case Heuristica:
			Mutation.Heuristica(pob, probabilidadMutacion);
			break;
		case Intercambio:
			Mutation.Intercambio(pob, probabilidadMutacion);
			break;
		case Inversion:
			Mutation.Inversion(pob, probabilidadMutacion);
			break;
		case Yojhan:
			Mutation.Yojhan(pob, probabilidadMutacion);
			break;
		default:
			break;

		}

	}

	private void Cruce(Individuo[] pob) {

		switch (tipoCruce) {
		case CO:
			Crossing.CO(pob, probabilidadCruce);
			break;
		case CX:
			Crossing.CX(pob, probabilidadCruce);
			break;
		case ERX:
			Crossing.ERX(pob, probabilidadCruce);
			break;
		case OX:
			Crossing.OX(pob, probabilidadCruce);
			break;
		case OX_PP:
			Crossing.OX_PP(pob, probabilidadCruce);
			break;
		case PMX:
			Crossing.PMX(pob, probabilidadCruce);
			break;
		case Yojhan:
			Crossing.Yojhan(pob, probabilidadCruce);
			break;
		default:
			break;

		}

	}

	private Individuo[] SeleccionarElite() {

		int eliteSize = (int) Math.round(poblacion.length * porcentajeElite);

		Individuo[] elite = new Individuo[eliteSize];

		Individuo[] poblacionOrdenada = ClonarPoblacion(); // Ordenamos la poblacion en funcion del fitness para elegir
															// a los mejores
		Selection.quickSort(poblacionOrdenada, 0, poblacion.length - 1);

		for (int i = 0; i < eliteSize; i++) {

			elite[i] = poblacionOrdenada[i];

		}

		return elite;
	}

	private void IntroducirElite(Individuo[] elite) {

		int eliteSize = elite.length;

		for (int i = 0; i < eliteSize; i++) {

			poblacion[i] = elite[i];

		}
	}

	private void ActualizarFitness(int idx) {

		double mejor = poblacion[0].CalculateFitness();
		double media = mejor;

		int indiceMejor = 0;

		for (int i = 1; i < poblacion.length; i++) {

			double fitness = poblacion[i].CalculateFitness();

			media += fitness;

			if (info.maximizacion) {
				if (fitness > mejor) {
					mejor = fitness;
					indiceMejor = i;
				}
			} else if (fitness < mejor) {
				mejor = fitness;
				indiceMejor = i;
			}

			allFitness[i][idx] = fitness;
		}

		media /= poblacion.length;

		fitnessMedio[idx] = media;
		fitnessMejor[idx] = mejor;

		boolean actualizarMejor = false;
		if (idx > 0) {

			if (info.maximizacion) {

				if (mejor > fitnessAbsoluto[idx - 1]) // En caso de haber encontrado uno mejor actualizamos los puntos
														// encontrados
				{
					actualizarMejor = true;
					fitnessAbsoluto[idx] = mejor;
				} else
					fitnessAbsoluto[idx] = fitnessAbsoluto[idx - 1];

			} else {

				if (mejor < fitnessAbsoluto[idx - 1]) {
					actualizarMejor = true;
					fitnessAbsoluto[idx] = mejor;
				} else
					fitnessAbsoluto[idx] = fitnessAbsoluto[idx - 1];

			}

		} else {

			actualizarMejor = true;
			fitnessAbsoluto[idx] = mejor;
		}

		if (actualizarMejor) {

			int[] mejorSolucion = poblacion[indiceMejor].GetGene().Fenotype();
			for (int i = 0; i < mejorSolucion.length; i++) {

				puntoSolucion[i] = mejorSolucion[i];

			}

		}

	}

	private void CorregirFitness() {

		double valorMenor = poblacion[0].Fitness();
		double valorMayor = valorMenor;

		for (int i = 1; i < poblacion.length; i++) {

			double fitness = poblacion[i].Fitness();
			if (fitness < valorMenor) {
				valorMenor = fitness;
			}

			if (fitness > valorMayor) {
				valorMayor = fitness;
			}

		}

		if (valorMenor < 0) {

			double incremento = valorMenor * -1.05;

			for (int i = 0; i < poblacion.length; i++) {

				poblacion[i].CorregirFitness(incremento);
			}

			valorMayor += incremento;

		}

		if (!info.maximizacion) {

			valorMayor *= 1.05;
			for (int i = 0; i < poblacion.length; i++) {

				// poblacion[i].InvertirFitness();

				poblacion[i].SetFitness(valorMayor - poblacion[i].Fitness());
			}

		}

	}

	private void IniciarPoblacion() {

		for (int i = 0; i < poblacion.length; i++) {

			poblacion[i] = new Individuo(info, tolerance);
			poblacion[i].Randomize();

			poblacion[i].CalculateFitness();
		}
	}

	Individuo[] ClonarPoblacion() {

		Individuo[] nuevaPoblacion = new Individuo[poblacion.length];

		for (int i = 0; i < poblacion.length; i++) {

			nuevaPoblacion[i] = poblacion[i].Clone();

		}

		return nuevaPoblacion;
	}

	public void SetTolerance(double tolerance) {

		this.tolerance = tolerance;
	}

	public double[] MejorFitness() {
		return fitnessMejor;
	}

	public double[] FitnessMedio() {
		return fitnessMedio;
	}

	public double[] MejorFitnessAbsoluto() {
		return fitnessAbsoluto;
	}

	public double MejorResultado() {

		return fitnessAbsoluto[fitnessAbsoluto.length - 1];
	}

	public int[] PuntoSolucion() {
		return puntoSolucion;
	}

	public void SetSelection(int index, int tamTorneo) {
		tipoSeleccion = Selection.Type.values()[index];

		this.tamTorneo = tamTorneo;
	}

	public void SetElitism(double elitism) {

		porcentajeElite = elitism;

		if (porcentajeElite > 0)
			elitismo = true;
	}

	public void SetCrossing(int index, double probCruce) {
		tipoCruce = Crossing.Type.values()[index];
		probabilidadCruce = probCruce;
	}

	public void SetMutation(int index, double probMutacion) {
		tipoMutacion = Mutation.Type.values()[index];
		probabilidadMutacion = probMutacion;
	}

	public void SetEscaladoLineal(boolean value) {

		this.escaladoLineal = value;
	}

}
