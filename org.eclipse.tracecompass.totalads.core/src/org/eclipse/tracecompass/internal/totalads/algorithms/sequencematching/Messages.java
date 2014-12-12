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

package org.eclipse.tracecompass.internal.totalads.algorithms.sequencematching;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.internal.totalads.algorithms.sequencematching.Messages;
/**
 * Message Bundle
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.internal.totalads.algorithms.sequencematching.messages"; //$NON-NLS-1$
    public static String SlidingWindow_DBUpdated;
    public static String SlidingWindow_Description;
    public static String SlidingWindow_DistinctSeq;
    public static String SlidingWindow_EnterInteger;
    public static String SlidingWindow_EnterTrueOrFalse;
    public static String SlidingWindow_EvalUptoSeq;
    public static String SlidingWindow_Finish;
    public static String SlidingWindow_LargeHam;
    public static String SlidingWindow_LargeSeq;
    public static String SlidingWindow_LargestHam;
    public static String SlidingWindow_LastHamSeq;
    public static String SlidingWindow_NoNull;
    public static String SlidingWindow_NoSeqLength;
    public static String SlidingWindow_SeqEval;
    public static String SlidingWindow_StartMsg;
    public static String SlidingWindow_TenEvents;
    public static String SlidingWindow_TopNSeq;
    public static String SlidingWindow_TotalNorm;
    public static String SlidingWindow_TotalTraces;
    public static String SlidingWindow_TotAnom;
    public static String SlidingWindow_TotAnomalousSeq;
    public static String SlidingWindow_UniqueMsg;
    public static String SlidingWindowTree_Count;
    public static String SlidingWindowTree_SaveinDB;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
