package nl.esciencecenter.diffevo;

import java.util.ArrayList;

import models.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int nGens = 0;
		int nPop = 0;
		ArrayList<Dimension> parSpace = new ArrayList<Dimension>();
		Model model = null;
		int modelSwitch = 6;
		
		switch (modelSwitch){
		case 1:{
			//DoubleNormalModel
			System.out.println("DoubleNormalModel will be optimized");
			nGens = 30;
			nPop = 50;
			Dimension dim1 = new Dimension(-20,18);
			model = new DoubleNormalModel();
			parSpace.add(dim1);
			break;
		}
		case 2:{
			//LinearDynamicModel
			System.out.println("LinearDynamicModel will be optimized");			
			nGens = 300;
			nPop = 50;
			Dimension dim1 = new Dimension(110,180);
			model = new LinearDynamicModel();
			parSpace.add(dim1);
			break;
		}
		case 3:{
			//RastriginModel
			System.out.println("RastriginModel will be optimized");
			nGens = 3000;
			nPop = 50;
			Dimension dim1 = new Dimension(-5.12,5.12);
			Dimension dim2 = new Dimension(-5.12,5.12);			
			model = new RastriginModel();
			parSpace.add(dim1);
			parSpace.add(dim2);			
			break;
		}
		case 4:{
			//RosenbrockModel
			System.out.println("RosenbrockModel will be optimized");
			nGens = 300;
			nPop = 50;
			Dimension dim1 = new Dimension(-100,100);
			Dimension dim2 = new Dimension(-100,100);			
			model = new RosenbrockModel();
			parSpace.add(dim1);
			parSpace.add(dim2);			
			break;
		}
		case 5:{
			//SingleNormalModel
			System.out.println("SingleNormalModel will be optimized");
			nGens = 3;
			nPop = 50;
			Dimension dim1 = new Dimension(-20,18);
			model = new SingleNormalModel();
			parSpace.add(dim1);
			break;
		}
		case 6:{
			//CubicModel
			System.out.println("CubicModel will be optimized");
			nGens = 300;
			nPop = 50;
			Dimension dim1 = new Dimension(-20,20);
			Dimension dim2 = new Dimension(-40,40);
			Dimension dim3 = new Dimension(-80,80);
			Dimension dim4 = new Dimension(-120,120);			
			model = new CubicModel();
			parSpace.add(dim1);
			parSpace.add(dim2);
			parSpace.add(dim3);
			parSpace.add(dim4);
			break;
		}
		}//switch

		DiffEvo diffEvo = new DiffEvo(parSpace, nPop, model);

		System.out.println("Starting Differential Evolution optimization...");		
		diffEvo.initializeParents();
		for (int iGen = 1;iGen<nGens;iGen++){
			diffEvo.proposeOffSpring();
			diffEvo.updateParentsWithProposals();
		}

		diffEvo.calcResponseSurface(10);		
		
		//		diffEvo.printEvalResults();
		diffEvo.writeEvalResultsToJSON();
		diffEvo.writeEvalResultsToTextFile();

		System.out.println("Done.");
//		
//		diffEvo.scatterEvalObj();
//		for (int iPar=0;iPar<parSpace.size();iPar++){
//			diffEvo.scatterEvalPar(iPar);
//		}
		diffEvo.panelTest();
		
	}

}
