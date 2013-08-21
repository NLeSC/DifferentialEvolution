package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.util.Arrays;


import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionDoubleNormalModelFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;

import org.junit.Test;

public class DoubleNormalModelTest {

	private int nPop;
	private EvalResults evalResults;

	public DoubleNormalModelTest(){
		int nGens = 5000;
		nPop = 25;

		double[] lowerBoundsParSpace = new double[]{-20};
		double[] upperBoundsParSpace = new double[]{18};
		String[] parNames = new String[]{"theta"};
		ParSpace parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
		parSpace.divideIntoIntervals(50);
		LikelihoodFunctionFactory likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionDoubleNormalModelFactory();
		long seed = 0;
		// run the optimization:
		DiffEvo diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory, seed);
		evalResults = diffEvo.runOptimization();
	}
	
	
	@Test
	public void testnPop() {
		int nPopExpected = nPop;
		int nPopActual = evalResults.getnPop();
		assertTrue(nPopExpected==nPopActual);
	}
	
	@Test
	public void testIfBestResultIsNearOptimum() {
		double optimumExpected = 5.0;
		double tolerance = 0.01;
		
		int[] bestIndices = evalResults.sampleIdentifiersOfBest();
		double optimumActual = evalResults.getParameterCombination(bestIndices[0])[0];
		double a = optimumExpected;
		double b = optimumActual;
		boolean testResult = Math.abs(a-b)<tolerance;
		assertTrue(testResult);
	}
	
	
	@Test
	public void testIfModeIsNearTrueMode() {
		double modeExpected = 5.0;
		double tolerance = 1.5;
		
		int nHistory = 10000;
		int nResults = evalResults.size();
		int nPars = evalResults.getParSpace().getNumberOfPars();
		double[][] parameterValues = new double[nHistory][nPars];
		for (int iHistory=nResults-nHistory;iHistory<nResults;iHistory++){
			parameterValues[iHistory-nResults+nHistory] = evalResults.getParameterCombination(iHistory);
		}
		double[] modesActual = calcModes(parameterValues);

		double a = modeExpected;
		double b = modesActual[0];
		boolean testResult = Math.abs(a-b)<tolerance;
		assertTrue(testResult);
	}
	

	private double[] calcModes(double[][] x){
		
		// note that this is not the same as the most frequently occurring parameter combination
		
		int nCols = x[0].length;
		int nRows = x.length;
		double[] modes = new double[nCols];
		
		for (int iCol=0;iCol<nCols;iCol++){
			double[] tmp = new double[nRows]; 
			for (int iRow=0;iRow<nRows;iRow++){
				tmp[iRow] = x[iRow][iCol];
			}
			modes[iCol] = calcMode(tmp);
		}
		return modes;
	}	
	
	private double calcMode(double[] x){
		
		Arrays.sort(x);
		int nx = x.length;
		boolean oddNumberOfElements = (nx % 2) == 1; 
		if (oddNumberOfElements){
			// nx is odd
			return x[(nx-1)/2];
		}
		else {
			// nx is even
			double a = x[(int) nx/2-1]; 
			double b = x[(int) nx/2+1]; 
			return (a + b)/2;
		}
		
	}

}



 

