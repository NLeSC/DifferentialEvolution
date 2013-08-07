package nl.esciencecenter.diffevo;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class DataReaderTest {

	@Test
	public void testDataReader() {
		
		File file = new File("test"+File.separator+"datareader-unit-testing.eas");
		DataReader reader = new DataReader(file);
		double[][] dataActual = reader.getData();
		
		double[][] dataExpected = new double[][]{{1.2,1.7},{0.0,1.0},{2.2,4.5},{4.5,2.8}};
		
		int nRows = 2;
		int nCols = 4;
		for (int iCol=0;iCol<nCols;iCol++){
			for (int iRow=0;iRow<nRows;iRow++){
		        assertTrue(dataActual[iCol][iRow]==dataExpected[iCol][iRow]);
			}
		}
		
	}

	@Test
	public void testGetData() {
		File file = new File("test"+File.separator+"datareader-unit-testing.eas");
		DataReader reader = new DataReader(file);
		double[][] dataActual = reader.getData();
		
		double[][] dataExpected = new double[][]{{1.2,1.7},{0.0,1.0},{2.2,4.5},{4.5,2.8}};
		
		int nRows = 2;
		int nCols = 4;
		for (int iCol=0;iCol<nCols;iCol++){
			for (int iRow=0;iRow<nRows;iRow++){
		        assertTrue(dataActual[iCol][iRow]==dataExpected[iCol][iRow]);
			}
		}
	}

}
