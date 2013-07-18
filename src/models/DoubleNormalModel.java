package models;

import nl.esciencecenter.diffevo.Model;

public class DoubleNormalModel implements Model {
	private double parMu1;
	private double parMu2;
	private double parSigma1;
	private double parSigma2;
	private double probabilityDensity;
	
	public DoubleNormalModel(){
		parMu1 = -10;
		parSigma1 = 3;
		parMu2 = 5;
		parSigma2 = 1;
	}
	
	private double calcProbabilityDensity(double[] x){
		double dens1;
		double dens2;

//        System.out.println("x[0] is " + x[0]);
//        System.out.println("parMu1 is " + parMu1);
//        System.out.println("parSigma1 is " + parSigma1);
//        System.out.println("(x[0]-parMu1)/parSigma1 is " + (x[0]-parMu1)/parSigma1);
//        System.out.println("Math.pow((x[0]-parMu1)/parSigma1,2) is " + Math.pow((x[0]-parMu1)/parSigma1,2));
//        System.out.println("-(1.0/2.0)*Math.pow((x[0]-parMu1)/parSigma1,2) is " + -(1.0/2.0)*Math.pow((x[0]-parMu1)/parSigma1,2));
//		  bloody integer division
		
		dens1 = (1.0/(Math.sqrt(2.0 * Math.PI * Math.pow(parSigma1,2)))) * Math.exp(-(1.0/2.0) * Math.pow((x[0]-parMu1)/parSigma1,2));
		dens2 = (1.0/(Math.sqrt(2.0 * Math.PI * Math.pow(parSigma2,2)))) * Math.exp(-(1.0/2.0) * Math.pow((x[0]-parMu2)/parSigma2,2));
		
		probabilityDensity = (dens1 + dens2)/2; 
		return probabilityDensity; 
	}
			

	public double calcLogLikelihood(double[] x){
		double objScore;
		probabilityDensity = calcProbabilityDensity(x);
		objScore = Math.log(probabilityDensity);
		return objScore;
	}

}
