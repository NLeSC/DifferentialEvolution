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
	private int[][] chunkIndices;
	private int nChunks;
	
	// constructor
	public ForcingChunks(double[] forcing, double[] assimilate){
		
		this.forcing = forcing.clone();
		assimilate[0] = 0;
		int nTimes =  forcing.length;
		for (int iTime=0;iTime<nTimes;iTime++){
			if (assimilate[iTime]==1){
				this.nChunks = this.nChunks + 1;
			}
		}
	
		int iIndexStart = 0;
		int iIndexEnd = 0;
		int iChunk = 0;
		this.chunkIndices = new int[nChunks][];
		for (int iTime=0;iTime<nTimes;iTime++){
			iIndexEnd = iTime;			
			if (assimilate[iTime]==1){
				int nIndices = iIndexEnd-iIndexStart+1;
				int[] tmp = new int[nIndices];
				for (int iIndex=iIndexStart;iIndex<=iIndexEnd;iIndex++){
					tmp[iIndex-iIndexStart] = iIndex;
				}
				this.chunkIndices[iChunk] = tmp;
				iChunk = iChunk + 1;
				iIndexStart = iIndexEnd;
			}
		}
	}
	
	public double[] getForcing(){
		return forcing.clone();
	}

	public int getnChunks(){
		return nChunks;
	}
	
	public int[][] getChunkIndices(){
		return chunkIndices.clone();
	}

	public int[] getChunkIndices(int iChunk){
		return chunkIndices[iChunk];
	}
	
	public double[] getChunk(int iChunk){
		int chunkSize = chunkIndices[iChunk].length;
		double[] forcingChunk = new double[chunkSize];
		for (int k=0;k<chunkSize;k++){
			forcingChunk[k] = forcing[chunkIndices[iChunk][k]]; 
		}
		return forcingChunk;
	}
}


