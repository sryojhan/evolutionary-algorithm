package Common;

@FunctionalInterface
public interface FitnessFunction{
	double CalculateFitness(int[] input, int wraps);
}
