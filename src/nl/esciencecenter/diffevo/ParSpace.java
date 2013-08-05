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

import java.util.ArrayList;
import java.util.List;

public class ParSpace {
	
	private final double[] lowerBounds;
	private final double[] upperBounds;
	private final double[] range;	
	private final String[] parNames;
	private final int nPars;
	private List<double[]> binBoundsAll = new ArrayList<double[]>();
	private double[] resolutions;

	public ParSpace(double[] lowerBounds, double[] upperBounds, String[] parNames){
		
		this.lowerBounds = lowerBounds.clone();
		this.upperBounds = upperBounds.clone();
		this.parNames = parNames.clone();
		this.nPars = lowerBounds.length;
		this.resolutions = new double[nPars];
		this.range = new double[nPars]; 
	
		for (int iPar=0;iPar<nPars;iPar++){
			this.range[iPar] = upperBounds[iPar] - lowerBounds[iPar];
			this.resolutions[iPar] = 0;
		}
		int nIntervalsDefault = 5;
		divideIntoIntervals(nIntervalsDefault);
	}


	public int getNumberOfPars() {
		return nPars;
	}

	public double getLowerBound(int index) {
		return lowerBounds[index];
	}

	public double getUpperBound(int index) {
		return upperBounds[index];
	}

	public double getRange(int index) {
		return range[index];
	}

	public String getParName(int index) {
		return parNames[index];
	}
	
	public void divideIntoIntervals(int nIntervals){
		int[] tmp = new int[nPars];		
		for (int iPar=0;iPar<nPars;iPar++){
			tmp[iPar] = nIntervals;
		}
		divideIntoIntervals(tmp);
	}

	public void divideIntoIntervals(int[] nIntervals){
		int[] nBinBounds = new int[nIntervals.length];
		for (int iPar=0;iPar<nPars;iPar++){
			nBinBounds[iPar] = nIntervals[iPar]+1;
			double[] binBounds = new double[nBinBounds[iPar]];
			for (int iBinBound = 0; iBinBound<nBinBounds[iPar];iBinBound++){
				binBounds[iBinBound] = lowerBounds[iPar] + ((double)iBinBound/nIntervals[iPar])*range[iPar];
			}
			binBoundsAll.add(iPar, binBounds);
			resolutions[iPar] = binBounds[1]-binBounds[0];
		}
	}


	public double[] getBinBounds(int iPar) {
		return binBoundsAll.get(iPar);
	}

	
	public double getResolution(int iPar) {
		return resolutions[iPar];
	}

	
	public int getnBins(int iPar){
		return getBinBounds(iPar).length-1;
	}




	
}
