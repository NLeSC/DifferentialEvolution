package nl.esciencecenter.diffevo;

public class Dimension {
	
	private double lowerBound;
	private double upperBound;
	private double range;
	
	Dimension(double lowerBound, double upperBound) {
		this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.range = this.upperBound-this.lowerBound;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}
	
	public double getRange() {
		return range;
	}
	
}
