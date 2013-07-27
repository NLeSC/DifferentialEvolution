package nl.esciencecenter.diffevo.statespacemodels;

import nl.esciencecenter.diffevo.statespacemodels.Model;

public interface ModelFactory {
	
	public Model create(double[] initState, double[] parameterVector, double[] forcing, double[] times);

}
