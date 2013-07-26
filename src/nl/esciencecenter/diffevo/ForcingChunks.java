/*
 * Copyrighted 2012-2013 Netherlands eScience Center.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").  
 * You may not use this file except in compliance with the License. 
 * For details, see the LICENCE.txt file location in the root directory of this 
 * distribution or obtain the Apache License at the following location: 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * For the full license, see: LICENCE.txt (located in the root folder of this distribution). 
 * ---
 */

package nl.esciencecenter.diffevo;

public class ForcingChunks {

	private double[] forcing;
	private int nTimes; 
	private int[][] chunkIndices;
	private int nChunks;
	
	// constructor
	public ForcingChunks(double[] forcing, boolean[] assimilate){
		
		this.forcing = forcing;
		this.nTimes =  forcing.length;
		
		assimilate[0] = false;
		int nChunks = 0;
		for (int iTime=0;iTime<nTimes;iTime++){
			if (assimilate[iTime]){
				nChunks = nChunks + 1;
			}
		}
		this.nChunks = nChunks;
		
		int iIndexStart = 0;
		int iIndexEnd = 0;
		int iChunk = 0;
		int[][] chunkIndices = new int[nChunks][];
		for (int iTime=0;iTime<nTimes;iTime++){
			iIndexEnd = iTime;			
			if (assimilate[iTime]){
				int nIndices = iIndexEnd-iIndexStart+1;
				int[] tmp = new int[nIndices];
				for (int iIndex=iIndexStart;iIndex<=iIndexEnd;iIndex++){
					tmp[iIndex-iIndexStart] = iIndex;
				}
				chunkIndices[iChunk] = tmp;
				iChunk = iChunk + 1;
				iIndexStart = iIndexEnd;
			}
		}
		this.chunkIndices = chunkIndices;
		
	}
	
	public double[] getForcing(){
		return forcing;
	}

	public int getnChunks(){
		return nChunks;
	}
	
	public int[][] getChunkIndices(){
		return chunkIndices;
	}

	public int[] getChunkIndices(int iChunk){
		return chunkIndices[iChunk];
	}
	
	public double[] getChunk(int iChunk){
		int[] chunkIndices = getChunkIndices(iChunk);
		int chunkSize = chunkIndices.length;
		double[] forcingChunk = new double[chunkSize];
		for (int k=0;k<chunkSize;k++){
			forcingChunk[k] = forcing[chunkIndices[k]]; 
		}
		return forcingChunk;
	}
}


