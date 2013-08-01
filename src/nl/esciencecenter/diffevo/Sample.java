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

public class Sample {
	private int sampleCounter;
    private double[] parameterVector;
    private double objScore;
    		
    /**
     *  
     * @param nDims number of dimensions of the parameter space
     */
	public Sample(int nDims) {
		this.sampleCounter = -1;
		this.parameterVector = new double[nDims];
		for (int iDim=0;iDim<nDims;iDim++){
			this.parameterVector[iDim] = Double.NaN;
		}
		this.objScore = Double.NaN;
	}

	public double getObjScore() {
		return objScore;
	}

	public void setObjScore(double objScore) {
		this.objScore = objScore;
	}

	public double[] getParameterVector() {
		return parameterVector.clone();
	}

	public void setParameterVector(double[] parameterVector) {
		this.parameterVector = parameterVector.clone();
	}

	public int getSampleCounter() {
		return sampleCounter;
	}

	public void setSampleCounter(int sampleCounter) {
		this.sampleCounter = sampleCounter ;
	}
}
