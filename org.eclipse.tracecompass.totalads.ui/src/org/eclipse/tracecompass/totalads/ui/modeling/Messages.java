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

package org.eclipse.tracecompass.totalads.ui.modeling;

import org.eclipse.tracecompass.totalads.ui.modeling.Messages;
import org.eclipse.osgi.util.NLS;
/**
 * Message Bundle
 * @author <p> Syed Shariyar Murtaza justsshary@hotmail.com </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.ui.modeling.messages"; //$NON-NLS-1$
    public static String BackgroundModeling_DBErr;
    public static String BackgroundModeling_Modeling;
    public static String BackgroundModeling_ModelingFinished;
    public static String BackgroundModeling_ReaderErr;
    public static String BackgroundModeling_SevereErr;
    public static String BackgroundModeling_UIErr;
    public static String Modeling_SelModelFirst;
    public static String Modeling_SelTrainDir;
    public static String Modeling_SelTrainTraces;
    public static String Modeling_SelValDir;
    public static String Modeling_SelValTraces;
    public static String Modeling_StartModeling;
    public static String Modeling_StopModeling;
    public static String Modeling_TracesTraining;
    public static String Modeling_TraceType;
    public static String ModelingView_UnableToLaunch;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
