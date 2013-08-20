package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.io.File;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionRastriginModelFactory;

import org.junit.Test;

public class RastriginModelTest {

	private int nGens;
	private int nPop;
	private ParSpace parSpace;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private DiffEvo diffEvo;
	private EvalResults evalResults;
	private DiffEvoVisualization vis;
	private DiffEvoOutputWriters writers;
	private File file;

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


}
