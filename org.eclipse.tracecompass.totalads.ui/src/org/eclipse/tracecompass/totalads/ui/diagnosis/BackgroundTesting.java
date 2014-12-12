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
package org.eclipse.tracecompass.totalads.ui.diagnosis;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmOutStream;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmUtility;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmUtilityResultsListener;
import org.eclipse.tracecompass.totalads.algorithms.Results;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.ui.diagnosis.Messages;
import org.eclipse.tracecompass.totalads.ui.modeling.BackgroundModeling;
import org.eclipse.tracecompass.totalads.ui.results.ResultsAndFeedback;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tracecompass.totalads.ui.io.ProgressConsole;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.*;//.Stopwatch;

/**
 * This class evaluates models on traces by running in background as a
 * thread
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 */

public class BackgroundTesting implements Runnable, IAlgorithmUtilityResultsListener {
    private String fTestDirectory;
    private ITraceTypeReader fTraceReader;
    private String[] fModels;
    private Button fBtnAnalysisEvaluateModels;
    private Button fBtnStop;
    private ResultsAndFeedback fResultsAndFeedback;

    /**
     * Constructor to create an object of BackgroundTesting
     *
     * @param testDirectory
     *            Test directory
     * @param traceReader
     *            Trace reader
     * @param modelNames
     *            Database names or model names
     * @param btnEvaluate
     *            Evaluate button
     * @param btnStop
     *            Stop Button
     * @param resultsAndFeedback
     *            An object to display results
     */
    public BackgroundTesting(String testDirectory, ITraceTypeReader traceReader,  String[] modelNames,
            Button btnEvaluate, Button btnStop, ResultsAndFeedback resultsAndFeedback) {
        this.fTestDirectory = testDirectory;
        this.fTraceReader = traceReader;
        this.fModels = modelNames;
        this.fBtnAnalysisEvaluateModels = btnEvaluate;
        this.fBtnStop = btnStop;
        this.fResultsAndFeedback = resultsAndFeedback;
        this.fResultsAndFeedback.registerAllModelNames(modelNames);

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        String msg = null;
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {

            ProgressConsole console = new ProgressConsole(Messages.BackgroundTesting_ConsoleTitle);
            console.println(Messages.BackgroundTesting_ConsoleStartMessage);
            AlgorithmOutStream outStreamAlg = new AlgorithmOutStream();
            outStreamAlg.addObserver(console);

            File []fileList=new File(fTestDirectory).listFiles();

            if (fileList==null){
                throw new TotalADSGeneralException(Messages.BackgroundTesting_NoFiles);
            }else  if (fileList.length > 15000) {
                throw new TotalADSGeneralException(Messages.BackgroundTesting_TraceLimit);
            }

            AlgorithmUtility.testModels(fTestDirectory, fTraceReader, fModels, outStreamAlg,this);

            fResultsAndFeedback.setTotalTraceCount(Integer.toString(fileList.length));

            stopwatch.stop();
            Long elapsedMins = stopwatch.elapsed(TimeUnit.MINUTES);
            Long elapsedSecs = stopwatch.elapsed(TimeUnit.SECONDS);
            console.println(NLS.bind(Messages.BackgroundTesting_TotalTime, elapsedMins.toString() , elapsedSecs));

        } catch (TotalADSGeneralException ex) {// handle UI exceptions here
            // UI exceptions are simply notifications--no need to log them
            if (ex.getMessage() == null) {
                msg = Messages.BackgroundTesting_GeneralException;
            } else {
                msg = ex.getMessage();
            }
        } catch (TotalADSDBMSException ex) {// handle IDataAccessObject
                                            // exceptions here
            if (ex.getMessage() == null) {
                msg = Messages.BackgroundTesting_CommonException;
            }
            else {
                msg = Messages.BackgroundTesting_DBMSException + ex.getMessage();
            }
            Logger.getLogger(BackgroundModeling.class.getName()).log(Level.WARNING, msg, ex);
        } catch (TotalADSReaderException ex) {// handle Reader exceptions here
            if (ex.getMessage() == null) {
                msg = Messages.BackgroundTesting_CommonException;
            } else {
                msg = Messages.BackgroundTesting_ReaderException + ex.getMessage();
            }
            Logger.getLogger(BackgroundModeling.class.getName()).log(Level.WARNING, msg, ex);
        } catch (Exception ex) { // handle all other exceptions here and log
                                 // them too
            if (ex.getMessage() == null) {
                msg = Messages.BackgroundTesting_CommonException;
            } else {
                msg = ex.getMessage();
            }
            Logger.getLogger(BackgroundTesting.class.getName()).log(Level.SEVERE, msg, ex);
            // An exception could be thrown due to unavailability of the db,
            // make sure that the connection is not lost
            DBMSFactory.INSTANCE.verifyConnection();
            // We don't have to worry about exceptions here as the above
            // function handles all the exceptions
            // and just returns a message. This function also initializes
            // connection info to a correct value
            // We cannot write above function under ConnectinException block
            // because such exception is never thrown
            // and Eclipse starts throwing errors
        } finally {

            final String exception = msg;

            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    String msgTitle="TotalADS"; //$NON-NLS-1$
                    if (exception != null) { // If there has been any exception
                                             // then show its message
                        MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                msgTitle, exception);

                    } else {
                        MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                msgTitle,Messages.BackgroundTesting_CompletionMessage);

                    }

                    fBtnAnalysisEvaluateModels.setEnabled(true);
                    fBtnStop.setEnabled(false);
                }
            });

            if (stopwatch.isRunning()) {
                stopwatch.stop();
            }
        }// End of finally
    }// end of function

    /*
     * (non-Javadoc)
     * @see org.eclipse.tracecompass.totalads.algorithms.IAlgorithmUtilityResultsListener#listenTestResults(java.lang.String, java.util.HashMap, java.util.HashMap)
     */
	@Override
	public void listenTestResults(String traceName,	HashMap<String, Results> modelsAndResults,
			HashMap<String, Double> modelsAndAnomalyCount) {

		 fResultsAndFeedback.setTotalAnomalyCount(modelsAndAnomalyCount);
         fResultsAndFeedback.addTraceResult(traceName, modelsAndResults);


	}

    // End of BackgroundTesting class
}
