package nl.esciencecenter.diffevo.statespacemodels;

import java.util.Random;


public class LinearDynamicStateSpaceModel implements StateSpaceModel {

//	private static double[] initialState = {30};
//	private static double[] priorTimes = {
//		125.5,126.0,126.5,127.0,127.5,
//		128.0,128.5,129.0,129.5,130.0,
//		130.5,131.0,131.5,132.0,132.5,
//		133.0,133.5,134.0,134.5,135.0,
//		135.5,136.0,136.5,137.0,137.5,
//		138.0,138.5,139.0,139.5,140.0,
//		140.5,141.0,141.5,142.0,142.5,
//		143.0,143.5,144.0,144.5,145.0,
//		145.5,146.0,146.5,147.0,147.5,
//		148.0,148.5,149.0,149.5};
//	private static double[] forcings = {
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,0,0,
//		0,0,0,Double.NaN};
//	private static double[] observedTrue = {
//		30.000000,29.899600,29.799500,29.699800,29.600400,
//		29.501300,29.402600,29.304200,29.206100,29.108400,
//		29.011000,28.913900,28.817100,28.720600,28.624500,
//		28.528700,28.433200,28.338100,28.243200,28.148700,
//		28.054500,27.960600,27.867000,27.773800,27.680800,
//		27.588200,27.495900,27.403800,27.312100,27.220700,
//		27.129600,27.038800,26.948300,26.858100,26.768200,
//		26.678700,26.589400,26.500400,26.411700,26.323300,
//		26.235200,26.147400,26.059900,25.972700,25.885700,
//		25.799100,25.712800,25.626700,25.540900};	// true state values for parameter = 149.39756262040834
//	private static int nObs = observedTrue.length;
//	private static double[] observed = new double[nObs];	
//	private Random generator = new Random();
	
	public LinearDynamicStateSpaceModel(){
//		for (int iObs=0;iObs<nObs;iObs++){
//			observed[iObs] = observedTrue[iObs] + generator.nextDouble()*0.005; 
//		}
	}
	
	private double calcSumOfSquaredResiduals(double[] observed,double[] simulated){
		double sumOfSquaredResiduals = 0.0;
		int nObs = observed.length;
		for (int iObs = 0;iObs<nObs-1;iObs++){
			sumOfSquaredResiduals = sumOfSquaredResiduals + Math.pow(observed[iObs]-simulated[iObs], 2);
		}
		//System.out.printf("%10.4f\n\n", sumOfSquaredResiduals);
		return sumOfSquaredResiduals; 
	}
			
	@Override
	public double calcLogLikelihood(double[] initState, double[] parameterVector, double[][] forcing, double[] priorTimes){
		
		double[] simulated = calcModelPrediction(initState,parameterVector,forcing,priorTimes);
		double sumOfSquaredResiduals = calcSumOfSquaredResiduals(observed,simulated);
		double objScore = -(1.0/2)*nObs*Math.log(sumOfSquaredResiduals);
		return objScore;
	}
	
	
	private double[] calcModelPrediction(double[] initState, double[] parameterVector, double[][] forcing, double[] priorTimes){
		
		int nPrior = priorTimes.length;
		double[] simulated = new double[nPrior];
		double[] state = new double[1];
		int iPrior = 0;
		double resistance = parameterVector[0];
		//double time;
		double timeStep;
		double flow;
		
		
		state[0] = initState[0];
		simulated[iPrior] = initState[0];
		
		//System.out.printf("%10.4f\n", parameterVector[0]);
		
		for (;iPrior<nPrior-1;iPrior++){
			
			//time = priorTimes[iPrior];
			timeStep = priorTimes[iPrior+1]-priorTimes[iPrior];
			flow = -state[0]/resistance;
			//System.out.printf("%10.4f %10.4f %10.4f\n", time,state[0],flow);	
			
			state[0] = state[0] + flow * timeStep;
			simulated[iPrior+1] = state[0];
			
		}

		//time = priorTimes[iPrior];
		flow = -state[0]/resistance;
		//System.out.printf("%10.4f %10.4f %10.4f\n", time,state[0],Double.NaN);			
		
		return simulated; 
	}
	
	public String getName(){
		return LinearDynamicStateSpaceModel.class.getSimpleName();
	}

	
	
}
