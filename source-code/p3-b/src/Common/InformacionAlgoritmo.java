package Common;


public class InformacionAlgoritmo {

	public boolean maximizacion;
	
	public int maxWraps;
	
	public InformacionAlgoritmo(boolean maximizacion, int maxWraps){
		
		this.maximizacion = maximizacion;
		this.maxWraps = maxWraps;
	}
	
	public FitnessFunction fitnessFunction;
	
}
