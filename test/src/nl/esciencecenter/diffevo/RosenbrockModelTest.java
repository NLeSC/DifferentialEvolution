package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionRosenbrockModelFactory;
import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;

import org.junit.Test;

public class RosenbrockModelTest {

	private int nGens;
	private int nPop;
	private ParSpace parSpace;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private DiffEvo diffEvo;
	private EvalResults evalResults;

	public RosenbrockModelTest(){
		nGens = 3000;
		nPop = 50;
		double[] lowerBoundsParSpace = new double[]{-50,-40};
		double[] upperBoundsParSpace = new double[]{50,80};
		String[] parNames = new String[]{"p1","p2"};
		parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
		parSpace.divideIntoIntervals(500);
		likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionRosenbrockModelFactory();
	
		// run the optimization:
		diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
		evalResults = diffEvo.runOptimization();
	
	}
	
	
	@Test
	public void testnPop() {
		int nPopExpected = nPop;
		int nPopActual = evalResults.getnPop();
		assertTrue(nPopExpected==nPopActual);
	}
	
	@Test
	public void testIfResultIsNearOptimum() {
		double[] optimumExpected = new double[]{1.0,1.0};
		double[] tolerance = new double[]{0.05,0.05};
		
		int nPars = evalResults.getParSpace().getNumberOfPars();
		int[] bestIndices = evalResults.sampleIdentifiersOfBest();
		double[] optimumActual = evalResults.getParameterCombination(bestIndices[0]);
		
		for (int iPar=0;iPar<nPars;iPar++){
			double a = optimumExpected[iPar];
			double b = optimumActual[iPar];
			assertTrue(Math.abs(a-b)<tolerance[iPar]);
		}
		
	}

	@Test
	public void testIfObjScoreIsAccurate() {
		double[] optimumExpected = new double[]{1.0,1.0};
		double objScoreExpected = 0.0;
		
		LikelihoodFunction model = likelihoodFunctionFactory.create();
		
		double objScoreActual = model.evaluate(optimumExpected);
		
		assertTrue(objScoreExpected==objScoreActual);
		
	}

}
