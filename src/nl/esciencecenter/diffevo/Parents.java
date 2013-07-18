package nl.esciencecenter.diffevo;

import java.util.ArrayList;
//import java.util.Random;
import java.util.Random;

public class Parents {

	private ArrayList<Sample> sampleList; 
	private int nPop;
	private ArrayList<Dimension> parSpace;
	private int nDims;
	
	// constructor
	public Parents(int nPop, ArrayList<Dimension> parSpace){
		
		this.nDims = parSpace.size();
		this.sampleList = new ArrayList<Sample>();
		this.nPop = nPop;
		this.parSpace = parSpace;
		for (int iPop = 1; iPop <= nPop; iPop++) {
			Sample sample = new Sample(nDims);
			sampleList.add(sample);
		}
	}
	
	public void add(Sample sample){
		sampleList.add(sample);
	}
	
	public void takeUniformRandomSamples(Random generator){

		for (int iPop=1;iPop<=nPop;iPop++){
			double[] values = new double[nDims];			
			for (int iDim=1;iDim<=nDims;iDim++){
				double g = generator.nextDouble();
				values[iDim-1] = parSpace.get(iDim-1).getLowerBound() + 
						g * parSpace.get(iDim-1).getRange();
			}
			this.setParameterVector(iPop-1, values);
		}
	}

	public void evaluateModel(Model model){
//		System.out.println("evaluate model");
		for (int iPop=0;iPop<nPop;iPop++){
			double[] parameterVector = new double[nDims];
			double objScore;
						
			parameterVector = getParameterVector(iPop);
			objScore = model.calcLogLikelihood(parameterVector);
			this.setObjScore(iPop, objScore);
		}
	}
	
	public ArrayList<Sample> getParents() {
		return this.sampleList;
	}
	
	public Sample getParent(int index) {
		return this.sampleList.get(index);
	}

	public void setParent(int index, Sample sample) {
		int sampleIdentifier;
		double[] parameterVector;
		double objScore;
		
		sampleIdentifier = sample.getSampleCounter();
		parameterVector = sample.getParameterVector();
		objScore = sample.getObjScore();
		
		this.sampleList.get(index).setSampleCounter(sampleIdentifier);
		this.sampleList.get(index).setParameterVector(parameterVector);
		this.sampleList.get(index).setObjScore(objScore);
		
	}

	public void setParameterVector(int index, double[] parameterVector) {
		this.sampleList.get(index).setParameterVector(parameterVector);
	}

	public double[] getParameterVector(int index) {
		return this.sampleList.get(index).getParameterVector();
	}

	public void setObjScore(int index, double objScore) {
		this.sampleList.get(index).setObjScore(objScore);
	}

	public double getObjScore(int index) {
		return this.sampleList.get(index).getObjScore();
	}
	
}


