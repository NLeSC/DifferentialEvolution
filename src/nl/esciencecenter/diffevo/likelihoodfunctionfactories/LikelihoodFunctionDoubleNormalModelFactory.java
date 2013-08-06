package nl.esciencecenter.diffevo.likelihoodfunctionfactories;

import nl.esciencecenter.diffevo.likelihoodfunctions.*;


public class LikelihoodFunctionDoubleNormalModelFactory implements
		LikelihoodFunctionFactory {

	@Override
	public LikelihoodFunction create() {
		return  new LikelihoodFunctionDoubleNormalModel();
	}

}
