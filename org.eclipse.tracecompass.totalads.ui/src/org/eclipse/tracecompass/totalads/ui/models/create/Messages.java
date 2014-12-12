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

package org.eclipse.tracecompass.totalads.ui.models.create;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.totalads.ui.models.create.Messages;
/**
 * Message Bundle
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.ui.models.create.messages"; //$NON-NLS-1$
    public static String AlgorithmSelectionPage_AlgSelTitle;
    public static String AlgorithmSelectionPage_NoDescription;
    public static String AlgorithmSelectionPage_SelAlgorithm;
    public static String AlgorithmSelectionPage_SelectAlgForDescription;
    public static String AlgorithmSettingsPage_AdjustSettings;
    public static String AlgorithmSettingsPage_AlgorithmSettings;
    public static String CreateModelHandler_NoConn;
    public static String CreateModelWizard_NewModel;
    public static String ModelNamePage_Description;
    public static String ModelNamePage_EnterModelName;
    public static String ModelNamePage_ModelLabel;
    public static String ModelNamePage_ModelTitle;
    public static String ModelNamePage_NoSpecialCharacters;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
