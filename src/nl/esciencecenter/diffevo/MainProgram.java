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

import java.io.File;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.*;
import nl.esciencecenter.diffevo.statespacemodelfactories.*;

public class MainProgram {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int nGens = 0;
		int nPop = 0;
		double[] initState = null;
		double[] assimilate = null;
		double[] times = null;
		double[] forcing = null;
		double[] lowerBounds;
		double[] upperBounds;
		String[] parNames;
		ParSpace parSpace = null;
		double[][] obs = null;
		ModelFactory modelFactory = null;
		LikelihoodFunctionFactory likelihoodFunctionFactory = null;
		DiffEvo diffEvo = null;
		File file = null;
		
		for (int modelSwitch = 0;modelSwitch<6;modelSwitch++){

			switch (modelSwitch){
			case 0:{
				//DoubleNormalModel
				nGens = 300;
				nPop = 50;

				lowerBounds = new double[]{-20};
				upperBounds = new double[]{18};
				parNames = new String[]{"theta"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				parSpace.divideIntoIntervals(50);
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionDoubleNormalModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break;
			}//case 0
			case 1:{
				//LinearDynamicStateSpaceModel
				nGens = 300;
				nPop = 50;
				file  = new File("data"+File.separator+"lineartank.eas");
				DataReader reader = new DataReader(file);
				double[][] data = reader.getData();
				
				initState = new double[] {30};
				times = data[0];
				assimilate = data[1];
				obs = new double[][]{data[3]};
				forcing = data[4];				
				lowerBounds = new double[] {110};
				upperBounds = new double[] {180};
				parNames = new String[] {"resistance"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				parSpace.divideIntoIntervals(100);

				modelFactory = (ModelFactory) new LinearDynamicStateSpaceModelFactory();
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSSRFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, initState, forcing, times, assimilate, obs, modelFactory, likelihoodFunctionFactory);
				break; 
			} // case 1
			case 2:{
				//RastriginModel
				nGens = 300;
				nPop = 50;
				lowerBounds = new double[]{-5.12,-5.12};
				upperBounds = new double[]{5.12,5.12};
				parNames = new String[]{"p1","p2"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				parSpace.divideIntoIntervals(200);
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionRastriginModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break; 
			} //case 2
			case 3:{
				//RosenbrockModel
				nGens = 300;
				nPop = 50;
				lowerBounds = new double[]{-50,-40};
				upperBounds = new double[]{50,80};
				parNames = new String[]{"p1","p2"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				parSpace.divideIntoIntervals(500);
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionRosenbrockModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break; 
			} // case 3
			case 4:{
				//SingleNormalModel
				nGens = 300;
				nPop = 50;
				lowerBounds = new double[]{-50};
				upperBounds = new double[]{40};
				parNames = new String[]{"theta"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				parSpace.divideIntoIntervals(50);
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSingleNormalModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break; 
			} // case 4
			case 5:{
				//CubicModel
				nGens = 300;
				nPop = 50;
				lowerBounds = new double[]{-20,-40,-80,-120};
				upperBounds = new double[]{ 20, 40, 80, 120};
				parNames = new String[]{"a","b","c","d"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				parSpace.divideIntoIntervals(new int[]{50,50,50,50});
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionCubicModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break; 
			} // case 5
			} //switch
			
			diffEvo.start();
			
			diffEvo.printEvalResults();
			
			file = new File("data"+File.separator+"evalresults.json");
			diffEvo.writeEvalResultsToJSON(file);
			
			file = new File("data"+File.separator+"evalresults.txt");
			diffEvo.writeEvalResultsToTextFile(file);

			if (parSpace.getNumberOfPars()>1){
				diffEvo.matrixOfScatterParPar();
				diffEvo.matrixOfHeatmapParPar();
			}
			diffEvo.margHist();
			
		} // int modelSwitch
	} // main
} //Test





