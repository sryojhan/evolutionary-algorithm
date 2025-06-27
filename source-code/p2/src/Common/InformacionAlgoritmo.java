package Common;


public class InformacionAlgoritmo {

	
	public int numeroElementos;

	public boolean maximizacion;
	
	public InformacionAlgoritmo(int numeroElementos, boolean maximizacion){
		
		
		this.numeroElementos = numeroElementos;
		this.maximizacion = maximizacion;
		
	}
	
	public FitnessFunction fitnessFunction;
	
}
