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

package org.eclipse.tracecompass.internal.totalads.algorithms.hiddenmarkovmodel;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.internal.totalads.algorithms.hiddenmarkovmodel.Messages;

/**
 * Message Bundle
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.internal.totalads.algorithms.hiddenmarkovmodel.messages"; //$NON-NLS-1$

    public static String HiddenMarkovModel_AdditionalEvents;
    public static String HiddenMarkovModel_AnomalousPatterns;
    public static String HiddenMarkovModel_Anomaly;
    public static String HiddenMarkovModel_BaumWelchMsg;
    public static String HiddenMarkovModel_Description;
    public static String HiddenMarkovModel_EventsOverlaodMsg;
    public static String HiddenMarkovModel_ExtractionMsg;
    public static String HiddenMarkovModel_Finish;
    public static String HiddenMarkovModel_HMMErrorMsg;
    public static String HiddenMarkovModel_MinLogLikeliHood;
    public static String HiddenMarkovModel_NullArguments;
    public static String HiddenMarkovModel_ReTrainHMM;
    public static String HiddenMarkovModel_SaveHMMMsg;
    public static String HiddenMarkovModel_SequenceEvalMsg;
    public static String HiddenMarkovModel_SequenceExtrractionMsg;
    public static String HiddenMarkovModel_SpecificSeq;
    public static String HiddenMarkovModel_UnkownEvents;
    public static String HiddenMarkovModel_ValidationFinished;
    public static String HiddenMarkovModel_ValidationStart;
    public static String HmmMahout_EmisssionProbs;
    public static String HmmMahout_HiddenStates;
    public static String HmmMahout_HmmModel;
    public static String HmmMahout_Initial;
    public static String HmmMahout_ObservableEvents;
    public static String HmmMahout_SelectDecForLog;
    public static String HmmMahout_SelectIntIteration;
    public static String HmmMahout_SelectIntSeq;
    public static String HmmMahout_SelectIntStates;
    public static String HmmMahout_SelectIntSymbols;
    public static String HmmMahout_SelectIterations;
    public static String HmmMahout_SelectNegForLog;
    public static String HmmMahout_Transition;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
