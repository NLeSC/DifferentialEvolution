package nl.esciencecenter.diffevo.statespacemodels;


import nl.esciencecenter.diffevo.ModelFactory;

public class LinearDynamicStateSpaceModelFactory implements ModelFactory{

	public Model create(double[] state,double[] parameterVector, double[] forcing, double[] times) {
		Model model = new LinearDynamicStateSpaceModel(state, parameterVector, forcing, times);

		return model;
	}
}