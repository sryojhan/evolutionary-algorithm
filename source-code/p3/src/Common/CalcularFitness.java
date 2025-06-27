package Common;

public class CalcularFitness {

	
	public static int numeroPuntos = 101;
	private static double[] dataset;
	
	public static void GenerarDataSet() {
		
		if(dataset != null) return;
		
		
		
		dataset = new double[numeroPuntos];
		
		
		for(int i = 0; i < numeroPuntos; i++) {
			
			
			dataset[i] = ComputeIndex(i);
		}
		
		
	}

	
	public static double ComputeIndex(int idx) {
		
		double x = (idx - 50) / 50.0;
		
		double v = Math.pow(x, 4) + Math.pow(x, 3) + Math.pow(x, 2) + x + 1;
		
		return v;
	}
	
	public static double EvaluateDataset(double value) {
		
		
		if(value < -1 || value > 1) return 0;
		
		int idx = (int)((value + 1) * 50);
		
		return dataset[idx];
	}
	
	
	
	public static double CalcularFitnessDeRama(Nodo rama, double x) {
		
		
		if(rama.GetValor().esFuncion())
		{
			
			switch(rama.GetValor().funcion) {
			case add:
				return CalcularFitnessDeRama(rama.izquierdo, x) + CalcularFitnessDeRama(rama.derecho, x);
			case mult:
				
				return CalcularFitnessDeRama(rama.izquierdo, x) * CalcularFitnessDeRama(rama.derecho, x);
			case sub:
				
				return CalcularFitnessDeRama(rama.izquierdo, x) - CalcularFitnessDeRama(rama.derecho, x);
			}
			
		}
		else {
			
			
			switch(rama.GetValor().terminal) {
			case Cero:
				return 0;
			case Dos:
				return 2;
			case MenosDos:
				return -2;
			case MenosUno:
				return -1;
			case Uno:
				return 1;
			case x:
				return x;
			}
		}
		
		return 0;
	}
	
	
	public static double CalculateFitness(Nodo raiz) {
		
		
		double error = 0;
		
		double incremento = 2.0 / (numeroPuntos - 1);
		
		for(int i = 0; i < numeroPuntos; i++) {
			
			double x = -1 + incremento * i;

			double fitnessReal = EvaluateDataset(x);
			
			double fitnessCalculado = CalcularFitnessDeRama(raiz, x);
			
			double dif = fitnessReal - fitnessCalculado;
			
			error += (dif * dif) / numeroPuntos;
		}
		
		
		return error;
	}
	
	
}
