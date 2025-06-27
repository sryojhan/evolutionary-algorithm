package Common;


public class InformacionAlgoritmo {

	
	public int numeroDimensiones;
	public double[] minimos;
	public double[] maximos;
	
	public boolean maximizacion;
	
	public InformacionAlgoritmo(int numeroDimensiones, boolean maximizacion){
		
		
		this.numeroDimensiones = numeroDimensiones;
		
		minimos = new double[numeroDimensiones];
		maximos = new double[numeroDimensiones];
		
		this.maximizacion = maximizacion;
		
		this.tipoGen = Individuo.Type.Binario;
	}
	
	public Individuo.Type tipoGen;
	
	public FitnessFunction fitnessFunction;
	
}
