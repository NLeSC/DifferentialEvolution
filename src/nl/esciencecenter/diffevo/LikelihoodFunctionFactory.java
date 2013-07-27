package nl.esciencecenter.diffevo;

import nl.esciencecenter.diffevo.statespacemodels.LikelihoodFunction;

public interface LikelihoodFunctionFactory {
	
	public LikelihoodFunction create();

}
