package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.io.File;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionSSRFactory;
import nl.esciencecenter.diffevo.statespacemodelfactories.LinearDynamicStateSpaceModelFactory;
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;

import org.junit.Test;

public class DiffEvoTest {
	
	private int nPopExpected;
	private int nGensExpected;
	private ParSpace parSpace;
	private ModelFactory modelFactory = null;
	private LikelihoodFunctionFactory likelihoodFunctionFactory = null;
	private DiffEvo diffEvo;

	
	public DiffEvoTest(){
		
		nGensExpected = 10;
		nPopExpected = 50;
		File file  = new File("test"+File.separator+"data"+File.separator+"lineartank.eas");
		DataReader reader = new DataReader(file);
		double[][] data = reader.getData();
		
		double[] initState = new double[] {30};
		double[] times = data[0];
		double[] assimilate = data[1];
		double[][] obs = new double[][]{data[3]};
		double[] forcing = data[4];				
		double[] lowerBounds = new double[] {110};
		double[]upperBounds = new double[] {180};
		String[] parNames = new String[] {"resistance"};
		parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		parSpace.divideIntoIntervals(100);

		modelFactory = (ModelFactory) new LinearDynamicStateSpaceModelFactory();
		likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSSRFactory();
		diffEvo = new DiffEvo(nGensExpected, nPopExpected, parSpace, initState, forcing, times, assimilate, obs, modelFactory, likelihoodFunctionFactory);
		
		
		// 
		int nPopActual = diffEvo.getnPop();
		assertTrue(nPopActual==nPopExpected);
		
		
		
	}

//	@Test
//	public void testDiffEvoIntIntParSpaceLikelihoodFunctionFactory() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDiffEvoIntIntParSpaceDoubleArrayDoubleArrayDoubleArrayDoubleArrayDoubleArrayArrayModelFactoryLikelihoodFunctionFactory() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testInitializeParents() {
		
		diffEvo.initializeParents();
		
	}

//	@Test
//	public void testProposeOffSpring() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUpdateParentsWithProposals() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testPrintEvalResults() {

		diffEvo.start();
		diffEvo.printEvalResults();

	}

	@Test
	public void testWriteEvalResultsToTextFile() {

		File file = new File("test"+File.separator+"out"+File.separator+"evalresults.txt");
		diffEvo.start();
		diffEvo.writeEvalResultsToJSON(file);

	}

	@Test
	public void testWriteEvalResultsToJSON() {
		
		File file = new File("test"+File.separator+"out"+File.separator+"evalresults.json");
		diffEvo.start();
		diffEvo.writeEvalResultsToJSON(file);
	}

//	@Test
//	public void testScatterEvalObj() {
//		diffEvo.start();
//		diffEvo.scatterEvalObj();
//	}
//
//	@Test
//	public void testScatterEvalPar() {
//		
//		int nPars = parSpace.getNumberOfPars();
//		diffEvo.start();
//		for (int iPar=0;iPar<nPars;iPar++){
//			diffEvo.scatterEvalPar(iPar);
//		}
//	}

	@Test
	public void testCalcHistogram() {
		diffEvo.start();
		int iPar = 0;
		int[] hist = diffEvo.calcHistogram(iPar);
		
		int nBinsActual = hist.length;
		int nBinsExpected = parSpace.getnBins(iPar);
		
		assertTrue(nBinsActual==nBinsExpected);
		
		int sum = 0;
		for (int iBin=0;iBin<nBinsActual;iBin++){
			sum = sum + hist[iBin];
		}
		assertTrue(nPopExpected*nGensExpected==sum);
		
	}

//	@Test
//	public void testMargHist() {
//		diffEvo.start();
//		diffEvo.margHist();
//	}

	@Test
	public void testGetnPop() {
		int nPopActual = diffEvo.getnPop();
		assertTrue(nPopActual==nPopExpected);
	}

	@Test
	public void testStart() {
		diffEvo.start();
	
	}

}
