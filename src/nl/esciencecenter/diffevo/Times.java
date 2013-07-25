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

public class Times {

	private double[] times;
	private int nTimes; 
	private int[][] chunks;
	private int nChunks;
	
	// constructor
	public Times(double[] times, boolean[] assimilate){
		
		this.times = times;
		this.nTimes =  times.length;
		
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
		int[][] chunks = new int[nChunks][];
		for (int iTime=0;iTime<nTimes;iTime++){
			iIndexEnd = iTime;			
			if (assimilate[iTime]){
				int nIndices = iIndexEnd-iIndexStart+1;
				int[] tmp = new int[nIndices];
				for (int iIndex=iIndexStart;iIndex<=iIndexEnd;iIndex++){
					tmp[iIndex-iIndexStart] = iIndex;
				}
				chunks[iChunk] = tmp;
				iChunk = iChunk + 1;
				iIndexStart = iIndexEnd;
			}
		}
		this.chunks = chunks;
		
	}
	
	public double[] getTimes(){
		return times;
	}
	
	public int getnTimes(){
		return nTimes;
	}

	public int getnChunks(){
		return nChunks;
	}
	
	public int[][] getChunks(){
		return chunks;
	}

	public int[] getChunk(int iChunk){
		return chunks[iChunk];
	}
	
	public double[] select(int[] indices){
		
		nTimes = getnTimes();
		int nIndices = indices.length;
		double[] timesSelection = new double[nIndices];

		for (int iIndex=0;iIndex<nIndices;iIndex++){
			timesSelection[iIndex] = times[indices[iIndex]];
		}
		return timesSelection;
	}
}


