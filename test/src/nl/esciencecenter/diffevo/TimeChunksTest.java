package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeChunksTest {

	
	
	@Test
	public void testTimeChunks() {
		double[] times = new double[]{0,1,2,3,4,5,6,7,8};
		boolean[] assimilate = new boolean[]{true, false, false, true, false,false, true, false, true}; 
		TimeChunks timeChunks = new TimeChunks(times,assimilate);
		int nChunks = timeChunks.getnChunks();
		int[][] chunkIndices = timeChunks.getChunkIndices();
		int nChunksExpected = 3;
		assertEquals(nChunksExpected,nChunks);
		
	}

	@Test
	public void testGetTimes() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetnTimes() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetnChunks() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChunkIndices() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChunkIndicesInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetChunk() {
		fail("Not yet implemented");
	}

}
