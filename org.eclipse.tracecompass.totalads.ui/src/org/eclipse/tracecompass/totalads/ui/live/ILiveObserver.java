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

import org.eclipse.tracecompass.totalads.ui.live.LiveMonitorView;
import org.eclipse.tracecompass.totalads.ui.live.LivePartListener;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;

/**
 *
 * Observer interface to be implemented by the {@link LiveMonitorView} so that
 * it could be updated by the {@link LivePartListener}
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */
public interface ILiveObserver {
    /**
     * Updates the Live observer
     *
     * @param results
     *            An object of type {@link ResultsAndFeedback}
     */
    public void update(ResultsAndFeedback results);

}
