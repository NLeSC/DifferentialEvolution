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


public class SingleNormalModel implements Model {
	private double parMu1;
	private double parSigma1;
	private double probabilityDensity;
	
	public SingleNormalModel(){
		parMu1 = -10;
		parSigma1 = 3;
	}
	
	private double calcProbabilityDensity(double[] x){
		double dens1;

		dens1 = (1.0/(Math.sqrt(2.0 * Math.PI * Math.pow(parSigma1,2)))) * Math.exp(-(1.0/2.0) * Math.pow((x[0]-parMu1)/parSigma1,2));
		
		probabilityDensity = dens1; 
		return probabilityDensity; 
	}
			
	@Override
	public double calcLogLikelihood(double[] x){
		double objScore;
		probabilityDensity = calcProbabilityDensity(x);
		objScore = Math.log(probabilityDensity);
		return objScore;
	}

	public String getName(){
		return SingleNormalModel.class.getSimpleName();
	}
	
	
	
}
