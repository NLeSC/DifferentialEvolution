package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionSingleNormalModelFactory;
import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;

import org.junit.Test;

public class SingleNormalModelTest {

	private int nGens;
	private int nPop;
	private ParSpace parSpace;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private DiffEvo diffEvo;
	private EvalResults evalResults;

	public SingleNormalModelTest(){
		nGens = 3000;
		nPop = 50;
		double[] lowerBoundsParSpace = new double[]{-50};
		double[] upperBoundsParSpace = new double[]{40};
		String[] parNames = new String[]{"theta"};
		parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
		parSpace.divideIntoIntervals(50);
		likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSingleNormalModelFactory();
	
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
	public void testIfBestResultIsNearOptimum() {
		double optimumExpected = -10.0;
		double tolerance = 0.001;
		
		int[] bestIndices = evalResults.sampleIdentifiersOfBest();
		double[] optimumActual = evalResults.getParameterCombination(bestIndices[0]);
		
		double a = optimumExpected;
		double b = optimumActual[0];
		assertTrue(Math.abs(a-b)<tolerance);
		
	}

	@Test
	public void testIfMeanIsNearTrueMean() {
		double meanExpected = -10.0;
		double tolerance = 0.75;
		
		int nHistory = 10000;
		int nResults = evalResults.size();
		double[] parameterValues = new double[nHistory];
		for (int iHistory=nResults-nHistory;iHistory<nResults;iHistory++){
			parameterValues[iHistory-nResults+nHistory] = evalResults.getParameterCombination(iHistory)[0];
		}
		double meanActual = calcMean(parameterValues);
		
		double a = meanExpected;
		double b = meanActual;
		assertTrue(Math.abs(a-b)<tolerance);
		
	}

	@Test
	public void testIfParameterUncertaintyIsAccurate() {
		
		// little bit tricky with diffevo, because standard deviation is greatly affected by 
		// outliers
		
		// given that the true mean is -10.0 and the true standard deviation is 3.0, I'm 
		// calculating the sample standard deviation based on parMu1 + [-1,1]*3*parSigma1  
		double tolerance = 2.0;
		double parMu1 = -10.0;
		double parSigma1 = 3.0;
	
		int nHistory = 1000;
		int nResults = evalResults.size();
		double[] parameterValuesTmp = new double[nHistory];
		int k = 0;
		double lowBound = parMu1-3*parSigma1;
		double highBound = parMu1+3*parSigma1;
		for (int iHistory=nResults-nHistory;iHistory<nResults;iHistory++){
			double x = evalResults.getParameterCombination(iHistory)[0];
			if (lowBound<=x & x<highBound){
				parameterValuesTmp[k] = x;
				k = k + 1;
			}
		}

		double[] parameterValues = new double[k-1];
		System.arraycopy(parameterValuesTmp, 0,parameterValues, 0, k-1);
		double stdActual = calcStandardDeviation(parameterValues);
		
		double a = parSigma1;
		double b = stdActual;
		assertTrue(a<b);
		assertTrue(b-a<tolerance);
		
	}
	
	@Test
	public void testIfObjScoreIsAccurate() {
		double[] optimumExpected = new double[]{-10.0};
		double parSigma1 = 3.0;
		double objScoreExpected = Math.log(1.0/(parSigma1*Math.sqrt(2*Math.PI)));
		
		LikelihoodFunction model = likelihoodFunctionFactory.create();
		
		double objScoreActual = model.evaluate(optimumExpected);
		
		assertTrue(objScoreExpected==objScoreActual);
		
	}
	
	private double calcMean(double[] x){
		int nx = x.length;
		double sum = 0;
		for (int ix=0;ix<nx;ix++){
			sum = sum + x[ix];
		}
		return sum/nx;
	}

	private double calcStandardDeviation(double[] x){
		// calculates the corrected sample standard deviation
		// from "http://en.wikipedia.org/wiki/Standard_deviation#Corrected_sample_standard_deviation" (21 Aug 2013)
		double meanx = calcMean(x);
		int nx = x.length;
		double sum = 0;
		for (int ix=0;ix<nx;ix++){
			sum = sum + Math.pow(x[ix]-meanx,2);
		}
		return Math.sqrt(sum/(nx-1));
	}
	
	

}
