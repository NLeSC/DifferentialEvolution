/*
 * Copyrighted 2012-2013 Netherlands eScience Center.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").  
 * You may not use this file except in compliance with the License. 
 * For details, see the LICENCE.txt file location in the root directory of this 
 * distribution or obtain the Apache License at the following location: 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * For the full license, see: LICENCE.txt (located in the root folder of this distribution). 
 * ---
 */

package nl.esciencecenter.diffevo.likelihoodfunctions;

import java.util.Random;


public class LikelihoodFunctionLinearDynamicModel implements LikelihoodFunction {

	private static double[] initialState = {30};
	private static double[] priorTimes = {
		125.5,126.0,126.5,127.0,127.5,
		128.0,128.5,129.0,129.5,130.0,
		130.5,131.0,131.5,132.0,132.5,
		133.0,133.5,134.0,134.5,135.0,
		135.5,136.0,136.5,137.0,137.5,
		138.0,138.5,139.0,139.5,140.0,
		140.5,141.0,141.5,142.0,142.5,
		143.0,143.5,144.0,144.5,145.0,
		145.5,146.0,146.5,147.0,147.5,
		148.0,148.5,149.0,149.5};
	private static double[] forcings = {
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,0,0,
		0,0,0,Double.NaN};
	private static double[] observedTrue = {
		30.000000,29.899600,29.799500,29.699800,29.600400,
		29.501300,29.402600,29.304200,29.206100,29.108400,
		29.011000,28.913900,28.817100,28.720600,28.624500,
		28.528700,28.433200,28.338100,28.243200,28.148700,
		28.054500,27.960600,27.867000,27.773800,27.680800,
		27.588200,27.495900,27.403800,27.312100,27.220700,
		27.129600,27.038800,26.948300,26.858100,26.768200,
		26.678700,26.589400,26.500400,26.411700,26.323300,
		26.235200,26.147400,26.059900,25.972700,25.885700,
		25.799100,25.712800,25.626700,25.540900};	// true state values for parameter = 149.39756262040834
	private static int nObs = observedTrue.length;
	private static double[] observed = new double[nObs];	
	private Random generator = new Random();
	
	public LikelihoodFunctionLinearDynamicModel(){
		
		for (int iObs=0;iObs<nObs;iObs++){
			observed[iObs] = observedTrue[iObs] + generator.nextDouble()*0.005; 
		}
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
			
	private double[] calcModelPrediction(double[] initialState, double[] parameterVector, double[] forcings, double[] priorTimes){
		double[] simulated = new double[nObs];
		double[] state = new double[1];
		int nPriors = priorTimes.length;
		int iPrior = 0;
		double resistance = parameterVector[0];
		//double time;
		double timeStep;
		double flow;
		
		
		state[0] = initialState[0];
		simulated[iPrior] = initialState[0];
		
		//System.out.printf("%10.4f\n", parameterVector[0]);
		
		for (;iPrior<nPriors-1;iPrior++){
			
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
		return LikelihoodFunctionLinearDynamicModel.class.getSimpleName();
	}

	@Override
	public double evaluate(double[][] obs, double[][] sim) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double evaluate(double[] parameterVector) {
		// TODO Auto-generated method stub
		double[] simulated = calcModelPrediction(initialState,parameterVector,forcings,priorTimes);
		double sumOfSquaredResiduals = calcSumOfSquaredResiduals(observed,simulated);
		double objScore = -(1.0/2)*nObs*Math.log(sumOfSquaredResiduals);
		return objScore;
	}

	
	
}
