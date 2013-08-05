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

public class LikelihoodFunctionRastriginModel implements LikelihoodFunction {
	private static final double A = 10;

	public LikelihoodFunctionRastriginModel(){
		//
	}
	
	private double calcProbabilityDensity(double[] x){
		
		// http://en.wikipedia.org/wiki/Rastrigin_function
		double sum = 0.0;
		int nDims = x.length;

        for (int iDim = 0;iDim<nDims;iDim++){
        	sum = sum + (Math.pow(x[iDim],2) - A*Math.cos(2*Math.PI*x[iDim]));
        }
		
     // not a real probability density, just a benchmark function value
		double probabilityDensity = A*nDims + sum; 
		return probabilityDensity; 
	}
			
	public String getName(){
		return LikelihoodFunctionRastriginModel.class.getSimpleName();
	}

	@Override
	public double evaluate(double[][] obs, double[][] sim) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double evaluate(double[] parameterVector) {
		double objScore;
		objScore = -calcProbabilityDensity(parameterVector);
		return objScore;
	}
	
	
}
