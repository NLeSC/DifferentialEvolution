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

		// make the list of samples
		int nPop = 2;
		int numberOfDimensions = 3;
		double[] lowerBounds = new double[]{-10,2.0,40};
		double[] upperBounds = new double[]{102,4.5,40.17};
		String[] parNames = new String[]{"a","b","c"};
		ParSpace parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		ListOfSamples listOfSamplesActual = new ListOfSamples(nPop,parSpace);
		
		// make the new sample
		Sample sample = new Sample(numberOfDimensions);
		int sampleCounterExpected = nPop+1;
		sample.setSampleCounter(sampleCounterExpected);
		
		double[] parameterVectorExpected = new double[]{-3.4,3.2,40.05};
		sample.setParameterVector(parameterVectorExpected);
		
		double objScoreExpected = -1.234;
		sample.setObjScore(objScoreExpected);
		
		// add the new sample to the list of samples
		listOfSamplesActual.add(sample);
		
		// verify the size
		assertEquals(nPop+1, listOfSamplesActual.getSize());
		
		// verify the parameter vector 
		double[] parameterVectorActual = listOfSamplesActual.getParameterVector(nPop);
		double parameterVectorAcceptableDifference = 0.0;
		for (int iDim=0;iDim<numberOfDimensions;iDim++){
			assertEquals(parameterVectorExpected[iDim], parameterVectorActual[iDim],parameterVectorAcceptableDifference);
		}
		
		double objScoreActual = listOfSamplesActual.getObjScore(nPop);
		double objScoreAcceptableDifference = 0.0;
		assertEquals(objScoreExpected, objScoreActual,objScoreAcceptableDifference);
		
		
	}
//
//	@Test
//	public void testSetParameterVector() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetParameterVector() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetObjScore() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetObjScore() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testCalcObjScore() {
//		fail("Not yet implemented");
//	}

}
