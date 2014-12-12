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

import org.eclipse.tracecompass.totalads.ui.results.Messages;
import org.eclipse.osgi.util.NLS;

/**
 * Message Bundle
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.ui.results.messages"; //$NON-NLS-1$
    public static String ResultsAndFeedback_Anomaly;
    public static String ResultsAndFeedback_AnomNo;
    public static String ResultsAndFeedback_AnomYes;
    public static String ResultsAndFeedback_Details;
    public static String ResultsAndFeedback_Evaluating;
    public static String ResultsAndFeedback_Results;
    public static String ResultsAndFeedback_SelModels;
    public static String ResultsAndFeedback_TotAnomalies;
    public static String ResultsAndFeedback_TotTraces;
    public static String ResultsAndFeedback_TraceList;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
