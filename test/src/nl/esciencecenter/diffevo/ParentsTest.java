package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ParentsTest {
	
	private static int nPopExpected;
	private ParSpace parSpace;
	private int nDimsExpected;
	private Parents parents;
	
	
	public ParentsTest(){

		nPopExpected = 10;

		double[] lowerBounds = new double[]{-20,-40,-80,-120};
		double[] upperBounds = new double[]{ 20, 40, 80, 120};
		String[] parNames = new String[]{"a","b","c","d"};
		parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		parSpace.divideIntoIntervals(new int[]{50,50,50,50});

		parents = new Parents(nPopExpected,parSpace);
		
		nDimsExpected = upperBounds.length;
		
	}

	@Test
	public void testParents() {
		// nDims
		assertTrue(nDimsExpected==parents.getnDims());
		
		//nPop
		assertTrue(nPopExpected==parents.getnPop());
		
		
		//parSpace
		assertTrue(parSpace==parents.getparSpace());
		
		//sampleList
		int nDimsActual = parents.getnDims();
		Sample sampleExpected = new Sample(nDimsActual);
		int nSamples = parents.getnPop();
		for (int iSample = 0;iSample<nSamples;iSample++){
			Sample sampleActual = parents.getParent(iSample);

			//sample counter
			assertTrue(sampleActual.getSampleCounter()==sampleExpected.getSampleCounter());

			// parameterVector
			double[] parameterVector = sampleActual.getParameterVector();
			for (int iPar=0;iPar<nDimsActual;iPar++){
				assertTrue(Double.isNaN(parameterVector[iPar]));
			}
			// objScore
			assertTrue(Double.isNaN(sampleActual.getObjScore()));			

		}
		
	}

// TODO	
//	@Test
//	public void testTakeUniformRandomSamples() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetParents() {
		int nDimsActual = parents.getnDims();
		Sample sampleExpected = new Sample(nDimsActual);
		int nSamples = parents.getnPop();
		List<Sample> samples = parents.getParents();
		
		for (int iSample = 0;iSample<nSamples;iSample++){
			Sample sampleActual = samples.get(iSample);
			
			//sample counter
			assertTrue(sampleActual.getSampleCounter()==sampleExpected.getSampleCounter());

			// parameterVector
			double[] parameterVector = sampleActual.getParameterVector();
			for (int iPar=0;iPar<nDimsActual;iPar++){
				assertTrue(Double.isNaN(parameterVector[iPar]));
			}
			// objScore
			assertTrue(Double.isNaN(sampleActual.getObjScore()));			
		}

	}

	@Test
	public void testGetParent() {
		int nDimsActual = parents.getnDims();
		Sample sampleExpected = new Sample(nDimsActual);
		int nSamples = parents.getnPop();
		
		for (int iSample = 0;iSample<nSamples;iSample++){
			Sample sampleActual = parents.getParent(iSample);
			
			//sample counter
			assertTrue(sampleActual.getSampleCounter()==sampleExpected.getSampleCounter());

			// parameterVector
			double[] parameterVector = sampleActual.getParameterVector();
			for (int iPar=0;iPar<nDimsActual;iPar++){
				assertTrue(Double.isNaN(parameterVector[iPar]));
			}
			// objScore
			assertTrue(Double.isNaN(sampleActual.getObjScore()));			
		}

	}

	@Test
	public void testSetParent() {
		
		Sample sample = new Sample(nDimsExpected);
		double[] parameterVectorExpected = new double[]{-8.4,20.3,62.34,10.2};
		
		int nParents = parents.getnPop();
		for (int iParent=0;iParent<nParents;iParent++){
			
			sample.setSampleCounter(iParent+1000);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(-1.2345*iParent);
			parents.setParent(iParent, sample);
			
		}

		for (int iParent=0;iParent<nParents;iParent++){
			sample = parents.getParent(iParent);
			int sampleCounterActual = sample.getSampleCounter();
			double[] parameterVectorActual = sample.getParameterVector();
			double objScoreActual = sample.getObjScore();
			
			int sampleCounterExpected = iParent+1000;
			double objScoreExpected = -1.2345*iParent;
			
			assertTrue(sampleCounterActual==sampleCounterExpected);
			for (int iPar=0;iPar<nDimsExpected;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}
			assertTrue(objScoreActual==objScoreExpected);
			
		}
		
		
		
		
	}

}
