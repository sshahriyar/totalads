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
package org.eclipse.tracecompass.totalads.ui.results;

import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * Launches a Results View
 * @author <p> Syed Shariyar Murtaza </p>
 * 		   <p> Efraim Lopez </p>
 *
 */
public class ResultsView extends ViewPart {

	/**
	 * VIEW ID
	 */
	public static final String VIEW_ID = "org.eclipse.tracecompass.totalads.ui.ResultsView"; //$NON-NLS-1$
	private ResultsAndFeedback results;

	/**
	 * Constructor
	 */
	public ResultsView() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		 results=new ResultsAndFeedback(parent, false);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {


	}

	/**
	 * Returns the instance of {@link ResultsAndFeedback}
	 * @return Results
	 */
	public ResultsAndFeedback getResultsAndFeddbackInstance(){

		return results;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose(){

	}


}
