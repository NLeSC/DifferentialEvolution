package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParSpaceTest {

	private double[] lowerBoundsExpected;
	private double[] upperBoundsExpected;
	private String[] parNamesExpected;
	private ParSpace parSpace;
	private int nParsExpected = 0; 
	
	public ParSpaceTest(){
		
		lowerBoundsExpected = new double[]{-5.12,-5.12};
		upperBoundsExpected = new double[]{5.12,5.12};
		parNamesExpected = new String[]{"p1","p2"};
		parSpace = new ParSpace(lowerBoundsExpected,upperBoundsExpected,parNamesExpected);
		
		nParsExpected = lowerBoundsExpected.length;

	}
//	
//	@Test
//	public void testParSpace() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetNumberOfPars() {
		int nParsActual = parSpace.getNumberOfPars(); 
		assertEquals(nParsExpected, nParsActual);
	}

	@Test
	public void testGetLowerBound() {
		for (int iPar=0;iPar<nParsExpected;iPar++){
			double lowerBoundActual = parSpace.getLowerBound(iPar);
			assertTrue(lowerBoundsExpected[iPar]==lowerBoundActual);
		}
	}

//	@Test
//	public void testGetUpperBound() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetRange() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetParName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDivideIntoIntervalsInt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDivideIntoIntervalsIntArray() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetBinBounds() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetResolution() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetnBins() {
//		fail("Not yet implemented");
//	}

}
