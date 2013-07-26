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

public class MainProgram {


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int nGens = 0;
		int nPop = 0;
		double[] initState = null;
		boolean[] assimilate = null;
		double[] times = null;
		double[] forcing = null;
		double[] lowerBounds;
		double[] upperBounds;
		String[] parNames;
		ParSpace parSpace = null;
		double[][] obs = null;
		ModelFactory modelFactory = null;
		
		for (int modelSwitch = 7 ;modelSwitch<8;modelSwitch++){

			switch (modelSwitch){
//			case 1:{
//				//DoubleNormalModel
//				System.out.println("DoubleNormalModel will be optimized");
//				nGens = 300;
//				nPop = 50;
//				model = new DoubleNormalModel();
//				double[] lowerBounds = {-20};
//				double[] upperBounds = {18};
//				String[] parNames = {"theta"};
//				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
//				parSpace.divideIntoIntervals(50);
//				break;
//			}//case 1
//			case 2:{
//				//LinearDynamicModel
//				System.out.println("LinearDynamicModel will be optimized");			
//				nGens = 300;
//				nPop = 50;
//				model = new LinearDynamicModel();
//				double[] lowerBounds = {110};
//				double[] upperBounds = {180};
//				String[] parNames = {"resistance"};
//				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
//				break; 
//			} // case 2
//			case 3:{
//				//RastriginModel
//				System.out.println("RastriginModel will be optimized");
//				nGens = 3000;
//				nPop = 50;
//				model = new RastriginModel();
//				double[] lowerBounds = {-5.12,-5.12};
//				double[] upperBounds = {5.12,5.12};
//				String[] parNames = {"p1","p2"};
//				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
//				parSpace.divideIntoIntervals(200);
//				break; 
//			} //case 3
//			case 4:{
//				//RosenbrockModel
//				System.out.println("RosenbrockModel will be optimized");
//				nGens = 3000;
//				nPop = 50;
//				model = new RosenbrockModel();
//				double[] lowerBounds = {-50,-40};
//				double[] upperBounds = {50,80};
//				String[] parNames = {"p1","p2"};
//				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
//				parSpace.divideIntoIntervals(500);
//				break; 
//			} // case 4
//			case 5:{
//				//SingleNormalModel
//				System.out.println("SingleNormalModel will be optimized");
//				nGens = 300;
//				nPop = 50;
//				model = new SingleNormalModel();
//				double[] lowerBounds = {-50};
//				double[] upperBounds = {40};
//				String[] parNames = {"theta"};
//				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
//				parSpace.divideIntoIntervals(50);
//				break; 
//			} // case 5
//			case 6:{
//				//CubicModel
//				System.out.println("CubicModel will be optimized");
//				nGens = 3000;
//				nPop = 50;
//				model = new CubicModel();
//				double[] lowerBounds = {-20,-40,-80,-120};
//				double[] upperBounds = { 20, 40, 80, 120};
//				String[] parNames = {"a","b","c","d"};
//				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
//				parSpace.divideIntoIntervals(new int[]{50,50,50,50});
//				break; 
//			} // case 6
			case 7:{
				//LinearDynamicStateSpaceModel
				System.out.println("LinearDynamicStateSpaceModel will be optimized");			
				nGens = 300;
				nPop = 50;
				initState = new double[] {30};
				assimilate = new boolean[]{
						false,false,false,true,true,
						true,true,true,true,true,
						true,true,true,true,true,
						true,true,true,true,true,
						false,false,false,false,false,
						true,true,true,true,true,
						true,true,true,true,true,
						true,true,true,true,true,
						true,true,true,true,true,
						true,true,true,true};
				
				times = new double[]{
						125.5,126.0,126.5,127.0,127.5,
						128.0,128.5,129.0,129.5,130.0,
						130.5,131.0,131.5,132.0,132.5,
						133.0,133.5,134.0,134.5,135.0,
						135.5,136.0,136.5,137.0,137.5,
						138.0,138.5,139.0,139.5,140.0,
						140.5,141.0,141.5,142.0,142.5,
						143.0,143.5,144.0,144.5,145.0,
						145.5,146.0,146.5,147.0,147.5,
						148.0,148.5,149.0,149.5};
				
				forcing = new double[]
				   {0.1,0.2,0.5,0.6,0.3,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,Double.NaN};

				lowerBounds = new double[] {110};
				upperBounds = new double[] {180};
				parNames = new String[] {"resistance"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				parSpace.divideIntoIntervals(100);
				double[][] observedTrue = {{
					30.000000,29.899600,29.799500,29.699800,29.600400,
					29.501300,29.402600,29.304200,29.206100,29.108400,
					29.011000,28.913900,28.817100,28.720600,28.624500,
					28.528700,28.433200,28.338100,28.243200,28.148700,
					28.054500,27.960600,27.867000,27.773800,27.680800,
					27.588200,27.495900,27.403800,27.312100,27.220700,
					27.129600,27.038800,26.948300,26.858100,26.768200,
					26.678700,26.589400,26.500400,26.411700,26.323300,
					26.235200,26.147400,26.059900,25.972700,25.885700,
					25.799100,25.712800,25.626700,25.540900}};	// true state values for parameter = 149.39756262040834
				int nObs = observedTrue[0].length;
				int nStates = 1;
				obs = new double[nStates][nObs];	
				Random generator = new Random();
				for (int iObs=0;iObs<nObs;iObs++){
					obs[0][iObs] = observedTrue[0][iObs] + generator.nextDouble()*0.005; 
				}
				modelFactory = (ModelFactory) new LinearDynamicStateSpaceModelFactory();
				break; 
 			} // case 7
			} //switch
			

			DiffEvo diffEvo = new DiffEvo(nGens, nPop, parSpace, initState, forcing, times, assimilate, obs, modelFactory);
			diffEvo.start();
			
			
			diffEvo.printEvalResults();
			diffEvo.writeEvalResultsToJSON();
			diffEvo.writeEvalResultsToTextFile();

			if (parSpace.getNumberOfPars()>1){
				diffEvo.matrixOfScatterParPar();
				diffEvo.matrixOfHeatmapParPar();
			}
			diffEvo.margHist();

			
		} // int modelSwitch
	} // main
} //Test





