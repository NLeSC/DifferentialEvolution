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


import nl.esciencecenter.diffevo.statespacemodels.*;

public class MainProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int nGens = 0;
		int nPop = 0;
		Model model = null;
		double[] initState = null;
		double[] times = null;
		double[][] forcing = null;
		double[] lowerBounds;
		double[] upperBounds;
		String[] parNames;
		ParSpace parSpace = null;
		
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
				model = new LinearDynamicStateSpaceModel();
				initState = new double[] {30};
				times = new double[] {
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
				forcing = new double[][] {
				   {0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,0,0,
					0,0,0,Double.NaN}
				   };
				lowerBounds = new double[] {110};
				upperBounds = new double[] {180};
				parNames = new String[] {"resistance"};
				parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
				break; 
 			} // case 7
			}//switch

			DiffEvo diffEvo = new DiffEvo(nGens, nPop, parSpace, model, initState, forcing, times);
			diffEvo.start();

			diffEvo.printEvalResults();
			diffEvo.writeEvalResultsToJSON();
			diffEvo.writeEvalResultsToTextFile();

			if (parSpace.getNumberOfPars()>1){
				diffEvo.matrixOfScatterParPar();
				diffEvo.matrixOfHeatmapParPar();
			}

			diffEvo.margHist();


			System.out.println("Done.");

		} // int modelSwitch
	} // main
} //Test





