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

package nl.esciencecenter.diffevo.statespacemodels;

public class LinearDynamicStateSpaceModel implements Model {

	private double[] initState;
	private double[] parameterVector;
	//private double[] forcing;
	private double[] times;
	
	public LinearDynamicStateSpaceModel(double[] initState, double[] parameterVector, double[] forcing, double[] times){
		this.initState = initState.clone();
		this.parameterVector = parameterVector.clone();
		//this.forcing = forcing;
		this.times = times.clone();
	}
	
	public String getName(){
		return LinearDynamicStateSpaceModel.class.getSimpleName();
	}

	@Override
	public double[][] evaluate() {
		
		int nTimes = times.length;
		int nStates = initState.length;
		
		double[][] simulated = new double[nStates][nTimes];
		double[] state = new double[nStates];

		double resistance = parameterVector[0];

		double timeStep;
		double flow;
		
		state[0] = initState[0];
		simulated[0][0] = initState[0];
		
		//System.out.printf("%10.4f\n", parameterVector[0]);
		
		for (int iTime=0;iTime<nTimes-1;iTime++){
			
			timeStep = times[iTime+1]-times[iTime];
			flow = -state[0]/resistance;
			
			state[0] = state[0] + flow * timeStep;
			simulated[0][iTime+1] = state[0];
			
		}
		return simulated; 
	}

	
	
}
