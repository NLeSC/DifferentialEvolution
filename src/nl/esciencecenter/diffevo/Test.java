package nl.esciencecenter.diffevo;


import models.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int nGens = 0;
		int nPop = 0;
		Model model = null;
		int modelSwitch = 6;
		ParSpace parSpace = null;
		
		switch (modelSwitch){
		case 1:{
			//DoubleNormalModel
			System.out.println("DoubleNormalModel will be optimized");
			nGens = 300;
			nPop = 50;
			model = new DoubleNormalModel();
			double[] lowerBounds = {-20};
			double[] upperBounds = {18};
			String[] parNames = {"theta"};
			parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
			parSpace.divideIntoIntervals(50);
			break;
		}//case 1
		case 2:{
			//LinearDynamicModel
			System.out.println("LinearDynamicModel will be optimized");			
			nGens = 300;
			nPop = 50;
			model = new LinearDynamicModel();
			double[] lowerBounds = {110};
			double[] upperBounds = {180};
			String[] parNames = {"theta"};
			parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
			break; 
		} // case 2
		case 3:{
			//RastriginModel
			System.out.println("RastriginModel will be optimized");
			nGens = 3000;
			nPop = 50;
			model = new RastriginModel();
			double[] lowerBounds = {-5.12,-5.12};
			double[] upperBounds = {5.12,5.12};
			String[] parNames = {"p1","p2"};
			parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
			parSpace.divideIntoIntervals(200);
			break; 
		} //case 3
		case 4:{
			//RosenbrockModel
			System.out.println("RosenbrockModel will be optimized");
			nGens = 3000;
			nPop = 50;
			model = new RosenbrockModel();
			double[] lowerBounds = {-50,-40};
			double[] upperBounds = {50,80};
			String[] parNames = {"p1","p2"};
			parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
			parSpace.divideIntoIntervals(500);
			break; 
		} // case 4
		case 5:{
			//SingleNormalModel
			System.out.println("SingleNormalModel will be optimized");
			nGens = 300;
			nPop = 50;
			model = new SingleNormalModel();
			double[] lowerBounds = {-20};
			double[] upperBounds = {18};
			String[] parNames = {"theta"};
			parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
			parSpace.divideIntoIntervals(50);
			break; 
		} // case 5
		case 6:{
			//CubicModel
			System.out.println("CubicModel will be optimized");
			nGens = 3000;
			nPop = 50;
			model = new CubicModel();
			double[] lowerBounds = {-20,-40,-80,-120};
			double[] upperBounds = { 20, 40, 80, 120};
			String[] parNames = {"a","b","c","d"};
			parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
			parSpace.divideIntoIntervals(new int[]{50,50,50,50});
			break; 
		} // case 6
		}//switch

		DiffEvo diffEvo = new DiffEvo(parSpace, nPop, model);

		System.out.println("Starting Differential Evolution optimization...");		
		diffEvo.initializeParents();
		for (int iGen = 1;iGen<nGens;iGen++){
			diffEvo.proposeOffSpring();
			diffEvo.updateParentsWithProposals();
		}

//		diffEvo.printEvalResults();
//		diffEvo.writeEvalResultsToJSON();
//		diffEvo.writeEvalResultsToTextFile();
	
//		if (parSpace.getNumberOfPars()>1){
//			diffEvo.matrixOfScatterParPar();
//			diffEvo.matrixOfHeatmapParPar();
//		}
		
		diffEvo.margHist();
		
		
		System.out.println("Done.");

	}

}





