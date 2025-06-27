package Common;

import Common.InformacionAlgoritmo;

public class Algoritmo {

	private Individuo[] poblacion;
	private int numeroGeneraciones; //Poblacion y numero de generaciones

	private InformacionAlgoritmo info; //Informacion del algoritmo

	private double tolerance; //Tolerancia para los genes binarios

	private double[] fitnessMedio; //Fitness medio de cada generacion
	private double[] fitnessMejor; //Fitness mejor de cada generacion
	private double[] fitnessAbsoluto; //El mejor fitness encontrado hasta el momento

	private double[] puntoSolucion; //Los valores de x que corresponden a la mejor solucion
	
	public double[][] allFitness; //Fitness de cada individuo en cada generacion para la grafica completa

	private boolean elitismo; //Hay elitismo
	private double porcentajeElite;  //Porcentaje de elitismo

	private Selection.Type tipoSeleccion; //tipo de seleccion
	private int tamTorneo; //Tamaño del torneo

	private Mutation.Type tipoMutacion; //tipo de mutacion
	private double probabilidadMutacion; //Probabilidad de que mute cada gen

	private Crossing.Type tipoCruce; //tipo de cruce
	private double probabilidadCruce; //Probabilidad de cruce

	public Algoritmo(int tamPoblacion, int numeroGeneraciones, InformacionAlgoritmo info) {

		this.info = info;

		this.numeroGeneraciones = numeroGeneraciones;
		poblacion = new Individuo[tamPoblacion];

		fitnessMedio = new double[numeroGeneraciones];
		fitnessMejor = new double[numeroGeneraciones];
		fitnessAbsoluto = new double[numeroGeneraciones];
		
		puntoSolucion = new double[info.numeroDimensiones];

		allFitness = new double[tamPoblacion][numeroGeneraciones];

	}

	public double run() {

		IniciarPoblacion();

		for (int i = 0; i < numeroGeneraciones; i++) {

			CorregirFitness(); //Hacer que los fitness no sean negativos, y ajusta el fitness en caso de que sea un ejercicio de minimizacion

			Individuo[] elite = null;
			if (elitismo)
				elite = SeleccionarElite(); //Seleccion de la elite 

			Individuo[] nuevaPoblacion = ClonarPoblacion(); //Clonamos la poblacion para que no haya problemas de punteros

			nuevaPoblacion = Seleccion(poblacion);
			Cruce(nuevaPoblacion);
			Mutacion(nuevaPoblacion);

			poblacion = nuevaPoblacion;   //Actualizamos el valor de la poblacion

			if (elitismo) {
				IntroducirElite(elite); //Introducir la elite de vuelta a la poblacion
			}

			ActualizarFitness(i); //Actualizamos la informacion de fitness para las graficas
		}

		return this.MejorResultado();  //Devolvemos el mejor fitness encontrado

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
		}

		return seleccionados;

	}

	private void Mutacion(Individuo[] pob) {

		switch (tipoMutacion) {
		case Uniform:
			Mutation.mutateUniform(pob, probabilidadMutacion);
			break;
		case NonUniform:
			Mutation.mutateNonUniform(pob, probabilidadMutacion);
			break;
		default:
			Mutation.mutateUniform(pob, probabilidadMutacion);
			break;

		}

	}

	private void Cruce(Individuo[] pob) {

		switch (tipoCruce) {
		case Mono:
			Crossing.MonopointCrossOver(pob, probabilidadCruce);
			break;
		case Uniform:
			Crossing.UniformCrossOver(pob, probabilidadCruce);
			
			break;
		case Aritmetic:
			
			if(info.tipoGen != Individuo.Type.Real)
			{
				System.out.println("No se puede seleccionar cruce aritmetico si no es un gen real");
				return;
			}
			
			Crossing.AritmeticCrossOver(pob, probabilidadCruce);
			break;
		case BLX:
			
			if(info.tipoGen != Individuo.Type.Real)
			{
				System.out.println("No se puede seleccionar cruce blx si no es un gen real");
				return;
			}
			
			Crossing.BLXCrossOver(pob, probabilidadCruce);
			break;
		default:
			Crossing.MonopointCrossOver(pob, probabilidadCruce);
			break;

		}

	}

	private Individuo[] SeleccionarElite() {

		int eliteSize = (int) Math.round(poblacion.length * porcentajeElite);

		Individuo[] elite = new Individuo[eliteSize];

		Individuo[] poblacionOrdenada = ClonarPoblacion(); //Ordenamos la poblacion en funcion del fitness para elegir a los mejores
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
			
			
			if (info.maximizacion)
			{
				
				if(mejor > fitnessAbsoluto[idx - 1]) //En caso de haber encontrado uno mejor actualizamos los puntos encontrados
				{
					actualizarMejor = true;
					fitnessAbsoluto[idx] = mejor;
				}
				else 
					fitnessAbsoluto[idx] = fitnessAbsoluto[idx - 1];
				
			}
			else
			{

				if(mejor < fitnessAbsoluto[idx - 1])
				{
					actualizarMejor = true;
					fitnessAbsoluto[idx] = mejor;
				}
				else 
					fitnessAbsoluto[idx] = fitnessAbsoluto[idx - 1];
				
			}
			
			
			
		} else {

			actualizarMejor = true;
			fitnessAbsoluto[idx] = mejor;
		}

		
		
		if(actualizarMejor) {
			
			
			for(int i = 0; i < poblacion[i].GetGeneCount(); i++) {
				
				puntoSolucion[i] = poblacion[indiceMejor].GetGene(i).Fenotype();

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

				//poblacion[i].InvertirFitness();
				
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
	
	public double[] PuntoSolucion() {
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

}
