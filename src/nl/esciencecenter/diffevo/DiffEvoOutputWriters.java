package nl.esciencecenter.diffevo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiffEvoOutputWriters {
	
	private EvalResults evalResults;
	private int nPars;
	
	public DiffEvoOutputWriters(EvalResults evalResults){
		
		this.evalResults = evalResults;
		this.nPars = evalResults.getParSpace().getNumberOfPars();
		
	}
	
	public void printEvalResults(){
		int nResults;
		int sampleCounter;
		double[] parameterVector; 
		double objScore;
		
		nResults = evalResults.getNumberOfEvalResults();
		for (int iResult=0;iResult<nResults;iResult++){
			sampleCounter = evalResults.getSampleIdentifier(iResult);
			parameterVector = evalResults.getParameterCombination(iResult);
			objScore = evalResults.getObjScore(iResult);
			System.out.printf("%6d ",sampleCounter);
			for (int iPar=0;iPar<nPars;iPar++){
				System.out.printf("%10.4g ",parameterVector[iPar]);
			}
			System.out.printf("%10.4f ",objScore);
			System.out.printf(" %n");
		}
	}

	
	public void writeEvalResultsToTextFile(File file){
		
		int nResults;
		int sampleCounter;
		int nDims;
		double[] parameterVector; 
		double objScore;

		StringBuilder stringBuilder = new StringBuilder();
		
		nResults = evalResults.getNumberOfEvalResults();
		nDims = evalResults.getParameterCombination(0).length;
		for (int iResult=0;iResult<nResults;iResult++){
			
			// sampleCounter part
			sampleCounter = evalResults.getSampleIdentifier(iResult);

			// parameterVector part
			parameterVector = evalResults.getParameterCombination(iResult);

			// objScore part
			objScore = evalResults.getObjScore(iResult);

			stringBuilder.append(String.format("%d ", sampleCounter));
			for (int iDim=0;iDim<nDims;iDim++){
				stringBuilder.append(String.format("%20.20g ",parameterVector[iDim]));
			}
			stringBuilder.append(String.format("%20.20g%n", objScore));
		}
		String string = stringBuilder.toString();
		
		try {
			System.out.println("Writing results to file: \'"+file+"\'.");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(string);
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}	
	
	
	public void writeEvalResultsToJSON(File file){
		
		int nResults;
		int sampleCounter;
		double[] parameterVector; 
		double objScore;

		StringBuilder stringBuild = new StringBuilder();
		stringBuild.append("[\n");
		
		
		nResults = evalResults.getNumberOfEvalResults();
		for (int iResult=0;iResult<nResults;iResult++){
			
			// sampleCounter part
			sampleCounter = evalResults.getSampleIdentifier(iResult);
			// parameterVector part
			parameterVector = evalResults.getParameterCombination(iResult);
			// objScore part
			objScore = evalResults.getObjScore(iResult);
			
			if (iResult>0){
				stringBuild.append(",\n");
			}
			
			stringBuild.append("{\"s\":"+sampleCounter+",\"p\":[");
			for (int iPar=0;iPar<nPars;iPar++){
				if (iPar>0){
					stringBuild.append(",");
				}
				stringBuild.append(parameterVector[iPar]);
			}
			
			stringBuild.append("],\"o\":"+objScore);			
			stringBuild.append("}");
		}
		
		stringBuild.append("\n]\n");
		
		String str = stringBuild.toString();
		
		try {
			System.out.println("Writing results to file: \'"+file+"\'.");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(str);
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	

	
	
}
