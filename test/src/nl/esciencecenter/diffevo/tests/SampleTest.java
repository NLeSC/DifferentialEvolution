package nl.esciencecenter.diffevo.tests;

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
	}

}
