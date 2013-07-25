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

package nl.esciencecenter.diffevo.models;


public class RosenbrockModel implements Model {
	private double probabilityDensity;
	
	public RosenbrockModel(){
		this.probabilityDensity = Double.NaN;
		
	}
	
	private double calcProbabilityDensity(double[] x){
		double sum = 0.0;
		int nPars = x.length;
		for (int iPar = 0;iPar<nPars-1;iPar++){
			sum = sum + (1-Math.pow(x[iPar],2) + 100*Math.pow(x[iPar+1]-Math.pow(x[iPar],2), 2));
		}
		
		probabilityDensity = sum; // it's not really a probability density since this is just a benchmark function
		return probabilityDensity; 
	}
			
	@Override
	public double calcLogLikelihood(double[] x){
		double objScore;
		probabilityDensity = calcProbabilityDensity(x);
		objScore = -Math.log(probabilityDensity);
		return objScore;
	}

	public String getName(){
		return RosenbrockModel.class.getSimpleName();
	}
	
	
}
