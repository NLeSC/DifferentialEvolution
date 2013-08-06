package nl.esciencecenter.diffevo;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class DataReader {
	
	private double[][] dataArrayTransposed;

	public DataReader(File file) {

		ArrayList<double[]> dataArrayList = new ArrayList<double[]>();
		try{
			Scanner fileScanner = new Scanner(file);
			int nSkipLines = fileScanner.nextInt();
			for (int iSkipLine=0;iSkipLine<nSkipLines;iSkipLine++){
				fileScanner.nextLine();
			}
			int nLines = 0;
			int nParts = 0;
			while (fileScanner.hasNextLine()){
				String line = fileScanner.nextLine();
				String[] parts = line.split(",");
				nParts = parts.length;
				double[] values = new double[nParts];
				for (int iPart=0;iPart<nParts;iPart++){
					values[iPart] = Double.valueOf(parts[iPart]);
				}
				dataArrayList.add(values);
				nLines = nLines + 1;
			} //while
			double[][] dataArray = dataArrayList.toArray(new double[nLines][nParts]);
			setData(dataArray);
		} //try
		catch(Exception e){
			System.out.println("Error while reading file line by line:" 
					+ e.getMessage());                      
		} //catch
	} //main

	public double[][] getData() {
		return dataArrayTransposed;
	} // getDataArray

	private void setData(double[][] dataArray) {
		
		int nRows = dataArray.length;
		int nCols = dataArray[0].length;

		dataArrayTransposed = new double[nCols][nRows];
		
		for (int iRow=0;iRow<nRows;iRow++){
			for (int iCol=0;iCol<nCols;iCol++){
				dataArrayTransposed[iCol][iRow] = dataArray[iRow][iCol];
			}
		}
		
	} // setDataArray
} //DataReader
