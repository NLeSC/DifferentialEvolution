package nl.esciencecenter.diffevo.statespacemodels;

public interface Model {
	
	public double calcLogLikelihood(double[] initState, double[] parameterVector, double[][] forcing, double[] priorTimes);
	
	public String getName();
	

}
