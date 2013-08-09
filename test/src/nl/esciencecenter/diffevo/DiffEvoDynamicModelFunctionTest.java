package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.io.File;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionSSRFactory;
import nl.esciencecenter.diffevo.statespacemodelfactories.LinearDynamicStateSpaceModelFactory;
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;

import org.junit.Test;

public class DiffEvoDynamicModelFunctionTest {
	
	private DiffEvo diffEvo;
	private int nPopExpected;
	
	public DiffEvoDynamicModelFunctionTest(){
		
		int nGens = 2;
		nPopExpected = 5;
		File file  = new File("data"+File.separator+"lineartank.eas");
		DataReader reader = new DataReader(file);
		double[][] data = reader.getData();
		ParSpace parSpace;
		StateSpace stateSpace;

		double[] initState = new double[] {30};
		double[] times = data[0];
		double[] assimilate = data[1];
		double[][] obs = new double[][]{data[3]};
		double[] forcing = data[4];
		{
			double[] lowerBoundsParSpace = new double[] {110};
			double[] upperBoundsParSpace = new double[] {180};
			String[] parNames = new String[] {"resistance"};
			parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
			parSpace.divideIntoIntervals(100);
		}
		{
			double[] lowerBoundsStateSpace = new double[] {0};
			double[] upperBoundsStateSpace = new double[] {100};
			String[] stateNames = new String[] {"waterlevel"};
			stateSpace = new StateSpace(lowerBoundsStateSpace,upperBoundsStateSpace,stateNames);
		}
		ModelFactory modelFactory = (ModelFactory) new LinearDynamicStateSpaceModelFactory();
		LikelihoodFunctionFactory likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSSRFactory();
		
		long randomSeed = 0;
		diffEvo = new DiffEvo(nGens, nPopExpected, parSpace, stateSpace, initState, forcing, times, assimilate, obs, 
				modelFactory, likelihoodFunctionFactory, randomSeed);
	}

	
	@Test
	public void testDiffEvoDynamicModelFunction() {
		EvalResults evalResults = diffEvo.runOptimization();
		
		File file = new File("data"+File.separator+"testDiffEvoDynamicModelFunction.eas");
		DataReader reader = new DataReader(file);
		
		int nCols = evalResults.getParSpace().getNumberOfPars()+1+1;
		int nResults = evalResults.getNumberOfEvalResults();
		
		assertTrue(nCols==3);
		assertTrue(nResults==10);
		
		for (int iResult=0;iResult<nResults;iResult++){
			double[] row = reader.getRow(iResult);
			double[] expecteds = row.clone();
			double[] actuals = new double[nCols];
			actuals[0] = (double)iResult;
			actuals[1] = evalResults.getParameterCombination(iResult)[0];
			actuals[2] = evalResults.getObjScore(iResult);
			
			assertArrayEquals(expecteds, actuals, 0.0000000000);
		}

	}

	@Test
	public void testGetnPop() {
		
		EvalResults evalResults = diffEvo.runOptimization();
		int nPopActual = evalResults.getnPop(); 
		assertTrue(nPopActual==nPopExpected);
	}
	
	
	
}
