package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ForcingChunksTest {

	private static double[] forcingExpected;
	private static double[] assimilateExpected;
	private ForcingChunks forcingChunksActual;

	
	public ForcingChunksTest(){
		forcingExpected = new double[]{0.1,0.2,0.3,0.4,0.0,0.4,0.3,0.2,0.1};
		assimilateExpected = new double[]{0,0,0,0,1,0,1,0,1};
		forcingChunksActual = new ForcingChunks(forcingExpected,assimilateExpected);
	}
	
	
	@Test
	public void testForcingChunks() {
		
		
		// chunkIndices
		{
			int[] chunkIndicesActual;
			int[] chunkIndicesExpected;
			int nElems;

			chunkIndicesActual = forcingChunksActual.getChunkIndices(0);
			chunkIndicesExpected = new int[]{0,1,2,3,4};
			nElems = chunkIndicesActual.length; 
			for (int iElem=0;iElem<nElems;iElem++){
				assertTrue(chunkIndicesActual[iElem]==chunkIndicesExpected[iElem]);
			}

			chunkIndicesActual = forcingChunksActual.getChunkIndices(1);
			chunkIndicesExpected = new int[]{4,5,6};
			nElems = chunkIndicesActual.length; 
			for (int iElem=0;iElem<nElems;iElem++){
				assertTrue(chunkIndicesActual[iElem]==chunkIndicesExpected[iElem]);
			}

			chunkIndicesActual = forcingChunksActual.getChunkIndices(2);
			chunkIndicesExpected = new int[]{6,7,8};
			nElems = chunkIndicesActual.length; 
			for (int iElem=0;iElem<nElems;iElem++){
				assertTrue(chunkIndicesActual[iElem]==chunkIndicesExpected[iElem]);
			}
		}
		
		// forcing
		{
			double[] forcingActual = forcingChunksActual.getForcing();
			int nElems = forcingActual.length;

			for (int iElem=0;iElem<nElems;iElem++){
				assertTrue(forcingActual[iElem]==forcingExpected[iElem]);
			}
		}
		// nChunks
		{
			int nElems = assimilateExpected.length;
			int nChunksExpected = 0;
			for (int iElem=0;iElem<nElems;iElem++){
				if (assimilateExpected[iElem]==1){
					nChunksExpected++; 
				}
			}
			int nChunksActual = forcingChunksActual.getnChunks();
			
			assertTrue(nChunksActual==nChunksExpected);

		}
		
	}

	@Test
	public void testGetForcing() {
		double[] forcingActual = forcingChunksActual.getForcing();
		int nElems = forcingActual.length;

		for (int iElem=0;iElem<nElems;iElem++){
			assertTrue(forcingActual[iElem]==forcingExpected[iElem]);
		}
	}

	@Test
	public void testGetnChunks() {
		
		int nElems = assimilateExpected.length;
		int nChunksExpected = 0;
		for (int iElem=0;iElem<nElems;iElem++){
			if (assimilateExpected[iElem]==1){
				nChunksExpected++; 
			}
		}
		int nChunksActual = forcingChunksActual.getnChunks();
		
		assertTrue(nChunksActual==nChunksExpected);

	}

	@Test
	public void testGetChunkIndices() {
		int[][] chunkIndicesActual;
		int[] chunkIndicesExpected;
		int nElems;

		chunkIndicesActual = forcingChunksActual.getChunkIndices();
		chunkIndicesExpected = new int[]{0,1,2,3,4};
		nElems = chunkIndicesActual.length; 
		for (int iElem=0;iElem<nElems;iElem++){
			assertTrue(chunkIndicesActual[0][iElem]==chunkIndicesExpected[iElem]);
		}

		chunkIndicesExpected = new int[]{4,5,6};
		nElems = chunkIndicesActual.length; 
		for (int iElem=0;iElem<nElems;iElem++){
			assertTrue(chunkIndicesActual[1][iElem]==chunkIndicesExpected[iElem]);
		}

		chunkIndicesExpected = new int[]{6,7,8};
		nElems = chunkIndicesActual.length; 
		for (int iElem=0;iElem<nElems;iElem++){
			assertTrue(chunkIndicesActual[2][iElem]==chunkIndicesExpected[iElem]);
		}

	}

	@Test
	public void testGetChunkIndicesInt() {
		int[] chunkIndicesActual;
		int[] chunkIndicesExpected;
		int nElems;

		chunkIndicesActual = forcingChunksActual.getChunkIndices(0);
		chunkIndicesExpected = new int[]{0,1,2,3,4};
		nElems = chunkIndicesActual.length; 
		for (int iElem=0;iElem<nElems;iElem++){
			assertTrue(chunkIndicesActual[iElem]==chunkIndicesExpected[iElem]);
		}

		chunkIndicesActual = forcingChunksActual.getChunkIndices(1);
		chunkIndicesExpected = new int[]{4,5,6};
		nElems = chunkIndicesActual.length; 
		for (int iElem=0;iElem<nElems;iElem++){
			assertTrue(chunkIndicesActual[iElem]==chunkIndicesExpected[iElem]);
		}

		chunkIndicesActual = forcingChunksActual.getChunkIndices(2);
		chunkIndicesExpected = new int[]{6,7,8};
		nElems = chunkIndicesActual.length; 
		for (int iElem=0;iElem<nElems;iElem++){
			assertTrue(chunkIndicesActual[iElem]==chunkIndicesExpected[iElem]);
		}

	}

	@Test
	public void testGetChunk() {

		{
			double[] chunkActual = forcingChunksActual.getChunk(0);
			double[] chunkExpected = new double[]{0.1,0.2,0.3,0.4,0.0};
			int nElems = chunkActual.length;
			for (int iElem=0;iElem<nElems;iElem++){
				assertTrue(chunkActual[iElem]==chunkExpected[iElem]);
			}
		}
		{
			double[] chunkActual = forcingChunksActual.getChunk(1);
			double[] chunkExpected = new double[]{0.0,0.4,0.3};
			int nElems = chunkActual.length;
			for (int iElem=0;iElem<nElems;iElem++){
				assertTrue(chunkActual[iElem]==chunkExpected[iElem]);
			}
		}
		{
			double[] chunkActual = forcingChunksActual.getChunk(2);
			double[] chunkExpected = new double[]{0.3,0.2,0.1};
			int nElems = chunkActual.length;
			for (int iElem=0;iElem<nElems;iElem++){
				assertTrue(chunkActual[iElem]==chunkExpected[iElem]);
			}
		}
		
	}

}
