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

import org.eclipse.tracecompass.totalads.ui.diagnosis.Messages;
import org.eclipse.osgi.util.NLS;

/**
 * Message Bundle
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.ui.diagnosis.messages"; //$NON-NLS-1$
    public static String BackgroundTesting_CommonException;
    public static String BackgroundTesting_CompletionMessage;
    public static String BackgroundTesting_ConsoleStartMessage;
    public static String BackgroundTesting_ConsoleTitle;
    public static String BackgroundTesting_DBMSException;
    public static String BackgroundTesting_EmptyDirectory;
    public static String BackgroundTesting_FolderContainsDir;
    public static String BackgroundTesting_GeneralException;
    public static String BackgroundTesting_InvalidTrace;
    public static String BackgroundTesting_LTTngFolderContainsFilesandDir;
    public static String BackgroundTesting_ModelEval;
    public static String BackgroundTesting_NoFiles;
    public static String BackgroundTesting_ReaderException;
    public static String BackgroundTesting_SelectFolder;
    public static String BackgroundTesting_TotalTime;
    public static String BackgroundTesting_TraceCountMessage;
    public static String BackgroundTesting_TraceLimit;
    public static String Diagnosis_SelectTMFTrace;
    public static String Diagnosis_SelModel;
    public static String Diagnosis_SelTestFolder;
    public static String Diagnosis_SelTrace;
    public static String Diagnosis_SelTraces;
    public static String Diagnosis_SelTraceType;
    public static String Diagnosis_StartEval;
    public static String Diagnosis_StopEval;
    public static String Diagnosis_TraceInTMF;
    public static String DiagnosisPartListener_UnableToLaunch;
    public static String DiagnosisView_UnableToLaunch;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
