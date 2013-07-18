package models;

import nl.esciencecenter.diffevo.Model;

public class SingleNormalModel implements Model {
	private double parMu1;
	private double parSigma1;
	private double probabilityDensity;
	
	public SingleNormalModel(){
		parMu1 = -10;
		parSigma1 = 3;
	}
	
	private double calcProbabilityDensity(double[] x){
		double dens1;

		dens1 = (1.0/(Math.sqrt(2.0 * Math.PI * Math.pow(parSigma1,2)))) * Math.exp(-(1.0/2.0) * Math.pow((x[0]-parMu1)/parSigma1,2));
		
		probabilityDensity = dens1; 
		return probabilityDensity; 
	}
			
	@Override
	public double calcLogLikelihood(double[] x){
		double objScore;
		probabilityDensity = calcProbabilityDensity(x);
		objScore = Math.log(probabilityDensity);
		return objScore;
	}

}
