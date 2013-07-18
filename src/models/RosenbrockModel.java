package models;

import nl.esciencecenter.diffevo.Model;

public class RosenbrockModel implements Model {
	private double probabilityDensity;
	
	public RosenbrockModel(){
		this.probabilityDensity = Double.NaN;
		
	}
	
	private double calcProbabilityDensity(double[] x){
		double sum = 0.0;
		int nPars = x.length;
		for (int iPar = 0;iPar<nPars-1;iPar++){
			sum = sum + (1-Math.pow(x[iPar],2) + 100*Math.pow(x[iPar+1]-Math.pow(x[iPar],2), 2));
		}
		
		probabilityDensity = sum; // it's not really a probability density since this is just a benchmark function
		return probabilityDensity; 
	}
			
	@Override
	public double calcLogLikelihood(double[] x){
		double objScore;
		probabilityDensity = calcProbabilityDensity(x);
		objScore = -Math.log(probabilityDensity);
		return objScore;
	}

}
