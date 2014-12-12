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

package org.eclipse.tracecompass.internal.totalads.algorithms.ksm;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.internal.totalads.algorithms.ksm.Messages;

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
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.internal.totalads.algorithms.ksm.messages"; //$NON-NLS-1$
    public static String KernelStateModeling_Abort;
    public static String KernelStateModeling_AnomsAtAlpha;
    public static String KernelStateModeling_Description;
    public static String KernelStateModeling_EnterDecimal;
    public static String KernelStateModeling_ExecuteSpecific;
    public static String KernelStateModeling_FinishEval;
    public static String KernelStateModeling_IncreaseAlpha;
    public static String KernelStateModeling_MeasureProbs;
    public static String KernelStateModeling_NoNull;
    public static String KernelStateModeling_SelectOne;
    public static String KernelStateModeling_StartTest;
    public static String KernelStateModeling_StateProb;
    public static String KernelStateModeling_TotTraces;
    public static String KernelStateModeling_TypeTrue;
    public static String KernelStateModeling_TypeTrueOrFalse;
    public static String KernelStateModeling_UpdateAlpha;
    public static String KernelStateModeling_UpdateDB;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
