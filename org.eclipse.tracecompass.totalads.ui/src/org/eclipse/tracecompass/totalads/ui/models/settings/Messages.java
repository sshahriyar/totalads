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


package org.eclipse.tracecompass.totalads.ui.models.settings;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.totalads.ui.models.settings.Messages;
/**
 * Message Bundle
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.ui.models.settings.messages"; //$NON-NLS-1$
    public static String TestSettingsDialog_SettingsTitle;
    public static String TestSettingsHandler_NoConn;
    public static String TestSettingsHandler_SelModel;
    public static String TestSettingsHandler_SelOneModel;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
