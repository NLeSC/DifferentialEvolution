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
import nl.esciencecenter.diffevo.statespacemodelfactories.ModelFactory;

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
	private Parents parents;
	private Proposals proposals;
	private EvalResults evalResults;
	private Random generator;
	private StateSpace stateSpace;
	private double[] initState;
	private ForcingChunks forcingChunks;
	private TimeChunks timeChunks;
	private double[][] obs;
	private ModelFactory modelFactory;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	
	
	// constructor:
	DiffEvo(int nGens, int nPop, ParSpace parSpace, LikelihoodFunctionFactory likelihoodFunctionFactory) {
		this.nGens = nGens;
		this.nPop = nPop;
		this.nPars = parSpace.getNumberOfPars();
		this.parents = new Parents(nPop,parSpace,stateSpace,0);
		this.proposals = new Proposals(nPop,parSpace,stateSpace,0);
		this.generator = new Random();
		this.generator.setSeed(0);
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.evalResults = new EvalResults(nGens, nPop, parSpace, likelihoodFunctionFactory, generator);
		}
	
	// constructor:
	DiffEvo(int nGens, int nPop, ParSpace parSpace, StateSpace stateSpace, double[] initState, double[] forcing, double[] times, 
			double[] assimilate, double[][] obs, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory) {
		this.nGens = nGens;
		this.nPop = nPop;
		this.stateSpace = stateSpace;
		if (modelFactory!=null){
			this.initState = initState.clone();
			this.forcingChunks = new ForcingChunks(forcing.clone(), assimilate.clone());
			this.timeChunks = new TimeChunks(times.clone(), assimilate.clone());
		}
		this.nPars = parSpace.getNumberOfPars();
		this.parents = new Parents(nPop,parSpace,stateSpace,times.length);
		this.proposals = new Proposals(nPop,parSpace,stateSpace,times.length);
		this.generator = new Random();
		this.generator.setSeed(0);
		this.obs = obs.clone();
		this.modelFactory = modelFactory;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.evalResults = new EvalResults(nGens, nPop, parSpace, stateSpace, initState, forcing, times, 
				assimilate, obs, modelFactory, likelihoodFunctionFactory, generator);
		}

	
	public void initializeParents(){
		int nModelEvals = 0;
		Sample sample;
		double[] parameterVector;
		double objScore;
		
		parents.takeUniformRandomSamples(generator);
		parents.calcObjScore(obs, initState,forcingChunks,timeChunks,modelFactory, likelihoodFunctionFactory);
		
		// now add the initial values of parents to the record, i.e. evalResults
		for (int iPop=0;iPop<nPop;iPop++){
			parameterVector = parents.getParameterVector(iPop);
			sample = new Sample(nPars);
			objScore = parents.getObjScore(iPop);
			sample.setSampleCounter(nModelEvals+iPop);
			sample.setParameterVector(parameterVector);
			sample.setObjScore(objScore);
			evalResults.add(sample);
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
			
			parent = parents.getParameterVector(iPop);

			proposal = new double[nPars];
			for (int iPar=0;iPar<nPars;iPar++){
				proposal[iPar] = parent[iPar] + diffEvoParF * dist1[iPar] + diffEvoParK * dist2[iPar]; 				
			}
			proposals.setParameterVector(iPop, proposal);
 		}
		proposals.reflectIfOutOfBounds();
		proposals.calcObjScore(obs, initState, forcingChunks, timeChunks, modelFactory, likelihoodFunctionFactory);
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
		
		fromPoint = parents.getParameterVector(fromIndex);
		toPoint = parents.getParameterVector(toIndex);
		
		dist = new double[nPars];

		for (int iPar=0;iPar<nPars;iPar++){
			dist[iPar] = fromPoint[iPar]-toPoint[iPar];
		}
		
		return dist;
	}

	public void updateParentsWithProposals(){
		double scoreParent;
		double scoreProposal;
		int nModelEvals = evalResults.size();
		Sample sample;
		double logOfUnifRandDraw;
		double[] parameterVector;
		double objScore;
		
		
		for (int iPop=0;iPop<nPop;iPop++){
			scoreParent = parents.getObjScore(iPop);
			scoreProposal = proposals.getObjScore(iPop);
			logOfUnifRandDraw = Math.log(generator.nextDouble());
			if (scoreProposal-scoreParent >= logOfUnifRandDraw){
				// accept proposal
				parameterVector = proposals.getParameterVector(iPop);
				objScore = proposals.getObjScore(iPop);
			}
			else{
				// reject proposal
				parameterVector = parents.getParameterVector(iPop);
				objScore = parents.getObjScore(iPop);
			}
			sample = new Sample(nPars);
			sample.setSampleCounter(nModelEvals+iPop);
			sample.setParameterVector(parameterVector);
			sample.setObjScore(objScore);
			parents.setParent(iPop, sample);
			
			// add most recent sample to the record array evalResults:
			evalResults.add(sample);
		}
	}
	
	public int getnPop() {
		return nPop;
	}

	public EvalResults start(){
		System.out.println("Starting Differential Evolution optimization...");		
		initializeParents();
		for (int iGen = 1;iGen<nGens;iGen++){
			proposeOffSpring();
			updateParentsWithProposals();
		}
		return evalResults; 
	}
	
	
}
