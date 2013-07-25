package nl.esciencecenter.diffevo;

import java.util.ArrayList;
import java.util.Random;

import nl.esciencecenter.diffevo.statespacemodels.Model;

public class Parents {

	private ArrayList<Sample> sampleList; 
	private int nPop;
	private ParSpace parSpace;
	private int nDims;
	
	// constructor
	public Parents(int nPop, ParSpace parSpace){
		
		this.nDims = parSpace.getNumberOfPars();
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
				values[iDim-1] = parSpace.getLowerBound(iDim-1) + 
						g * parSpace.getRange(iDim-1);
			}
			this.setParameterVector(iPop-1, values);
		}
	}

	public void evaluateModel(Model model, double[] initState, double[][] forcing, double[] times){

		double state[];
		int nStates = initState.length;
		int nTimes = times.length;
		
		for (int iState=0;iState<nStates;iState++){
			state[iState] = initState[iState];
		}

		
		
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


