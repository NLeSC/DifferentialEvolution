package nl.esciencecenter.diffevo.likelihoodfunctionfactories;

import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;
import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunctionSSR;


public class LikelihoodFunctionSSRFactory implements LikelihoodFunctionFactory{

	public LikelihoodFunction create() {
		LikelihoodFunction likelihoodFunction = new LikelihoodFunctionSSR();

		return likelihoodFunction;
	}

}