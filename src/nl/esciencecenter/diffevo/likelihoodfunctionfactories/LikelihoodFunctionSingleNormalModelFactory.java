package nl.esciencecenter.diffevo.likelihoodfunctionfactories;

import nl.esciencecenter.diffevo.likelihoodfunctions.*;


public class LikelihoodFunctionSingleNormalModelFactory implements
		LikelihoodFunctionFactory {

	@Override
	public LikelihoodFunction create() {
		return new LikelihoodFunctionSingleNormalModel();
	}

}
