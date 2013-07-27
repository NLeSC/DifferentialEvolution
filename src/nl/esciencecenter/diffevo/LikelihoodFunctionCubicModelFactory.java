package nl.esciencecenter.diffevo;

import nl.esciencecenter.diffevo.likelihoodfunctions.*;


public class LikelihoodFunctionCubicModelFactory implements
		LikelihoodFunctionFactory {

	@Override
	public LikelihoodFunction create() {
		LikelihoodFunction likelihoodFunction = new LikelihoodFunctionCubicModel();
		return likelihoodFunction;
	}

}
