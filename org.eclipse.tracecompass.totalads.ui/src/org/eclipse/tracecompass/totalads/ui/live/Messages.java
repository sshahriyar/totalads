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

package org.eclipse.tracecompass.totalads.ui.live;

import org.eclipse.tracecompass.totalads.ui.live.Messages;
import org.eclipse.osgi.util.NLS;

/**
 * Message Bundle
 *
 * @author <p>
 *         Syed Shariyar Murtaza jusstshary@hotmail.com
 *         </p>
 *
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.totalads.ui.live.messages"; //$NON-NLS-1$
    public static String BackgroundLiveMonitor_AnAnomaly;
    public static String BackgroundLiveMonitor_EvalOnModel;
    public static String BackgroundLiveMonitor_ExecFinish;
    public static String BackgroundLiveMonitor_FewMins;
    public static String BackgroundLiveMonitor_LiveMonitor;
    public static String BackgroundLiveMonitor_NotAnomaly;
    public static String BackgroundLiveMonitor_PauseFor;
    public static String BackgroundLiveMonitor_PleaseWait;
    public static String BackgroundLiveMonitor_Plotting;
    public static String BackgroundLiveMonitor_SSHTerminated;
    public static String BackgroundLiveMonitor_StopMonitor;
    public static String BackgroundLiveMonitor_StoppingMonitor;
    public static String BackgroundLiveMonitor_UnknwonErr;
    public static String BackgroundLiveMonitor_UpdateModel;
    public static String BackgroundLiveMonitor_UpdatingModel;
    public static String BackgroundLiveMonitor_Wait;
    public static String BackgroundLiveMonitor_WaitingForHost;
    public static String LiveMonitor_EmptyHost;
    public static String LiveMonitor_EmptyPassword;
    public static String LiveMonitor_EmptyPort;
    public static String LiveMonitor_EvalType;
    public static String LiveMonitor_IntegerPort;
    public static String LiveMonitor_NoOnlineLearnSupport;
    public static String LiveMonitor_Password;
    public static String LiveMonitor_Port;
    public static String LiveMonitor_SelDirForSotrage;
    public static String LiveMonitor_SelModel;
    public static String LiveMonitor_SnapshotDuration;
    public static String LiveMonitor_SnapshotInterval;
    public static String LiveMonitor_SshConfig;
    public static String LiveMonitor_Start;
    public static String LiveMonitor_Stop;
    public static String LiveMonitor_StorageDir;
    public static String LiveMonitor_Testing;
    public static String LiveMonitor_TrainAndTest;
    public static String LiveMonitor_UnableToWrite;
    public static String LiveMonitor_UserAtHost;
    public static String LiveMonitorView_UnableToLaunch;
    public static String LivePartListener_UnableToLaunch;
    public static String LiveResultsView_Details;
    public static String LiveResultsView_Timeline;
    public static String LiveXYChart_Anomalies;
    public static String LiveXYChart_NoSeries;
    public static String LiveXYChart_Time;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
