package models;

import nl.esciencecenter.diffevo.Model;

public class RastriginModel implements Model {
	private static final double A = 10;
	private double probabilityDensity;
	
	public RastriginModel(){
		//
	}
	
	private double calcProbabilityDensity(double[] x){
		
		// http://en.wikipedia.org/wiki/Rastrigin_function
		double sum = 0.0;
		int nDims = x.length;

        for (int iDim = 0;iDim<nDims;iDim++){
        	sum = sum + (Math.pow(x[iDim],2) - A*Math.cos(2*Math.PI*x[iDim]));
        }
		
		probabilityDensity = A*nDims + sum; // not a real probability density, just a benchmark function value
		return probabilityDensity; 
	}
			
	@Override
	public double calcLogLikelihood(double[] x){
		double objScore;
		objScore = -calcProbabilityDensity(x);
		return objScore;
	}

}
