package nl.esciencecenter.diffevo;

public class ParSpace {
	
	private double[] lowerBounds;
	private double[] upperBounds;
	private double[] range;	
	private String[] parNames;
	private int nPars;


	public ParSpace(double[] lowerBounds, double[] upperBounds, String[] parNames){
		
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
		this.parNames = parNames;
		this.setNumberOfPars(lowerBounds.length);
		
		double[] range = new double[getNumberOfPars()]; 
	
		for (int iPar=0;iPar<getNumberOfPars();iPar++){
			range[iPar] = upperBounds[iPar] - lowerBounds[iPar];
		}

		this.range = range;
	}


	public int getNumberOfPars() {
		return nPars;
	}

	private void setNumberOfPars(int nPars) {
		this.nPars = nPars;
	}

	
	public double getLowerBound(int index) {
		return lowerBounds[index];
	}

	public double getUpperBound(int index) {
		return upperBounds[index];
	}

	public double getRange(int index) {
		return range[index];
	}


	public String getParName(int index) {
		return parNames[index];
	}



	
	
}
