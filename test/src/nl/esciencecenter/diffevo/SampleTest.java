package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import nl.esciencecenter.diffevo.Sample;

import org.junit.Test;

public class SampleTest {

	@Test
	public void testSample() {
		
		int nDims = 5;

		
		Sample sample = new Sample(nDims);
		sample.getObjScore();
		
		assertTrue(Double.isNaN((sample.getObjScore())));
		
		for (double parameterValue: sample.getParameterVector()){
			assertTrue(Double.isNaN((parameterValue)));
		}

		double objScoreExpected = -10.3;
		sample.setObjScore(objScoreExpected);
		double objScoreActual = sample.getObjScore();
		double objScoreAcceptableDelta = 0.0;
		assertEquals(objScoreExpected, objScoreActual,objScoreAcceptableDelta);
		
		int sampleCounterExpected = 1002;
		sample.setSampleCounter(sampleCounterExpected);
		int sampleCounterActual = sample.getSampleCounter();
		assertEquals(sampleCounterExpected,sampleCounterActual);
		
		
		
	}

}
