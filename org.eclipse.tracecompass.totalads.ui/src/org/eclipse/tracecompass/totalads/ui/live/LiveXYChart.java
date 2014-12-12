/*********************************************************************************************
 * Copyright (c) 2014-2015  Software Behaviour Analysis Lab, Concordia University, Montreal, Canada
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of Eclipse Public License v1.0 License which
 * accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Syed Shariyar Murtaza -- Initial design and implementation
 **********************************************************************************************/

package org.eclipse.tracecompass.totalads.ui.live;

import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.ui.live.Messages;
import org.eclipse.tracecompass.totalads.ui.utilities.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.Range;

/**
 * This class creates a chart
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class LiveXYChart {
    private Chart fXyChart;
    private PlotSymbolType[] fPlotSymbols;
    private Color[] fPlotColors;
    private final int fMAX_SERIES = 5;

    /**
     * Constructor
     *
     * @param compParent
     *            Composite
     */
    public LiveXYChart(Composite compParent) {

        fXyChart = new Chart(compParent, SWT.NONE);
        fXyChart.getTitle().setVisible(false); // Keep title invisible to get
                                               // more space for the chart
        fXyChart.getAxisSet().getXAxis(0).getTitle().setText(Messages.LiveXYChart_Time);
        fXyChart.getAxisSet().getYAxis(0).getTitle().setText(Messages.LiveXYChart_Anomalies);

        setXRange(0, 9, 100);
        setYRange(-1, 2);

        fPlotSymbols = new PlotSymbolType[fMAX_SERIES];
        fPlotSymbols[0] = PlotSymbolType.CIRCLE;
        fPlotSymbols[1] = PlotSymbolType.DIAMOND;
        fPlotSymbols[2] = PlotSymbolType.CROSS;
        fPlotSymbols[3] = PlotSymbolType.INVERTED_TRIANGLE;
        fPlotSymbols[4] = PlotSymbolType.SQUARE;

        fPlotColors = new Color[fMAX_SERIES];
        fPlotColors[0] = SWTResourceManager.getColor(SWT.COLOR_GREEN);
        fPlotColors[1] = SWTResourceManager.getColor(SWT.COLOR_BLUE);
        fPlotColors[2] = SWTResourceManager.getColor(SWT.COLOR_RED);
        fPlotColors[3] = SWTResourceManager.getColor(SWT.COLOR_MAGENTA);
        fPlotColors[4] = SWTResourceManager.getColor(SWT.COLOR_GRAY);

    }

    /**
     * This function initializes different series in a chart. It must be called
     * immediately after the creation of an object
     *
     * @param seriesNames
     *            An array of series names
     *
     */
    public void inititaliseSeries(final String[] seriesNames) {

        Display.getDefault().asyncExec(new Runnable() {// Always execute on the
                                                       // main GUI thread

                    @Override
                    public void run() {
                        for (int j = 0; j < seriesNames.length; j++) {

                            ILineSeries lineSeries = (ILineSeries) fXyChart.getSeriesSet().
                                    createSeries(SeriesType.LINE, seriesNames[j]);

                            lineSeries.setSymbolType(getSymbol(j));
                            lineSeries.setLineColor(getColor(j));
                            lineSeries.enableStep(true);
                        }
                    }
                });

    }

    /**
     * Sets minimum and maximum on Y axis
     *
     * @param min
     *            Minimum value
     * @param max
     *            Maximum value
     */
    public void setYRange(final int min, final int max) {
        Display.getDefault().syncExec(new Runnable() {// Always execute on the
                                                      // main GUI thread
                    @Override
                    public void run() {

                        IAxis yAxis = fXyChart.getAxisSet().getYAxis(0);
                        yAxis.setRange(new Range(min, max));
                        yAxis.getTick().setTickMarkStepHint(100);
                        // fXyChart.getAxisSet().adjustRange();
                    }
                });
    }

    /**
     * Sets minimum and maximum on X axis
     *
     * @param min
     *            Minimum value
     * @param max
     *            Maximum value
     * @param step
     *            Step
     */
    public void setXRange(final int min, final int max, final int step) {
        Display.getDefault().syncExec(new Runnable() {// Always execute on the
                                                      // main GUI thread
                    @Override
                    public void run() {
                        IAxis xAxis = fXyChart.getAxisSet().getXAxis(0);
                        xAxis.setRange(new Range(min, max));
                        xAxis.getTick().setTickMarkStepHint(step);// fXyChart.getAxisSet().adjustRange();
                    }
                });
    }

    /**
     * Returns the symbol for a series out of five symbols
     *
     * @param index
     *            Index of five symbols
     * @return PlotSymbolType
     */
    private PlotSymbolType getSymbol(int index) {
        if (index < fMAX_SERIES) {
            return fPlotSymbols[index];
        }
        return fPlotSymbols[0];
    }

    /**
     * Returns the color for a series out of five colors
     *
     * @param index
     *            Index of five colors
     * @return Color
     */
    private Color getColor(int index) {
        if (index < fMAX_SERIES) {
            return fPlotColors[index];
        }
        return fPlotColors[0];
    }

    /**
     * This function allows to add the values to a series that has been
     * intialised earlier using inititalizeSeries function. It throws an
     * exception if series name is not found
     *
     * @param ySeries
     *            Array of double values
     * @param seriesName
     *            Series name
     * @throws TotalADSGeneralException
     *             An exception for invalid series
     */
    public void addYSeriesValues(double[] ySeries, String seriesName) throws TotalADSGeneralException {
        // create line series
        ILineSeries lineSeries = (ILineSeries) fXyChart.getSeriesSet().getSeries(seriesName);
        if (lineSeries == null) {
            throw new TotalADSGeneralException(Messages.LiveXYChart_NoSeries);
        }
        lineSeries.setYSeries(ySeries);

    }

    /**
     * This function allows to add the values to a series that has been
     * intialized earlier using inititalizeSeries function. It throws an
     * exception if series name is not found
     *
     * @param ySeries
     *            Array of double values
     * @param seriesName
     *            Series name
     * @throws TotalADSGeneralException
     *             An exception for invalid series
     */
    public void addYSeriesValues(Double[] ySeries, String seriesName) throws TotalADSGeneralException {
        // First convert Double to double
        double[] yVals = new double[ySeries.length];
        for (int j = 0; j < ySeries.length; j++) {
            yVals[j] = ySeries[j];
        }

        // create line series
        ILineSeries lineSeries = (ILineSeries) fXyChart.getSeriesSet().getSeries(seriesName);
        if (lineSeries == null) {
            throw new TotalADSGeneralException(Messages.LiveXYChart_NoSeries);
        }
        lineSeries.setYSeries(yVals);

    }

    /**
     * This function sets the values on X axis that would be displayed for
     * corresponding points on Y axis
     *
     * @param xSeries
     *            An array of double values
     * @param seriesName
     *            Name of the series
     * @throws TotalADSGeneralException
     *             An exception for invalid series
     */
    public void addXSeriesValues(double[] xSeries, String seriesName) throws TotalADSGeneralException {
        // create line series
        // fXyChart.getSeriesSet().getSeries()[0].setXSeries(xSeries);
        ILineSeries lineSeries = (ILineSeries) fXyChart.getSeriesSet().getSeries(seriesName);
        if (lineSeries == null) {
            throw new TotalADSGeneralException(Messages.LiveXYChart_NoSeries);
        }
        lineSeries.setXSeries(xSeries);

    }

    /**
     * This function sets the values on X axis that would be displayed for
     * corresponding points on Y axis
     *
     * @param xSeries
     *            An array of double values
     * @param seriesName
     *            Series' name
     * @throws TotalADSGeneralException
     *             An exception for invalid series
     */
    public void addXSeriesValues(Double[] xSeries, String seriesName) throws TotalADSGeneralException {
        // First convert Double to double
        double[] xVals = new double[xSeries.length];
        for (int j = 0; j < xSeries.length; j++) {
            xVals[j] = xSeries[j];
        }
        // create line series
        // fXyChart.getSeriesSet().getSeries()[0].setXSeries(xVals);
        ILineSeries lineSeries = (ILineSeries) fXyChart.getSeriesSet().getSeries(seriesName);
        if (lineSeries == null) {
            throw new TotalADSGeneralException(Messages.LiveXYChart_NoSeries);
        }
        lineSeries.setXSeries(xVals);

    }

    /**
     * Draws the chart. It must be called every time the series is updated
     */
    public void drawChart() {

        Display.getDefault().syncExec(new Runnable() {// Always execute on the
                                                      // main GUI thread

                    @Override
                    public void run() {
                        // adjust the axis range

                        fXyChart.redraw();
                    }
                });
    }

    /**
     * Clears the chart
     */
    public void clearChart() {

        Display.getDefault().syncExec(new Runnable() {// Always execute on the
                                                      // main GUI thread

                    @Override
                    public void run() {
                        ISeriesSet seriesSet = fXyChart.getSeriesSet();
                        ISeries[] series = fXyChart.getSeriesSet().getSeries();

                        for (int j = 0; j < series.length; j++) {
                            seriesSet.deleteSeries(series[j].getId());
                        }

                        fXyChart.redraw();

                    }
                });

    }

}
