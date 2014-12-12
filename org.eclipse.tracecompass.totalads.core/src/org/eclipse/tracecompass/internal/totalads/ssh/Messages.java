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

package org.eclipse.tracecompass.internal.totalads.ssh;

import org.eclipse.tracecompass.internal.totalads.ssh.Messages;
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
    private static final String BUNDLE_NAME = "org.eclipse.tracecompass.internal.totalads.ssh.messages"; //$NON-NLS-1$
    public static String SSHConnector_CommunicationError;
    public static String SSHConnector_ConnectionEstablish;
    public static String SSHConnector_Downloading;
    public static String SSHConnector_Error;
    public static String SSHConnector_ExitStatus;
    public static String SSHConnector_IdentityAdded;
    public static String SSHConnector_ProcessRemote;
    public static String SSHConnector_TraceDuration;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
