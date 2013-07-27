/*
 * Copyrighted 2012-2013 Netherlands eScience Center.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").  
 * You may not use this file except in compliance with the License. 
 * For details, see the LICENCE.txt file location in the root directory of this 
 * distribution or obtain the Apache License at the following location: 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 * 
 * For the full license, see: LICENCE.txt (located in the root folder of this distribution). 
 * ---
 */

package nl.esciencecenter.diffevo;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.GrayPaintScale;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;




/**
 * This is the Differential Evolution algorithm by Storn and Price
 * 
 * @author Jurriaan H. Spaaks
 * 
 */
public class DiffEvo {

	private final int nPars;
	private final int nPop;
	private final int nGens;
	private final int idCol;
	private final int[] parCols;
	private final int objCol;
	private Parents parents;
	private Proposals proposals;
	private EvalResults evalResults;
	private Random generator;
	private ParSpace parSpace;
	private double[] initState;
	private ForcingChunks forcingChunks;
	private TimeChunks timeChunks;
	private double[][] obs;
	private ModelFactory modelFactory;
	private String modelName;
	private LikelihoodFunctionFactory likelihoodFunctionFactory;
	
	// constructor:
	DiffEvo(int nGens, int nPop, ParSpace parSpace, double[] initState, double[] forcing, double[] times, 
			boolean[] assimilate, double[][] obs, ModelFactory modelFactory, LikelihoodFunctionFactory likelihoodFunctionFactory) {
		this.nGens = nGens;
		this.nPop = nPop;
		this.parSpace = parSpace;
		this.initState = initState;
		this.forcingChunks = new ForcingChunks(forcing, assimilate);
		this.timeChunks = new TimeChunks(times, assimilate);
		this.nPars = parSpace.getNumberOfPars();
		this.idCol = 1;
		this.parCols = new int[nPars]; 
		for (int iPar = 1; iPar <= this.nPars; iPar++) {
			this.parCols[iPar-1] = this.idCol + iPar;
		}
		this.objCol = 1 + this.nPars + 1;
		this.parents = new Parents(nPop,parSpace);
		this.proposals = new Proposals(nPop,parSpace);
		this.evalResults = new EvalResults();
		this.generator = new Random();
		this.generator.setSeed(0);
		this.obs = obs;
		this.modelFactory = modelFactory;
		this.likelihoodFunctionFactory = likelihoodFunctionFactory;
		this.modelName = modelFactory.getClass().getSimpleName().toString();
		}

	public void initializeParents(){
		int nModelEvals = 0;
		Sample sample;
		double[] parameterVector;
		double objScore;
		
//		System.out.println("initializing parents array");
		parents.takeUniformRandomSamples(generator);
		parents.calcObjScore(obs, initState,forcingChunks,timeChunks,modelFactory, likelihoodFunctionFactory);

		
		// now add the initial values of parents to the record, i.e. evalResults
		for (int iPop=0;iPop<nPop;iPop++){
			parameterVector = parents.getParameterVector(iPop);
			sample = new Sample(nPars);
			objScore = parents.getObjScore(iPop);
			sample.setSampleCounter(nModelEvals+iPop);
			sample.setParameterVector(parameterVector);
			sample.setObjScore(objScore);
			evalResults.add(sample);
		}
	}
	
	public void proposeOffSpring(){
//		System.out.println("proposing offspring");
		
		final int nDraws = 3;
		final double F = 0.6;
		final double K = 0.4;
		int[] availables = new int[nDraws]; // should maybe be called inside the loop?
		boolean drawAgain = true;
		int index;
		double[] dist1;
		double[] dist2;
		double[] proposal;
		double[] parent;
		
		// draw 3 random integer indices from [0,nPop], but not your own index and no recurrent samples
		for (int iPop=0;iPop<nPop;iPop++){
			availables[0] = -1;
			availables[1] = -1;
			availables[2] = -1;
			for (int iDraw=0;iDraw<nDraws;iDraw++){
				drawAgain = true;
				index = -1;
				while (drawAgain){
					index = generator.nextInt(nPop);
					drawAgain = index == iPop | index == availables[0] | index == availables[1] | index == availables[2];
				}
				availables[iDraw] = index;
			}
//			System.out.println(iPop+": ["+availables[0]+","+availables[1]+","+availables[2]+"]");
			
			dist1 = calcDistance(iPop, availables, 1);
			dist2 = calcDistance(iPop, availables, 2);
			
			parent = parents.getParameterVector(iPop);

			proposal = new double[nPars];
			for (int iPar=0;iPar<nPars;iPar++){
				proposal[iPar] = parent[iPar] + F * dist1[iPar] + K * dist2[iPar]; 				
			}
			proposals.setParameterVector(iPop, proposal);
 		}
		proposals.reflectIfOutOfBounds();
		proposals.calcObjScore(obs, initState, forcingChunks, timeChunks, modelFactory, likelihoodFunctionFactory);
	}
	
	private double[] calcDistance(int iPop, int[] availables, int distanceOneOrTwo){
		int fromIndex;
		int toIndex;
		double[] fromPoint;
		double[] toPoint;
		double[] dist;
 		
		if (distanceOneOrTwo==1){
			fromIndex = iPop;
			toIndex = availables[0];
		}
		else{
			fromIndex = availables[1];
			toIndex = availables[2];
		}
//		System.out.println("calculating distance vector between two parents "+fromIndex+" and "+toIndex);
		
		fromPoint = parents.getParameterVector(fromIndex);
		toPoint = parents.getParameterVector(toIndex);
		
		dist = new double[nPars];

		for (int iPar=0;iPar<nPars;iPar++){
			dist[iPar] = fromPoint[iPar]-toPoint[iPar];
		}
		
		return dist;
	}

	public void updateParentsWithProposals(){
//		System.out.println("accepting or rejecting proposals");
		double scoreParent;
		double scoreProposal;
		int nModelEvals = evalResults.size();
		Sample sample;
		double logOfUnifRandDraw;
		double[] parameterVector;
		double objScore;
		
		
		for (int iPop=0;iPop<nPop;iPop++){
			scoreParent = parents.getObjScore(iPop);
			scoreProposal = proposals.getObjScore(iPop);
			logOfUnifRandDraw = Math.log(generator.nextDouble());
			if (scoreProposal-scoreParent >= logOfUnifRandDraw){
				// accept proposals[iPop];
				parameterVector = proposals.getParameterVector(iPop);
				objScore = proposals.getObjScore(iPop);
			}
			else{
				// reject proposals[iPop];
				parameterVector = parents.getParameterVector(iPop);
				objScore = parents.getObjScore(iPop);
			}
			sample = new Sample(nPars);
			sample.setSampleCounter(nModelEvals+iPop);
			sample.setParameterVector(parameterVector);
			sample.setObjScore(objScore);
			parents.setParent(iPop, sample);
			
			// add most recent sample to the record array evalResults:
			evalResults.add(sample);
		}
	}
	
	
	public void printEvalResults(){
		int nResults;
		int sampleCounter;
		double[] parameterVector; 
		double objScore;
		
		nResults = evalResults.size();
		for (int iResult=0;iResult<nResults;iResult++){
			sampleCounter = evalResults.getSampleCounter(iResult);
			parameterVector = evalResults.getParameterVector(iResult);
			objScore = evalResults.getObjScore(iResult);
			//System.out.printf("%6d: [ ",iResult);
			System.out.printf("%6d ",sampleCounter);
			for (int iPar=0;iPar<nPars;iPar++){
				System.out.printf("%10.4g ",parameterVector[iPar]);
			}
			System.out.printf("%10.4f ",objScore);
			System.out.printf(" \n");
		}
	}

	
	public void writeEvalResultsToTextFile(){
		
		int nResults;
		int sampleCounter;
		int nDims;
		double[] parameterVector; 
		double objScore;

		StringBuilder stringBuilder = new StringBuilder();
		
		nResults = evalResults.size();
		nDims = evalResults.getParameterVector(0).length;
		for (int iResult=0;iResult<nResults;iResult++){
			
			// sampleCounter part
			sampleCounter = evalResults.getSampleCounter(iResult);

			// parameterVector part
			parameterVector = evalResults.getParameterVector(iResult);

			// objScore part
			objScore = evalResults.getObjScore(iResult);

			String s = new String();
			s = s + String.format("%d ", sampleCounter);
			for (int iDim=0;iDim<nDims;iDim++){
				s = s + String.format("%20.20g ",parameterVector[iDim]);
			}
			s = s + String.format("%20.20g\n", objScore);
			stringBuilder.append(s);
		}
		String string = stringBuilder.toString();
		
		try {
			File file = new File("evalresults.txt");
			System.out.println("Writing results to file: \'"+file+"\'.");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(string);
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}	
	
	public void oldWriteEvalResultsToJSON(){
		
		int nResults;
		int sampleCounter;
		double[] parameterVector; 
		double objScore;

		StringBuilder sampleCounterJSONStrB = new StringBuilder();
		sampleCounterJSONStrB.append("\"sampleIdentifier\" : [");
		
		StringBuilder parameterVectorJSONStrB = new StringBuilder();
		parameterVectorJSONStrB.append("\n\"parameterVector\" : [");		
		
		StringBuilder objScoreJSONStrB = new StringBuilder();
		objScoreJSONStrB.append("\n\"objScore\" : [");		

		StringBuilder allJSONStrB = new StringBuilder();
		
		nResults = evalResults.size();
		for (int iResult=0;iResult<nResults;iResult++){
			
			// sampleCounter part
			sampleCounter = evalResults.getSampleCounter(iResult);
			if (iResult>0){
				sampleCounterJSONStrB.append(",");
			}
			sampleCounterJSONStrB.append(sampleCounter);

			// parameterVector part
			parameterVector = evalResults.getParameterVector(iResult);
			if (iResult>0){
				parameterVectorJSONStrB.append(",");
			}
			parameterVectorJSONStrB.append("[");
			for (int iPar=0;iPar<nPars;iPar++){
				if (iPar>0){
					parameterVectorJSONStrB.append(",");
				}
				parameterVectorJSONStrB.append(parameterVector[iPar]);
			}
			parameterVectorJSONStrB.append("]");

			// objScore part
			objScore = evalResults.getObjScore(iResult);
			if (iResult>0){
				objScoreJSONStrB.append(",");
			}
			objScoreJSONStrB.append(objScore);
		}
		sampleCounterJSONStrB.append("],");
		String sampleCounterJSONStr = sampleCounterJSONStrB.toString();

		parameterVectorJSONStrB.append("],");
		String parameterVectorJSONStr = parameterVectorJSONStrB.toString();
	
		objScoreJSONStrB.append("]");
		String objScoreJSONStr = objScoreJSONStrB.toString();


		allJSONStrB.append("\"nResults\" : "+nResults+",\n");		
		allJSONStrB.append(sampleCounterJSONStr);
		allJSONStrB.append(parameterVectorJSONStr);
		allJSONStrB.append(objScoreJSONStr);
		
		String allJSONStr = "{\n"+allJSONStrB.toString()+"\n}";
		
		try {
			File file = new File("evalresults.json");
			System.out.println("Writing results to file: \'"+file+"\'.");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(allJSONStr);
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void writeEvalResultsToJSON(){
		
		int nResults;
		int sampleCounter;
		double[] parameterVector; 
		double objScore;

		StringBuilder stringBuild = new StringBuilder();
//		stringBuild.append("<!-- attributes overview: -->\n");
//		stringBuild.append("<!-- s: sample identifier -->\n");
//		stringBuild.append("<!-- p: parameter vector -->\n");
//		stringBuild.append("<!-- o: objective score -->\n");
		stringBuild.append("[\n");
		
		
		nResults = evalResults.size();
		for (int iResult=0;iResult<nResults;iResult++){
			
			// sampleCounter part
			sampleCounter = evalResults.getSampleCounter(iResult);
			// parameterVector part
			parameterVector = evalResults.getParameterVector(iResult);
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
			File file = new File("evalresults.json");
			System.out.println("Writing results to file: \'"+file+"\'.");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(str);
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	
	private int[][] calcResponseSurface(int iParRow,int iParCol){
		
		int nResults = evalResults.size();
		int iResult;
		int iPar;
		int iRowResponseSurface;
		int iColResponseSurface; 
		int nRowsResponseSurface = parSpace.getBinBounds(iParRow).length-1;
		int nColsResponseSurface = parSpace.getBinBounds(iParCol).length-1;
		int[][] responseSurfaceIndices = new int[nResults][2];
		int[][] responseSurface = new int[nRowsResponseSurface][nColsResponseSurface];
	    double[] parameterVector;

	    for (iResult=0;iResult<nResults;iResult++){
			parameterVector = evalResults.getParameterVector(iResult);
			
			int k=0;
			while (k<2){
				if (k==0){
					iPar = iParRow;
				}
				else {
					iPar = iParCol;
				}

				double[] binBounds = parSpace.getBinBounds(iPar);
				
				int nBinBounds = binBounds.length;
				for (int iBinBound=0; iBinBound<nBinBounds;iBinBound++){
					if (binBounds[iBinBound]>parameterVector[iPar]){
						responseSurfaceIndices[iResult][k] = iBinBound-1;
						break;
					}
				}

				
				k = k + 1;
				
			}
		}

	    for (iResult=0;iResult<nResults;iResult++){
	    	iRowResponseSurface = responseSurfaceIndices[iResult][0];
	    	iColResponseSurface = responseSurfaceIndices[iResult][1];
	    	responseSurface[iRowResponseSurface][iColResponseSurface] += 1;
	    }
	    
	    return responseSurface;
	} // calcResponseSurface

	
	public void scatterEvalObj(){
		
		Color markerFillColor = new Color(255,128, 0);
		
        XYDataset data = createDatasetEvalObj();
        String xAxisLabel = "number of model evaluations"; 
        String yAxisLabel = "objective score";
        Boolean showLegend = false;
        Boolean showTooltips = true;
        
		scatter(data,"eval-obj", markerFillColor,xAxisLabel,yAxisLabel, showLegend, showTooltips);
		
	}

	public void scatterEvalPar(int iPar){
		
		Color markerFillColor = new Color(0,128, 255);
		
        XYDataset data = createDatasetEvalPar(iPar);
        String xAxisLabel = "number of model evaluations"; 
        String yAxisLabel = "par"+iPar;
        Boolean showLegend = false;
        Boolean showTooltips = true;
        
		scatter(data,"eval-par"+iPar, markerFillColor,xAxisLabel,yAxisLabel, showLegend, showTooltips);
		
	}
	
	
	private void scatter(XYDataset data,String figureName, Color markerFillColor, String xAxisLabel, 
			String yAxisLabel, Boolean showLegend, Boolean showTooltips){
		
		Font tickFont = new Font("Ubuntu",Font.ROMAN_BASELINE,14);
		Font labelFont = new Font("Ubuntu",Font.ROMAN_BASELINE,20);
		RectangleInsets padding = new RectangleInsets(50,50,50,50); // TLBR in px
		
		Rectangle marker = new Rectangle(-3,-3,6,6);
		
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension defaultWindowSize = new java.awt.Dimension();
		defaultWindowSize.width = (int) (screenSize.width*0.8);
		defaultWindowSize.height = (int) (screenSize.height*0.8); 
		
		java.awt.Dimension preferredSize = new java.awt.Dimension(defaultWindowSize);

        // create a chart...
        JFreeChart chart = ChartFactory.createXYLineChart(figureName,
        		xAxisLabel,
        		yAxisLabel,
        		data,
        		PlotOrientation.VERTICAL,
        		showLegend, showTooltips, false);


        chart.setBackgroundPaint(Color.LIGHT_GRAY);
        chart.getTitle().setFont(labelFont);
        chart.setPadding(padding);
        
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinePaint(Color.BLACK);
        
        plot.getDomainAxis().setLabelPaint(Color.BLACK);
        plot.getDomainAxis().setTickLabelPaint(Color.BLACK);
        plot.getDomainAxis().setAxisLinePaint(Color.BLACK);
        plot.getDomainAxis().setTickMarkPaint(Color.BLACK);
        plot.getDomainAxis().setLabelFont(labelFont);
        plot.getDomainAxis().setTickLabelFont(tickFont);
        
        plot.getRangeAxis().setLabelPaint(Color.BLACK);
        plot.getRangeAxis().setTickLabelPaint(Color.BLACK);
        plot.getRangeAxis().setAxisLinePaint(Color.BLACK);
        plot.getRangeAxis().setTickMarkPaint(Color.BLACK);
        plot.getRangeAxis().setLabelFont(labelFont);
        plot.getRangeAxis().setTickLabelFont(tickFont);

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseLinesVisible(false);
        renderer.setBaseShapesFilled(true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, marker);
        renderer.setSeriesPaint(0, markerFillColor);
                
        final ChartPanel panel = new ChartPanel(chart);
        
        JFrame frame = new JFrame("diffevo scatter");
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.setMaximumDrawHeight(e.getComponent().getHeight());
                panel.setMaximumDrawWidth(e.getComponent().getWidth());
                panel.setMinimumDrawWidth(e.getComponent().getWidth());
                panel.setMinimumDrawHeight(e.getComponent().getHeight());
            }
        });        
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(preferredSize.width, preferredSize.height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
        
//        RefineryUtilities.centerFrameOnScreen(frame);
        
	}

	private XYDataset createDatasetEvalObj(){
		
		int nResults = evalResults.size();
		XYSeries series = new XYSeries("eval-obj");
		for (int iResult=0;iResult<nResults;iResult++){
			series.add(evalResults.getSampleCounter(iResult),evalResults.getObjScore(iResult));
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}

	
	private XYDataset createDatasetEvalPar(int iPar){
		
		int nResults = evalResults.size();
		XYSeries series = new XYSeries("eval-par"+iPar);
		for (int iResult=0;iResult<nResults;iResult++){
			double[] parameterVector = evalResults.getParameterVector(iResult);
			series.add(evalResults.getSampleCounter(iResult),parameterVector[iPar]);
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;
	}
	
	
	
	public void matrixOfScatterParPar(){
		
		Font tickFont = new Font("Ubuntu",Font.ROMAN_BASELINE,14);
		Font labelFont = new Font("Ubuntu",Font.ROMAN_BASELINE,20);
		RectangleInsets padding = new RectangleInsets(10,10,10,10); // TLBR in px
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension defaultWindowSize = new java.awt.Dimension();
		defaultWindowSize.width = (int) (screenSize.width*0.8);
		defaultWindowSize.height = (int) (screenSize.height*0.8); 
		java.awt.Dimension preferredSize = new java.awt.Dimension(defaultWindowSize);
		
		Color markerFillColor = new Color(255,128, 0);
		Rectangle marker = new Rectangle(-2,-2,4,4);
		
		int nResults = evalResults.size();
		
		NumberAxis[] allAxes = new NumberAxis[nPars];
		for (int iPar=0;iPar<nPars;iPar++){
			allAxes[iPar] = new NumberAxis(parSpace.getParName(iPar));
			allAxes[iPar].setAxisLinePaint(Color.BLACK);
			allAxes[iPar].setLabelPaint(Color.BLACK);
			allAxes[iPar].setTickLabelPaint(Color.BLACK);
		}

		StandardXYToolTipGenerator ttG = new StandardXYToolTipGenerator();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseToolTipGenerator(ttG);

		JFrame frame = new JFrame(getModelName()+" // matrixOfScatterParPar // "+getName());

        frame.setLayout(new GridLayout(nPars-1,nPars-1));
        
        for (int iRow=0;iRow<nPars-1;iRow++){
        	for (int iCol=1;iCol<nPars;iCol++){

				XYSeries series = new XYSeries("("+parSpace.getParName(iCol)+","+parSpace.getParName(iRow)+")");
				for (int iResult=0;iResult<nResults;iResult++){
					double[] parameterVector = evalResults.getParameterVector(iResult);
					series.add(parameterVector[iCol],parameterVector[iRow]);
				}

				XYSeriesCollection xycoll = new XYSeriesCollection();
				xycoll.addSeries(series);
				XYDataset xydataset = (XYDataset) xycoll;
				XYPlot subplot = new XYPlot(xydataset, allAxes[iCol], allAxes[iRow], renderer);
		        subplot.setOutlinePaint(Color.BLACK);
		        subplot.setOutlineVisible(true);
		        subplot.setOutlineStroke(allAxes[0].getAxisLineStroke());
		        subplot.setRangePannable(true);
		        subplot.setDomainPannable(true);
		        subplot.setDomainGridlinePaint(Color.BLACK);
		        subplot.setRangeGridlinePaint(Color.BLACK);
		        
		        ///

		        subplot.setBackgroundPaint(Color.WHITE);
		        
		        subplot.getDomainAxis().setLabelPaint(Color.BLACK);
		        subplot.getDomainAxis().setTickLabelPaint(Color.BLACK);
		        subplot.getDomainAxis().setAxisLinePaint(Color.BLACK);
		        subplot.getDomainAxis().setTickMarkPaint(Color.BLACK);
		        subplot.getDomainAxis().setLabelFont(labelFont);
		        subplot.getDomainAxis().setTickLabelFont(tickFont);
		        
		        subplot.getRangeAxis().setLabelPaint(Color.BLACK);
		        subplot.getRangeAxis().setTickLabelPaint(Color.BLACK);
		        subplot.getRangeAxis().setAxisLinePaint(Color.BLACK);
		        subplot.getRangeAxis().setTickMarkPaint(Color.BLACK);
		        subplot.getRangeAxis().setLabelFont(labelFont);
		        subplot.getRangeAxis().setTickLabelFont(tickFont);


		        ///

				JFreeChart chart = new JFreeChart("",JFreeChart.DEFAULT_TITLE_FONT, subplot, false);
				
		        renderer.setBaseLinesVisible(false);
		        renderer.setBaseShapesFilled(true);
		        renderer.setSeriesShapesVisible(0, true);
		        renderer.setSeriesShape(0, marker);
		        renderer.setSeriesPaint(0, markerFillColor);
				
		        chart.setBackgroundPaint(Color.LIGHT_GRAY);
		        chart.getTitle().setFont(labelFont);
		        chart.setPadding(padding);
		        
		        if (iCol>iRow){
			        final ChartPanel chartPanel = new ChartPanel(chart);
			        chartPanel.setBackground(Color.LIGHT_GRAY);
					frame.add(chartPanel,iRow*(nPars-1)+iCol-1);
			        frame.addComponentListener(new ComponentAdapter() {
			            @Override
			            public void componentResized(ComponentEvent e) {
			                chartPanel.setMaximumDrawHeight(e.getComponent().getHeight()/(nPars-1));
			                chartPanel.setMaximumDrawWidth(e.getComponent().getWidth()/(nPars-1));
			                chartPanel.setMinimumDrawWidth(e.getComponent().getWidth()/(nPars-1));
			                chartPanel.setMinimumDrawHeight(e.getComponent().getHeight()/(nPars-1));
			            }
			        });        
		        }
		        else {
			        final ChartPanel chartPanel = new ChartPanel(null);
			        chartPanel.setBackground(Color.LIGHT_GRAY);
					frame.add(chartPanel,iRow*(nPars-1)+iCol-1);
		        }
			}
		}

        frame.setSize(preferredSize.width, preferredSize.height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
		
		
	}

	
	
	public void matrixOfHeatmapParPar(){
		
		Font tickFont = new Font("Ubuntu",Font.ROMAN_BASELINE,14);
		Font labelFont = new Font("Ubuntu",Font.ROMAN_BASELINE,20);
		RectangleInsets padding = new RectangleInsets(0,0,0,0); // TLBR in px
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension defaultWindowSize = new java.awt.Dimension();
		defaultWindowSize.width = (int) (screenSize.width*0.8);
		defaultWindowSize.height = (int) (screenSize.height*0.8); 
		java.awt.Dimension preferredSize = new java.awt.Dimension(defaultWindowSize);
		
		
		NumberAxis[] allAxes = new NumberAxis[nPars];
		for (int iPar=0;iPar<nPars;iPar++){
			allAxes[iPar] = new NumberAxis(parSpace.getParName(iPar));
			allAxes[iPar].setAxisLinePaint(Color.BLACK);
			allAxes[iPar].setLabelPaint(Color.BLACK);
			allAxes[iPar].setTickLabelPaint(Color.BLACK);
		}

		JFrame frame = new JFrame(getModelName()+" // matrixOfHeatMap //"+getName());
        frame.setLayout(new GridLayout(nPars-1,nPars-1));
        
        for (int iRow=0;iRow<nPars-1;iRow++){ //0,1,2
        	for (int iCol=1;iCol<nPars;iCol++){ // 1,2,3
        		
		        if (iCol>iRow){        		

		        	// Calculate the response surface of parameter parSpace[iRow] and parSpace[iCol] using 
		        	// parSpace.getResponseSurfaceBinBounds[iRow] and parSpace.getResponseSurfaceBinBounds[iCol]

		        	int[][] responseSurface = calcResponseSurface(iRow, iCol);
		        	
		        	int responseSurfaceMin = 0;
		        	int responseSurfaceMax = 0;	        	
		        		        	
		        	double[] xBinBounds = parSpace.getBinBounds(iCol);
		        	double[] yBinBounds = parSpace.getBinBounds(iRow);
		        	int nBinsX = xBinBounds.length-1;
		        	int nBinsY = yBinBounds.length-1;
		        	
		        	DefaultXYZDataset dataset = new DefaultXYZDataset();
		        	double[][] data = new double[3][nBinsY*nBinsX];

		        	int iData=0;
		        	for (int iBinX=0;iBinX<nBinsX;iBinX++){
		        		for (int iBinY=0;iBinY<nBinsY;iBinY++){

		        			if (responseSurface[iBinY][iBinX]>responseSurfaceMax){
			        			responseSurfaceMax = responseSurface[iBinY][iBinX];		        				
		        			}
		        			if (responseSurface[iBinY][iBinX]<responseSurfaceMin){
			        			responseSurfaceMin = responseSurface[iBinY][iBinX];		        				
		        			}

		        			double xBlock = (xBinBounds[iBinX]+xBinBounds[iBinX+1])/2;
		        			double yBlock = (yBinBounds[iBinY]+yBinBounds[iBinY+1])/2;
		        			
		        			data[0][iData] = xBlock;
		        			data[1][iData] = yBlock;
		        			data[2][iData] = responseSurface[iBinY][iBinX];
		        			
		        			iData++;

		        		}
	        			dataset.addSeries(0,data);
		        	}
		        	
	        	    // http://stackoverflow.com/questions/8441269/jfreechart-to-represent-3d-data-in-a-2d-graph-using-colourmaps
        			XYBlockRenderer renderer = new XYBlockRenderer();
        			PaintScale scale = new GrayPaintScale(responseSurfaceMin, responseSurfaceMax);
      			    renderer.setPaintScale(scale);
        			renderer.setBlockHeight(parSpace.getResolution(iRow));
        			renderer.setBlockWidth(parSpace.getResolution(iCol));

		        	XYPlot subplot = new XYPlot(dataset, allAxes[iCol], allAxes[iRow], renderer);

		        	subplot.setOutlinePaint(Color.BLACK);
		        	subplot.setOutlineVisible(true);
		        	subplot.setOutlineStroke(allAxes[0].getAxisLineStroke());
		        	subplot.setRangePannable(true);
		        	subplot.setDomainPannable(true);
		        	subplot.setDomainGridlinePaint(Color.BLACK);
		        	subplot.setRangeGridlinePaint(Color.BLACK);
		        	
			        
			        subplot.getDomainAxis().setLabelPaint(Color.BLACK);
			        subplot.getDomainAxis().setTickLabelPaint(Color.BLACK);
			        subplot.getDomainAxis().setAxisLinePaint(Color.BLACK);
			        subplot.getDomainAxis().setTickMarkPaint(Color.BLACK);
			        subplot.getDomainAxis().setLabelFont(labelFont);
			        subplot.getDomainAxis().setTickLabelFont(tickFont);
			        
			        subplot.getRangeAxis().setLabelPaint(Color.BLACK);
			        subplot.getRangeAxis().setTickLabelPaint(Color.BLACK);
			        subplot.getRangeAxis().setAxisLinePaint(Color.BLACK);
			        subplot.getRangeAxis().setTickMarkPaint(Color.BLACK);
			        subplot.getRangeAxis().setLabelFont(labelFont);
			        subplot.getRangeAxis().setTickLabelFont(tickFont);		        	

		        	JFreeChart chart = new JFreeChart("",JFreeChart.DEFAULT_TITLE_FONT, subplot, false);

		        	chart.setBackgroundPaint(Color.LIGHT_GRAY);
		        	chart.getTitle().setFont(labelFont);
		        	chart.setPadding(padding);


			        final ChartPanel chartPanel = new ChartPanel(chart);
			        chartPanel.setBackground(Color.LIGHT_GRAY);
					frame.add(chartPanel,iRow*(nPars-1)+iCol-1);
			        frame.addComponentListener(new ComponentAdapter() {
			            @Override
			            public void componentResized(ComponentEvent e) {
			                chartPanel.setMaximumDrawHeight(e.getComponent().getHeight()/(nPars-1));
			                chartPanel.setMaximumDrawWidth(e.getComponent().getWidth()/(nPars-1));
			                chartPanel.setMinimumDrawWidth(e.getComponent().getWidth()/(nPars-1));
			                chartPanel.setMinimumDrawHeight(e.getComponent().getHeight()/(nPars-1));
			            }
			        });        
		        }
		        else {
			        final ChartPanel chartPanel = new ChartPanel(null);
			        chartPanel.setBackground(Color.LIGHT_GRAY);
					frame.add(chartPanel,iRow*(nPars-1)+iCol-1);
		        }
        	}
        }

        frame.setSize(preferredSize.width, preferredSize.height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
		
		
	}

	public int[] calcHistogram(int iPar){
		

		int nResults = evalResults.size();
		int nBins = parSpace.getnBins(iPar);
		int[] histogram = new int[nBins];
	    double[] parameterVector;
		double[] binBounds = parSpace.getBinBounds(iPar);
		int nBinBounds = binBounds.length;
		
	    for (int iResult=0;iResult<nResults;iResult++){
			parameterVector = evalResults.getParameterVector(iResult);
			for (int iBinBound=0; iBinBound<nBinBounds;iBinBound++){
				if (binBounds[iBinBound]>parameterVector[iPar]){
					histogram[iBinBound-1]++;
					break;
				}
			}
	    }
	    
	    return histogram;		
		
	}
	
	public void margHist(){
		
		
		Font tickFont = new Font("Ubuntu",Font.ROMAN_BASELINE,14);
		Font labelFont = new Font("Ubuntu",Font.ROMAN_BASELINE,20);
		RectangleInsets padding = new RectangleInsets(10,50,10,50); // TLBR in px
		
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension defaultWindowSize = new java.awt.Dimension();
		defaultWindowSize.width = (int) (screenSize.width*0.8);
		defaultWindowSize.height = (int) (screenSize.height*0.8); 
		java.awt.Dimension preferredSize = new java.awt.Dimension(defaultWindowSize);
		
		boolean showTooltips = true;
		boolean showURLs = false;
		boolean showLegend = false;
		Color barFillColor = new Color(255,128, 0);
		Color barOutlineColor = Color.BLACK;//new Color(255,128, 0);

		JFrame frame = new JFrame(getModelName()+" // margHist // "+getName());
        frame.setLayout(new GridLayout(nPars,1));
        for (int iPar=0;iPar<nPars;iPar++){
        
        	int[] histogram = calcHistogram(iPar);
        	int nBins = parSpace.getnBins(iPar);
        	double[] binBounds = parSpace.getBinBounds(iPar);
        	
        	XYSeries series = new XYSeries("histogram");
        	for (int iBin=0;iBin<nBins;iBin++){
        		series.add((binBounds[iBin]+binBounds[iBin+1])/2,histogram[iBin]);
        	}

        	XYSeriesCollection xycoll = new XYSeriesCollection();
        	xycoll.addSeries(series);
        	
        	double barWidth = parSpace.getResolution(iPar);
        	xycoll.setIntervalWidth(barWidth);
        	
            JFreeChart chart = ChartFactory.createXYBarChart(
                    "",
                    parSpace.getParName(iPar),
                    false,
                    "count",
                    xycoll,
                    PlotOrientation.VERTICAL,
                    showLegend,
                    showTooltips,
                    showURLs);
            
            chart.setBackgroundPaint(Color.LIGHT_GRAY);
            chart.getTitle().setFont(labelFont);
            chart.setPadding(padding);
            
            XYPlot plot = chart.getXYPlot();

            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinePaint(Color.BLACK);
            plot.setDomainGridlinePaint(Color.BLACK);
            
            plot.getDomainAxis().setLabelPaint(Color.BLACK);
            plot.getDomainAxis().setTickLabelPaint(Color.BLACK);
            plot.getDomainAxis().setAxisLinePaint(Color.BLACK);
            plot.getDomainAxis().setTickMarkPaint(Color.BLACK);
            plot.getDomainAxis().setLabelFont(labelFont);
            plot.getDomainAxis().setTickLabelFont(tickFont);
            
            plot.getRangeAxis().setLabelPaint(Color.BLACK);
            plot.getRangeAxis().setTickLabelPaint(Color.BLACK);
            plot.getRangeAxis().setAxisLinePaint(Color.BLACK);
            plot.getRangeAxis().setTickMarkPaint(Color.BLACK);
            plot.getRangeAxis().setLabelFont(labelFont);
            plot.getRangeAxis().setTickLabelFont(tickFont);

        	plot.setOutlinePaint(Color.BLACK);
        	plot.setOutlineVisible(true);
        	plot.setOutlineStroke(plot.getDomainAxis().getAxisLineStroke());
        	plot.setRangePannable(true);
        	plot.setDomainPannable(true);
        	plot.setDomainGridlinePaint(Color.BLACK);
        	plot.setRangeGridlinePaint(Color.BLACK);
        	
        	XYItemRenderer renderer = plot.getRenderer(0); 
        	renderer.setSeriesPaint(0,barFillColor);
        	renderer.setSeriesOutlinePaint(0,barOutlineColor);
        	
        	XYBarRenderer brenderer = (XYBarRenderer) renderer;
        	StandardXYBarPainter painter = new StandardXYBarPainter();
        	brenderer.setBarPainter(painter);
        	
        	
        	
	        final ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setBackground(Color.LIGHT_GRAY);
			frame.add(chartPanel,iPar);
	        frame.addComponentListener(new ComponentAdapter() {
	            @Override
	            public void componentResized(ComponentEvent e) {
	            		chartPanel.setMaximumDrawHeight(e.getComponent().getHeight()/nPars);
	            		chartPanel.setMaximumDrawWidth(e.getComponent().getWidth());
	            		chartPanel.setMinimumDrawWidth(e.getComponent().getWidth());
	            		chartPanel.setMinimumDrawHeight(e.getComponent().getHeight()/nPars);
	            }
	        });        
        }
        
        frame.setSize(preferredSize.width, preferredSize.height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
	}
	
	
	
	/* 
	 * * * * * * * * * * * * * * * * * * * * * * * *  
	 * GETTERS
	 * * * * * * * * * * * * * * * * * * * * * * * * 
	 */

	public int getObjCol() {
		return objCol;
	}

	public int getnPop() {
		return nPop;
	}

	private String getName(){
		return DiffEvo.class.getSimpleName();
	}


	public void start(){
		System.out.println("Starting Differential Evolution optimization...");		
		initializeParents();
		for (int iGen = 1;iGen<nGens;iGen++){
			proposeOffSpring();
			updateParentsWithProposals();
		}
	}
	
	private String getModelName(){
		return modelName;
	}
	
	
	/* 
	 * * * * * * * * * * * * * * * * * * * * * * * *  
	 * SETTERS
	 * * * * * * * * * * * * * * * * * * * * * * * * 
	 */

	

	
}
