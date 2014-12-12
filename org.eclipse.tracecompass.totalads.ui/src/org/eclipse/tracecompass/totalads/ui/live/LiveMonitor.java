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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmUtility;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.ui.io.DirectoryBrowser;
import org.eclipse.tracecompass.totalads.ui.live.BackgroundLiveMonitor;
import org.eclipse.tracecompass.totalads.ui.live.LiveXYChart;
import org.eclipse.tracecompass.totalads.ui.live.Messages;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * This class creates the GUI elements/widgets for live diagnosis using SSH.
 * LTTng tracing can be started on a system, and both live training and testing
 * can be done simultaneously.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class LiveMonitor {

    private Text fTxtUserAtHost;
    private Combo fCmbSnapshot;
    private Combo fCmbInterval;
    private Text fTxtPort;
    private Text fPassword;
    private ResultsAndFeedback fResultsAndFeedback;
    private Button fBtnStart;
    private Button fBtnStop;
    private Text fTxtTraces;
    private BackgroundLiveMonitor fLiveExecutor;
    private LiveXYChart fLiveChart;
    private Button fBtnTrainingAndEval;
    private Button fBtnTesting;
    private HashSet<String> fModelsList;
    private String msgTitle="TotalADS"; //$NON-NLS-1$
    /**
     * Constructor for the Live Monitor
     */
    public LiveMonitor() {
        fModelsList = new HashSet<>();
    }

    /**
     * Creates GUI widgets
     *
     * @param compParent
     *            Composite
     */
    public void createControls(Composite compParent) {

        ScrolledComposite scrolCompAnom = new ScrolledComposite(compParent, SWT.H_SCROLL | SWT.V_SCROLL);
        Composite comptbItmLive = new Composite(scrolCompAnom, SWT.NONE);

        // Designing the Layout of the GUI Items for the LiveMonitor Tab Item
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.horizontalSpan = 1;
        comptbItmLive.setLayoutData(gridData);
        comptbItmLive.setLayout(new GridLayout(2, false));

        // /////////////////////////////////////////////////////////////////////////
        // Creating GUI widgets for SSH parameters
        // /////////////////////////////////////////////////////////////////

        // Create GUI elements for SSH Configuration
        selectHostUsingSSH(comptbItmLive);
        trainingAndTesting(comptbItmLive);
        traceStorage(comptbItmLive);

        // ////////////////////////////////////////////////////////////////////
        // Creating GUI widgets for buttons
        // ////////////////////////////////////////////////////////////////

        Composite compButtons = new Composite(comptbItmLive, SWT.NONE);
        compButtons.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
        compButtons.setLayout(new GridLayout(5, false));

        fBtnStart = new Button(compButtons, SWT.BORDER);
        fBtnStart.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 1, 1));
        fBtnStart.setText(Messages.LiveMonitor_Start);

        fBtnStop = new Button(compButtons, SWT.BORDER);
        fBtnStop.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
        fBtnStop.setText(Messages.LiveMonitor_Stop);
        fBtnStop.setEnabled(false);

        // Adjust settings for scrollable LiveMonitor Tab Item
        scrolCompAnom.setContent(comptbItmLive);
        // Set the minimum size
        scrolCompAnom.setMinSize(200, 200);
        // Expand both horizontally and vertically
        scrolCompAnom.setExpandHorizontal(true);
        scrolCompAnom.setExpandVertical(true);
        addHandlers();
    }

    /**
     * Creates GUI widgets for a selection of traces and trace types
     *
     * @param compParent
     *            Composite of LiveMonitor
     */
    private void selectHostUsingSSH(Composite compParent) {
        /**
         * Group trace selection
         */
        Group grpSSHConfig = new Group(compParent, SWT.NONE);
        grpSSHConfig.setText(Messages.LiveMonitor_SshConfig);

        grpSSHConfig.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 2));
        grpSSHConfig.setLayout(new GridLayout(2, false));

        // /////////////////////////////////////////////
        // /User name, password, and port
        // ////////////////////////////////////////////
        Composite compUserPasPort = new Composite(grpSSHConfig, SWT.NONE);
        compUserPasPort.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        compUserPasPort.setLayout(new GridLayout(3, false));

        Label userAtHost = new Label(compUserPasPort, SWT.NONE);
        userAtHost.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
        userAtHost.setText(Messages.LiveMonitor_UserAtHost);

        Label lblPassword = new Label(compUserPasPort, SWT.NONE);
        lblPassword.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
        lblPassword.setText(Messages.LiveMonitor_Password);

        Label lblPort = new Label(compUserPasPort, SWT.NONE);
        lblPort.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
        lblPort.setText(Messages.LiveMonitor_Port);

        fTxtUserAtHost = new Text(compUserPasPort, SWT.BORDER);
        fTxtUserAtHost.setEnabled(true);
        fTxtUserAtHost.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        fTxtUserAtHost.setText(System.getProperty("user.name") + "@localhost"); //$NON-NLS-1$ //$NON-NLS-2$

        fPassword = new Text(compUserPasPort, SWT.BORDER | SWT.PASSWORD);
        fPassword.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        fPassword.setText("123456"); //$NON-NLS-1$

        fTxtPort = new Text(compUserPasPort, SWT.BORDER);
        fTxtPort.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        fTxtPort.setText("22"); //$NON-NLS-1$
        // ///////
        // /SSH Password and Private Key: Currently disabling this to provide it
        // in the next version
        // ///////
        /*
         * Group grpPrivacy = new Group(grpSSHConfig, SWT.NONE);
         * grpPrivacy.setText("SSH Password/Pvt. Key");
         *
         * grpPrivacy.setLayoutData(new
         * GridData(SWT.FILL,SWT.FILL,false,false,3,2));
         * grpPrivacy.setLayout(new GridLayout(3,false));
         *
         * btnPassword = new Button(grpPrivacy, SWT.RADIO);
         * btnPassword.setText("Enter Password"); btnPassword.setLayoutData(new
         * GridData(SWT.FILL,SWT.TOP,true, false,1,1));
         * btnPassword.setSelection(true);
         *
         * btnPvtKey = new Button(grpPrivacy, SWT.RADIO);
         * btnPvtKey.setText("Select Private Key"); btnPvtKey.setLayoutData(new
         * GridData(SWT.FILL,SWT.TOP,true, false,2,1));
         *
         *
         * txtPassword=new Text(grpPrivacy,SWT.BORDER|SWT.PASSWORD);
         * txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
         * false,1,1)); txtPassword.setText("grt_654321");
         *
         * txtPvtKey=new Text(grpPrivacy,SWT.BORDER);
         * txtPvtKey.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
         * false,1,1)); //txtPvtKey.setText(""); txtPvtKey.setEnabled(false);
         *
         * trcbrowser=new FileBrowser(grpPrivacy, txtPvtKey, new
         * GridData(SWT.RIGHT, SWT.TOP, false, false));
         * trcbrowser.disableBrowsing();
         */
        // ////////
        // /End SSH password and private name
        // ////
        // /////////////
        // Duration and Port
        // ////////
        Composite compDuration = new Composite(grpSSHConfig, SWT.NONE);
        compDuration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        compDuration.setLayout(new GridLayout(2, false));

        Label lblSnapshotDuration = new Label(compDuration, SWT.NONE);
        lblSnapshotDuration.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
        lblSnapshotDuration.setText(Messages.LiveMonitor_SnapshotDuration);

        Label lblIntervalDuration = new Label(compDuration, SWT.NONE);
        lblIntervalDuration.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1));
        lblIntervalDuration.setText(Messages.LiveMonitor_SnapshotInterval);

        fCmbSnapshot = new Combo(compDuration, SWT.NONE | SWT.READ_ONLY);
        fCmbSnapshot.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
        fCmbSnapshot.add("5"); //$NON-NLS-1$
        fCmbSnapshot.add("10"); //$NON-NLS-1$
        fCmbSnapshot.add("15"); //$NON-NLS-1$
        fCmbSnapshot.add("20"); //$NON-NLS-1$
        fCmbSnapshot.add("35"); //$NON-NLS-1$
        fCmbSnapshot.add("60"); //$NON-NLS-1$
        fCmbSnapshot.select(0);

        fCmbInterval = new Combo(compDuration, SWT.NONE | SWT.READ_ONLY);
        fCmbInterval.setLayoutData(new GridData(SWT.CENTER, SWT.TOP, false, false, 1, 1));
        fCmbInterval.add("0"); //$NON-NLS-1$
        fCmbInterval.add("1"); //$NON-NLS-1$
        fCmbInterval.add("3"); //$NON-NLS-1$
        fCmbInterval.add("5"); //$NON-NLS-1$
        fCmbInterval.add("7"); //$NON-NLS-1$
        fCmbInterval.add("10"); //$NON-NLS-1$
        fCmbInterval.add("15"); //$NON-NLS-1$
        fCmbInterval.add("20"); //$NON-NLS-1$
        fCmbInterval.add("30"); //$NON-NLS-1$
        fCmbInterval.select(0);

        /**
         * End group trace selection
         */
    }

    /**
     * Training and Testing Widgets
     *
     * @param compParent
     *            Parent composite
     */
    public void trainingAndTesting(Composite compParent) {
        // ///////
        // /Training and Evaluation
        // ///////
        Group grpTrainingAndEval = new Group(compParent, SWT.NONE);
        grpTrainingAndEval.setText(Messages.LiveMonitor_EvalType);
        grpTrainingAndEval.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        grpTrainingAndEval.setLayout(new GridLayout(2, false));

        fBtnTrainingAndEval = new Button(grpTrainingAndEval, SWT.NONE | SWT.RADIO);
        fBtnTrainingAndEval.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        fBtnTrainingAndEval.setText(Messages.LiveMonitor_TrainAndTest);

        fBtnTesting = new Button(grpTrainingAndEval, SWT.NONE | SWT.RADIO);
        fBtnTesting.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        fBtnTesting.setText(Messages.LiveMonitor_Testing);
        fBtnTesting.setSelection(true);

    }

    /**
     * Trace storage widgets
     *
     * @param compParent
     *            Composite
     */
    private void traceStorage(Composite compParent) {
        // ///////
        // /Training and Evaluation
        // ///////
        Group grpStorage = new Group(compParent, SWT.NONE);
        grpStorage.setText(Messages.LiveMonitor_StorageDir);
        grpStorage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        grpStorage.setLayout(new GridLayout(3, false));

        fTxtTraces = new Text(grpStorage, SWT.BORDER);
        fTxtTraces.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        new DirectoryBrowser(grpStorage, fTxtTraces, new GridData(SWT.RIGHT, SWT.TOP, false, false));

    }

    /**
     * Handlers
     */
    private void addHandlers() {
        /**
		 *
		 */
        fBtnStart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {

                if (findInvalidSSettings() == false && inValidModel() == false) {
                    fResultsAndFeedback.clearData();

                    String password = ""; //$NON-NLS-1$
                    String privateKey = ""; //$NON-NLS-1$
                    Boolean isTrainAndEval = false;

                    int port = Integer.parseInt(fTxtPort.getText());
                    int snapshotDuration = Integer.parseInt(fCmbSnapshot.getItem(fCmbSnapshot.getSelectionIndex()));
                    int snapshotIntervals = Integer.parseInt(fCmbInterval.getItem(fCmbInterval.getSelectionIndex()));

                    /*
                     * // Will be enabled in the next version if
                     * (btnPassword.getSelection())
                     * password=txtPassword.getText(); else if
                     * (btnPvtKey.getSelection())
                     * privateKey=txtPvtKey.getText();
                     */
                    // so using the following for this version
                    password = fPassword.getText();

                    if (fBtnTrainingAndEval.getSelection()) {
                        isTrainAndEval = true;
                    } else if (fBtnTesting.getSelection()) {
                        isTrainAndEval = false;
                    }

                    fBtnStart.setEnabled(false);
                    fBtnStop.setEnabled(true);

                    fLiveExecutor = new BackgroundLiveMonitor
                            (fTxtUserAtHost.getText(), password, null,
                                    privateKey, port, snapshotDuration, snapshotIntervals, fBtnStart,
                                    fBtnStop, fModelsList, fResultsAndFeedback, fLiveChart, fTxtTraces.getText(),
                                    isTrainAndEval);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(fLiveExecutor);
                    executor.shutdown();
                }

            }

        });
        /**
         * Stop button event handler
         */
        fBtnStop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
                fLiveExecutor.stopMonitoring();
            }
        });

        // **** Pvt name button handler: Will be enabled in the next version
        /*
         * btnPvtKey.addSelectionListener(new SelectionAdapter() {
         *
         * @Override public void widgetSelected(SelectionEvent e) {
         * trcbrowser.enableBrowsing(); txtPvtKey.setEnabled(true);
         * txtPassword.setEnabled(false); txtPassword.setText(""); } });
         */

        // **** Password button name handler
        /*
         * btnPassword.addSelectionListener(new SelectionAdapter() {
         *
         * @Override public void widgetSelected(SelectionEvent e) {
         *
         * txtPvtKey.setEnabled(false); txtPvtKey.setText("");
         * trcbrowser.disableBrowsing(); txtPassword.setEnabled(true); } });
         */

    }

    /**
     * Validates the fields before execution
     *
     * @return
     */
    private Boolean findInvalidSSettings() {

        String msg = ""; //$NON-NLS-1$
        if (fTxtUserAtHost.getText().isEmpty()) {
            msg = Messages.LiveMonitor_EmptyHost;
        } else if (fPassword.getText().isEmpty()) {
            msg = Messages.LiveMonitor_EmptyPassword;
        } else if (fTxtPort.getText().isEmpty()) {
            msg = Messages.LiveMonitor_EmptyPort;
        } else if (fModelsList.size() <= 0) {
            msg = Messages.LiveMonitor_SelModel;
        }
        else if (fTxtTraces.getText().isEmpty()) {
            msg = Messages.LiveMonitor_SelDirForSotrage;
        }
        else {
            try {
                Integer.parseInt(fTxtPort.getText());
            } catch (Exception ex) {
                msg = Messages.LiveMonitor_IntegerPort;
            }
            if (msg.isEmpty()) {
                File file = new File(fTxtTraces.getText() + File.separator + "tmp"); //$NON-NLS-1$
                try {
                    file.createNewFile();
                    file.delete();
                } catch (IOException e) {
                    msg = Messages.LiveMonitor_UnableToWrite;
                }

            }
        }

        if (!msg.isEmpty()) {

            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                     msgTitle,msg);

            return true;
        }
        return false;
    }

    /**
     * Checks whether an algorithm of a model supports online training
     *
     * @return
     */
    private Boolean inValidModel() {

        if (fBtnTrainingAndEval.getSelection() == false) {
            return false;
        }

        String exception = ""; //$NON-NLS-1$
        Iterator<String> it = fModelsList.iterator();
        while (it.hasNext()) {

            String model = it.next();
            try {
                IDetectionAlgorithm algorithm = AlgorithmUtility.getAlgorithmFromModelName(model);
                if (!algorithm.isOnlineLearningSupported()) {
                    exception = NLS.bind(Messages.LiveMonitor_NoOnlineLearnSupport, algorithm.getName());
                    break;

                }
            } catch (TotalADSGeneralException e) {
                exception = e.getMessage();
            }

        }

        if (exception.isEmpty()) {
            return false;
        }

        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                 msgTitle,exception);

        return true;
    }

    /**
     * Sets the chart object
     *
     * @param chart
     *            Chart object
     */
    public void setLiveChart(LiveXYChart chart) {
        fLiveChart = chart;
    }

    /**
     * Sets ResultsAndFeedback object
     *
     * @param results
     *            Results object
     */
    public void setResultsAndFeedback(ResultsAndFeedback results) {

        this.fResultsAndFeedback = results;
    }

    /**
     * Updates the selected model list
     *
     * @param modelsList
     *            Models' list
     */
    public void updateOnModelSelction(HashSet<String> modelsList) {
        this.fModelsList = modelsList;
    }
}
