package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class EvalResultsTest {
	

	@Test
	public void testAdd() {
		
		EvalResults evalResults = new EvalResults();
		
		int nDims = 2;
		int nPop = 10;
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = new Sample(nDims);
			sample.setSampleCounter(sampleCounterExpected);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			evalResults.add(sample);
		}
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = evalResults.getSample(iPop);
			int sampleCounterActual = sample.getSampleCounter();
			double[] parameterVectorActual = sample.getParameterVector();
			double objScoreActual = sample.getObjScore();
			
			
			
			assertTrue(sampleCounterExpected==sampleCounterActual);
			
			int nPars = nDims;
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}

			assertTrue(objScoreExpected==objScoreActual);
			
		}
		
	}

	@Test
	public void testGetEvalResults() {
		EvalResults evalResults = new EvalResults();
		
		int nDims = 2;
		int nPop = 10;
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = new Sample(nDims);
			sample.setSampleCounter(sampleCounterExpected);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			evalResults.add(sample);
		}
		
		List<Sample> sampleList = evalResults.getEvalResults();
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			int sampleCounterActual = sampleList.get(iPop).getSampleCounter();
			double[] parameterVectorActual = sampleList.get(iPop).getParameterVector();
			double objScoreActual = sampleList.get(iPop).getObjScore();
			
			assertTrue(sampleCounterExpected==sampleCounterActual);
			
			int nPars = nDims;
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}

			assertTrue(objScoreExpected==objScoreActual);
			
		}
		

	}

	@Test
	public void testGetSample() {
		
		EvalResults evalResults = new EvalResults();
		
		int nDims = 2;
		int nPop = 10;
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			
			Sample sample = new Sample(nDims);
			sample.setSampleCounter(sampleCounterExpected);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			evalResults.add(sample);
		}
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = evalResults.getSample(iPop);
			int sampleCounterActual = sample.getSampleCounter();
			double[] parameterVectorActual = sample.getParameterVector();
			double objScoreActual = sample.getObjScore();
			
			
			
			assertTrue(sampleCounterExpected==sampleCounterActual);
			
			int nPars = nDims;
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}

			assertTrue(objScoreExpected==objScoreActual);
			
		}
		

	}

	@Test
	public void testGetParameterVector() {
		
		EvalResults evalResults = new EvalResults();
		
		int nDims = 2;
		int nPop = 10;
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = new Sample(nDims);
			sample.setSampleCounter(sampleCounterExpected);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			evalResults.add(sample);
		}
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = evalResults.getSample(iPop);
			int sampleCounterActual = sample.getSampleCounter();
			double[] parameterVectorActual = sample.getParameterVector();
			double objScoreActual = sample.getObjScore();
			
			
			
			assertTrue(sampleCounterExpected==sampleCounterActual);
			
			int nPars = nDims;
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}

			assertTrue(objScoreExpected==objScoreActual);
			
		}
		

	}

	@Test
	public void testGetObjScore() {
		
		EvalResults evalResults = new EvalResults();
		
		int nDims = 2;
		int nPop = 10;
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = new Sample(nDims);
			sample.setSampleCounter(sampleCounterExpected);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			evalResults.add(sample);
		}
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = evalResults.getSample(iPop);
			int sampleCounterActual = sample.getSampleCounter();
			double[] parameterVectorActual = sample.getParameterVector();
			double objScoreActual = sample.getObjScore();
			
			
			
			assertTrue(sampleCounterExpected==sampleCounterActual);
			
			int nPars = nDims;
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}

			assertTrue(objScoreExpected==objScoreActual);
			
		}
		

	}

	@Test
	public void testSize() {
		
		EvalResults evalResults = new EvalResults();
		
		int nDims = 2;
		int nPop = 10;
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = new Sample(nDims);
			sample.setSampleCounter(sampleCounterExpected);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			evalResults.add(sample);
		}
		
		int sizeActual = evalResults.size();
		int sizeExpected = nPop; 
		assertTrue(sizeActual==sizeExpected);

	}

	@Test
	public void testGetSampleCounter() {
		EvalResults evalResults = new EvalResults();
		
		int nDims = 2;
		int nPop = 10;
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = new Sample(nDims);
			sample.setSampleCounter(sampleCounterExpected);
			sample.setParameterVector(parameterVectorExpected);
			sample.setObjScore(objScoreExpected);
			
			evalResults.add(sample);
		}
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			Sample sample = evalResults.getSample(iPop);
			int sampleCounterActual = sample.getSampleCounter();
			double[] parameterVectorActual = sample.getParameterVector();
			double objScoreActual = sample.getObjScore();
			
			assertTrue(sampleCounterExpected==sampleCounterActual);
			
			int nPars = nDims;
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}

			assertTrue(objScoreExpected==objScoreActual);
			
		}

	}

}
