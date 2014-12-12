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

import org.eclipse.tracecompass.totalads.ui.live.LiveXYChart;
import org.eclipse.tracecompass.totalads.ui.live.Messages;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

/**
 * Creates a view for Live Results
 *
 * @author <p>
 *         Syed Shariyar Murtaza jusstshary@hotmail.com
 *         </p>
 *
 */
public class LiveResultsView extends ViewPart {
    /**
     * View ID
     */
    public static final String VIEW_ID = "org.eclipse.tracecompass.totalads.ui.LiveResultsView"; //$NON-NLS-1$
    private LiveXYChart liveXYChart;
    private ResultsAndFeedback results;

    /**
     * Default constructor
     */
    public LiveResultsView() {

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createPartControl(Composite compParent) {
        TabFolder tabFolder = new TabFolder(compParent, SWT.NONE);
        TabItem itemChart = new TabItem(tabFolder, SWT.NONE);
        itemChart.setText(Messages.LiveResultsView_Timeline);
        TabItem itemResults = new TabItem(tabFolder, SWT.NONE);
        itemResults.setText(Messages.LiveResultsView_Details);

        Composite compChart = new Composite(tabFolder, SWT.NONE);
        compChart.setLayout(new FillLayout());
        liveXYChart = new LiveXYChart(compChart);
        itemChart.setControl(compChart);

        Composite compResults = new Composite(tabFolder, SWT.NONE);
        compResults.setLayout(new GridLayout(1, false));
        results = new ResultsAndFeedback(compResults, false);
        itemResults.setControl(compResults);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {

    }

    /**
     * Get LiveXYChart object
     *
     * @return LiveXYChart object
     */
    public LiveXYChart getLiveChart() {

        return liveXYChart;
    }

    /**
     * Get ResultsAndFeedback object
     *
     * @return ResultsAndFeedback object
     */
    public ResultsAndFeedback getResults() {
        return results;
    }

}
