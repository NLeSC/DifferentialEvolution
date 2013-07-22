package nl.esciencecenter.diffevo;

import java.util.ArrayList;

public class ParSpace {
	
	private double[] lowerBounds;
	private double[] upperBounds;
	private double[] range;	
	private String[] parNames;
	private int nPars;
	private ArrayList<double[]> binBoundsAll = new ArrayList<double[]>();
	private double[] resolutions;

	public ParSpace(double[] lowerBounds, double[] upperBounds, String[] parNames){
		
		this.lowerBounds = lowerBounds;
		this.upperBounds = upperBounds;
		this.parNames = parNames;
		this.setNumberOfPars(lowerBounds.length);
		
		double[] resolutions = new double[getNumberOfPars()];
		double[] range = new double[getNumberOfPars()]; 
	
		for (int iPar=0;iPar<getNumberOfPars();iPar++){
			range[iPar] = upperBounds[iPar] - lowerBounds[iPar];
			resolutions[iPar] = 0;
		}

		this.range = range;
		this.resolutions = resolutions;
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
	
	
	public void divideIntoIntervals(int nIntervals){
		int[] tmp = new int[nPars];		
		for (int iPar=0;iPar<nPars;iPar++){
			tmp[iPar] = nIntervals;
		}
		divideIntoIntervals(tmp);
	}

	public void divideIntoIntervals(int[] nIntervals){
		int[] nBinBounds = new int[nIntervals.length];
		for (int iPar=0;iPar<nPars;iPar++){
			nBinBounds[iPar] = nIntervals[iPar]+1;
			double[] binBounds = new double[nBinBounds[iPar]];
			for (int iBinBound = 0; iBinBound<nBinBounds[iPar];iBinBound++){
				binBounds[iBinBound] = lowerBounds[iPar] + ((double)iBinBound/nIntervals[iPar])*range[iPar];
			}
			binBoundsAll.add(iPar, binBounds);
			resolutions[iPar] = binBounds[1]-binBounds[0];
		}
	}


	public double[] getBinBounds(int iPar) {
		return binBoundsAll.get(iPar);
	}

	
	public double getResolution(int iPar) {
		return resolutions[iPar];
	}





	
}
