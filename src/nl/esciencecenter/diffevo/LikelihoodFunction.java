package nl.esciencecenter.diffevo;

public class LikelihoodFunction {
	
	public LikelihoodFunction(){
		
	}
	
	
	public double evaluate(double[][] obs, double[][] sim){

		int nTimes = obs[0].length;
		int nStates = obs.length;
		int nObs = nTimes;
		double ssr = 0;
		double deviation;
		for (int iState=0;iState<nStates;iState++){
			for (int iTime=1;iTime<nTimes;iTime++){
				deviation = obs[iState][iTime]-sim[iState][iTime];
				ssr = ssr + Math.pow(deviation,2); 
			}
		}
		double objScore = -(1.0/2)*nObs * Math.log(ssr); 
		return objScore;
	}

}
