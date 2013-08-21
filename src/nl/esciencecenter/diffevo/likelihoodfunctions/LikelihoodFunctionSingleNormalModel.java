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

package nl.esciencecenter.diffevo.likelihoodfunctions;


public class LikelihoodFunctionSingleNormalModel implements LikelihoodFunction {
	private double parMu1;
	private double parSigma1;
	private double probabilityDensity;
	
	public LikelihoodFunctionSingleNormalModel(){
		parMu1 = -10;
		parSigma1 = 3;
	}
	
	private double calcProbabilityDensity(double[] x){
		
		double term1 = (1.0/(parSigma1*Math.sqrt(2*Math.PI)));
		double term2 = (x[0]-parMu1)/parSigma1;
		double term3 = Math.exp((-1.0/2)*Math.pow(term2, 2));
		double dens1 = term1 * term3;
		
		probabilityDensity = dens1; 
		return probabilityDensity; 
	}
			
	public String getName(){
		return LikelihoodFunctionSingleNormalModel.class.getSimpleName();
	}

	@Override
	public double evaluate(double[][] obs, double[][] sim) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double evaluate(double[] parameterVector) {
		double objScore;
		probabilityDensity = calcProbabilityDensity(parameterVector);
		objScore = Math.log(probabilityDensity);
		return objScore;
	}
	
	
	
}
