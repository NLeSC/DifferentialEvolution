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

public class Proposals extends ListOfSamples{

	// constructor
	public Proposals(int nPop, ParSpace parSpace){
		super(nPop,parSpace);
	}
	
	public void reflectIfOutOfBounds(){
		for (int iPop=0;iPop<nPop;iPop++){
			for (int iDim = 0;iDim<nDims;iDim++){
				double[] parameterVector = getParameterVector(iPop);
				double lb = parSpace.getLowerBound(iDim);
				double ub = parSpace.getUpperBound(iDim);
				double s = parameterVector[iDim];
				if (s<lb){
					parameterVector[iDim] = lb+(lb-s);
				}					
				if (s>ub){
					parameterVector[iDim] = ub+(ub-s);
				}
				setParameterVector(iPop, parameterVector);
			}
		}
	}

	public List<Sample> getProposals() {
		return this.sampleList;
	}
	
	public Sample getProposal(int index) {
		return this.sampleList.get(index);
	}
}


