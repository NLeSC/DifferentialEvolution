package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import nl.esciencecenter.diffevo.TimeChunks;

import org.junit.Test;

public class TimeChunksTest {

	
	
	@Test
	public void testTimeChunks() {
		double[] times = new double[]{0,1,2,3,4,5,6,7,8};
		double[] assimilate = new double[]{1, 0, 0, 1, 0, 0, 1, 0, 1}; 
		TimeChunks timeChunks = new TimeChunks(times,assimilate);
		int nChunks = timeChunks.getnChunks();
		int nChunksExpected = 3;
		assertEquals(nChunksExpected,nChunks);
		
		
		int[][] chunksIndicesExpected = new int[][]{{0,1,2,3},{3,4,5,6},{6,7,8}};
		int[][] chunkIndices = timeChunks.getChunkIndices();
		assertArrayEquals(chunksIndicesExpected,chunkIndices);
		
		
		double[] timesActual = timeChunks.getTimes();
		int nTimes = timesActual.length; 
		for (int iTime = 0;iTime<nTimes;iTime++){
			assertTrue(timesActual[iTime]==times[iTime]);
		}
		
	}
	
	
	
	
	
	
}
