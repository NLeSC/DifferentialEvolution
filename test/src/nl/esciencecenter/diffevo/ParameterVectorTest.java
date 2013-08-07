package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParameterVectorTest {

	private ParSpace parSpace;
	
	public ParameterVectorTest(){
		
		double[] lowerBounds = new double[]{-20,-40,-80,-120};
		double[] upperBounds = new double[]{ 20, 40, 80, 120};
		String[] parNames = new String[]{"a","b","c","d"};
		parSpace = new ParSpace(lowerBounds,upperBounds,parNames);
		
	}
	
	
	@Test
	public void testGetValues() {
		double[] expecteds = new double[]{0.1,-0.2,0.3,-0.4};

		ParameterVector parameterVector = new ParameterVector(parSpace);
		parameterVector.setValues(expecteds);
		
		int nx = expecteds.length;
		double[] actuals = parameterVector.getValues();
		for (int ix=0;ix<nx;ix++){
			assertTrue(expecteds[ix]==actuals[ix]);
		}
	}

	@Test
	public void testSetValues() {
		
		double[] expecteds = new double[]{0.1,-0.2,0.3,-0.4};

		ParameterVector parameterVector = new ParameterVector(parSpace);
		parameterVector.setValues(expecteds);
		
		int nx = expecteds.length;
		double[] actuals = parameterVector.getValues();
		for (int ix=0;ix<nx;ix++){
			assertTrue(expecteds[ix]==actuals[ix]);
		}
	}

	@Test
	public void testParameterVector() {

		int nPars = parSpace.getNumberOfPars();
		ParameterVector parameterVector = new ParameterVector(parSpace);
		
		double[] actuals = parameterVector.getValues();
		for (int iPar=0;iPar<nPars;iPar++){
			assertTrue(actuals[iPar]==0.0);
		}
	}
	
}
