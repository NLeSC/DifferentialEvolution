package nl.esciencecenter.diffevo.likelihoodfunctionfactories;

import nl.esciencecenter.diffevo.likelihoodfunctions.*;


public class LikelihoodFunctionCubicModelFactory implements
		LikelihoodFunctionFactory {

	@Override
	public LikelihoodFunction create() {
		return new LikelihoodFunctionCubicModel();
	}

}
