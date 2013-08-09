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
import java.util.Random;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;

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
		
	}

	// constructor
	public EvalResults(int nGens, int nPop, ParSpace parSpace, StateSpace stateSpace, double[] initState, double[] forcing, double[] times, 
			double[] assimilate, double[][] obs, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory, Random generator){
		
		this.nGens = nGens;
		this.nPop = nPop;
		this.parSpace = parSpace;
		this.stateSpace = stateSpace;

		LikelihoodFunction likelihoodFunction = likelihoodFunctionFactory.create();
		this.modelName = likelihoodFunction.getName();
		this.modelFactory = modelFactory;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.generator = generator;

		this.initState = initState.clone();
		this.forcing = forcing.clone();
		this.times = times.clone();
		this.assimilate = assimilate.clone();
		this.obs = obs.clone();
		
		this.listOfEvalResult= new ArrayList<EvalResult>();

	}
	
	
	public void add(EvalResult evalResult){
		listOfEvalResult.add(evalResult);
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
		return initState;
	}

	public double[] getForcing() {
		return forcing;
	}

	public double[] getTimes() {
		return times;
	}

	public double[] getAssimilate() {
		return assimilate;
	}

	public double[][] getObs() {
		return obs;
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


}


