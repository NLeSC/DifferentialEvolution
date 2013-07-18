package nl.esciencecenter.diffevo;

import java.util.ArrayList;

public class ParameterVector {

	private double[] values;
	
	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}
	
	public ParameterVector(ArrayList<Dimension> parSpace){		
		int nDims = parSpace.size();
		values = new double[nDims];
	}		

}