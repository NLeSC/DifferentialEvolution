package nl.esciencecenter.diffevo.statespacemodelfactories;

import nl.esciencecenter.diffevo.statespacemodels.LinearDynamicStateSpaceModel;
import nl.esciencecenter.diffevo.statespacemodels.Model;



public class LinearDynamicStateSpaceModelFactory implements ModelFactory{

	public Model create(double[] state,double[] parameterVector, double[] forcing, double[] times) {
		Model model = new LinearDynamicStateSpaceModel(state, parameterVector, forcing, times);

		return model;
	}
}