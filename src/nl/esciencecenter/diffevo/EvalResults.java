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
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;

public class EvalResults {

	private List<Sample> sampleList;
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
		this.modelName = likelihoodFunctionFactory.getClass().getSimpleName().toString();
		this.sampleList = new ArrayList<Sample>();
		
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
		this.modelName = likelihoodFunctionFactory.getClass().getSimpleName().toString();
		this.sampleList = new ArrayList<Sample>();
	}
	
	
	public void add(Sample sample){
		sampleList.add(sample);
	}
	
	public List<Sample> getEvalResults() {
		return this.sampleList;
	}
	
	public Sample getSample(int index) {
		return this.sampleList.get(index);
	}
	
	public double[] getParameterVector(int index) {
		return this.sampleList.get(index).getParameterVector();
	}

	public double getObjScore(int index) {
		return this.sampleList.get(index).getObjScore();
	}

	public int size() {
		return this.sampleList.size();
	}
	
	public int getSampleCounter(int index) {
		return this.sampleList.get(index).getSampleCounter();
	}

	public List<Sample> getSampleList() {
		return sampleList;
	}

	public ParSpace getParSpace() {
		return parSpace;
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

	public String getModelName() {
		return modelName;
	}

	public void setSampleList(List<Sample> sampleList) {
		this.sampleList = sampleList;
	}

	public void setParSpace(ParSpace parSpace) {
		this.parSpace = parSpace;
	}

	public void setnGens(int nGens) {
		this.nGens = nGens;
	}

	public void setnPop(int nPop) {
		this.nPop = nPop;
	}

	public void setStateSpace(StateSpace stateSpace) {
		this.stateSpace = stateSpace;
	}

	public void setInitState(double[] initState) {
		this.initState = initState;
	}

	public void setForcing(double[] forcing) {
		this.forcing = forcing;
	}

	public void setTimes(double[] times) {
		this.times = times;
	}

	public void setAssimilate(double[] assimilate) {
		this.assimilate = assimilate;
	}

	public void setObs(double[][] obs) {
		this.obs = obs;
	}

	public void setModelFactory(ModelFactory modelFactory) {
		this.modelFactory = modelFactory;
	}

	public void setLikelihoodFunctionFactory(
			LikelihoodFunctionFactory likelihoodFunctionFactory) {
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
	}

	public void setGenerator(Random generator) {
		this.generator = generator;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	
	
}


