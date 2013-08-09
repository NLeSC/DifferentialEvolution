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
	private ParSpace parSpace;
	private int nGens;
	private int nPop;
	private StateSpace stateSpace;
	private double[] initState;
	private double[] forcing;
	private double[] times;
	private double[] assimilate;
	private double[][] obs;
	private ModelFactory modelFactory;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private Random generator;
	private String modelName;
	
	// constructor
	public EvalResults(int nGens, int nPop, ParSpace parSpace, LikelihoodFunctionFactory likelihoodFunctionFactory, Random generator){
		
		this.nGens = nGens;
		this.nPop = nPop;
		this.parSpace = parSpace;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.generator = generator;
		LikelihoodFunction likelihoodFunction = likelihoodFunctionFactory.create();
		this.modelName = likelihoodFunction.getName();
		this.listOfEvalResult = new ArrayList<EvalResult>();
		
	}

	// constructor
	public EvalResults(int nGens, int nPop, ParSpace parSpace, StateSpace stateSpace, double[] initState, double[] forcing, double[] times, 
			double[] assimilate, double[][] obs, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory, Random generator){
		
		this.nGens = nGens;
		this.nPop = nPop;
		this.parSpace = parSpace;
		this.stateSpace = stateSpace;
		this.initState = initState.clone();
		this.forcing = forcing.clone();
		this.times = times.clone();
		this.assimilate = assimilate.clone();
		this.obs = obs.clone();
		this.modelFactory = modelFactory;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.generator = generator;
		LikelihoodFunction likelihoodFunction = likelihoodFunctionFactory.create();
		this.modelName = likelihoodFunction.getName();
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
		return this.listOfEvalResult.get(index).getParameterVector();
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
	
	// 
	
	
	
}


