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

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;
import nl.esciencecenter.diffevo.statespacemodels.Model;

public class ListOfParameterCombinations{

	private double[][] parameterCombinations;
	private int nPop;
	private int nPars;
	private double[] objScores;
	private double[] initState;
	private TimeChunks timeChunks;
	private ForcingChunks forcingChunks;
	private ModelFactory modelFactory;
	private double[][][] modelResults;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private int[] firstOccurrence;

	// constructor
	public ListOfParameterCombinations(int nPop, int nPars, LikelihoodFunctionFactory likelihoodFunctionFactory){

		this.parameterCombinations = new double[nPop][nPars];
		this.nPop = nPop;
		this.nPars = nPars;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.objScores = new double[nPop];
		this.firstOccurrence = new int[nPop];
		
		double[] parameterCombinationNan = new double[nPars];
		for (int iPar=0;iPar<nPars;iPar++){
			parameterCombinationNan[iPar] = Double.NaN;
		}
		for (int iPop=0;iPop<nPop;iPop++){
			objScores[iPop] = Double.NaN;
			firstOccurrence[iPop] = iPop;
		}
		
	}
	
	// constructor
	public ListOfParameterCombinations(int nPop, int nPars, LikelihoodFunctionFactory likelihoodFunctionFactory, 
			double[] initState, TimeChunks timeChunks, ForcingChunks forcingChunks, ModelFactory modelFactory){

		this(nPop,nPars, likelihoodFunctionFactory);
		
		int nStates = initState.length;
		int nTimes = timeChunks.getnTimes();
		modelResults = new double[nPop][nStates][nTimes];
		for (int iPop=0;iPop<nPop;iPop++){
			for (int iState=0;iState<nStates;iState++){
				for (int iTime=0;iTime<nTimes;iTime++){
					modelResults[iPop][iState][iTime] = Double.NaN;
				}
			}
			objScores[iPop] = Double.NaN;
		}
		this.initState = initState;
		this.timeChunks = timeChunks;
		this.forcingChunks = forcingChunks;
		this.modelFactory = modelFactory;
	}
	
	public void calcModelResults() {

		int nStates = initState.length;
		int nTimes = timeChunks.getnTimes();

		if (modelFactory!=null){
			int nChunks = timeChunks.getnChunks();

			for (int iPop=0;iPop<nPop;iPop++){
				double[] parameterVector = parameterCombinations[iPop];
				double[] state = new double[initState.length];
				System.arraycopy(initState, 0, state, 0, nStates);

				double[][] sim = new double[nStates][nTimes];
				for (int iState=0;iState<nStates;iState++){
					sim[iState][0] = Double.NaN;
				}
				for (int iChunk=0;iChunk<nChunks;iChunk++){
					double[] times = timeChunks.getChunk(iChunk);
					double[] forcing = forcingChunks.getChunk(iChunk);
					int[] indices = timeChunks.getChunkIndices(iChunk);
					int nIndices = indices.length;

					Model model = modelFactory.create(state, parameterVector, forcing, times);
					double[][] simChunk = model.evaluate();

					for (int iState=0;iState<nStates;iState++){
						for (int iIndex=1;iIndex<nIndices;iIndex++){
							sim[iState][indices[iIndex]] = simChunk[iState][iIndex];
						}
						state[iState] = simChunk[iState][nIndices-1];
					}
				}//iChunk
				setModelResults(iPop, sim); 
			} //iPop
		}
		
	} // calcModelResults()
	
	
	public void calcObjScores() {
		
		// model is not dynamic

		LikelihoodFunction likelihoodFunction = likelihoodFunctionFactory.create();
		double objScore;
		
		for (int iPop=0;iPop<nPop;iPop++){
			double[] parameterVector = getParameterCombination(iPop);
			objScore = likelihoodFunction.evaluate(parameterVector);
			setObjScore(iPop, objScore);
		} //iPop
	} // calcObjScores()
	
	
	
	public void calcObjScores(double[][] obs) {
		
		// model is dynamic
		
		LikelihoodFunction likelihoodFunction = likelihoodFunctionFactory.create();
		double objScore;
		
		for (int iPop=0;iPop<nPop;iPop++){
			double[][] sim = getModelResult(iPop);
			objScore = likelihoodFunction.evaluate(obs, sim);
			setObjScore(iPop, objScore);
		} //iPop
	} // calcObjScores()
	
	

	
	public int getNumberOfPars(){
		return nPars;
	}

	public int getPopulationSize(){
		return nPop;
	}

	public double[] getParameterCombination(int iPop){
		return parameterCombinations[iPop].clone(); 
	}

	public void setParameterCombination(int iPop,double[] parameterValues){
		System.arraycopy(parameterValues, 0, parameterCombinations[iPop], 0, nPars);
	}

	public double getObjScore(int iPop) {
		return objScores[iPop];
	}

	public void setObjScore(int iPop, double objScore) {
		this.objScores[iPop] = objScore;
	}

	public double[][] getModelResult(int iPop) {
		return modelResults[iPop].clone();
	}

	public void setModelResults(int iPop, double[][] modelResult) {
		this.modelResults[iPop] = modelResult;
	}
	
	public int getFirstOccurrence(int iPop){
		return firstOccurrence[iPop];
	}

	public int setFirstOccurrence(int iPop, int index){
		return firstOccurrence[iPop] = index;
	}

	
	
}


