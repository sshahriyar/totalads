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

package org.eclipse.tracecompass.totalads.ui.models.dbconnect;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.totalads.ui.models.dbconnect.Messages;
/**
 * Message Bundle
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.ui.models.dbconnect.messages"; //$NON-NLS-1$
    public static String AdvanceDBConfigurationPage_AdvanceConfigureTitle;
    public static String AdvanceDBConfigurationPage_AdvanDBConfigTitle;
    public static String AdvanceDBConfigurationPage_Database;
    public static String AdvanceDBConfigurationPage_Password;
    public static String AdvanceDBConfigurationPage_UserName;
    public static String DBConfigurationPage_3;
    public static String DBConfigurationPage_4;
    public static String DBConfigurationPage_BasicDBConfigTitle;
    public static String DBConfigurationPage_DBConfigureTitle;
    public static String DBConfigurationPage_PressFinishOrNext;
    public static String DBConnectionHandler_SuccessfulCon;
    public static String DBConnectWizard_DBConnectionWizard;
    public static String DBConnectWizard_InvalidPor;
    public static String DBConnectWizard_NoConnection;
    public static String DBConnectWizard_NoEmptyFields;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
