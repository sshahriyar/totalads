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

package org.eclipse.tracecompass.totalads.core.tests.algorithms;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.eclipse.tracecompass.totalads.core.tests.traces.TestTraces;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceTypeReader;
import org.eclipse.tracecompass.totalads.readers.TraceTypeFactory;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmOutStream;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmTypes;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmUtility;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutObserver;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmUtilityResultsListener;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.algorithms.Results;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * Test cases for {@link AlgorithmUtility}. This class acts as entry point to
 * the system and the test cases in it tests most of the functionality in the
 * systems; e.g., executing different algorithms and readers
 *
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class AlgorithmUtilityTest {
    // private static final String DATABASE_NAME = "embedded";

    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

    // /////////////////////////////////////////
    // / Initial setup
    // ///////////////////////////////////////

    /**
     * Initial setup before class
     *
     * @throws TotalADSGeneralException
     *             Validation exception
     * @throws IOException
     *             I/O Exception
     * @throws UnknownHostException
     *             Unknown host exception
     *
     */
    @BeforeClass
    public static void setUpBeforeClass() throws TotalADSGeneralException, UnknownHostException, IOException {
        AlgorithmFactory.getInstance().initialize();

        MongodStarter runtime = MongodStarter.getDefaultInstance();

        mongodExe = runtime.prepare(new MongodConfigBuilder()
                .version(Version.Main.V2_4)
                .net(new Net(12345, Network.localhostIsIPv6()))
                .build());

        mongod = mongodExe.start();
        DBMSFactory.INSTANCE.openConnection("localhost", 12345);
    }

    /**
     * Destroying database instances after the class
     */
    @AfterClass
    public static void tearDownAfterClass() {
        if (mongod != null) {
            DBMSFactory.INSTANCE.closeConnection();
            mongod.stop();
            mongodExe.stop();
        }
        AlgorithmFactory.destroyInstance();
    }

    // //////////////////////////////////
    // / Testing CreateModel
    // /////////////////////////////////
    /**
     * Testing AlgorithmUtility.createModel function. Tests the creation of
     * models of all algorithms.
     *
     */
    @Test
    public void testCreateModel() {

        String modelName = "model";
        IDetectionAlgorithm[] algorithm = AlgorithmFactory.getInstance().
                getAlgorithms(AlgorithmTypes.ANOMALY);

        try {
            for (int i = 0; i < algorithm.length; i++) {

                AlgorithmUtility.createModel(modelName, algorithm[i], algorithm[i].getTrainingSettings());
                IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
                String model = modelName + "_" + algorithm[i].getAcronym();
                model = model.toUpperCase();
                assertTrue(algorithm[i].getName(), dao.datbaseExists(model));
            }

        } catch (TotalADSGeneralException e) {
            assertNull(e);
        } catch (TotalADSDBMSException e) {
            assertNull(e);
        } catch (Exception e) {
            assertNull(e);
        }

    }

    /**
     * Testing AlgorithmUtility.getAlgorithmFromModelName for an empty parameter
     *
     * @throws TotalADSGeneralException
     *             Validation parameter exception
     */
    @Test(expected = TotalADSGeneralException.class)
    public void testGetAlgorithmFromModelNameEmptyModel() throws TotalADSGeneralException {

        AlgorithmUtility.getAlgorithmFromModelName("");

    }

    /**
     * Testing AlgorithmUtility.getAlgorithmFromModelName for an invalid model
     *
     * @throws TotalADSGeneralException
     *             Validation parameter exception
     */
    @Test(expected = TotalADSGeneralException.class)
    public void testGetAlgorithmFromModelNameInvalidModel() throws TotalADSGeneralException {

        AlgorithmUtility.getAlgorithmFromModelName("model_wrongacronym");

    }

    /**
     * Testing AlgorithmUtility.getAlgorithmFromModelName for a valid model
     *
     * @throws TotalADSGeneralException
     *             Validation parameter exception
     */
    @Test
    public void testGetAlgorithmFromModelName() throws TotalADSGeneralException {

        IDetectionAlgorithm algorithm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("SQM");
        String model = "tmp";
        try {
            AlgorithmUtility.createModel(model, algorithm, algorithm.getTrainingSettings());
        } catch (TotalADSDBMSException e) {
            Assume.assumeFalse(true);
        }

        AlgorithmUtility.getAlgorithmFromModelName("TMP_SQM");

    }

    // //////////////////////////////////////////////
    // // Testing trainAndValidateModels and testModels
    // /////////////////////////////////////////////
    /**
     * Testing AlgorithmUtility.trainAndValidateModels for null arguments
     *
     * @throws TotalADSGeneralException
     *             Validation exception
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSReaderException
     *             Reader exception
     */
    @Test(expected = TotalADSGeneralException.class)
    public void testTrainAndValidateModelsForNullArguments() throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {
        String trainDirectory = "traces";
        String validationDirectory = "traces";
        ITraceTypeReader traceReader = null;
        String[] modelName = null;
        AlgorithmOutStream outStream = null;

        AlgorithmUtility.trainAndValidateModels(trainDirectory, validationDirectory,
                traceReader, modelName, outStream);

    }

    /**
     * Testing the function AlgorithmUtility.trainAndValidateModels for
     * LTTngtraces. This function tests all the anomaly detection algorithms and
     * models created using those algorithms on LTTng traces
     *
     * @throws TotalADSGeneralException
     *             Validation exception
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSReaderException
     *             Reader exception
     */

    @Test
    public void testTrainValidateAndTestModelsForLTTngTraces() throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {

        String trainDirectory =  TestTraces.LTTNG_TRACE_DIR.getPath();
        String validationDirectory = TestTraces.LTTNG_TRACE_DIR.getPath();
        String testDirectory = TestTraces.LTTNG_TRACE_DIR.getPath();

        // Get the trace reader for LTTng traces
        ITraceTypeReader traceReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(true);

        // Get all the algorithms for anomaly detection
        IDetectionAlgorithm[] algorithms = AlgorithmFactory.getInstance().getAlgorithms(AlgorithmTypes.ANOMALY);
        String model = "MYLTTNGMODEL";

        trainValidateAndTest(trainDirectory, validationDirectory, testDirectory, traceReader, algorithms, model);
        assertTrue(true);// If it reaches here, then everything went well
    }

    /**
     * Testing the function AlgorithmUtility.trainAndValidateModels for
     * LTTngtraces. This function tests all the anomaly detection algorithms and
     * models created using those algorithms on LTTng traces
     *
     * @throws TotalADSGeneralException
     *             Validation exception
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSReaderException
     *             Reader exception
     */

    @Test
    public void testTrainValidateAndTestModelsForTextTraces() throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {
        String trainDirectory = TestTraces.TXT_TRACE_DIR.getPath();
        String validationDirectory = TestTraces.TXT_TRACE_DIR.getPath();
        String testDirectory = TestTraces.TXT_TRACE_DIR.getPath();

        // Get the simple text based trace reader
        ITraceTypeReader traceReader = TraceTypeFactory.getInstance().getCTFKernelReaderOrSimpleTextReader(false);

        // Get all the algorithms for anomaly detection
        IDetectionAlgorithm[] algorithms = AlgorithmFactory.getInstance().getAlgorithms(AlgorithmTypes.ANOMALY);

        String model = "TXTMODEL";
        trainValidateAndTest(trainDirectory, validationDirectory, testDirectory, traceReader, algorithms, model);
        assertTrue(true);// if it reaches here, then everything went well
    }

    /**
     * Function to train, validate, and test traces on multiple models
     *
     * @param trainDirectory
     *            Training directory
     * @param validationDirectory
     *            Validation directory
     * @param testDirectory
     *            Test directory
     * @param traceReader
     *            Trace reader
     * @param algorithms
     *            Algorithm names
     * @param model
     *            Model name
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSGeneralException
     *             General exception
     * @throws TotalADSReaderException
     *             Reader exception
     */
    private static void trainValidateAndTest(String trainDirectory, String validationDirectory,
            String testDirectory, ITraceTypeReader traceReader, IDetectionAlgorithm[] algorithms,
            String model) throws TotalADSDBMSException, TotalADSGeneralException, TotalADSReaderException {
        // Create models for all the algorithms
        String[] modelNames = new String[algorithms.length];
        for (int j = 0; j < algorithms.length; j++) {

            AlgorithmUtility.createModel(model, algorithms[j], algorithms[j].getTrainingSettings());
            String acronym = algorithms[j].getAcronym();
            modelNames[j] = model + "_" + acronym;

        }

        // All the models are trained and tested one by one
        // Code for training

        AlgorithmOutStream outStream = new AlgorithmOutStream();
        outStream.addObserver(new IAlgorithmOutObserver() {

            @Override
            public void updateOutput(String message) {
                System.out.print(message);

            }
        });
        AlgorithmUtility.trainAndValidateModels(trainDirectory, validationDirectory, traceReader, modelNames, outStream);
        // Code for training ends
        // Code for testing
        IAlgorithmUtilityResultsListener resultListener = new IAlgorithmUtilityResultsListener() {

            @Override
            public void listenTestResults(String traceName,
                    HashMap<String, Results> modelsAndResults,
                    HashMap<String, Double> modelsAndAnomalyCount) {
                System.out.println(traceName);
                for (Map.Entry<String, Results> modRes : modelsAndResults.entrySet()) {
                    System.out.println(modRes.getKey() + " " + modRes.getValue().getAnomaly());
                }

            }
        };
        AlgorithmUtility.testModels(testDirectory, traceReader, modelNames, outStream, resultListener);
        // Code fore testing ends

    }

    // //////////////////////////////////////////////////////////////////////
    // // testing startOnlineModeling and stopOnlineModeling
    // /////////////////////////////////////////////////////////////////////

    /**
     * Testing AlgorithmUtility.startOnlineModeling and
     * AlgorithmUtility.stopOnlineModeling. This function tests the incremental
     * (real time) training and testing on live traces by using SSH. It requires
     * LTTng tracing and SSH server to be enabled on a system. It assumes that
     * the test case passes because SSH or LTTng may not be available in an
     * environment that builds Eclipse. Check for console messages for errors if
     * SSH and LTTng are present
     */
    @Test
    public void testStartOnlineModeling() {
        OnlineModeling online = new OnlineModeling();

        java.util.concurrent.ExecutorService executor = Executors.newSingleThreadExecutor();
        System.out.println("Start online monitor");

        executor.execute(online);
        executor.shutdown();
        testStopOnlineModeling();

    }

    private static void testStopOnlineModeling() {
        AlgorithmOutStream outStream = new AlgorithmOutStream();
        outStream.addObserver(new IAlgorithmOutObserver() {

            @Override
            public void updateOutput(String message) {
                System.out.print(message);

            }
        });
        System.out.println("Stopping online monitor");
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {

        }
        AlgorithmUtility.stopOnlineModeling(outStream);
    }

    /*
     * Inner class to launch a thread for online modeling
     */

    private class OnlineModeling implements Runnable {
        private String userAtHost = "shary@localhost";
        private String password = "pwd";
        private Integer port = 7225;

        public OnlineModeling() {
        }

        @Override
        @Test
        public void run() {

            AlgorithmOutStream outStream = new AlgorithmOutStream();
            outStream.addObserver(new IAlgorithmOutObserver() {

                @Override
                public void updateOutput(String message) {
                    System.out.print(message);

                }
            });

            IAlgorithmUtilityResultsListener resultListener = new IAlgorithmUtilityResultsListener() {

                @Override
                public void listenTestResults(String traceName,
                        HashMap<String, Results> modelsAndResults,
                        HashMap<String, Double> modelsAndAnomalyCount) {

                    System.out.println(traceName);
                    for (Map.Entry<String, Results> modRes : modelsAndResults.entrySet()) {
                        System.out.println("In live modeling: " + modRes.getKey() + " " + modRes.getValue().getAnomaly());
                    }
                }
            };

            String dir = TestTraces.MAIN_TRACE_DIR.getPath()+File.separatorChar+"live";
            try {
                IDetectionAlgorithm algorithm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("SQM");
                String model = "LTTNGMODEL";

                AlgorithmUtility.createModel(model, algorithm, algorithm.getTrainingSettings());

                String[] modelName = { model + "_SQM" };

                File traceDir = new File(dir);
                traceDir.mkdir();

                AlgorithmUtility.startOnlineModeling(userAtHost, password, port,
                        1, 1, dir, modelName, outStream, resultListener, true);

            } catch (TotalADSGeneralException | TotalADSDBMSException e) {

                System.out.println(e.getMessage());

            } finally {
                // clean up directory
                deleteLTTngTraces(new File(dir));
            }

        }

        /**
         * Deletes all the contents of a folder. This function is used to delete
         * an LTTng trace, which is a collection of files in a folder
         *
         * @param folder
         *            Folder name
         */
        private void deleteLTTngTraces(File folder) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteLTTngTraces(f);
                    } else {
                        f.delete();
                    }
                }
            }
            folder.delete();
        }

    }

}
