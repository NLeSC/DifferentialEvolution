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

	private ParSpace parSpace;
	
	// constructor
	public Proposals(int nPop, ParSpace parSpace, StateSpace stateSpace,int nTimes){
		super(nPop,parSpace,stateSpace,nTimes);
		this.parSpace = parSpace;
	}
	
	public void reflectIfOutOfBounds(){

		int nPop = getnPop();
		int nPars = getnPars();
		
		for (int iPop=0;iPop<nPop;iPop++){
			for (int iPar = 0;iPar<nPars;iPar++){
				double[] parameterVector = getParameterVector(iPop);
				double lb = parSpace.getLowerBound(iPar);
				double ub = parSpace.getUpperBound(iPar);
				double s = parameterVector[iPar];
				if (s<lb){
					parameterVector[iPar] = lb+(lb-s);
				}					
				if (s>ub){
					parameterVector[iPar] = ub+(ub-s);
				}
				setParameterVector(iPop, parameterVector);
			}
		}
	}

	public List<Sample> getProposals() {
		return this.getSampleList();
	}
	
	public Sample getProposal(int index) {
		return this.getSampleList().get(index);
	}
}


