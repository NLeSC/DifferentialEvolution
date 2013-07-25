package nl.esciencecenter.diffevo;

import java.util.ArrayList;

import nl.esciencecenter.diffevo.models.Model;

public class Proposals {

	private ArrayList<Sample> sampleList; 
	private int nDims;
	private int nPop;
	private ParSpace parSpace;
	
	// constructor
	public Proposals(int nPop, ParSpace parSpace){
		
		this.nDims = parSpace.getNumberOfPars();
		this.parSpace = parSpace;
		this.nPop = nPop;
		this.sampleList = new ArrayList<Sample>();
		for (int iPop = 1; iPop <= nPop; iPop++) {
			Sample sample = new Sample(nDims);
			sampleList.add(sample);
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
	
	public void reflectIfOutOfBounds(){
		for (int iPop=0;iPop<nPop;iPop++){
			double[] parameterVector = new double[nDims];
			for (int iDim = 0;iDim<nDims;iDim++){
				parameterVector = getParameterVector(iPop);
				double lb = parSpace.getLowerBound(iDim);
				double ub = parSpace.getUpperBound(iDim);
				double s = parameterVector[iDim];
				//System.out.printf("sample in dimension %d is %6g\n",iDim,s);
				//System.out.printf("lower boundary in dimension %d is %6g\n",iDim,lb);
				//System.out.printf("upper boundary in dimension %d is %6g\n",iDim,ub);
				if (s<lb){
					parameterVector[iDim] = lb+(lb-s);
				}					
				if (s>ub){
					parameterVector[iDim] = ub+(ub-s);
				}
				setParameterVector(iPop, parameterVector);
			}
		}
	}
	
	
	public void add(Sample sample){
		sampleList.add(sample);
	}
	
	public ArrayList<Sample> getProposals() {
		return this.sampleList;
	}
	
	public Sample getProposal(int index) {
		return this.sampleList.get(index);
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


