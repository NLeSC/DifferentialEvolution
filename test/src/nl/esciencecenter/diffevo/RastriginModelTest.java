package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionRastriginModelFactory;
import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;

import org.junit.Test;

public class RastriginModelTest {

	private int nGens;
	private int nPop;
	private ParSpace parSpace;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private DiffEvo diffEvo;
	private EvalResults evalResults;

	public RastriginModelTest(){
		nGens = 3000;
		nPop = 50;
		double[] lowerBoundsParSpace = new double[]{-5.12,-5.12};
		double[] upperBoundsParSpace  = new double[]{5.12,5.12};
		String[] parNames = new String[]{"p1","p2"};
		parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
		parSpace.divideIntoIntervals(200);
		likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionRastriginModelFactory();
	
		// run the optimization:
		diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
		evalResults = diffEvo.runOptimization();
	
//		// do some visualization of the results:
//		vis = new DiffEvoVisualization(evalResults);
//		vis.matrixOfScatterParPar();
//		vis.matrixOfHeatmapParPar();
//		vis.margHist();
//		vis.scatterEvalObj();
//		for (int iPar=0;iPar<parSpace.getNumberOfPars();iPar++){
//			vis.scatterEvalPar(iPar); 
//		}
//
//		// do some printing to file and standard out:
//		writers = new DiffEvoOutputWriters(evalResults);
//		writers.printEvalResults();
//		writers.printEvalResults();
//		file = new File("out"+File.separator+"evalresults.json");
//		writers.writeEvalResultsToJSON(file);
//		file = new File("out"+File.separator+"evalresults.txt");
//		writers.writeEvalResultsToTextFile(file);

	}
	
	
	@Test
	public void testnPop() {
		int nPopExpected = nPop;
		int nPopActual = evalResults.getnPop();
		assertTrue(nPopExpected==nPopActual);
	}
	
	@Test
	public void testIfResultIsNearOptimum() {
		double[] optimumExpected = new double[]{0.0,0.0};
		double[] tolerance = new double[]{0.01,0.01};
		
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
		double[] optimumExpected = new double[]{0.0,0.0};
		double objScoreExpected = 0.0;
		
		LikelihoodFunction model = likelihoodFunctionFactory.create();
		
		double objScoreActual = model.evaluate(optimumExpected);
		
		assertTrue(objScoreExpected==objScoreActual);
		
	}
	
	
	


}
