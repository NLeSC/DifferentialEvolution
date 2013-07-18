package nl.esciencecenter.diffevo;

import java.util.ArrayList;

public class EvalResults {

	private ArrayList<Sample> sampleList; 
	
	// constructor
	public EvalResults(){
		
		this.sampleList = new ArrayList<Sample>();
	}
	
	public void add(Sample sample){
		sampleList.add(sample);
	}
	
	public ArrayList<Sample> getEvalResults() {
		return this.sampleList;
	}
	
	public Sample EvalResult(int index) {
		return this.sampleList.get(index);
	}

	
	public double[] getParameterVector(int index) {
		return this.sampleList.get(index).getParameterVector();
	}

	public double getObjScore(int index) {
		return this.sampleList.get(index).getObjScore();
	}

	public int size() {
		return this.sampleList.size();
	}
	
	public int getSampleCounter(int index) {
		return this.sampleList.get(index).getSampleCounter();
	}
	
	
	
}


