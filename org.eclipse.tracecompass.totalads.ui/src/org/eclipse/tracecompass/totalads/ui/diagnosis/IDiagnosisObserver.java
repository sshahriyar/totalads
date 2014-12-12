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
package org.eclipse.tracecompass.totalads.ui.diagnosis;

import org.eclipse.tracecompass.totalads.ui.diagnosis.DiagnosisPartListener;
import org.eclipse.tracecompass.totalads.ui.diagnosis.DiagnosisView;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;

/**
 *
 * Implements an observer interface to be implemented by the
 * {@link DiagnosisView} so that it could be updated by the
 * {@link DiagnosisPartListener}
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */
public interface IDiagnosisObserver {
    /**
     * Updates the Diagnosis observer
     * @param results An object used to fill the results widgets in real time
     */
    public void update(ResultsAndFeedback results);

}
