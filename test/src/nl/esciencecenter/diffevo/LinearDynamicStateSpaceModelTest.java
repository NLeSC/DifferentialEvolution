package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.io.File;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionSSRFactory;
import nl.esciencecenter.diffevo.statespacemodelfactories.LinearDynamicStateSpaceModelFactory;
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;

import org.junit.Test;

public class LinearDynamicStateSpaceModelTest {

	private int nGens;
	private int nPop;
	private ParSpace parSpace;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private DiffEvo diffEvo;
	private EvalResults evalResults;
	private File file;
	private double[] initState;
	private double[] times;
	private double[] assimilate;
	private double[] forcing;
	private double obs[][];
	private StateSpace stateSpace;
	private ModelFactory modelFactory;

	public LinearDynamicStateSpaceModelTest(){
		nGens = 500;
		nPop = 50;
		file  = new File("data"+File.separator+"lineartank.eas");
		DataReader reader = new DataReader(file);
		double[][] data = reader.getData();
		initState = new double[] {30};
		times = data[0];
		assimilate = data[1];
		obs = new double[][]{data[3]};
		forcing = data[4];

		double[] lowerBoundsParSpace = new double[] {10};
		double[] upperBoundsParSpace = new double[] {500};
		String[] parNames = new String[] {"resistance"};
		parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
		parSpace.divideIntoIntervals(100);
		
		double[] lowerBoundsStateSpace = new double[] {0};
		double[] upperBoundsStateSpace = new double[] {100};
		String[] stateNames = new String[] {"waterlevel"};
		stateSpace = new StateSpace(lowerBoundsStateSpace,upperBoundsStateSpace,stateNames);

		modelFactory = (ModelFactory) new LinearDynamicStateSpaceModelFactory();
		likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSSRFactory();
		
		// run the optimization:
		diffEvo = new DiffEvo(nGens, nPop, parSpace, stateSpace, initState, forcing, times, assimilate, obs, modelFactory, likelihoodFunctionFactory);
	
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
		double optimumExpected = 149.39756262040834;
		double tolerance = 0.25;
		
		int[] bestIndices = evalResults.sampleIdentifiersOfBest();
		double[] optimumActual = evalResults.getParameterCombination(bestIndices[0]);
		
		double a = optimumExpected;
		double b = optimumActual[0];
		assertTrue(Math.abs(a-b)<tolerance);
		
	}

	@Test
	public void testIfMeanIsNearTrueMean() {
		double meanExpected = 149.39756262040834;
		double tolerance = 5.0;
		
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

	private double calcMean(double[] x){
		int nx = x.length;
		double sum = 0;
		for (int ix=0;ix<nx;ix++){
			sum = sum + x[ix];
		}
		return sum/nx;
	}

}
