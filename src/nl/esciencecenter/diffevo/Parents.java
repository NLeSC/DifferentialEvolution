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

import java.util.List;
import java.util.Random;

public class Parents extends ListOfSamples {

	// constructor
	private ParSpace parSpace;
	public Parents(int nPop, ParSpace parSpace, StateSpace stateSpace, int nTimes){
		
		super(nPop,parSpace, stateSpace, nTimes);
		this.parSpace = parSpace;
	}
	
	public void takeUniformRandomSamples(Random generator){
		int nPop = getnPop();
		int nPars = getnPars();

		for (int iPop=1;iPop<=nPop;iPop++){
			double[] values = new double[nPars];			
			for (int iPar=1;iPar<=nPars;iPar++){
				double g = generator.nextDouble();
				values[iPar-1] = parSpace.getLowerBound(iPar-1) + 
						g * parSpace.getRange(iPar-1);
			}
			this.setParameterVector(iPop-1, values);
		}
	}
	
	public List<Sample> getParents() {
		return this.getSampleList();
	}
	
	public Sample getParent(int index) {
		return this.getSampleList().get(index);
	}

	public void setParent(int index, Sample sample) {
		int sampleIdentifier;
		double[] parameterVector;
		double objScore;
		
		sampleIdentifier = sample.getSampleCounter();
		parameterVector = sample.getParameterVector();
		objScore = sample.getObjScore();
		
		this.getSampleList().get(index).setSampleCounter(sampleIdentifier);
		this.getSampleList().get(index).setParameterVector(parameterVector);
		this.getSampleList().get(index).setObjScore(objScore);
		
	}
}


