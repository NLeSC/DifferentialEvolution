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
import java.util.List;

public class ListOfSamples {

	private List<Sample> sampleList; 
	private int nPop;
	private int nPars;
	private int nStates;
	private double[][][] statesPriors;

	// constructor
	public ListOfSamples(int nPop, ParSpace parSpace, StateSpace stateSpace, int nTimes ){

		this.nPars = parSpace.getNumberOfPars();
		this.sampleList = new ArrayList<Sample>();
		this.nPop = nPop;
		for (int iPop = 1; iPop <= nPop; iPop++) {
			Sample sample = new Sample(nPars);
			sampleList.add(sample);
		}
		if (stateSpace==null){
	        this.nStates = 0;
		}
		else {
	        this.nStates = stateSpace.getNumberOfStates();
		}
		statesPriors = new double[nPop][nStates][nTimes];
		for (int iPop=0;iPop<nPop;iPop++){
			for (int iState=0;iState<nStates;iState++){
				statesPriors[iPop][iState][0] = Double.NaN;
			}
		}
	}

	public void add(Sample sample){
		sampleList.add(sample);
	}

	public void setParameterVector(int index, double[] parameterVector) {
		this.sampleList.get(index).setParameterVector(parameterVector);
	}

	public double[] getParameterVector(int index) {
		return this.sampleList.get(index).getParameterVector();
	}

	public void setObjScore(int index, double objScore) {
		this.sampleList.get(index).setObjScore(objScore);
	}

	public double getObjScore(int index) {
		return this.sampleList.get(index).getObjScore();
	}

//	public void calcObjScore(double[][] obs, double[] initState, ForcingChunks forcingChunks,
//			TimeChunks timeChunks, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory) {
//
//		LikelihoodFunction likelihoodFunction = likelihoodFunctionFactory.create();
//		
//		if (modelFactory!=null){
//			int nChunks = timeChunks.getnChunks();
//
//			int nStates = initState.length;
//			int nTimes = timeChunks.getnTimes();
//
//			for (int iPop=0;iPop<nPop;iPop++){
//				double[] parameterVector = getParameterVector(iPop);
//				double[] state = new double[initState.length];
//				System.arraycopy(initState, 0, state, 0, nStates);
//				
//				double[][] sim = new double[nStates][nTimes];
//				for (int iState=0;iState<nStates;iState++){
//					sim[iState][0] = Double.NaN;
//				}
//				for (int iChunk=0;iChunk<nChunks;iChunk++){
//					double[] times = timeChunks.getChunk(iChunk);
//					double[] forcing = forcingChunks.getChunk(iChunk);
//					int[] indices = timeChunks.getChunkIndices(iChunk);
//					int nIndices = indices.length;
//
//					Model model = modelFactory.create(state, parameterVector, forcing, times);
//					double[][] simChunk = model.evaluate();
//
//					for (int iState=0;iState<nStates;iState++){
//						for (int iIndex=1;iIndex<nIndices;iIndex++){
//							sim[iState][indices[iIndex]] = simChunk[iState][iIndex];
//						}
//						state[iState] = simChunk[iState][nIndices-1];
//					}
//					
//					updateStatesPrior(iPop, indices, simChunk);					
//				}//iChunk
//				
//
//
//				double objScore = likelihoodFunction.evaluate(obs, sim);
//				setObjScore(iPop, objScore);
//
//			} //iPop		
//		}
//		else {
//			for (int iPop=0;iPop<nPop;iPop++){
//				double[] parameterVector = getParameterVector(iPop);
//				double objScore = likelihoodFunction.evaluate(parameterVector);
//				setObjScore(iPop, objScore);
//			} // iPop
//		} //else
//
//	} // calcObjScore()
	
	
	public void updateStatesPrior(int iPop, int[] timeIndices, double[][] stateValues){
		
		int nStates = stateValues.length;
		int nTimes = timeIndices.length;
		
		for (int iTime=1;iTime<nTimes;iTime++){
			for (int iState=0;iState<nStates;iState++){
				int t = timeIndices[iTime];
				statesPriors[iPop][iState][t] = stateValues[iState][iTime];
			}
		}
	}


	public int getSize(){
		return sampleList.size();
	}
	
	public int getnPop(){
		return nPop;
	}

	public int getnPars(){
		return nPars;
	}

	public List<Sample> getSampleList(){
		return sampleList;
	}
	
	public double[][][] getStatesPriors(){
		return statesPriors;
	}
	

}


