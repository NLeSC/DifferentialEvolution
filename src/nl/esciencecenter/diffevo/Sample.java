package nl.esciencecenter.diffevo;

public class Sample {
	private int sampleCounter;
    private double[] parameterVector;
    private double objScore;
    		
	public Sample(int nDims) {
		this.sampleCounter = -1;
		this.parameterVector = new double[nDims];
		for (int iDim=0;iDim<nDims;iDim++){
			this.parameterVector[iDim] = Double.NaN;
		}
		this.objScore = Double.NaN;
	}

	public double getObjScore() {
		return objScore;
	}

	public void setObjScore(double objScore) {
		this.objScore = objScore;
	}

	public double[] getParameterVector() {
		return parameterVector;
	}

	public void setParameterVector(double[] parameterVector) {
		this.parameterVector = parameterVector;
	}

	public int getSampleCounter() {
		return sampleCounter;
	}

	public void setSampleCounter(int sampleCounter) {
		this.sampleCounter = sampleCounter ;
	}
}
