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
package org.eclipse.tracecompass.totalads.algorithms;

import org.eclipse.tracecompass.totalads.algorithms.Messages;
import org.eclipse.osgi.util.NLS;

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
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.algorithms.messages"; //$NON-NLS-1$
    public static String AlgorithmFactory_DuplicateName;
    public static String AlgorithmFactory_EmptyField;
    public static String AlgorithmFactory_NullValues;
    public static String AlgorithmFactory_UnderscoreMsg;
    public static String Algorithms_EmptyDirectory;
    public static String Algorithms_FolderContainsDir;
    public static String Algorithms_InvalidTrace;
    public static String Algorithms_LTTngFolderContainsFilesandDir;
    public static String Algorithms_ModelEval;
    public static String Algorithms_SelectFolder;
    public static String Algorithms_TraceCountMessage;
    public static String AlgorithmUtility_anomalies;
    public static String AlgorithmUtility_CurrentTrainingTrace;
    public static String AlgorithmUtility_CurrentValidationTrace;
    public static String AlgorithmUtility_EmptyDir;
    public static String AlgorithmUtility_EmptyDirectories;
    public static String AlgorithmUtility_EmptyModel;
    public static String AlgorithmUtility_EmptyTestDir;
    public static String AlgorithmUtility_FolderContainsDirectories;
    public static String AlgorithmUtility_FolderContainsMixture;
    public static String AlgorithmUtility_InvalidModel;
    public static String AlgorithmUtility_InvalidModelTotalADS;
    public static String AlgorithmUtility_InvalidTrainingTraces;
    public static String AlgorithmUtility_InvalidValidationTraces;
    public static String AlgorithmUtility_ModelingOn;
    public static String AlgorithmUtility_ModelTraining;
    public static String AlgorithmUtility_NoDB;
    public static String AlgorithmUtility_NoDBofTypeFound;
    public static String AlgorithmUtility_NullAlgorithm;
    public static String AlgorithmUtility_NullArguments;
    public static String AlgorithmUtility_NullModel;
    public static String AlgorithmUtility_SelectFolder;
    public static String AlgorithmUtility_StartSsh;
    public static String AlgorithmUtility_StartTest;
    public static String AlgorithmUtility_StartTrain;
    public static String AlgorithmUtility_StopMonitor;
    public static String AlgorithmUtility_TimeToStopMonitor;
    public static String AlgorithmUtility_TotalTime;
    public static String AlgorithmUtility_UpdateModel;
    public static String AlgorithmUtility_UpdateModelSpecific;
    public static String AlgorithmUtility_Validation;
    public static String AlgorithmUtility_Wait;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
