package nl.esciencecenter.diffevo;

public class ParameterVector {

	private double[] values;
	
	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}
	
	public ParameterVector(ParSpace parSpace){		
		int nDims = parSpace.getNumberOfPars();
		values = new double[nDims];
	}		

}