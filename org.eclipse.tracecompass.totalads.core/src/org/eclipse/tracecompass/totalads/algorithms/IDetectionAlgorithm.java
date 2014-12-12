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

import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.algorithms.Results;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;

/**
 * Each new algorithm or technique should implement the functions of this
 * interface and, in addition, a static function registerAlgorithm
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 * */
public interface IDetectionAlgorithm {

    /**
     * Creates a database (model) along with its collections where an algorithm
     * would store its model
     *
     * @param modelName
     *            Name of the database or the model name
     * @param dataAccessObject
     *            An object of IDataAccessObject
     * @param trainingSettings
     *            Training Settings
     * @throws TotalADSDBMSException
     *             An exception related to the DBMS
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     */
    public void initializeModelAndSettings(String modelName, IDataAccessObject dataAccessObject, String[] trainingSettings) throws TotalADSDBMSException, TotalADSGeneralException;

    /**
     * Returns the training settings/options of an algorithm as: setting name at
     * index i and value at index i+1.
     *
     * @return Array of Strings as options/settings
     */
    public String[] getTrainingSettings();

    /**
     * Returns the testing options/settings of an algorithm as option name at
     * index i and value at index i+1. It takes database name and data access
     * object, in case if the model (database) is already created and previously
     * modified settings exist in the database
     *
     * @param database
     *            Database name
     * @param dataAccessObject
     *            IDataAccessObject object
     * @return An array of String as options/settings
     * @throws TotalADSDBMSException
     *             An exception related to the DBMS
     */
    public String[] getTestSettings(String database, IDataAccessObject dataAccessObject) throws TotalADSDBMSException;

    /**
     * Validates the testing options and saves them into the database. On error
     * throws exception
     *
     * @param options
     *            Settings array
     * @param database
     *            Model(database name)
     * @param dataAccessObject
     *            An object to access database
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     * @throws TotalADSDBMSException
     *             An exception related to the DBMS
     */
    public void saveTestSettings(String[] options, String database, IDataAccessObject dataAccessObject) throws TotalADSGeneralException, TotalADSDBMSException;

    /**
     * Returns settings selected for a model by a user during training and
     * testing. These would be displayed in the properties view.
     *
     * @param database
     *            Model(database) name
     * @param dataAccessObject
     *            An object to access database
     * @return An array of selected settings
     * @throws TotalADSDBMSException
     *             An exception related to the DBMS
     */
    public String[] getSettingsToDisplay(String database, IDataAccessObject dataAccessObject) throws TotalADSDBMSException;

    /**
     * An algorithm will take a trace through this function. Some algorithms can
     * train on the traces as they come and some need to wait till the last
     * trace. Caller will make isLastTrace true when the lastTrace will be sent
     * to this function. This function is called for every trace separately
     *
     * @param trace
     *            Trace iterator to a trace
     * @param isLastTrace
     *            True if the trace is the last trace, else false
     * @param database
     *            Database/mode name
     * @param dataAccessObject
     *            Data access object
     * @param outStream
     *            Use this object to display the events during processing
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     * @throws TotalADSDBMSException
     *             An exception related to DBMS
     * @throws TotalADSReaderException
     *             An exception related to the trace reader
     */
    public void train(ITraceIterator trace, Boolean isLastTrace, String database, IDataAccessObject dataAccessObject, IAlgorithmOutStream outStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException;

    /**
     * This function is called after the train function has finished processing
     * and has built a model. This function is called for every single trace in
     * the validation set separately
     *
     * @param trace
     *            Trace iterator to one trace
     * @param database
     *            Database name
     * @param dataAccessObject
     *            Data Access Object
     * @param isLastTrace
     *            True if the trace is the last trace, else false
     * @param outStream
     *            Use this object to display the events during processing
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     * @throws TotalADSDBMSException
     *             An exception related to DBMS
     * @throws TotalADSReaderException
     *             An exception related to the trace reader
     */
    public void validate(ITraceIterator trace, String database, IDataAccessObject dataAccessObject, Boolean isLastTrace, IAlgorithmOutStream outStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException;

    /**
     * This function evaluates an existing model in the database on the traces
     * in the test set. It is called for every single trace separately.
     *
     * @param trace
     *            Trace iterator to a single trace
     * @param database
     *            Database name
     * @param dataAccessObject
     *            Data access object
     * @param outputStream
     *            Use this object to display the events during processing
     * @return An object of type Result containing the evaluation information of
     *         a trace
     * @throws TotalADSGeneralException
     *             An exception related to validation of parameters
     * @throws TotalADSDBMSException
     *             An exception related to DBMS
     * @throws TotalADSReaderException
     *             An exception related to reader
     */
    public Results test(ITraceIterator trace, String database, IDataAccessObject dataAccessObject, IAlgorithmOutStream outputStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException;

    /**
     * Returns the total anomalies during testing
     *
     * @return total anomalies
     **/
    public Double getTotalAnomalyPercentage();

    /**
     * Returns the graphical result in the form of a chart if any for a trace.
     * Currently unimplemented.
     *
     * @param traceIterator
     *            An iterator a trace
     * @return A chart object
     **/
    public org.swtchart.Chart graphicalResults(ITraceIterator traceIterator);

    /**
     * Returns a self created instance of the algorithm
     *
     * @return An instance of the algorithm
     **/
    public IDetectionAlgorithm createInstance();

    // /////////////////////////////////////////////////////////////////////////////////
    // An algorithm registers itself with the AlgorithmFactory
    // Each derived class must implement the following static method
    // public static void registerAlgorithm() throws TotalADSGeneralException;
    // /////////////////////////////////////////////////////////////////////////////////
    /**
     * Gets the name of the algorithm
     *
     * @return The name
     **/
    public String getName();

    /**
     * Gets the description of an algorithm
     *
     * @return The description
     */
    public String getDescription();

    /**
     * Returns the acronym of the algorithm. This acronym is very important as
     * it facilitates in finding out which algorithm represents the model
     *
     * @return Acronym
     */
    public String getAcronym();

    /**
     * Returns true if online learning is supported. If false is returned it
     * would mean the algorithm can only train in batch mode and live training
     * is not supported
     *
     * @return True if online learning is supported, else false
     */
    public boolean isOnlineLearningSupported();
}