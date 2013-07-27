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

package likelihoodfunctions;

public class LikelihoodFunctionSSR implements LikelihoodFunction {

	
	public LikelihoodFunctionSSR(double[][] obs, double[][] sim){
	}
	
	public LikelihoodFunctionSSR() {
		// TODO Auto-generated constructor stub
	}

	public String getName(){
		return LikelihoodFunctionSSR.class.getSimpleName();
	}

	@Override
	public double evaluate(double[][] obs, double[][] sim) {
		
		int nStates = obs.length;
		int nTimes = obs[0].length;
		double ssr = 0;
		for (int iState=0;iState<nStates;iState++){
			for (int iTime=1;iTime<nTimes;iTime++){
				ssr = ssr + Math.pow(obs[iState][iTime] - sim[iState][iTime], 2);
			}
		}
		
		int nObs = (nTimes-1) * nStates;
		double objScore = -(1.0/2) * nObs * Math.log(ssr);
		return objScore;
	}

	@Override
	public double evaluate(double[] parameterVector) {
		return 0;
	}
	
}
