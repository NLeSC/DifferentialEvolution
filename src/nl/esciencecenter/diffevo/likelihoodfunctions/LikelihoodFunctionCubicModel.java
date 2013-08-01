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

public class LikelihoodFunctionCubicModel implements LikelihoodFunction {

	private int nObs;	
	private double[] xObs;	
	private Random generator = new Random();
	private double[] yObs;
	
	
	public LikelihoodFunctionCubicModel(){
		
		double a = -1.0;
		double b = 7.0;
		double c = -5.0;
		double d = 9.0;
		
		double xObsStart = -1.0;
		double xObsEnd = 6.8;
		nObs = 14;
		double xObsStep = (xObsEnd-xObsStart)/(nObs-1);
		double randnFactor = 5.0;
		xObs = new double[nObs];
		double[] yObsTrue = new double[nObs];
		double[] randnTerm = new double[nObs];
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
	

	public String getName(){
		return LikelihoodFunctionCubicModel.class.getSimpleName();
	}

	@Override
	public double evaluate(double[][] obs, double[][] sim) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double evaluate(double[] parameterVector) {
		double a = parameterVector[0];
		double b = parameterVector[1];
		double c = parameterVector[2];
		double d = parameterVector[3];
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
