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



import java.util.Random;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
//import nl.esciencecenter.diffevo.likelihoodfunctions.LikelihoodFunction;
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;
//import nl.esciencecenter.diffevo.statespacemodels.Model;

/**
 * This is the Differential Evolution algorithm by Storn and Price
 * 
 * @author Jurriaan H. Spaaks
 * 
 */
public class DiffEvo {

	private final int nPars;
	private final int nPop;
	private final int nGens;
	private ListOfParameterCombinations parents;
	private ListOfParameterCombinations  proposals;
	private EvalResults evalResults;
	private Random generator;
	private ParSpace parSpace;	
//	private double[] initState;
	private ForcingChunks forcingChunks;
	private TimeChunks timeChunks;
	private double[][] obs;
//	private ModelFactory modelFactory;
//	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private final static long defaultRandomSeed = 0;
	private boolean modelIsDynamic;
	
	
	// constructor:
	DiffEvo(int nGens, int nPop, ParSpace parSpace, LikelihoodFunctionFactory likelihoodFunctionFactory) {
		// use seed of zero by default:
		this(nGens, nPop, parSpace, likelihoodFunctionFactory, defaultRandomSeed);
	}

	// constructor:
	DiffEvo(int nGens, int nPop, ParSpace parSpace, StateSpace stateSpace, double[] initState, double[] forcing, double[] times, 
			double[] assimilate, double[][] obs, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory) {
		// use seed of zero by default:
		this(nGens, nPop, parSpace, stateSpace, initState, forcing, times, assimilate, obs, 
				modelFactory, likelihoodFunctionFactory, defaultRandomSeed);
	}
	
	// constructor:
	DiffEvo(int nGens, int nPop, ParSpace parSpace, LikelihoodFunctionFactory likelihoodFunctionFactory, long seed) {
		this.nGens = nGens;
		this.nPop = nPop;
		this.nPars = parSpace.getNumberOfPars();
		this.parSpace = parSpace;
		this.parents = new ListOfParameterCombinations(nPop, nPars, likelihoodFunctionFactory);
		this.proposals = new ListOfParameterCombinations(nPop, nPars, likelihoodFunctionFactory);
		this.generator = new Random();
		this.generator.setSeed(seed);
//		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.evalResults = new EvalResults(nGens, nPop, parSpace, likelihoodFunctionFactory, generator);
		this.modelIsDynamic = false;
	}

	// constructor:
	DiffEvo(int nGens, int nPop, ParSpace parSpace, StateSpace stateSpace, double[] initState, double[] forcing, double[] times, 
			double[] assimilate, double[][] obs, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory, long seed) {
		this(nGens, nPop, parSpace, likelihoodFunctionFactory, seed);
		if (modelFactory!=null){
//			this.initState = initState.clone();
			this.forcingChunks = new ForcingChunks(forcing.clone(), assimilate.clone());
			this.timeChunks = new TimeChunks(times.clone(), assimilate.clone());
			this.obs = obs.clone();
//			this.modelFactory = modelFactory;
			this.parents = new ListOfParameterCombinations(nPop, nPars, likelihoodFunctionFactory, initState, timeChunks, forcingChunks, modelFactory);
			this.proposals = new ListOfParameterCombinations(nPop, nPars, likelihoodFunctionFactory, initState, timeChunks, forcingChunks, modelFactory);
		}
		this.evalResults = new EvalResults(nGens, nPop, parSpace, stateSpace, initState, forcing, times, 
				assimilate, obs, modelFactory, likelihoodFunctionFactory, generator);
		this.modelIsDynamic = true;
	}

	
	public EvalResults runOptimization(){
		System.out.println("Starting Differential Evolution optimization...");		
		initializeParents();
		for (int iGen = 1;iGen<nGens;iGen++){
			proposeOffSpring();
			updateParentsWithProposals();
		}
		return evalResults; 
	}
	
	
	public void initializeParents(){
		
		// take uniform random samples of the parameter space and add them to the parents array:
		for (int iPop=0;iPop<nPop;iPop++){
			double[] parameterCombination = parSpace.takeUniformRandomSample(generator);
			parents.setParameterCombination(iPop, parameterCombination);
		}

		if (modelIsDynamic){
			// if the model in question is dynamic, generate the model prediction by running the model. Then feed the list of model
			// predictions into a function that runs the likelihoodfunction on the prediction, yielding a list of objective scores
			parents.calcModelResults();
			parents.calcObjScores(obs);
		}
		else {
			// if the model in question is not dynamic, use the likelihood function directly to calculate an objective score for each entry in parents.
			parents.calcObjScores();
		}
		
		// now add the initial values of parents to the record, i.e. evalResults
		for (int iPop=0;iPop<nPop;iPop++){
			int sampleIdentifier = iPop; 
			double[] parameterCombination = parents.getParameterCombination(iPop).clone();
			double objScore = parents.getObjScore(iPop);
			EvalResult evalResult = null;
			if (modelIsDynamic){
				evalResult = new EvalResult(sampleIdentifier, parameterCombination, objScore, parents.getModelResult(iPop));
			}
			else {
				evalResult = new EvalResult(sampleIdentifier, parameterCombination, objScore);
			}
			evalResults.add(evalResult);
		}
	}
	
	public void proposeOffSpring(){
	
		final int nDraws = 3;
		final double diffEvoParF = 0.6;
		final double diffEvoParK = 0.4;
		int[] availables = new int[nDraws];
		boolean drawAgain = true;
		int index;
		double[] dist1;
		double[] dist2;
		double[] proposal;
		double[] parent;
		
		// draw 3 random integer indices from [0,nPop], but not your own index and no recurrent samples
		for (int iPop=0;iPop<nPop;iPop++){
			availables[0] = -1;
			availables[1] = -1;
			availables[2] = -1;
			for (int iDraw=0;iDraw<nDraws;iDraw++){
				drawAgain = true;
				index = -1;
				while (drawAgain){
					index = generator.nextInt(nPop);
					drawAgain = index == iPop | index == availables[0] | index == availables[1] | index == availables[2];
				}
				availables[iDraw] = index;
			}
			
			dist1 = calcDistance(iPop, availables, 1);
			dist2 = calcDistance(iPop, availables, 2);
			
			parent = parents.getParameterCombination(iPop);

			proposal = new double[nPars];
			for (int iPar=0;iPar<nPars;iPar++){
				proposal[iPar] = parent[iPar] + diffEvoParF * dist1[iPar] + diffEvoParK * dist2[iPar]; 				
			}
			proposals.setParameterCombination(iPop, proposal);
 		}
		proposals = parSpace.reflectIfOutOfBounds(proposals);
		
		if (modelIsDynamic){
			// if the model in question is dynamic, generate the model prediction by running the model. Then feed the list of model
			// predictions into a function that runs the likelihoodfunction on the prediction, yielding a list of objective scores
			proposals.calcModelResults();
			proposals.calcObjScores(obs);
		}
		else {
			// if the model in question is not dynamic, use the likelihood function directly to calculate an objective score for each entry in parents.
			proposals.calcObjScores();
		}
		
	}
	
	private double[] calcDistance(int iPop, int[] availables, int distanceOneOrTwo){
		int fromIndex;
		int toIndex;
		double[] fromPoint;
		double[] toPoint;
		double[] dist;
 		
		if (distanceOneOrTwo==1){
			fromIndex = iPop;
			toIndex = availables[0];
		}
		else{
			fromIndex = availables[1];
			toIndex = availables[2];
		}
		
		fromPoint = parents.getParameterCombination(fromIndex);
		toPoint = parents.getParameterCombination(toIndex);
		
		dist = new double[nPars];

		for (int iPar=0;iPar<nPars;iPar++){
			dist[iPar] = fromPoint[iPar]-toPoint[iPar];
		}
		
		return dist;
	}

	public void updateParentsWithProposals(){
		double scoreParent;
		double scoreProposal;
		int nModelEvals = evalResults.getNumberOfEvalResults();

		double logOfUnifRandDraw;
		double[] parameterCombination;
		double objScore;
		double[][] sim = null;
		
		for (int iPop=0;iPop<nPop;iPop++){
			scoreParent = parents.getObjScore(iPop);
			scoreProposal = proposals.getObjScore(iPop);
			logOfUnifRandDraw = Math.log(generator.nextDouble());
			int sampleIdentifier = nModelEvals+iPop;
			if (scoreProposal-scoreParent >= logOfUnifRandDraw){
				// accept proposal
				parameterCombination = proposals.getParameterCombination(iPop);
				objScore = proposals.getObjScore(iPop);
				if (modelIsDynamic){
					sim = proposals.getModelResult(iPop); 
				}
			}
			else{
				// reject proposal
				parameterCombination = parents.getParameterCombination(iPop);
				objScore = parents.getObjScore(iPop);
				if (modelIsDynamic){
					sim = parents.getModelResult(iPop); 
				}
 
			}
			
			parents.setParameterCombination(iPop, parameterCombination);
			parents.setObjScore(iPop, objScore);
			
			// add most recent sample to the record array evalResults:
			EvalResult evalResult;
			if (modelIsDynamic){
				evalResult = new EvalResult(sampleIdentifier, parameterCombination.clone(), objScore, sim.clone());
			}
			else {
				evalResult = new EvalResult(sampleIdentifier, parameterCombination.clone(), objScore);
			}
			evalResults.add(evalResult);
		}
	}
	
	public int getnPop() {
		return nPop;
	}

	
	
}
