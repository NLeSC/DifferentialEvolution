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


import java.awt.Color;
import java.io.File;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.*;
import nl.esciencecenter.diffevo.statespacemodelfactories.*;

public class MainProgram {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (int modelSwitch = 3;modelSwitch<4;modelSwitch++){

			int nGens = 0;
			int nPop = 0;
			double[] initState = null;
			double[] assimilate = null;
			double[] times = null;
			double[] forcing = null;
			ParSpace parSpace = null;
			StateSpace stateSpace = null;		
			double[][] obs = null;
			ModelFactory modelFactory = null;
			LikelihoodFunctionFactory likelihoodFunctionFactory = null;
			DiffEvo diffEvo = null;
			File file = null;
			
			switch (modelSwitch){
			case 0:{
				//DoubleNormalModel
				nGens = 300;
				nPop = 50;
				{
					double[] lowerBoundsParSpace = new double[]{-20};
					double[] upperBoundsParSpace = new double[]{18};
					String[] parNames = new String[]{"theta"};
					parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
					parSpace.divideIntoIntervals(50);
				}
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionDoubleNormalModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break;
			}//case 0
			case 1:{
				//LinearDynamicStateSpaceModel
				nGens = 500;
				nPop = 50;
				file  = new File("data"+File.separator+"lineartank.eas");
				DataReader reader = new DataReader(file);
				double[][] data = reader.getData();

				initState = new double[] {30};
				times = data[0];
				assimilate = data[1];
				obs = new double[][]{data[3]};
				forcing = data[4];
				{
					double[] lowerBoundsParSpace = new double[] {10};
					double[] upperBoundsParSpace = new double[] {500};
					String[] parNames = new String[] {"resistance"};
					parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
					parSpace.divideIntoIntervals(100);
				}
				{
					double[] lowerBoundsStateSpace = new double[] {0};
					double[] upperBoundsStateSpace = new double[] {100};
					String[] stateNames = new String[] {"waterlevel"};
					stateSpace = new StateSpace(lowerBoundsStateSpace,upperBoundsStateSpace,stateNames);
				}
				modelFactory = (ModelFactory) new LinearDynamicStateSpaceModelFactory();
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSSRFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, stateSpace, initState, forcing, times, assimilate, obs, modelFactory, likelihoodFunctionFactory);
				break; 
			} // case 1
			case 4:{
				//SingleNormalModel
				nGens = 3000;
				nPop = 50;
				{
					double[] lowerBoundsParSpace = new double[]{-50};
					double[] upperBoundsParSpace = new double[]{40};
					String[] parNames = new String[]{"theta"};
					parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
					parSpace.divideIntoIntervals(50);
				}
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionSingleNormalModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break; 
			} // case 4
			case 5:{
				//CubicModel
				nGens = 300;
				nPop = 50;
				{
					double[] lowerBoundsParSpace = new double[]{-20,-40,-80,-120};
					double[] upperBoundsParSpace = new double[]{ 20, 40, 80, 120};
					String[] parNames = new String[]{"a","b","c","d"};
					parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
					parSpace.divideIntoIntervals(new int[]{50,50,50,50});
				}
				likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionCubicModelFactory();
				diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory);
				break; 
			} // case 5
			default: {
				System.out.println("You asked for a case that isn\'t there.");
			}
			} //switch

			// run the optimization:
			EvalResults evalResults = diffEvo.runOptimization();
			
			// do some visualization of the results:
			DiffEvoVisualization vis = new DiffEvoVisualization(evalResults);
			vis.matrixOfScatterParPar();
			vis.matrixOfHeatmapParPar();
			vis.margHist();
			vis.scatterEvalObj();
			for (int iPar=0;iPar<parSpace.getNumberOfPars();iPar++){
				vis.scatterEvalPar(iPar); 
			}
			
			boolean modelIsDynamic = modelFactory!=null;
			
			if (modelIsDynamic){

				XYSeriesCollection data = new XYSeriesCollection();
				XYSeries series;
				{
					series = vis.createSeries("time v. obs",times, obs[0]);
					data.addSeries(series);
				}
				{
					int iResult[] = evalResults.sampleIdentifiersOfBest();
					double[][] modelResult = evalResults.getEvalResult(iResult[0]).getModelResult();
					series = vis.createSeries("time v. prior["+iResult[0]+"]",times, modelResult[0]);
					data.addSeries(series);
				}
				{
					Random generator = new Random();
					int iResult = generator.nextInt(evalResults.size());
					double[][] modelResult = evalResults.getEvalResult(iResult).getModelResult();
					series = vis.createSeries("time v. randomly selected prior["+iResult+"]", times, modelResult[0]);
					data.addSeries(series);
				}
				
				Color[] colors = new Color[data.getSeriesCount()];
				colors[0] = new Color(0,0,255);
				colors[1] = new Color(0,128,0);
				colors[2] = new Color(255,128,0);				
				
				vis.scatter(data, "states", colors, "time", "state", true, true);
				
			}
			
		
			// do some printing to file and standard out:
			DiffEvoOutputWriters writers = new DiffEvoOutputWriters(evalResults);
			writers.printEvalResults();
			writers.printEvalResults();
			file = new File("out"+File.separator+"evalresults.json");
			writers.writeEvalResultsToJSON(file);
			file = new File("out"+File.separator+"evalresults.txt");
			writers.writeEvalResultsToTextFile(file);


		} // int modelSwitch
	} // main
} //Test





