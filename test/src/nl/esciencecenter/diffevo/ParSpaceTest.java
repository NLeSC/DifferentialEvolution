package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParSpaceTest {

	private static double[] lowerBoundsExpected;
	private static double[] upperBoundsExpected;
	private static String[] parNamesExpected;
	private ParSpace parSpace;
	private static int nParsExpected;
	private static double[] rangeExpected;
	
	public ParSpaceTest(){
		
		lowerBoundsExpected = new double[]{-5.00,-5.00};
		upperBoundsExpected = new double[]{5.00,5.00};
		parNamesExpected = new String[]{"p1","p2"};
		parSpace = new ParSpace(lowerBoundsExpected,upperBoundsExpected,parNamesExpected);
		
		nParsExpected = lowerBoundsExpected.length;
		
		rangeExpected = new double[nParsExpected];
		for (int iPar=0;iPar<nParsExpected;iPar++){
			rangeExpected[iPar] = upperBoundsExpected[iPar]-lowerBoundsExpected[iPar];
		}

	}
	
	@Test
	public void testParSpace() {
		for (int iPar=0;iPar<nParsExpected;iPar++){
			double lowerBoundActual = parSpace.getLowerBound(iPar);
			assertTrue(lowerBoundsExpected[iPar]==lowerBoundActual);
		}

		for (int iPar=0;iPar<nParsExpected;iPar++){
			double upperBoundActual = parSpace.getUpperBound(iPar);
			assertTrue(upperBoundsExpected[iPar]==upperBoundActual);
		}
		
		for (int iPar=0;iPar<nParsExpected;iPar++){
			String parNameActual = parSpace.getParName(iPar);
			assertTrue(parNamesExpected[iPar]==parNameActual);
		}
		
		for (int iPar=0;iPar<nParsExpected;iPar++){
			double rangeActual = parSpace.getRange(iPar);
			assertTrue(rangeExpected[iPar]==rangeActual);
		}
		
		assertTrue(nParsExpected==parSpace.getNumberOfPars());

		
	}

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

	@Test
	public void testGetUpperBound() {
		for (int iPar=0;iPar<nParsExpected;iPar++){
			double upperBoundActual = parSpace.getUpperBound(iPar);
			assertTrue(upperBoundsExpected[iPar]==upperBoundActual);
		}
	}

	@Test
	public void testGetRange() {
		for (int iPar=0;iPar<nParsExpected;iPar++){
			double rangeActual = parSpace.getRange(iPar);
			assertTrue(rangeExpected[iPar]==rangeActual);
		}

	}

	@Test
	public void testGetParName() {
		for (int iPar=0;iPar<nParsExpected;iPar++){
			String parNameActual = parSpace.getParName(iPar);
			assertTrue(parNamesExpected[iPar]==parNameActual);
		}
	}

	@Test
	public void testDivideIntoIntervalsInt() {
		
		
		int nIntervalsExpected;
		int nIntervalsActual;
		
		nIntervalsExpected = 4;
		parSpace.divideIntoIntervals(nIntervalsExpected);
		for (int iPar=0;iPar<nParsExpected;iPar++){
			
			nIntervalsActual = parSpace.getnBins(iPar); 
 			assertTrue(nIntervalsActual==nIntervalsExpected);
		}

		nIntervalsExpected = 1000;
		parSpace.divideIntoIntervals(nIntervalsExpected);
		for (int iPar=0;iPar<nParsExpected;iPar++){
			
			nIntervalsActual = parSpace.getnBins(iPar); 
 			assertTrue(nIntervalsActual==nIntervalsExpected);
		}
	}

	@Test
	public void testDivideIntoIntervalsIntArray() {
		
		
		int[] nIntervalsExpected = new int[nParsExpected];
		int[] nIntervalsActual = new int[nParsExpected];
		
		nIntervalsExpected = new int[]{4,6};
		parSpace.divideIntoIntervals(nIntervalsExpected);
		for (int iPar=0;iPar<nParsExpected;iPar++){
			
			nIntervalsActual[iPar] = parSpace.getnBins(iPar); 
 			assertTrue(nIntervalsActual[iPar]==nIntervalsExpected[iPar]);
		}

		nIntervalsExpected = new int[]{1000,500};
		parSpace.divideIntoIntervals(nIntervalsExpected);
		for (int iPar=0;iPar<nParsExpected;iPar++){
			
			nIntervalsActual[iPar] = parSpace.getnBins(iPar); 
 			assertTrue(nIntervalsActual[iPar]==nIntervalsExpected[iPar]);
		}
	}

	@Test
	public void testGetBinBounds() {
		int nIntervals = 4;
		int nBins = nIntervals;
		double[] binBoundsExpected = new double[nBins+1];
		parSpace.divideIntoIntervals(nIntervals);
		for (int iPar=0;iPar<nParsExpected;iPar++){
			double[] binBoundsActual = parSpace.getBinBounds(iPar);
			double lowerBound = parSpace.getLowerBound(iPar);
			double resolution = parSpace.getResolution(iPar);
			
			for (int iBin=0;iBin<nBins+1;iBin++){
				binBoundsExpected[iBin] = lowerBound + iBin*resolution; 
			}
 			assertArrayEquals(binBoundsExpected, binBoundsActual, 0.0);
		}

	}

	@Test
	public void testGetResolution() {
		int nIntervals = 4;
		parSpace.divideIntoIntervals(nIntervals);
		
		for (int iPar=0;iPar<nParsExpected;iPar++){
			double resolutionExpected = (parSpace.getUpperBound(iPar) - parSpace.getLowerBound(iPar))/nIntervals;
			double resolutionActual = parSpace.getResolution(iPar);
			
 			assertTrue(resolutionExpected==resolutionActual);
		}
	}

	@Test
	public void testGetnBins() {
		int nIntervals = 4;
		parSpace.divideIntoIntervals(nIntervals);
		for (int iPar=0;iPar<nParsExpected;iPar++){
			int nBinsActual = parSpace.getnBins(iPar);
			assertTrue(nIntervals==nBinsActual);
		}
	}
	

	@Test
	public void testGetnDimensions() {
		int nDimensionsExpected = 2;
		int nDimensionsActual = parSpace.getnDimensions();
        assertTrue(nDimensionsExpected==nDimensionsActual);
	}
	
	
	@Test
	public void testGetDimensionNames() {
		String[] parNamesActual = parSpace.getDimensionNames();
		int nPars = parSpace.getNumberOfPars();
		for (int iPar=-0;iPar<nPars;iPar++){
	        assertTrue(parNamesActual[iPar]==parNamesExpected[iPar]);
		}
	}
	
	
	@Test
	public void testGetUpperBounds() {
		double[] upperBoundsActual = parSpace.getUpperBounds();
		int nPars = parSpace.getNumberOfPars();
		for (int iPar=-0;iPar<nPars;iPar++){
	        assertTrue(upperBoundsActual[iPar]==upperBoundsExpected[iPar]);
		}
	}
	
	
	
	
	
}




