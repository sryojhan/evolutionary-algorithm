package Common;

public class CalcularFitness {

	public static int numeroPuntos = 101;
	private static double[] dataset;

	public enum Funcion {

		Add, Sub, Mult
	}

	public enum Terminal {

		x, MenosDos, MenosUno, Cero, Uno, Dos
	}

	public static void GenerarDataSet() {

		if (dataset != null)
			return;

		dataset = new double[numeroPuntos];

		for (int i = 0; i < numeroPuntos; i++) {

			dataset[i] = ComputeIndex(i);
		}

	}

	public static double ComputeIndex(int idx) {

		double x = (idx - 50) / 50.0;

		double v = Math.pow(x, 4) + Math.pow(x, 3) + Math.pow(x, 2) + x + 1;

		return v;
	}

	public static double EvaluateDataset(double value) {

		if (value < -1 || value > 1)
			return 0;

		int idx = (int) ((value + 1) * 50);

		return dataset[idx];
	}

	public static class DecodificationData {

		public int idx;
		public int wrapUsados;

		private int size;
		private int maxWrap;
		
		public DecodificationData(int size, int maxWrap) {
			idx = wrapUsados = 0;
			
			this.size = size;
			this.maxWrap = maxWrap;
		}

		public int Pop() {
			
			int i = idx;
			Avanzar();
			return i;
		}
		
		public void Avanzar() {

			idx++;

			if (idx >= size) {
				idx = 0;
				wrapUsados++;
			}

		}

		public boolean isValid() {

			return wrapUsados < maxWrap;

		}
	}

	public static double Valor(Terminal terminal, double x) {

		switch (terminal) {
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

		return 0;
	}
	

	public static String ValorString(Terminal terminal) {

		switch (terminal) {
		case Cero:
			return "0";
		case Dos:
			return "2";
		case MenosDos:
			return "-2";
		case MenosUno:
			return "-1";
		case Uno:
			return "1";
		case x:
			return "x";
		}

		return "";
	}



	public static boolean SiguienteEsFuncion(int[] codones, int idx) {
		
		return codones[idx] % 2 == 0;		
	}
	
	public static Funcion GetFuncion(int value) {
		
		return Funcion.values()[value % Funcion.values().length];
	}
	
	public static Terminal GetTerminal(int value) {
		
		return Terminal.values()[value % Terminal.values().length];
	}

	

	public static String Decodificar(int[] codones, DecodificationData data, boolean esFuncion) {

		if(!data.isValid())
		{
			//System.out.println("Error: se ha sobrepasado el numero de wraps permitidos");
			
			return "0";
		}
		
		int current = codones[data.Pop()];
		
		
		if (esFuncion) {

			Funcion f = GetFuncion(current);

			
			
			boolean isFunction = SiguienteEsFuncion(codones, data.Pop());
			
			String a = Decodificar(codones, data, isFunction);
			
			isFunction = SiguienteEsFuncion(codones, data.Pop());
			
			String b = Decodificar(codones, data, isFunction);
			
			
			switch (f) {
			case Add:
				return "(" + a + " + " + b + ")";
			case Mult:
				return "(" + a + " * " + b + ")";
			case Sub:
				return "(" + a + " - " + b + ")";
			}

		} 
		
		return ValorString(GetTerminal(current));
	}
	
	
	public static double CalculateFitness(double x, int[] codones, DecodificationData data, boolean esFuncion) {


		if(!data.isValid())
		{
			//System.out.println("Error: se ha sobrepasado el numero de wraps permitidos");
			
			//Si 
			
			return 0;
		}
		
		int current = codones[data.Pop()];
		
		
		if (esFuncion) {

			Funcion f = GetFuncion(current);

			
			
			boolean isFunction = SiguienteEsFuncion(codones, data.Pop());
			
			double a = CalculateFitness(x, codones, data, isFunction);
			
			isFunction = SiguienteEsFuncion(codones, data.Pop());
			
			double b = CalculateFitness(x, codones, data, isFunction);
			
			
			switch (f) {
			case Add:
				return a + b;
			case Mult:
				return a * b;
			case Sub:
				return a - b;
			}

		} 
		
		return Valor(GetTerminal(current), x);
	}
	
	
	
	

	public static double CalculateFitness(int[] codones, int maxWrap) {

		double error = 0;

		double incremento = 2.0 / (numeroPuntos - 1);

		for (int i = 0; i < numeroPuntos; i++) {

			double x = -1 + incremento * i;

			double fitnessReal = EvaluateDataset(x);

			DecodificationData data =  new DecodificationData(codones.length, maxWrap);
			
			double fitnessCalculado = CalculateFitness(x, codones, data, true);

			if(!data.isValid()) { //Si no es valido penalizamos el fitness
				
				return 2;
			}
			
			double dif = fitnessReal - fitnessCalculado;

			error += (dif * dif) / numeroPuntos;
		}

		return error;
	}

}
