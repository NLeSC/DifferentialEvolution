package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProposalsTest {

	private ParSpace parSpace;
	int nPopExpected = 12;
	Proposals proposals;
	
	public ProposalsTest(){
		double[] lowerBounds = new double[]{-20};
		double[] upperBounds = new double[]{18};
		String[] parNames = new String[]{"theta"};
		
		parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		parSpace.divideIntoIntervals(50);

		proposals = new Proposals(nPopExpected,parSpace);
		
	} 
	
	@Test
	public void testProposals() {
		int nPopActual = proposals.getSampleList().size(); 
		assertTrue(nPopExpected==nPopActual);
	}

	@Test
	public void testReflectIfOutOfBounds() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProposals() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetProposal() {
		fail("Not yet implemented");
	}

}
