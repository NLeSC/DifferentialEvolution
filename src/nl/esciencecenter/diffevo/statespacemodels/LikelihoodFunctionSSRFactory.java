package nl.esciencecenter.diffevo.statespacemodels;


import nl.esciencecenter.diffevo.LikelihoodFunctionFactory;

public class LikelihoodFunctionSSRFactory implements LikelihoodFunctionFactory{

	public LikelihoodFunction create() {
		LikelihoodFunction likelihoodFunction = new LikelihoodFunctionSSR();

		return likelihoodFunction;
	}



}