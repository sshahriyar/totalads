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

package org.eclipse.tracecompass.totalads.algorithms;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.tracecompass.internal.totalads.readers.ctfreaders.CTFLTTngSysCallTraceReader;
import org.eclipse.tracecompass.internal.totalads.ssh.SSHConnector;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSNetException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmUtilityResultsListener;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.algorithms.Messages;
import org.eclipse.tracecompass.totalads.algorithms.Results;
import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;

import com.google.common.base.Stopwatch;

/**
 * This is the utility class to execute algorithms by implementing common
 * recurring tasks required by all the algorithms.
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class AlgorithmUtility {
    private static volatile boolean fIsExecuting = true;

    private AlgorithmUtility() {

    }

    /**
     * Creates a model in the database with training settings
     *
     * @param modelName
     *            Model name
     * @param algorithm
     *            Algorithm type
     * @param trainingSettings
     *            Training Settings
     * @throws TotalADSDBMSException
     *             An exception related to DBMS
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     */
    public static void createModel(String modelName, IDetectionAlgorithm algorithm, String[] trainingSettings) throws TotalADSDBMSException, TotalADSGeneralException {
        if (modelName == null || modelName.isEmpty()) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_EmptyModel);
        }

        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        if (dao == null || !dao.isConnected()) {
            throw new TotalADSDBMSException(Messages.AlgorithmUtility_NoDB);
        }
        if (algorithm == null) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_NullAlgorithm);
        }

        String model = modelName + "_" + algorithm.getAcronym(); //$NON-NLS-1$
        model = model.toUpperCase();
        algorithm.initializeModelAndSettings(model, dao, trainingSettings);
    }

    /**
     * Returns the algorithm for a given model name
     *
     * @param modelName
     *            Name of the model
     * @return An object of type IDetectionAlgorithm
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     */
    public static IDetectionAlgorithm getAlgorithmFromModelName(String modelName) throws TotalADSGeneralException {
        if (modelName == null) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_NullModel);
        }
        String[] modelParts = modelName.split("_"); //$NON-NLS-1$
        if (modelParts == null || modelParts.length < 2) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_InvalidModel);
        }

        String algorithmAcronym = modelParts[1];
        IDetectionAlgorithm algorithm = AlgorithmFactory.getInstance().getAlgorithmByAcronym(algorithmAcronym);
        if (algorithm == null) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_InvalidModelTotalADS);
        }

        return algorithm;
    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // //////// Training and Validation
    // ///////////////////////////////////////////////////////////////////////////////////
    /**
     * This function trains and validate models
     *
     * @param trainDirectory
     *            Train Directory
     * @param validationDirectory
     *            Validation Directory
     * @param traceReader
     *            Trace Reader
     * @param modelsNames
     *            Names of models as an array
     * @param outStream
     *            Output stream where the algorithm would display its output
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     * @throws TotalADSDBMSException
     *             An exception related to DBMS
     * @throws TotalADSReaderException
     *             An exception related to the trace reader
     */
    public static void trainAndValidateModels(String trainDirectory, String validationDirectory, ITraceTypeReader traceReader,
            String[] modelsNames, IAlgorithmOutStream outStream)
            throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {

        if (trainDirectory == null || validationDirectory == null || traceReader == null
                || modelsNames == null || outStream == null) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_NullArguments);
        }

        if (trainDirectory.isEmpty() || validationDirectory.isEmpty()) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_EmptyDirectories);
        }

        IDataAccessObject dataAcessObject = DBMSFactory.INSTANCE.getDataAccessObject();
        if (!dataAcessObject.isConnected()) {
            throw new TotalADSDBMSException(Messages.AlgorithmUtility_NoDB);
        }
        for (int i = 0; i < modelsNames.length; i++) {
            if (dataAcessObject.datbaseExists(modelsNames[i]) == false) {
                throw new TotalADSDBMSException(NLS.bind(Messages.AlgorithmUtility_NoDBofTypeFound, modelsNames[i]));
            }
        }

        Stopwatch stopwatch = Stopwatch.createStarted();

        for (int i = 0; i < modelsNames.length; i++) {
            Boolean isLastTrace = false;
            String modelName = modelsNames[i];
            outStream.addOutputEvent(NLS.bind(Messages.AlgorithmUtility_ModelingOn, modelName));
            outStream.addNewLine();

            // //////////////////
            // /File verifications of traces
            // /////////////////
            // Check for valid trace type reader and training traces before
            // creating a database
            // Get a file handler

            File fileList[] = getDirectoryHandler(trainDirectory, traceReader);
            try (ITraceIterator it = traceReader.getTraceIterator(fileList[0])) {

            } catch (TotalADSReaderException ex) {
                stopwatch.stop();
                String message = Messages.AlgorithmUtility_InvalidTrainingTraces + ex.getMessage();
                throw new TotalADSGeneralException(message);
            }

            // Check for valid trace type reader and validation traces before
            // creating a database
            File validationFileList[] = getDirectoryHandler(validationDirectory, traceReader);
            try (ITraceIterator it = traceReader.getTraceIterator(validationFileList[0]);) {

            } catch (TotalADSReaderException ex) {
                stopwatch.stop();
                String message = Messages.AlgorithmUtility_InvalidValidationTraces + ex.getMessage();
                throw new TotalADSGeneralException(message);
            }

            // /////////
            // Start training
            // ////////
            outStream.addOutputEvent(Messages.AlgorithmUtility_ModelTraining);
            outStream.addNewLine();

            IDetectionAlgorithm algorithm = getAlgorithmFromModelName(modelName);

            for (int trcCnt = 0; trcCnt < fileList.length; trcCnt++) {

                if (trcCnt == fileList.length - 1) {
                    isLastTrace = true;
                }
                // Get the trace
                try (ITraceIterator trace = traceReader.getTraceIterator(fileList[trcCnt])) {

                    outStream.addOutputEvent(NLS.bind(Messages.AlgorithmUtility_CurrentTrainingTrace, (trcCnt + 1), fileList[trcCnt].getName()));
                    outStream.addNewLine();
                    algorithm.train(trace, isLastTrace, modelName, dataAcessObject, outStream);
                }
                // Check if user has asked to stop modeling
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }

            // Start validation
            validateModels(validationFileList, traceReader, algorithm, modelName, outStream, dataAcessObject);
            // Check if user has asked to stop modeling
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }

        stopwatch.stop();
        Long elapsedMins = stopwatch.elapsed(TimeUnit.MINUTES);
        Long elapsedSecs = stopwatch.elapsed(TimeUnit.SECONDS);
        String msg = NLS.bind(Messages.AlgorithmUtility_TotalTime, elapsedMins, elapsedSecs);
        outStream.addOutputEvent(msg);
        outStream.addNewLine();
    }

    /**
     * This functions validates a model for a given database of that model
     *
     * @param fileList
     *            Array of files
     * @param traceReader
     *            trace reader
     * @param algorithm
     *            Algorithm object
     * @param database
     *            Database name
     * @param outStream
     *            console object
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     * @throws TotalADSReaderException
     *             An exception related to the trace reader
     * @throws TotalADSDBMSException
     *             An exception related to the DBMS
     */
    private static void validateModels(File[] fileList, ITraceTypeReader traceReader, IDetectionAlgorithm algorithm,
            String database, IAlgorithmOutStream outStream, IDataAccessObject dao) throws TotalADSGeneralException, TotalADSReaderException,
            TotalADSDBMSException {

        // process now
        outStream.addOutputEvent(Messages.AlgorithmUtility_Validation);
        outStream.addNewLine();
        Boolean isLastTrace = false;

        for (int trcCnt = 0; trcCnt < fileList.length; trcCnt++) {
            // Check if user has asked to stop modeling
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            // get the trace
            if (trcCnt == fileList.length - 1) {
                isLastTrace = true;
            }

            try (ITraceIterator trace = traceReader.getTraceIterator(fileList[trcCnt])) {
                outStream.addOutputEvent(NLS.bind(Messages.AlgorithmUtility_CurrentValidationTrace, (trcCnt + 1), fileList[trcCnt].getName()));
                outStream.addNewLine();
                algorithm.validate(trace, database, dao, isLastTrace, outStream);
            }

        }

    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // / Test models
    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Tests algorithms against a set of traces in a folder
     *
     * @param testDirectory
     *            Test directory
     * @param traceReader
     *            Trace reader
     * @param models
     *            Model names
     * @param outStream
     *            Output stream to print the output
     * @param resultListener
     *            The resultListener object will receives messages about the
     *            results in real time
     * @return Model names and the total number of anomalies found in the test
     *         folder for each model
     * @throws TotalADSGeneralException
     *             General exception (usually validation errors)
     * @throws TotalADSReaderException
     *             Exception related to trace reading
     * @throws TotalADSDBMSException
     *             Exception related to database
     *
     */
    public static HashMap<String, Double> testModels(String testDirectory, ITraceTypeReader traceReader,
            String[] models, IAlgorithmOutStream outStream, IAlgorithmUtilityResultsListener resultListener) throws TotalADSGeneralException, TotalADSReaderException, TotalADSDBMSException {
        // First verify selections
        Integer totalFiles;
        if (testDirectory == null || traceReader == null || models == null || outStream == null ||
                resultListener == null) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_NullArguments);
        }

        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        if (!dao.isConnected()) {
            throw new TotalADSDBMSException(Messages.AlgorithmUtility_NoDB);
        }

        for (int i = 0; i < models.length; i++) {
            if (dao.datbaseExists(models[i]) == false) {
                throw new TotalADSDBMSException(NLS.bind(Messages.AlgorithmUtility_NoDBofTypeFound, models[i]));
            }
        }

        if (testDirectory.isEmpty()) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_EmptyTestDir);
        }

        // Get a file and a db handler
        File fileList[] = getDirectoryHandler(testDirectory, traceReader);

        // Check for valid trace type reader and traces before creating a
        // fDatabase
        try (ITraceIterator it = traceReader.getTraceIterator(fileList[0])) {

        } catch (TotalADSReaderException ex) {
            // this is just a validation error, cast it to UI exception
            String message = NLS.bind(Messages.Algorithms_InvalidTrace, ex.getMessage());
            throw new TotalADSGeneralException(message);
        }
        // Second, get all the algorithm instances related to models
        IDetectionAlgorithm[] algorithm = new IDetectionAlgorithm[models.length];

        for (int i = 0; i < models.length; i++) {
            algorithm[i] = getAlgorithmFromModelName(models[i]);
        }

        // Third, start testing

        totalFiles = fileList.length;

        HashMap<String, Double> modelsAndAnomalyCount = new HashMap<>();
        int anomCount = 0;

        // for each trace
        for (int trcCnt = 0; trcCnt < totalFiles; trcCnt++) {// totalFiles

            outStream.addOutputEvent(NLS.bind(Messages.Algorithms_TraceCountMessage, trcCnt, fileList[trcCnt]));
            outStream.addNewLine();
            // for each selected model
            HashMap<String, Results> modelResults = new HashMap<>();
            final String traceName = fileList[trcCnt].getName();

            for (int modelCnt = 0; modelCnt < models.length; modelCnt++) {

                outStream.addOutputEvent(NLS.bind(Messages.Algorithms_ModelEval, models[modelCnt]));
                outStream.addNewLine();

                try (ITraceIterator trace =
                        traceReader.getTraceIterator(fileList[trcCnt])) {// get
                                                                         // the
                                                                         // trace

                    Results results = algorithm[modelCnt].test(trace, models[modelCnt], dao, outStream);
                    modelResults.put(models[modelCnt], results);

                }
                // get total anomalies so far for each instance of the algorithm
                Double totalAnoms = algorithm[modelCnt].getTotalAnomalyPercentage();
                modelsAndAnomalyCount.put(models[modelCnt], totalAnoms);
                // update the listener about the results of te trace
                resultListener.listenTestResults(traceName, modelResults, modelsAndAnomalyCount);

                // Check if Executor has been stopped by the user
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

            }

            // Check if Executor has been stopped by the user
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

        }

        outStream.addNewLine();
        outStream.addOutputEvent(NLS.bind(Messages.AlgorithmUtility_anomalies, anomCount));
        outStream.addNewLine();
        return modelsAndAnomalyCount;

    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////
    // /Online/Live Testing and Training
    // ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function to start online modeling. Once started, it will continuously
     * collect a trace from a remote system, and train and test models. This
     * function must be launched in a new thread otherwise it will block the
     * running thread. To stop it call stopOnlineModeling function.
     *
     * @param userAtHost
     *            User and host name in the format user@hostname
     * @param password
     *            Password
     * @param port
     *            Port number
     * @param snapshotDuration
     *            Time to collect trace
     * @param intervalBetweenSnapshots
     *            Interval between the start of collection of two traces
     * @param directoryToStoreTraces
     *            directory to store traces
     * @param models
     *            Model (database) names
     * @param outStream
     *            Output stream to display messages of processing
     * @param resultListener
     *            Result listener to display results in the real time
     * @param isTrainAndEval
     *            True for both training and testing, or false for only testing
     * @throws TotalADSGeneralException
     *             Validation exception of parameters
     */
    public static synchronized void startOnlineModeling(String userAtHost, String password, Integer port,
            Integer snapshotDuration, Integer intervalBetweenSnapshots,
            String directoryToStoreTraces, String[] models, IAlgorithmOutStream outStream,
            IAlgorithmUtilityResultsListener resultListener, Boolean isTrainAndEval)
            throws TotalADSGeneralException {

        if (userAtHost == null || password == null || port == null || snapshotDuration == null ||
                intervalBetweenSnapshots == null || directoryToStoreTraces == null ||
                models == null || outStream == null || resultListener == null || isTrainAndEval == null) {
            throw new TotalADSGeneralException(Messages.AlgorithmUtility_NullArguments);
        }

        SSHConnector ssh = new SSHConnector();
        outStream.addOutputEvent(Messages.AlgorithmUtility_StartSsh);
        outStream.addNewLine();
        try {
            // Connecting to SSH

            ssh.openSSHConnectionUsingPassword(userAtHost, password, port, outStream, snapshotDuration);

            ITraceTypeReader traceReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);

            while (fIsExecuting) {

                String tracePath = ssh.collectATrace(directoryToStoreTraces);
                outStream.addOutputEvent(Messages.AlgorithmUtility_StartTest);
                outStream.addNewLine();
                testModels(tracePath, traceReader, models, outStream, resultListener);

                if (isTrainAndEval) {// if it is both training and evaluation
                    outStream.addOutputEvent(Messages.AlgorithmUtility_StartTrain);
                    outStream.addNewLine();

                    trainModels(traceReader, tracePath, models, outStream);
                }
            }
        } catch (TotalADSGeneralException | TotalADSReaderException |
                TotalADSNetException | TotalADSDBMSException ex) {
            outStream.addOutputEvent(ex.getMessage());
            outStream.addNewLine();
        } finally {
            ssh.close();
        }

    }

    /**
     * Stops Online Modeling that was started by calling startOnlineModeling
     *
     * @param outStream
     *            OuputStream to display processing messages
     */
    public static synchronized void stopOnlineModeling(IAlgorithmOutStream outStream) {
        fIsExecuting = false;
        outStream.addOutputEvent(Messages.AlgorithmUtility_StopMonitor);
        outStream.addNewLine();
        outStream.addOutputEvent(Messages.AlgorithmUtility_TimeToStopMonitor);
        outStream.addNewLine();
        outStream.addOutputEvent(Messages.AlgorithmUtility_Wait);
        outStream.addNewLine();

    }

    /**
     * Trains already existing models on a trace. Used by startOnlineModeling
     * function
     *
     * @param traceReader
     *            Trace reader
     * @param tracePath
     *            Trace Path
     * @param models
     *            Model list
     * @param outStream
     *            Output stream to display messages
     * @throws TotalADSGeneralException
     *             Validation exception
     * @throws TotalADSDBMSException
     *             DBMS related exception
     * @throws TotalADSReaderException
     *             Reader related exception
     */
    private static void trainModels(ITraceTypeReader traceReader, String tracePath, String[] models, IAlgorithmOutStream outStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {

        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        if (!dao.isConnected()) {
            throw new TotalADSDBMSException(Messages.AlgorithmUtility_NoDB);
        }

        for (int i = 0; i < models.length; i++) {
            // Check if Executor has been stopped by the user
            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            IDetectionAlgorithm algorithm = getAlgorithmFromModelName(models[i]);
            try (ITraceIterator traceIterator =
                    traceReader.getTraceIterator(new File(tracePath))) {

                outStream.addOutputEvent(NLS.bind(Messages.AlgorithmUtility_UpdateModelSpecific, models[i], algorithm.getName()));
                outStream.addNewLine();
                outStream.addOutputEvent(Messages.AlgorithmUtility_UpdateModel);
                outStream.addNewLine();
                algorithm.train(traceIterator, true, models[i], dao, outStream);
            }
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // File Handling functions
    // ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param directory
     *            The name of the directory
     * @param traceReader
     *            An object of the trace reader
     * @return An array list of traces suited for the appropriate type
     * @throws TotalADSGeneralException
     *             An exception related to the validation of parameters
     */

    private static File[] getDirectoryHandler(String directory, ITraceTypeReader traceReader) throws TotalADSGeneralException {
        File traces = new File(directory);

        CTFLTTngSysCallTraceReader kernelReader = new CTFLTTngSysCallTraceReader();
        if (traceReader.getAcronym().equals(kernelReader.getAcronym())) {
            return getDirectoryHandlerforLTTngTraces(traces);
        }
        return getDirectoryHandlerforTextTraces(traces);
    }

    /**
     * Get an array of trace list for a directory or just one file handler if
     * there is only one file
     *
     * @param traces
     *            File object representing traces
     * @return the file handler to the correct path
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     */
    private static File[] getDirectoryHandlerforTextTraces(File traces) throws TotalADSGeneralException {

        File[] fileList;

        if (traces.isDirectory()) { // if it is a directory return the list of
                                    // all files
            Boolean isAllFiles = false, isAllFolders = false;
            fileList = traces.listFiles();
            for (File file : fileList) {

                if (file.isDirectory()) {
                    isAllFolders = true;
                } else if (file.isFile()) {
                    isAllFiles = true;
                }

                if (isAllFolders) {
                    throw new TotalADSGeneralException(NLS.bind(Messages.AlgorithmUtility_FolderContainsDirectories, traces.getName()));
                }

            }

            if (!isAllFiles && !isAllFolders) {
                throw new TotalADSGeneralException(Messages.AlgorithmUtility_EmptyDir + traces.getName());
            }

        }
        else {// if it is a single file return the single file; however, this
              // code will never be reached
              // as in GUI we are only using a directory handle, but if in
              // future we decide to make changes then this could come handy
            fileList = new File[1];
            fileList[0] = traces;
        }

        return fileList;
    }

    /**
     * Gets an array of list of directories
     *
     * @param traces
     *            File object representing traces
     * @return Handler to the correct path of files
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     */
    private static File[] getDirectoryHandlerforLTTngTraces(File traces) throws TotalADSGeneralException {

        if (traces.isDirectory()) {
            File[] fileList = traces.listFiles();
            File[] fileHandler;
            Boolean isAllFiles = false, isAllFolders = false;

            for (File file : fileList) {

                if (file.isDirectory()) {
                    if (!file.getName().equalsIgnoreCase("index")) { //$NON-NLS-1$
                        isAllFolders = true;
                    }
                } else if (file.isFile()) {
                    isAllFiles = true;
                }

                if (isAllFiles && isAllFolders) {
                    throw new TotalADSGeneralException(NLS.bind(Messages.AlgorithmUtility_FolderContainsMixture, traces.getName()));

                }

            }
            // if it has reached this far
            if (!isAllFiles && !isAllFolders) {
                throw new TotalADSGeneralException(NLS.bind(Messages.AlgorithmUtility_EmptyDir, traces.getName()));
            } else if (isAllFiles) { // return the name of folder as a trace
                fileHandler = new File[1];
                fileHandler[0] = traces;
            } else {
                fileHandler = fileList;
            }

            return fileHandler;

        }
        // this code may not be reached
        throw new TotalADSGeneralException(NLS.bind(Messages.AlgorithmUtility_SelectFolder, traces.getName()));
    }

}
