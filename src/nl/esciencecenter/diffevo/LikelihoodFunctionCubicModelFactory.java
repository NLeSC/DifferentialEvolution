package nl.esciencecenter.diffevo;

import likelihoodfunctions.LikelihoodFunction;
import likelihoodfunctions.LikelihoodFunctionCubicModel;
import likelihoodfunctions.LikelihoodFunctionFactory;

public class LikelihoodFunctionCubicModelFactory implements
		LikelihoodFunctionFactory {

	@Override
	public LikelihoodFunction create() {
		LikelihoodFunction likelihoodFunction = new LikelihoodFunctionCubicModel();
		return likelihoodFunction;
	}

}
