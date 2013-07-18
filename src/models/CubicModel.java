package models;

import java.util.Random;

import nl.esciencecenter.diffevo.Model;

public class CubicModel implements Model {

	private double xObsStart;
	private double xObsEnd;
	private int nObs;	
	private double xObsStep;
	private double[] xObs;	
	private double randnFactor;	
	private double[] randnTerm;
	private Random generator = new Random();
	private double[] yObsTrue;
	private double[] yObs;
	
	
	public CubicModel(){

		
		double a = -1.0;
		double b = 7.0;
		double c = -5.0;
		double d = 9.0;
		
		xObsStart = -1.0;
		xObsEnd = 6.8;
		nObs = 14;
		xObsStep = (xObsEnd-xObsStart)/(nObs-1);
		randnFactor = 5.0;
		xObs = new double[nObs];
		yObsTrue = new double[nObs];
		randnTerm = new double[nObs];
		yObs = new double[nObs];
		
		for (int iObs = 0; iObs<nObs;iObs++){
			xObs[iObs] = xObsStart + iObs*xObsStep;
			yObsTrue[iObs] = a * Math.pow(xObs[iObs],3) +
					         b * Math.pow(xObs[iObs],2) +
					         c * xObs[iObs] +
					         d;
			
			randnTerm[iObs] = generator.nextGaussian() * randnFactor;
			
			yObs[iObs] = yObsTrue[iObs] + randnTerm[iObs];
		}
	}
	
	
			
	private double calcSumOfSquaredResiduals(double[] yObs,double[] ySim){

		double sum = 0;
		
		for (int iObs = 0; iObs<nObs;iObs++){
			sum = sum + Math.pow(yObs[iObs]-ySim[iObs],2);
		}
		return sum;
		
	}
	
	
	public double calcLogLikelihood(double[] x){
		double a = x[0];
		double b = x[1];
		double c = x[2];
		double d = x[3];
		double[] ySim;

		ySim = new double[nObs];
		
		for (int iObs = 0; iObs<nObs;iObs++){
			ySim[iObs] = a * Math.pow(xObs[iObs],3) +
					     b * Math.pow(xObs[iObs],2) +
					     c * xObs[iObs] +
					     d;
		}
		
 		double ssr = calcSumOfSquaredResiduals(yObs,ySim);
 		
		double objScore = -(1.0/2)*nObs*Math.log(ssr);
		
		return objScore;
	}

}
