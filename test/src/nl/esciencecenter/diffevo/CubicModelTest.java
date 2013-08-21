package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.util.Arrays;


import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionCubicModelFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;

import org.junit.Test;

public class CubicModelTest {

	private int nPop;
	private EvalResults evalResults;

	public CubicModelTest(){
		int nGens = 5000;
		nPop = 25;

		double[] lowerBoundsParSpace = new double[]{-20,-40,-80,-120};
		double[] upperBoundsParSpace = new double[]{ 20, 40, 80, 120};
		String[] parNames = new String[]{"a","b","c","d"};
		ParSpace parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
		parSpace.divideIntoIntervals(new int[]{50,50,50,50});
		
		LikelihoodFunctionFactory likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionCubicModelFactory();
	
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
		double[] optimumExpected = new double[]{-1.0,7.0,-5.0,9.0};
		double[] tolerance =       new double[]{ 0.2,1.5, 3.0,5.0};
		
		int[] bestIndices = evalResults.sampleIdentifiersOfBest();
		double[] optimumActual = evalResults.getParameterCombination(bestIndices[0]);
		
		System.out.println("[a,b,c,d] = ["+optimumActual[0]+
                ","+optimumActual[1]+
                ","+optimumActual[2]+
                ","+optimumActual[3]+"]");
		
		int nPars = evalResults.getParSpace().getNumberOfPars();
		for (int iPar=0;iPar<nPars;iPar++){
			double a = optimumExpected[iPar];
			double b = optimumActual[iPar];
			boolean testResult = Math.abs(a-b)<tolerance[iPar];
			if (!testResult){
				System.out.println("best [a,b,c,d] = ["+optimumActual[0]+
                                                    ","+optimumActual[1]+
                                                    ","+optimumActual[2]+
                                                    ","+optimumActual[3]+"]");
			}
			assertTrue(testResult);
		}
	}
	
	
	@Test
	public void testIfModeIsNearTrueMode() {
		double[] modesExpected = new double[]{-1.0,7.0,-5.0, 9.0};
		double[] tolerance =     new double[]{ 0.2,1.5, 7.0,14.0};
		
		int nHistory = 10000;
		int nResults = evalResults.size();
		int nPars = evalResults.getParSpace().getNumberOfPars();
		double[][] parameterValues = new double[nHistory][nPars];
		for (int iHistory=nResults-nHistory;iHistory<nResults;iHistory++){
			parameterValues[iHistory-nResults+nHistory] = evalResults.getParameterCombination(iHistory);
		}
		double[] modesActual = calcModes(parameterValues);
		
		System.out.println("modes [a,b,c,d] = ["+modesActual[0]+
                                             ","+modesActual[1]+
                                             ","+modesActual[2]+
                                             ","+modesActual[3]+"]");

		for (int iPar=0;iPar<nPars;iPar++){
			double a = modesExpected[iPar];
			double b = modesActual[iPar];
			boolean testResult = Math.abs(a-b)<tolerance[iPar];
			if (!testResult){
				System.out.println("[a,b,c,d] = ["+modesActual[0]+
                                               ","+modesActual[1]+
                                               ","+modesActual[2]+
                                               ","+modesActual[3]+"]");
			}
			assertTrue(testResult);
		}
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



 

