package nl.esciencecenter.diffevo.likelihoodfunctions;

public class LikelihoodFunctionSSRFactory implements LikelihoodFunctionFactory{

	public LikelihoodFunction create() {
		LikelihoodFunction likelihoodFunction = new LikelihoodFunctionSSR();

		return likelihoodFunction;
	}

}