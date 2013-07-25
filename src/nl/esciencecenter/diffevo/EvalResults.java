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

public class EvalResults {

	private ArrayList<Sample> sampleList; 
	
	// constructor
	public EvalResults(){
		
		this.sampleList = new ArrayList<Sample>();
	}
	
	public void add(Sample sample){
		sampleList.add(sample);
	}
	
	public ArrayList<Sample> getEvalResults() {
		return this.sampleList;
	}
	
	public Sample EvalResult(int index) {
		return this.sampleList.get(index);
	}

	
	public double[] getParameterVector(int index) {
		return this.sampleList.get(index).getParameterVector();
	}

	public double getObjScore(int index) {
		return this.sampleList.get(index).getObjScore();
	}

	public int size() {
		return this.sampleList.size();
	}
	
	public int getSampleCounter(int index) {
		return this.sampleList.get(index).getSampleCounter();
	}
	
	
	
}


