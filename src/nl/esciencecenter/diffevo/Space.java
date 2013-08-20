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

public class Space {

	private final double[] lowerBounds;
	private final double[] upperBounds;
	private final double[] range;	
	private final String[] dimensionNames;
	private final int nDimensions;

	public Space(double[] lowerBounds, double[] upperBounds, String[] dimensionNames){

		this.lowerBounds = lowerBounds.clone();
		this.upperBounds = upperBounds.clone();
		this.dimensionNames = dimensionNames.clone();
		this.nDimensions = lowerBounds.length;
		this.range = new double[nDimensions]; 

		for (int iDim=0;iDim<nDimensions;iDim++){
			this.range[iDim] = upperBounds[iDim] - lowerBounds[iDim];
		}
	}

	public int getNumberOfDimensions() {
		return nDimensions;
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

	public String getDimensionName(int index) {
		return dimensionNames[index];
	}

	public double[] getLowerBounds() {
		return lowerBounds.clone();
	}

	public double[] getUpperBounds() {
		return upperBounds.clone();
	}

	public double[] getRange() {
		return range.clone();
	}

	public String[] getDimensionNames() {
		return dimensionNames.clone();
	}

	public int getnDimensions() {
		return nDimensions;
	}

}
