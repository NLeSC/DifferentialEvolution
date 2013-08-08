package nl.esciencecenter.diffevo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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

public class DiffEvoVisualization {
	
	
	private EvalResults evalResults;
	private ParSpace parSpace;
	private Font tickFont;
	private Font labelFont;
	private int nPars;
	private String modelName;

	
	public DiffEvoVisualization(EvalResults evalResults){
		
		this.evalResults = evalResults;
		this.parSpace = evalResults.getParSpace();
		this.tickFont = new Font("Ubuntu",Font.ROMAN_BASELINE,14);
		this.labelFont = new Font("Ubuntu",Font.ROMAN_BASELINE,20);
		this.nPars = evalResults.getParSpace().getNumberOfDimensions();
		this.modelName = evalResults.getModelName();
		
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
		
		RectangleInsets padding = new RectangleInsets(50,50,50,50);
		
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
		
		if (nPars>1){
		RectangleInsets padding = new RectangleInsets(10,10,10,10);
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

		JFrame frame = new JFrame(modelName+" // matrixOfScatterParPar // "+getName());

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
		else {
			System.out.println("The model "+modelName+" only has one parameter.");
		}
	}

	
	
	public void matrixOfHeatmapParPar(){
		
		if (nPars>1){
		
		RectangleInsets padding = new RectangleInsets(0,0,0,0);
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

		JFrame frame = new JFrame(modelName+" // matrixOfHeatMap //"+getName());
        frame.setLayout(new GridLayout(nPars-1,nPars-1));
        
        for (int iRow=0;iRow<nPars-1;iRow++){
        	for (int iCol=1;iCol<nPars;iCol++){
        		
		        if (iCol>iRow){        		

		        	/*
		        	Calculate the response surface of parameter parSpace[iRow] and parSpace[iCol] using 
		        	parSpace.getResponseSurfaceBinBounds[iRow] and parSpace.getResponseSurfaceBinBounds[iCol]
		        	 */
		        	
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
		else {
			System.out.println("The model "+modelName+" only has one parameter.");
		}
		
		
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
		
		RectangleInsets padding = new RectangleInsets(10,50,10,50);
		
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		java.awt.Dimension defaultWindowSize = new java.awt.Dimension();
		defaultWindowSize.width = (int) (screenSize.width*0.8);
		defaultWindowSize.height = (int) (screenSize.height*0.8); 
		java.awt.Dimension preferredSize = new java.awt.Dimension(defaultWindowSize);
		
		boolean showTooltips = true;
		boolean showURLs = false;
		boolean showLegend = false;
		Color barFillColor = new Color(255,128, 0);
		Color barOutlineColor = Color.BLACK;

		JFrame frame = new JFrame(modelName+" // margHist // "+getName());
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
	
	private String getName(){
		return DiffEvoVisualization.class.getSimpleName();
	}

	
	
}
