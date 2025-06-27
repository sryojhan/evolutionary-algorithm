package Common;

import Common.InformacionAlgoritmo;

public class Algoritmo {

	public Individuo[] poblacion;
	private int numeroGeneraciones; // Poblacion y numero de generaciones

	private InformacionAlgoritmo info; // Informacion del algoritmo

	private double[] fitnessMedio; // Fitness medio de cada generacion
	private double[] fitnessMejor; // Fitness mejor de cada generacion
	private double[] fitnessAbsoluto; // El mejor fitness encontrado hasta el momento

	public double[][] allFitness; // Fitness de cada individuo en cada generacion para la grafica completa

	public Nodo mejor; //Mejor encontrado
	
	private boolean elitismo; // Hay elitismo
	private double porcentajeElite; // Porcentaje de elitismo

	private Selection.Type tipoSeleccion; // tipo de seleccion
	private int tamTorneo; // Tamaño del torneo

	private Mutation.Type tipoMutacion; // tipo de mutacion
	public double probabilidadMutacion; // Probabilidad de que mute cada gen

	private Crossing.Type tipoCruce; // tipo de cruce
	public double probabilidadCruce; // Probabilidad de cruce

	private boolean escaladoLineal = false;
	private boolean controlBloating = true;
	private int tarpeianValue = 2;
	
	public Algoritmo(int tamPoblacion, int numeroGeneraciones, InformacionAlgoritmo info) {

		this.info = info;

		this.numeroGeneraciones = numeroGeneraciones;
		poblacion = new Individuo[tamPoblacion];

		fitnessMedio = new double[numeroGeneraciones];
		fitnessMejor = new double[numeroGeneraciones];
		fitnessAbsoluto = new double[numeroGeneraciones];


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

			if(controlBloating) {
				ControlBloating();
			}
			
			ActualizarFitness(i); // Actualizamos la informacion de fitness para las graficas
		}

		return this.MejorResultado(); // Devolvemos el mejor fitness encontrado

	}

	private void ControlBloating() {
		
		
		double media = 0;
		int size = poblacion.length;
		
		int[] profundidad = new int[size];
		
		for(int i = 0; i < size; i++) {
			
			int p = poblacion[i].GetGene().Profundidad();
			
			profundidad[i] = p;
			
			media += p;
		}
		
		media /=  size;
		
		
		
		double probEliminado = 1 / tarpeianValue;
		
		for(int i = 0; i <size; i++) {
			
			if(profundidad[i] > media && Math.random() < probEliminado)
			{
				poblacion[i] = new Individuo(info);
			}
		}
		
		
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
		case Funcional:
			
			Mutation.Funcional(pob,  probabilidadMutacion);
			
			break;
		case Permutacion:
			
			Mutation.Permutacion(pob, probabilidadMutacion);
			break;
		case Terminal:
			
			Mutation.Terminal(pob, probabilidadMutacion);
			break;
		default:
			break;

		}

	}

	private void Cruce(Individuo[] pob) {

		switch (tipoCruce) {
		case CruceRamas:
			
			Crossing.Intercambio(pob, probabilidadCruce);
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

			this.mejor = poblacion[indiceMejor].Clone().GetGene().Fenotype();
			//int[] mejorSolucion = poblacion[indiceMejor].GetGene().Fenotype();
//			for (int i = 0; i < mejorSolucion.length; i++) {
//
//				puntoSolucion[i] = mejorSolucion[i];
//
//			}

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

			poblacion[i] = new Individuo(info);
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
	
	public void SetBloating(boolean value, int n) {
		
		this.controlBloating = value;
		this.tarpeianValue = n;
		
	}

}
