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
import java.util.Random;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;
import nl.esciencecenter.diffevo.statespacemodels.Model;

public class EvalResults {

	private ArrayList<EvalResult> listOfEvalResult;
	private final ParSpace parSpace;
	private final int nGens;
	private final int nPop;
	private final StateSpace stateSpace;
	private final double[] initState;
	private final double[] forcing;
	private final double[] times;
	private final double[] assimilate;
	private final double[][] obs;
	private final ModelFactory modelFactory;
	private final LikelihoodFunctionFactory likelihoodFunctionFactory;
	private final Random generator;
	private final String modelName;
	private int nResults;
	
	// constructor
	public EvalResults(int nGens, int nPop, ParSpace parSpace, LikelihoodFunctionFactory likelihoodFunctionFactory, Random generator){
		
		this.nGens = nGens;
		this.nPop = nPop;
		this.parSpace = parSpace;
		this.stateSpace = null;

		LikelihoodFunction likelihoodFunction = likelihoodFunctionFactory.create();
		this.modelName = likelihoodFunction.getName();
		this.modelFactory = null;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.generator = generator;

		this.initState = null;
		this.forcing = null;
		this.times = null;
		this.assimilate = null;
		this.obs = null;
		
		this.listOfEvalResult= new ArrayList<EvalResult>();
		updateSize();
		
	}

	// constructor
	public EvalResults(int nGens, int nPop, ParSpace parSpace, StateSpace stateSpace, double[] initState, double[] forcing, double[] times, 
			double[] assimilate, double[][] obs, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory, Random generator){
		
		this.nGens = nGens;
		this.nPop = nPop;
		this.parSpace = parSpace;
		this.stateSpace = stateSpace;

		Model model = modelFactory.create(initState, new double[parSpace.getNumberOfPars()], forcing, times);
		this.modelName = model.getName();
		this.modelFactory = modelFactory;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.generator = generator;

		this.initState = initState.clone();
		this.forcing = forcing.clone();
		this.times = times.clone();
		this.assimilate = assimilate.clone();
		this.obs = obs.clone();
		
		this.listOfEvalResult= new ArrayList<EvalResult>();
		updateSize();

	}
	
	
	public void add(EvalResult evalResult){
		listOfEvalResult.add(evalResult);
		updateSize();
	}

	
	// get the whole list:
	public ArrayList<EvalResult> getEvalResults() {
		return this.listOfEvalResult;
	}

	// get an element of the list:
	public EvalResult getEvalResult(int index) {
		return this.listOfEvalResult.get(index);
	}
	
	// get only the sample identifier of one element of the list:
	public int getSampleIdentifier(int index) {
		return this.listOfEvalResult.get(index).getSampleIdentifier();
	}

	// get only the parameter combination of one element of the list:
	public double[] getParameterCombination(int index) {
		return this.listOfEvalResult.get(index).getParameterCombination();
	}
	
	// get only the objective score of one element of the list:
	public double getObjScore(int index) {
		return this.listOfEvalResult.get(index).getObjScore();
	}
	
	public int getNumberOfEvalResults(){
		return listOfEvalResult.size();
	}
	
	public ParSpace getParSpace(){
		return parSpace;
	}

	public String getModelName(){
		return modelName;
	}

	public ArrayList<EvalResult> getListOfEvalResult() {
		return listOfEvalResult;
	}

	public int getnGens() {
		return nGens;
	}

	public int getnPop() {
		return nPop;
	}

	public StateSpace getStateSpace() {
		return stateSpace;
	}

	public double[] getInitState() {
		return initState.clone();
	}

	public double[] getForcing() {
		return forcing.clone();
	}

	public double[] getTimes() {
		return times.clone();
	}

	public double[] getAssimilate() {
		return assimilate.clone();
	}

	public double[][] getObs() {
		return obs.clone();
	}

	public ModelFactory getModelFactory() {
		return modelFactory;
	}

	public LikelihoodFunctionFactory getLikelihoodFunctionFactory() {
		return likelihoodFunctionFactory;
	}

	public Random getGenerator() {
		return generator;
	}
	
	
	public int[] sampleIdentifiersOfBest(){
		
		List<Integer> list = new ArrayList<Integer>();		
		
		int nResults = this.getListOfEvalResult().size();
		double currentObjScore;
		double highestObjScore = this.highestObjScore();
		
		for (int iResult=0;iResult<nResults;iResult++){
			currentObjScore = this.getObjScore(iResult);
			if (currentObjScore==highestObjScore){
				list.add(iResult);
			}
		}
		int nItems = list.size();
		int[] bestIndices = new int[nItems];
		for (int iItem=0;iItem<nItems;iItem++){
			bestIndices[iItem] = list.get(iItem);
		}
		return bestIndices;
	}

	public double highestObjScore(){
		
		double currentObjScore = this.getObjScore(0);
		double highestObjScore = currentObjScore;
		
		int nResults = this.getListOfEvalResult().size();
		for (int iResult=1;iResult<nResults;iResult++){
			currentObjScore = this.getObjScore(iResult);
			if (currentObjScore>highestObjScore){
				highestObjScore = currentObjScore;
			}
		}
		return highestObjScore;
	}
	

	public double[][] parameterCombinationsOfBest(){
		
		int[] sampleIdentifiersOfBest = this.sampleIdentifiersOfBest();
		int nPars = this.getParSpace().getNumberOfPars();
		int nSamplesBest = sampleIdentifiersOfBest.length;
		double[][] bestParameterCombinations = new double[nSamplesBest][nPars];
		for (int iSampleBest=0;iSampleBest<nSamplesBest;iSampleBest++){
			for (int iPar=0;iPar<nPars;iPar++){
				bestParameterCombinations[iSampleBest] = this.getEvalResult(iSampleBest).getParameterCombination(); 
			}
		}
		return bestParameterCombinations;
	}

	public int size() {
		return nResults;
	}

	public void updateSize() {
		this.nResults = listOfEvalResult.size();
	}
	
	
	
	
}


