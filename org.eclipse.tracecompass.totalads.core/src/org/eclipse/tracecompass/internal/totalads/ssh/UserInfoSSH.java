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

import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream;

import com.jcraft.jsch.*;

/**
 * This class implements the UserInfo Interface in JSch package. This interface
 * is necessary to implement in order to connect to an SSH server
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class UserInfoSSH implements UserInfo {
    private String fPassword;
    private IAlgorithmOutStream fOutStream;

    /**
     * Constructor
     *
     * @param password
     *            Password
     * @param outStream
     *            Output stream to display processing messages
     */
    public UserInfoSSH(String password, IAlgorithmOutStream outStream) {
        this.fPassword = password;
        this.fOutStream = outStream;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jcraft.jsch.UserInfo#showMessage(java.lang.String)
     */
    @Override
    public void showMessage(String msg) {
        fOutStream.addOutputEvent(msg);
        fOutStream.addNewLine();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jcraft.jsch.UserInfo#promptYesNo(java.lang.String)
     */
    @Override
    public boolean promptYesNo(String arg0) {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jcraft.jsch.UserInfo#promptPassword(java.lang.String)
     */
    @Override
    public boolean promptPassword(String arg0) {

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jcraft.jsch.UserInfo#promptPassphrase(java.lang.String)
     */
    @Override
    public boolean promptPassphrase(String arg0) {

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jcraft.jsch.UserInfo#getPassword()
     */
    @Override
    public String getPassword() {

        return fPassword;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jcraft.jsch.UserInfo#getPassphrase()
     */
    @Override
    public String getPassphrase() {

        return null;
    }

}
