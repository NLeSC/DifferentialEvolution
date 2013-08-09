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

public class ListOfParameterCombinations{

	private double[][] parameterCombinations;
	private int nPop;
	private int nPars;
	private double[] objScores;

	// constructor
	public ListOfParameterCombinations(int nPop, int nPars){

		this.parameterCombinations = new double[nPop][nPars];
		this.objScores = new double[nPop];
		this.nPop = nPop;
		this.nPars = nPars;
		
		double[] parameterCombinationNan = new double[nPars];
		for (int iPar=0;iPar<nPars;iPar++){
			parameterCombinationNan[iPar] = Double.NaN;
		}

		for (int iPop=0;iPop<nPop;iPop++){
			setParameterCombination(iPop, parameterCombinationNan);			
			setObjScore(iPop,Double.NaN);
		}
	}
	
	public int getNumberOfPars(){
		return nPars;
	}

	public int getPopulationSize(){
		return nPop;
	}

	public double[] getParameterCombination(int iPop){
		return parameterCombinations[iPop].clone(); 
	}

	public double getObjScore(int iPop){
		return objScores[iPop]; 
	}
	
	public void setObjScore(int iPop,double objScoreValue){
		objScores[iPop] = objScoreValue;
	}

	public void setParameterCombination(int iPop,double[] parameterValues){
		for (int iPar = 0;iPar<nPars;iPar++){
			parameterCombinations[iPop][iPar] = parameterValues[iPar];
		}
	}
	
}


