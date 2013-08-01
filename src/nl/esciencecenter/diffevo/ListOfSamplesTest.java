package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ListOfSamplesTest {

	@Test
	public void testListOfSamples() {

		int nPop = 10;
		int numberOfDimensions = 3;
		double[] lowerBounds = new double[]{-10,2.0,40};
		double[] upperBounds = new double[]{102,4.5,40.17};
		String[] parNames = new String[]{"a","b","c"};
		ParSpace parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		ListOfSamples listOfSamplesActual = new ListOfSamples(nPop,parSpace);
		
		Sample expectedSample = new Sample(numberOfDimensions);
		double[] parameterVectorExpected = expectedSample.getParameterVector();
		double parameterVectorAcceptableDifference = 0.0;
		double objScoreExpected = expectedSample.getObjScore();
		double objScoreAcceptableDifference = 0.0;
				
		int nSamples = nPop;
		for (int iSample = 0;iSample<nSamples;iSample++){
			
			double[] parameterVectorActual = listOfSamplesActual.getParameterVector(iSample);
			assertArrayEquals(parameterVectorExpected,parameterVectorActual,parameterVectorAcceptableDifference);
			
			double objScoreActual = listOfSamplesActual.getObjScore(iSample);
			assertEquals(objScoreExpected,objScoreActual,objScoreAcceptableDifference);
			
		}

		assertEquals(nPop, listOfSamplesActual.getSize());
		
	}

	@Test
	public void testAdd() {

		int nPop = 9;
		int numberOfDimensions = 3;
		double[] lowerBounds = new double[]{-10,2.0,40};
		double[] upperBounds = new double[]{102,4.5,40.17};
		String[] parNames = new String[]{"a","b","c"};
		ParSpace parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		ListOfSamples listOfSamplesActual = new ListOfSamples(nPop,parSpace);
		
		Sample sample = new Sample(numberOfDimensions);
		int sampleCounterExpected = 9;
		double[] parameterVectorExpected = new double[]{-3.4,3.2,40.05};
		sample.setSampleCounter(sampleCounterExpected);
		
		double objScoreExpected = -1.234;
		sample.setObjScore(objScoreExpected);
		
		listOfSamplesActual.add(sample);
		
		assertEquals(nPop+1, listOfSamplesActual.getSize());
		
		double[] parameterVectorActual = listOfSamplesActual.getParameterVector(nPop+1);
		double parameterVectorAcceptableDifference = 0.0;

		for (int iDim=0;iDim<numberOfDimensions;iDim++){
			assertEquals(parameterVectorExpected[iDim], parameterVectorActual[iDim],parameterVectorAcceptableDifference);
		}
		
		double objScoreActual = listOfSamplesActual.getObjScore(nPop+1);
		double objScoreAcceptableDifference = 0.0;
		assertEquals(objScoreExpected, objScoreActual,objScoreAcceptableDifference);
		
		
	}

	@Test
	public void testSetParameterVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParameterVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetObjScore() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetObjScore() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalcObjScore() {
		fail("Not yet implemented");
	}

}
