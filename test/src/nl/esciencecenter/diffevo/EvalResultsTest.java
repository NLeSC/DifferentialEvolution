package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionFactory;
import nl.esciencecenter.diffevo.likelihoodfunctionfactories.LikelihoodFunctionRastriginModelFactory;

import org.junit.Test;

public class EvalResultsTest {

	private int nGens;
	private int nPop;
	private ParSpace parSpace;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	private DiffEvo diffEvo;
	private long randomSeed;
	
	public EvalResultsTest(){
		
		//RastriginModel
		this.nGens = 30;
		this.nPop = 50;
		{
			double[] lowerBoundsParSpace = new double[]{-5.12,-5.12};
			double[] upperBoundsParSpace  = new double[]{5.12,5.12};
			String[] parNames = new String[]{"p1","p2"};
			this.parSpace = new ParSpace(lowerBoundsParSpace,upperBoundsParSpace,parNames);
			this.parSpace.divideIntoIntervals(200);
		}
		this.randomSeed = 0;
		this.likelihoodFunctionFactory = (LikelihoodFunctionFactory) new LikelihoodFunctionRastriginModelFactory();
		this.diffEvo = new DiffEvo(nGens, nPop, parSpace, likelihoodFunctionFactory,randomSeed);
	}

	@Test
	public void testAdd() {
		
		
		//EvalResults evalResults = new EvalResults(nGens,nPop,parSpace,likelihoodFunctionFactory,generator);
		EvalResults evalResults = diffEvo.runOptimization();
		
		int nPars = evalResults.getParSpace().getNumberOfPars();
		
		int nResultsActual = evalResults.getNumberOfEvalResults();
		int nResultsExpected = nPop*nGens;
		
		assertTrue(nResultsActual==nResultsExpected);
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop+nResultsExpected;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			EvalResult evalResult = new EvalResult(sampleCounterExpected, parameterVectorExpected, objScoreExpected);

			evalResults.add(evalResult);
		}
		
		for (int iPop=0;iPop<nPop;iPop++){

			int sampleCounterExpected = iPop+nResultsExpected;
			double[] parameterVectorExpected = new double[]{0.1+iPop,0.2+iPop};
			double objScoreExpected = -2.0*iPop;
			
			EvalResult evalResult = evalResults.getEvalResult(iPop+nResultsExpected);
			int sampleCounterActual = evalResult.getSampleIdentifier();
			double[] parameterVectorActual = evalResult.getParameterCombination();
			double objScoreActual = evalResult.getObjScore();
			
			assertTrue(sampleCounterExpected==sampleCounterActual);
		
			for (int iPar=0;iPar<nPars;iPar++){
				assertTrue(parameterVectorActual[iPar]==parameterVectorExpected[iPar]);
			}

			assertTrue(objScoreExpected==objScoreActual);
			
		}
		
	}


}
