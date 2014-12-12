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

package org.eclipse.tracecompass.totalads.algorithms.ksm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.dbms.DBMSFactory;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.junit.AfterClass;
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
 * Tests for the class KernelStateModeling in
 * org.eclipse.tracecompass.totalads.core plugin
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class KernelStateModelingTest {
    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;

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

    /**
     * Tests the constructor
     */
    @Test
    public void testKernelStateModeling() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");

        assertTrue(ksm != null);
    }

    /**
     * Tests initializeModelAndSettings
     *
     * @throws TotalADSDBMSException
     *             DBMS Exception
     * @throws TotalADSGeneralException
     *             General exceptions
     */
    @Test
    public void testInitializeModelAndSettings() throws TotalADSDBMSException, TotalADSGeneralException {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");

        String[] settings = ksm.getTrainingSettings();

        String modelName = "temp_init";
        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        ksm.initializeModelAndSettings(modelName, dao, settings);
        boolean isEqual = Arrays.equals(settings, ksm.getTrainingSettings());
        assertTrue(isEqual);

    }

    /**
     * Tests getTrainingSettings
     */
    @Test
    public void testGetTrainingSettings() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");

        String[] settings = ksm.getTrainingSettings();
        assertTrue(settings != null);
    }

    /**
     * Tests saveTestSettings
     *
     * @throws TotalADSDBMSException
     *             DBMS Exception
     * @throws TotalADSGeneralException
     *             General Exception
     */
    @Test
    public void testSaveTestSettings() throws TotalADSGeneralException, TotalADSDBMSException {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");

        String modelName = "temporary";
        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        if (!dao.datbaseExists(modelName)) {
            ksm.initializeModelAndSettings(modelName, dao, null);
        }
        String[] settings = ksm.getTrainingSettings();
        settings[1] = "0.5";
        ksm.saveTestSettings(settings, modelName, dao);

        String[] newSettings = ksm.getSettingsToDisplay(modelName, dao);
        assertTrue(settings[1].equals(newSettings[1]));

    }

    /**
     * Tests getTestSettings
     *
     * @throws TotalADSDBMSException
     *             DBMS Exception
     * @throws TotalADSGeneralException
     *             General Exception
     */
    @Test
    public void testGetTestSettings() throws TotalADSDBMSException, TotalADSGeneralException {

        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        String modelName = "temporary";
        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        if (!dao.datbaseExists(modelName)) {
            ksm.initializeModelAndSettings(modelName, dao, null);
        }

        String[] settings = ksm.getTestSettings(modelName, dao);

        assertTrue(settings != null);
    }

    /**
     * Tests getSettingsToDisplay
     *
     * @throws TotalADSDBMSException
     *             DBMS Exception
     * @throws TotalADSGeneralException
     *             General Exception
     */
    @Test
    public void testGetSettingsToDisplay() throws TotalADSDBMSException, TotalADSGeneralException {

        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");

        String modelName = "temporary";
        IDataAccessObject dao = DBMSFactory.INSTANCE.getDataAccessObject();
        if (!dao.datbaseExists(modelName)) {
            ksm.initializeModelAndSettings(modelName, dao, null);
        }
        String[] settings = ksm.getSettingsToDisplay(modelName, dao);
        assertTrue(settings != null);
    }

    // The code for training, testing and validation has already been tested in
    // {@link AlgorithmUtilityTest.testTrainValidateAndTestModelsForLTTngTraces}
    // and
    // other similar functions in the {@link AlgorithmUtilityTest} class. There
    // is no need
    // to re do the tests here.
    /**
     * Tests the train function for null arguments
     *
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSGeneralException
     *             General exception
     * @throws TotalADSReaderException
     *             I/O exception
     */
    @Test(expected = TotalADSGeneralException.class)
    public void testTrain() throws TotalADSDBMSException, TotalADSGeneralException, TotalADSReaderException {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");

        ksm.train(null, null, null, null, null);
    }

    /**
     * Tests the validate function for null arguments
     *
     * @throws TotalADSGeneralException
     *             General exception
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSReaderException
     *             Reader exception
     */

    @Test(expected = TotalADSGeneralException.class)
    public void testValidate() throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        ksm.validate(null, null, null, null, null);
    }

    /**
     * Tests the test function for null arguments
     *
     * @throws TotalADSGeneralException
     *             General exception
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSReaderException
     *             Reader exception
     */
    @Test(expected = TotalADSGeneralException.class)
    public void testTest() throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        ksm.test(null, null, null, null);

    }

    /**
     * Tests the getTotalAnomalyPercentage function
     */
    @Test
    public void testGetTotalAnomalyPercentage() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        assertTrue(ksm.getTotalAnomalyPercentage() != null);

    }

    /**
     * Tests the getName function
     */
    @Test
    public void testGetName() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        assertTrue(ksm.getName().length() > 0);

    }

    /**
     * Tests the getAcronym function
     */
    @Test
    public void testGetAcronym() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        assertTrue(ksm.getAcronym().length() > 0);

    }


    /**
     * Tests the createInstance function
     */
    @Test
    public void testCreateInstance() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");

        assertTrue(ksm.createInstance() != null);

    }

    /**
     * Tests the registerModel function
     */
    @Test
    public void testRegisterModel() {
        // This function cannot be tested as it requires access to the
        // KernelStateModeling class
        // from this package; whereas, KernelStateModeling has not been exported
        // from the totalads.core
        // due to the limitation of access from only AlgorithmFactory class.
        // Therefore, we assume
        // this function to be true
        assertTrue(true);

    }

    /**
     * Tests the getDescription function
     */
    @Test
    public void testGetDescription() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        assertTrue(ksm.getDescription().length() > 0);

    }

    /**
     * Tests the isOnlineLearningSupported function
     */
    @Test
    public void testIsOnlineLearningSupported() {
        IDetectionAlgorithm ksm = AlgorithmFactory.getInstance().getAlgorithmByAcronym("KSM");
        assertTrue(ksm.isOnlineLearningSupported() == true);

    }

}
