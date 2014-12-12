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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSNetException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

/**
 * This class connects to a remote system using SSH and collects an LTTng trace
 * Some of the code in this class is extracted from the following link:
 * http://www.jcraft.com/fJsch/examples/Sudo.java.html
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class SSHConnector {

    private JSch fJsch;
    private Integer fPort;
    private UserInfo fUi;
    private String fUser;
    private String fHost;
    private Session fSession;
    private IAlgorithmOutStream fOutStream;

    private Integer fSnapshotDuration;

    /**
     * Constructor
     */
    public SSHConnector() {
        fJsch = new JSch();

    }

    /**
     * Returns the path of the downloaded LTTng trace from the remote system
     *
     * @return The path of a trace
     *
     *         public String getTrace(){ return fTotalADSLocalDir; }
     */
    /**
     * Opens an SSH connection using a password, executes LTTng commands on a
     * remote system and
     *
     * @param userAtHost
     *            User and host as user@hostname
     * @param password
     *            Password
     * @param portToConnect
     *            Port
     * @param outStream
     *            An object which will print the output
     * @param snapshotDurationInSeconds
     *            Snapshot duration in seconds
     * @throws TotalADSNetException
     *             A network related exception
     */
    public void openSSHConnectionUsingPassword(String userAtHost, String password, Integer portToConnect,
            IAlgorithmOutStream outStream, Integer snapshotDurationInSeconds) throws TotalADSNetException {
        try {

            fPort = portToConnect;
            fUser = userAtHost.substring(0, userAtHost.indexOf('@'));
            fHost = userAtHost.substring(userAtHost.indexOf('@') + 1);
            this.fOutStream = outStream;
            this.fSnapshotDuration = snapshotDurationInSeconds;
            // password will be given via UserInfo interface.
            fUi = new UserInfoSSH(password, outStream);
            fSession = fJsch.getSession(fUser, fHost, fPort);

            fSession.setUserInfo(fUi);
            fSession.connect();
            outStream.addOutputEvent(Messages.SSHConnector_ConnectionEstablish);
            outStream.addNewLine();

        } catch (JSchException e) {
            throw new TotalADSNetException(Messages.SSHConnector_CommunicationError + e.getMessage());
        }
    }

    /**
     * Connects with an SSH server using a private Key file present on the hard
     * disk (Public name should be present on the server)
     *
     * @param userAtHost
     *            User and host (user@host)
     * @param pathToPrivateKey
     *            Private Key
     * @param portToConnect
     *            Port
     * @param outStream
     *            An outStream which will print the output
     * @param snapshotDurationInSeconds
     *            Snapshot Duration
     * @throws TotalADSNetException
     *             A network related exception
     */
    public void openSSHConnectionUsingPrivateKey(String userAtHost, String pathToPrivateKey, Integer portToConnect,
            IAlgorithmOutStream outStream, Integer snapshotDurationInSeconds) throws TotalADSNetException {
        try {

            fPort = portToConnect;
            fUser = userAtHost.substring(0, userAtHost.indexOf('@'));
            fHost = userAtHost.substring(userAtHost.indexOf('@') + 1);
            this.fOutStream = outStream;
            this.fSnapshotDuration = snapshotDurationInSeconds;

            fJsch.addIdentity(pathToPrivateKey);
            outStream.addOutputEvent(Messages.SSHConnector_IdentityAdded);
            outStream.addNewLine();
            fSession = fJsch.getSession(fUser, fHost, fPort);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no"); //$NON-NLS-1$ //$NON-NLS-2$
            fSession.setConfig(config);
            fSession.setUserInfo(fUi);
            fSession.connect();
            outStream.addOutputEvent(Messages.SSHConnector_ConnectionEstablish);
            outStream.addNewLine();

        } catch (JSchException e) {
            throw new TotalADSNetException(Messages.SSHConnector_CommunicationError + e.getMessage());
        }
    }

    /**
     *
     * @param traceStorageDir
     *            Directory to store traces
     * @return Path to a trace
     * @throws TotalADSNetException
     *             Network exception
     */
    public String collectATrace(String traceStorageDir) throws TotalADSNetException {

        String totalADSRemoteDir = "/tmp/totalads"; //$NON-NLS-1$
        String totalADSRemoteTrace = totalADSRemoteDir + "/kernel/"; //$NON-NLS-1$
        String sessionName = "totalads-trace-" + getCurrentTimeStamp(); //$NON-NLS-1$
        // If an exception occurs, don't execute further commands and let the
        // exception be thrown
        executeCommand("rm -rf " + totalADSRemoteTrace); //$NON-NLS-1$
        executeCommand("mkdir -p " + totalADSRemoteDir); //$NON-NLS-1$
        executeCommand("lttng create " + sessionName + " -o " + totalADSRemoteDir); //$NON-NLS-1$ //$NON-NLS-2$
        executeCommand("lttng enable-event -a -k"); //$NON-NLS-1$
        executeCommand("lttng start"); //$NON-NLS-1$

        // Wait for these many seconds and then stop the trace
        try {
            fOutStream.addOutputEvent(NLS.bind(Messages.SSHConnector_TraceDuration, fSnapshotDuration));
            fOutStream.addNewLine();
            TimeUnit.SECONDS.sleep(fSnapshotDuration);
        } catch (InterruptedException ee) {
        }

        executeCommand("lttng stop"); //$NON-NLS-1$
        executeCommand("lttng destroy " + sessionName); //$NON-NLS-1$

        String trace = "trace-" + getCurrentTimeStamp(); //$NON-NLS-1$
        File localDir = new File(traceStorageDir + File.separator + trace);
        localDir.mkdir();

        downloadTrace(fSession, totalADSRemoteTrace, localDir.getPath());
        executeCommand("rm -rf " + totalADSRemoteTrace); //$NON-NLS-1$

        return localDir.getPath();
    }

    /**
     * Executes a command
     *
     * @param command
     *            Command to execute
     *
     */

    private void executeCommand(String command) throws TotalADSNetException {
        Channel channel = null;
        String msg = null;
        try {

            channel = fSession.openChannel("exec"); //$NON-NLS-1$
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setErrStream(System.err);

            try (InputStream in = channel.getInputStream(); OutputStream out = channel.getOutputStream()) {
                channel.connect();

                displayStream(in, channel);
            }

        } catch (IOException e) {
            fOutStream.addOutputEvent(Messages.SSHConnector_Error + e.getMessage());
            fOutStream.addNewLine();
            msg = e.getMessage();
        } catch (JSchException e) {
            fOutStream.addOutputEvent(Messages.SSHConnector_Error + e.getMessage());
            fOutStream.addNewLine();
            msg = e.getMessage();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }

        }
        if (msg != null)
        {
            throw new TotalADSNetException(msg);// Don't continue further
        }

    }

    /**
     * Executes LTTng commands on a remote system and downloads the trace in a
     * local folder
     *
     * @param sudoPassword
     *            Sudo password
     * @param traceStorageDir
     *            Trace directory
     * @return Path of the trace
     * @throws TotalADSNetException
     *             A network related exception
     */
    public String collectATrace(String sudoPassword, String traceStorageDir) throws TotalADSNetException {

        String totalADSRemoteDir = "/tmp/totalads"; //$NON-NLS-1$
        String totalADSRemoteTrace = totalADSRemoteDir + "/kernel/"; //$NON-NLS-1$
        String sessionName = "totalads-trace-" + getCurrentTimeStamp(); //$NON-NLS-1$
        // If an exception occurs, don't execute further commands and let the
        // exception be thrown
        executeSudoCommand("sudo -S -p  '' rm -rf " + totalADSRemoteTrace, sudoPassword); //$NON-NLS-1$
        executeSudoCommand("sudo -S -p '' mkdir -p " + totalADSRemoteDir, sudoPassword); //$NON-NLS-1$
        executeSudoCommand("sudo -S -p '' lttng create " + sessionName + " -o " + totalADSRemoteDir, sudoPassword); //$NON-NLS-1$ //$NON-NLS-2$
        executeSudoCommand("sudo -S -p '' lttng enable-event -a -k", sudoPassword); //$NON-NLS-1$
        executeSudoCommand("sudo -S -p '' lttng start", sudoPassword); //$NON-NLS-1$

        // Wait for these many seconds and then stop the trace
        try {
            fOutStream.addOutputEvent(NLS.bind(Messages.SSHConnector_TraceDuration, fSnapshotDuration));
            fOutStream.addNewLine();
            TimeUnit.SECONDS.sleep(fSnapshotDuration);
        } catch (InterruptedException ee) {
        }

        executeSudoCommand("sudo -S -p '' lttng stop", sudoPassword); //$NON-NLS-1$
        executeSudoCommand("sudo -S -p  '' lttng destroy " + sessionName, sudoPassword); //$NON-NLS-1$
        executeSudoCommand("sudo -S -p  '' chmod -R 777 " + totalADSRemoteDir, sudoPassword); //$NON-NLS-1$

        String trace = "trace-" + getCurrentTimeStamp(); //$NON-NLS-1$
        File localDir = new File(traceStorageDir + File.separator + trace);
        localDir.mkdir();

        downloadTrace(fSession, totalADSRemoteTrace, localDir.getPath());
        executeSudoCommand("sudo -S -p  '' rm -rf " + totalADSRemoteTrace, sudoPassword); //$NON-NLS-1$

        return localDir.getPath();

    }

    /**
     * Executes a sudo (root) command
     *
     * @param command
     * @param sudoPass
     *
     */
    private void executeSudoCommand(String command, String sudoPass) throws TotalADSNetException {
        Channel channel = null;
        String msg = null;
        try {

            channel = fSession.openChannel("exec"); //$NON-NLS-1$
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setErrStream(System.err);

            try (InputStream in = channel.getInputStream();
                    OutputStream out = channel.getOutputStream();) {
                channel.connect();
                out.write((sudoPass + "\n").getBytes()); //$NON-NLS-1$
                out.flush();
                displayStream(in, channel);
            }

        } catch (IOException e) {
            fOutStream.addOutputEvent(Messages.SSHConnector_Error + e.getMessage());
            fOutStream.addNewLine();
            msg = e.getMessage();
        } catch (JSchException e) {
            fOutStream.addOutputEvent(Messages.SSHConnector_Error + e.getMessage());
            fOutStream.addNewLine();
            msg = e.getMessage();
        } finally {

            if (channel != null) {
                channel.disconnect();
            }

        }

        if (msg != null)
        {
            throw new TotalADSNetException(msg);// Don't continue further
        }
    }

    /**
     * Display the output of a command on a remote system
     *
     * @param in
     * @param channel
     * @throws IOException
     */
    private void displayStream(InputStream in, Channel channel) throws IOException {

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                // System.out.print(new String(tmp, 0, i));
                fOutStream.addOutputEvent(new String(tmp, 0, i));
                fOutStream.addNewLine();
            }

            if (channel.isClosed()) {
                if (in.available() > 0) {
                    continue;
                }
                fOutStream.addOutputEvent(Messages.SSHConnector_ExitStatus + channel.getExitStatus());
                fOutStream.addNewLine();
                break;
            }
            try {
                Thread.sleep(1000); // Wait for some time to get more data over
                                    // network stream
            } catch (Exception ex) {
            }
        }
    }

    /**
     * This functions downloads the trace collected at the remote system
     *
     * @param fSession
     * @param remoteFolder
     * @throws IOException
     */
    private void downloadTrace(Session session, String remoteFolder, String localDownloadFolder) throws TotalADSNetException {
        ChannelSftp sftpChannel = null;
        try {

            sftpChannel = (ChannelSftp) session.openChannel("sftp"); //$NON-NLS-1$
            sftpChannel.connect();
            downloadRecursively(sftpChannel, localDownloadFolder, remoteFolder);
        } catch (SftpException e) {
            fOutStream.addOutputEvent(Messages.SSHConnector_Error + e.getCause().getMessage()); // Exception
            // printed
            fOutStream.addNewLine();
            throw new TotalADSNetException(e);// Don't continue further
        } catch (JSchException e) {
            fOutStream.addOutputEvent(Messages.SSHConnector_Error + e.getCause().getMessage()); // Exception
            // printed
            fOutStream.addNewLine();
            throw new TotalADSNetException(e);// Don't continue further
        } finally {
            if (sftpChannel != null) {
                sftpChannel.exit();
            }
        }

    }

    /**
     * Recursively downloads folders, sub folders and all the files in it
     *
     * @param sftpChannel
     * @param localDownloadFolder
     * @param remoteFolder
     * @throws SftpException
     */
    private void downloadRecursively(ChannelSftp sftpChannel, String localDownloadFolder, String remoteFolder) throws SftpException {
        sftpChannel.cd(remoteFolder);
        java.util.Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*"); //$NON-NLS-1$

        for (ChannelSftp.LsEntry entry : list) {
            fOutStream.addOutputEvent(Messages.SSHConnector_ProcessRemote + entry.getFilename()); // actually
            // Downloading
            fOutStream.addNewLine();

            if (entry.getAttrs().isDir()) {// check for a folder
                fOutStream.addOutputEvent(Messages.SSHConnector_Downloading + entry.getFilename());
                fOutStream.addNewLine();

                File subDir = new File(localDownloadFolder + File.separator + entry.getFilename());
                subDir.mkdir();
                String subRemoteDir = remoteFolder + File.separator + entry.getFilename();

                // recursively download all the contents of sub folders
                downloadRecursively(sftpChannel, subDir.getPath(), subRemoteDir);

                sftpChannel.cd(remoteFolder); // Change back to current
                                              // directory

            } else {
                sftpChannel.get(entry.getFilename(), localDownloadFolder + File.separator + entry.getFilename());
            }
        }

    }

    /**
     * Closes the SSH connection and clears the trace from the local drive
     */
    public void close() {
        // deleteFolderContents(new File(this.totalADSLocalDir));
        fSession.disconnect();
    }

    /**
     * Get current time stamp
     */
    private static String getCurrentTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss"); //$NON-NLS-1$
        // get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);

    }

}
