package nl.esciencecenter.diffevo;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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
	private final int idCol;
	private final int[] parCols;
	private final int objCol;
	private Parents parents;
	private Proposals proposals;
	private EvalResults evalResults;
	private Random generator;
	private Model model;
	private ArrayList<Dimension> parSpace;

	// constructor:
	DiffEvo(ArrayList<Dimension> parSpace, int nPop, Model model) {
		this.nPars = parSpace.size();
		this.nPop = nPop;
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
		this.model = model;
		this.parSpace = parSpace;
		}
	
	public void initializeParents(){
		int nModelEvals = 0;
		Sample sample;
		double[] parameterVector;
		double objScore;
		
//		System.out.println("initializing parents array");
		parents.takeUniformRandomSamples(generator);
		parents.evaluateModel(model);

		
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
		proposals.evaluateModel(model);
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
	
	
	public void calcResponseSurface(int nPartitions){
		
		int nResults=evalResults.size();
		int iResult;
		int iPar;
	    int[][] responseSurfaceIndices = new int[nResults][nPars];
	    double[] parameterVector;
	    double standardizedParameterValue;

	    for (iResult=0;iResult<nResults;iResult++){
			parameterVector = evalResults.getParameterVector(iResult);
			for (iPar=0;iPar<nPars;iPar++){
				standardizedParameterValue = (parameterVector[iPar]-parSpace.get(iPar).getLowerBound()) / parSpace.get(iPar).getRange();
				responseSurfaceIndices[iResult][iPar] = (int)Math.floor(standardizedParameterValue*nPartitions);				
			}
		}
	    
	    
		StringBuilder stringBuild = new StringBuilder();

		for (iPar=0;iPar<nPars;iPar++){
			if (iPar>0){
				stringBuild.append(",");
			}
			stringBuild.append("\"par"+iPar+"\"");
		}
		stringBuild.append("\n");		
		
		for (iResult=0;iResult<nResults;iResult++){
			if (iResult>0){
				stringBuild.append("\n");
			}
			for (iPar=0;iPar<nPars;iPar++){
				if (iPar>0){
					stringBuild.append(",");
				}
				stringBuild.append(responseSurfaceIndices[iResult][iPar]);
			}
		}
		
		String str = stringBuild.toString();
		
		try {
			File file = new File("response-surface-indices.csv");
			System.out.println("Writing results to file: \'"+file+"\'.");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write(str);
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
		}

	    
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
		
		Font tickFont = new Font("Ubuntu",Font.ROMAN_BASELINE,12);
		Font labelFont = new Font("Ubuntu",Font.ROMAN_BASELINE,16);
		RectangleInsets padding = new RectangleInsets(50,50,50,50); // TLBR in px
		
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
        renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
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
	
	
	
	public void panelTest(){
		
		//Font tickFont = new Font("Ubuntu",Font.ROMAN_BASELINE,12);
		Font labelFont = new Font("Ubuntu",Font.ROMAN_BASELINE,16);
		RectangleInsets padding = new RectangleInsets(5,5,5,5); // TLBR in px
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension defaultWindowSize = new java.awt.Dimension();
		defaultWindowSize.width = (int) (screenSize.width*0.8);
		defaultWindowSize.height = (int) (screenSize.height*0.8); 
		java.awt.Dimension preferredSize = new java.awt.Dimension(defaultWindowSize);
		
		Color markerFillColor = new Color(255,128, 0);
		
		int nResults = evalResults.size();
		
		NumberAxis[] allAxes = new NumberAxis[nPars];
		for (int iPar=0;iPar<nPars;iPar++){
			allAxes[iPar] = new NumberAxis("par"+iPar);
		}

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		JFrame frame = new JFrame("frame title");
        frame.setLayout(new GridLayout(nPars,nPars));
        
        for (int iRow=0;iRow<nPars;iRow++){
        	for (int iCol=0;iCol<nPars;iCol++){

				XYSeries series = new XYSeries("series_"+iRow+"_"+iCol);
				for (int iResult=0;iResult<nResults;iResult++){
					double[] parameterVector = evalResults.getParameterVector(iResult);
					series.add(parameterVector[iCol],parameterVector[iRow]);
				}

				XYSeriesCollection xycoll = new XYSeriesCollection();
				xycoll.addSeries(series);
				XYDataset xydataset = (XYDataset) xycoll;
				XYPlot subplot = new XYPlot(xydataset, allAxes[iCol], allAxes[iRow], renderer);

				JFreeChart chart = new JFreeChart("",JFreeChart.DEFAULT_TITLE_FONT, subplot, false);
				
		        renderer.setBaseLinesVisible(false);
		        renderer.setBaseShapesFilled(true);
		        renderer.setSeriesShapesVisible(0, true);
		        renderer.setSeriesShape(0, new java.awt.Rectangle(-1,-1,2,2));
		        renderer.setSeriesPaint(0, markerFillColor);
				
		        chart.setBackgroundPaint(Color.LIGHT_GRAY);
		        chart.getTitle().setFont(labelFont);
		        chart.setPadding(padding);
				
				final ChartPanel chartPanel = new ChartPanel(chart);
				
				frame.add(chartPanel);				
			}
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


	
	/* 
	 * * * * * * * * * * * * * * * * * * * * * * * *  
	 * SETTERS
	 * * * * * * * * * * * * * * * * * * * * * * * * 
	 */

	

	
}
