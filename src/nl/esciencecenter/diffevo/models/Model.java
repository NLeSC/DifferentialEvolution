package nl.esciencecenter.diffevo.models;

public interface Model {
	
	public double calcLogLikelihood(double[] x);
	
	public String getName();

}
