package nl.esciencecenter.diffevo;

import java.util.ArrayList;

public class ParSpace {
	
	private double[] lowerBounds;
	private double[] upperBounds;
	private double[] range;	
	private String[] parNames;
	private int nPars;
	private ArrayList<double[]> responseSurfaceBins = new ArrayList<double[]>();

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
	
	
	public void divideIntoIntervals(int nBinBounds){
		
		for (int iPar=0;iPar<nPars;iPar++){
			double[] binBounds = new double[nBinBounds];
			for (int iBinBound= 0; iBinBound<nBinBounds;iBinBound++){
				binBounds[iBinBound] = lowerBounds[iPar] + ((double)iBinBound/(nBinBounds-1))*range[iPar];
			}
			responseSurfaceBins.add(iPar, binBounds);
		}
	}

	public void divideIntoIntervals(int[] nBinBounds){
		
		for (int iPar=0;iPar<nPars;iPar++){
			double[] binBounds = new double[nBinBounds[iPar]];
			for (int iBinBound= 0; iBinBound<nBinBounds[iPar];iBinBound++){
				binBounds[iBinBound] = lowerBounds[iPar] + ((double)iBinBound/(nBinBounds[iPar]-1))*range[iPar];
			}
			responseSurfaceBins.add(iPar, binBounds);
		}
	}


	public double[] getResponseSurfaceBins(int index) {
		return responseSurfaceBins.get(index);
	}

	
	
}
