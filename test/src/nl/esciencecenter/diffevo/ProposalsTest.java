package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.Test;

public class ProposalsTest {

	private ParSpace parSpace;
	Proposals proposals;
	double[][] parameters = new double[][]{{2.5},{10.5},{-23},{20},{-1.234}};
	int nPopExpected = parameters.length;
	
	public ProposalsTest(){
		double[] lowerBounds = new double[]{-20};
		double[] upperBounds = new double[]{18};
		String[] parNames = new String[]{"theta"};
		
		parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		parSpace.divideIntoIntervals(50);

		int nSamples = nPopExpected;
		proposals = new Proposals(nPopExpected,parSpace);
		
		for (int iSample=0;iSample<nSamples;iSample++){
			double[] parameterVector = parameters[iSample];
			proposals.setParameterVector(iSample, parameterVector);
		}
	} 
	
	@Test
	public void testProposals() {
		int nPopActual = proposals.getSampleList().size(); 
		assertTrue(nPopExpected==nPopActual);
	}

	@Test
	public void testReflectIfOutOfBounds() {
		
		proposals.reflectIfOutOfBounds();
		
		double[] parameterVector = new double[parSpace.getNumberOfPars()]; 
		
		parameterVector = proposals.getParameterVector(0);
		assertArrayEquals(parameters[0], parameterVector, 0.0);

		parameterVector = proposals.getParameterVector(1);
		assertArrayEquals(parameters[1], parameterVector, 0.0);

		parameterVector = proposals.getParameterVector(2);
		assertArrayEquals(new double[] {-17.0}, parameterVector, 0.0);

		parameterVector = proposals.getParameterVector(3);
		assertArrayEquals(new double[] {16.0}, parameterVector, 0.0);

		parameterVector = proposals.getParameterVector(4);
		assertArrayEquals(parameters[4], parameterVector, 0.0);

	}

	@Test
	public void testGetProposals() {
		int nSamples = nPopExpected;
		List<Sample> samples = proposals.getProposals();		
		for (int iSample = 0;iSample<nSamples;iSample++){
			double[] actuals = samples.get(iSample).getParameterVector();
			assertArrayEquals(parameters[iSample], actuals, 0.0);
		}
	}

	@Test
	public void testGetProposal() {
		
		int nSamples = nPopExpected;
		for (int iSample = 0;iSample<nSamples;iSample++){
			Sample sample = proposals.getProposal(iSample);
			double[] actuals = sample.getParameterVector();
			assertArrayEquals(parameters[iSample], actuals, 0.0);
		}
	}

}
