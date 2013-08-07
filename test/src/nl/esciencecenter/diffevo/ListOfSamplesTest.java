package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ListOfSamplesTest {

	private static int nPopExpected;
	private static ParSpace parSpaceExpected;
	private static int nDimsExpected;
	
	public ListOfSamplesTest(){
		nPopExpected = 50;
		double[] lowerBounds = new double[]{-20,-40,-80,-120};
		double[] upperBounds = new double[]{ 20, 40, 80, 120};
		String[] parNames = new String[]{"a","b","c","d"};
		parSpaceExpected = new ParSpace(lowerBounds,upperBounds,parNames);
		nDimsExpected = lowerBounds.length;
		
	}
	
	
	@Test
	public void testListOfSamples() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		int nDimsActual = listOfSamples.getnDims();
		assertTrue(nDimsActual==nDimsExpected);
		 
		int nPopActual = listOfSamples.getnPop();
		assertTrue(nPopActual == nPopExpected);
		
		ParSpace parSpaceActual = listOfSamples.getparSpace();

		assertTrue(parSpaceActual.equals(parSpaceExpected));

		int nPars = parSpaceActual.getNumberOfPars();
		
		List<Sample> sampleList = listOfSamples.getSampleList(); 
		
		int sampleCounterExpected = -1;
		
		for (int iPop=0;iPop<nPopActual;iPop++){
			
			int sampleCounterActual = sampleList.get(iPop).getSampleCounter(); 
			assertTrue(sampleCounterActual==sampleCounterExpected);
			
			double[] parameterVectorActual = sampleList.get(iPop).getParameterVector();
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(Double.isNaN(parameterVectorActual[iPar]));
			}
			
			double objScoreActual = sampleList.get(iPop).getObjScore();
			assertTrue(Double.isNaN(objScoreActual));
			
		}
		
	}

	@Test
	public void testAdd() {
		
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		int nParsActual = listOfSamples.getparSpace().getNumberOfPars();

		Sample sampleExpected = new Sample(nDimsExpected);
		
		int sampleCounterExpected = 51;
		double[] parameterVectorExpected = new double[]{-0.11,1.2,-13.0,140};
		double objScoreExpected = -12345.6789;
		
		sampleExpected.setSampleCounter(sampleCounterExpected);
		sampleExpected.setParameterVector(parameterVectorExpected);
		sampleExpected.setObjScore(objScoreExpected);
		
		listOfSamples.add(sampleExpected);
		
		assertTrue(listOfSamples.getSize()==nPopExpected+1);

		double[] parameterVectorActual = listOfSamples.getParameterVector(nPopExpected);
		for (int iPar=0; iPar<nParsActual;iPar++){
			assertTrue(parameterVectorActual[iPar] == parameterVectorExpected[iPar]);
		}
		
		double objScoreActual = listOfSamples.getObjScore(nPopExpected);
		assertTrue(objScoreActual==objScoreExpected);

	}

	@Test
	public void testSetParameterVector() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		double[] parameterVectorExpected = new double[]{-1.0,2.0,-3.0,4.0};
		listOfSamples.setParameterVector(0, parameterVectorExpected);
		
		double[] parameterVectorActual = listOfSamples.getParameterVector(0);
		
		int nPars = listOfSamples.getparSpace().getNumberOfPars();
		
		for (int iPar=0;iPar<nPars;iPar++){
			assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
		}
		
	}

	@Test
	public void testGetParameterVector() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		double[] parameterVectorExpected = new double[]{-1.0,2.0,-3.0,4.0};
		listOfSamples.setParameterVector(0, parameterVectorExpected);
		
		double[] parameterVectorActual = listOfSamples.getParameterVector(0);
		
		int nPars = listOfSamples.getparSpace().getNumberOfPars();
		
		for (int iPar=0;iPar<nPars;iPar++){
			assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
		}

	}

	@Test
	public void testSetObjScore() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		double objScoreExpected = -123.456;
		listOfSamples.setObjScore(0, objScoreExpected);
		
		double objScoreActual = listOfSamples.getObjScore(0);
		
		assertTrue(objScoreActual==objScoreExpected);

	}

	@Test
	public void testGetObjScore() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		double objScoreExpected = -123.456;
		listOfSamples.setObjScore(0, objScoreExpected);
		
		double objScoreActual = listOfSamples.getObjScore(0);
		
		assertTrue(objScoreActual==objScoreExpected);
	}

	@Test
	public void testCalcObjScore() {
		//TODO
	}

	@Test
	public void testGetSize() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		int nPopActual = listOfSamples.getSize();
		
		assertTrue(nPopActual==nPopExpected);

	}

	@Test
	public void testGetnPop() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		int nPopActual = listOfSamples.getnPop();
		
		assertTrue(nPopActual==nPopExpected);

	}

	@Test
	public void testGetnDims() {
		ListOfSamples listOfSamples = new ListOfSamples(nPopExpected,parSpaceExpected);
		
		int nDimsActual = listOfSamples.getparSpace().getNumberOfPars();
		
		assertTrue(nDimsActual==nDimsExpected);

	}

	@Test
	public void testGetparSpace() {
		//TODO
	}

	@Test
	public void testGetSampleList() {
		ListOfSamples listOfSamples = new ListOfSamples(0,parSpaceExpected);

		int nPars = listOfSamples.getparSpace().getNumberOfPars();

		for (int iPop=0;iPop<nPopExpected;iPop++){
			
			Sample sample = new Sample(nDimsExpected);
			double[] parameterVectorExpected = new double[]{0.3+iPop,0.4+iPop,0.5+iPop,0.6+iPop}; 
			double objScoreExpected = iPop*-10.0;
			
			sample.setSampleCounter(iPop);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			listOfSamples.add(sample);
		}
		
		
		List<Sample> sampleList = listOfSamples.getSampleList(); 
		
		for (int iPop=0;iPop<nPopExpected;iPop++){
			
			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.3+iPop,0.4+iPop,0.5+iPop,0.6+iPop}; 
			double objScoreExpected = iPop*-10.0;
			
			int sampleCounterActual = sampleList.get(iPop).getSampleCounter();
			assertTrue(sampleCounterActual==sampleCounterExpected);
			
			double[] parameterVectorActual = listOfSamples.getParameterVector(iPop);
			for (int iPar = 0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]); 
			}

			
			double objScoreActual = sampleList.get(iPop).getObjScore();
			assertTrue(objScoreActual==objScoreExpected);
		}
		
		
	}

}
