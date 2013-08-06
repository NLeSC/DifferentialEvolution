package nl.esciencecenter.diffevo.likelihoodfunctionfactories;

import nl.esciencecenter.diffevo.likelihoodfunctions.*;


public class LikelihoodFunctionRosenbrockModelFactory implements
		LikelihoodFunctionFactory {

	@Override
	public LikelihoodFunction create() {
		return new LikelihoodFunctionRosenbrockModel();
	}

}
