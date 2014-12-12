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

package org.eclipse.tracecompass.internal.totalads.algorithms.hiddenmarkovmodel;

import java.util.LinkedList;

import org.eclipse.osgi.util.NLS;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmFactory;
import org.eclipse.tracecompass.totalads.algorithms.AlgorithmTypes;
import org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream;
import org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm;
import org.eclipse.tracecompass.totalads.algorithms.Results;
import org.eclipse.tracecompass.totalads.dbms.IDataAccessObject;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSDBMSException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSGeneralException;
import org.eclipse.tracecompass.totalads.exceptions.TotalADSReaderException;
import org.eclipse.tracecompass.totalads.readers.ITraceIterator;
import org.swtchart.Chart;

/**
 * This class implements the Hidden Markov Model for anomaly detection
 *
 * @author <p>
 *         Syed Shariyar Murtaza justsshary@hotmail.com
 *         </p>
 *
 */
public class HiddenMarkovModel implements IDetectionAlgorithm {

    private Integer fSeqLength;
    private HmmMahout fHmm;
    private NameToIDMapper fNameToID;
    private boolean fIsTrainIntialized = false, fIsTestInitialized = false;
    private int fNumStates, fNumSymbols, fNumIterations, fTestNameToIDSize;
    private Double fTotalTestAnomalies = 0.0, fTotalTestTraces = 0.0, fLogThresholdTest = 0.0;
    private LinkedList<Integer> fBatchLargeTrainingSeq;
    private Double fTestTraceMinThreshold;

    /**
     * Constructor
     */
    public HiddenMarkovModel() {

        fNameToID = new NameToIDMapper();
        fSeqLength = 1000;

    }

    /**
     * Self registration of the model with the modelFactory
     *
     * @throws TotalADSGeneralException
     *             Validation exception
     */
    public static void registerAlgorithm() throws TotalADSGeneralException {
        AlgorithmFactory modelFactory = AlgorithmFactory.getInstance();
        HiddenMarkovModel hmm = new HiddenMarkovModel();
        modelFactory.registerModelWithFactory(AlgorithmTypes.ANOMALY, hmm);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * initializeModelAndSettings(java.lang.String,
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject,
     * java.lang.String[])
     */
    @Override
    public void initializeModelAndSettings(String modelName, IDataAccessObject dataAccessObject, String[] trainingSettings) throws TotalADSDBMSException, TotalADSGeneralException {
        String[] setting = null;

        if (trainingSettings != null) {
            setting = new String[trainingSettings.length + 8];
            setting[0] = SettingsCollection.KEY.toString();
            setting[1] = "HMM"; //$NON-NLS-1$
            for (int i = 0; i < trainingSettings.length; i++) {
                setting[i + 2] = trainingSettings[i];
            }
            int idx = trainingSettings.length + 1;
            setting[++idx] = SettingsCollection.NUM_SYMBOLS.toString();
            setting[++idx] = "0"; //$NON-NLS-1$
            setting[++idx] = SettingsCollection.LOG_LIKELIHOOD.toString();
            setting[++idx] = "0.0"; //$NON-NLS-1$
            setting[++idx] = SettingsCollection.SEQ_LENGTH.toString();
            setting[++idx] = fSeqLength.toString();
        } else {

            String[] settings = { SettingsCollection.KEY.toString(), "Hmm", //$NON-NLS-1$
                    SettingsCollection.NUM_STATES.toString(), "5", //$NON-NLS-1$
                    SettingsCollection.NUMBER_OF_ITERATIONS.toString(), "10", //$NON-NLS-1$
                    SettingsCollection.NUM_SYMBOLS.toString(), "0", //$NON-NLS-1$
                    SettingsCollection.LOG_LIKELIHOOD.toString(), "0.0", //$NON-NLS-1$
                    SettingsCollection.SEQ_LENGTH.toString(), fSeqLength.toString() };
            setting = settings;

        }
        HmmMahout hmm = new HmmMahout();
        hmm.verifySaveSettingsCreateDb(setting, modelName, dataAccessObject, true, true);

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * getTrainingOptions()
     */
    @Override
    public String[] getTrainingSettings() {

        String[] trainingSettings = new String[4];
        trainingSettings[0] = SettingsCollection.NUM_STATES.toString();
        trainingSettings[1] = "5"; //$NON-NLS-1$
        trainingSettings[2] = SettingsCollection.NUMBER_OF_ITERATIONS.toString();
        trainingSettings[3] = "10"; //$NON-NLS-1$
        return trainingSettings;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * getTestingOptions(java.lang.String,
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject)
     */
    @Override
    public String[] getTestSettings(String database, IDataAccessObject dataAccessObject) throws TotalADSDBMSException {
        HmmMahout hmm = new HmmMahout();
        String[] settings = hmm.loadSettings(database, dataAccessObject);
        if (settings == null) {
            return null;
        }

        String[] testingSettings = new String[4];
        testingSettings[0] = SettingsCollection.LOG_LIKELIHOOD.toString();
        testingSettings[1] = settings[7]; // probability
        testingSettings[2] = SettingsCollection.SEQ_LENGTH.toString();
        testingSettings[3] = settings[9]; // probability
        return testingSettings;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * saveTestingOptions(java.lang.String[], java.lang.String,
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject)
     */
    @Override
    public void saveTestSettings(String[] options, String database, IDataAccessObject dataAccessObject) throws TotalADSGeneralException, TotalADSDBMSException
    {
        HmmMahout hmm = new HmmMahout();
        hmm.verifySaveSettingsCreateDb(options, database, dataAccessObject, false, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * getSettingsToDisplay()
     */
    @Override
    public String[] getSettingsToDisplay(String database, IDataAccessObject dataAccessObject) throws TotalADSDBMSException {
        HmmMahout hmm = new HmmMahout();
        String[] settings = hmm.loadSettings(database, dataAccessObject);
        return settings;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#train
     * (org.eclipse.tracecompass.totalads.readers.ITraceIterator,
     * java.lang.Boolean, java.lang.String,
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject,
     * org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream)
     */
    @Override
    public void train(ITraceIterator trace, Boolean isLastTrace, String database, IDataAccessObject dataAccessObject, IAlgorithmOutStream outStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {

        if (trace == null || isLastTrace == null || database == null || dataAccessObject == null || outStream == null) {
            throw new TotalADSGeneralException(Messages.HiddenMarkovModel_NullArguments);
        }

        batchTraining(trace, isLastTrace, database, dataAccessObject, outStream);

    }

    /**
     * Trains an HMM on a collection of traces at once; i.e., in a batch
     *
     * @param trace
     *            Trace iterator
     * @param isLastTrace
     *            True if it is the last trace
     * @param database
     *            Database name
     * @param dao
     *            data access object
     * @param outStream
     *            output stream to display message
     * @throws TotalADSGeneralException
     *             Validation exception
     * @throws TotalADSDBMSException
     *             DBMS exception
     * @throws TotalADSReaderException
     *             Reader exception
     */
    private void batchTraining(ITraceIterator trace, Boolean isLastTrace, String database, IDataAccessObject dao, IAlgorithmOutStream outStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {

        if (!fIsTrainIntialized) {
            fHmm = new HmmMahout();

            String[] options = fHmm.loadSettings(database, dao);// get settings
                                                                // from db
            fNumStates = Integer.parseInt(options[1]);
            fNumIterations = Integer.parseInt(options[3]);
            fNameToID.loadMap(dao, database);
            fIsTrainIntialized = true;
            fBatchLargeTrainingSeq = new LinkedList<>();

        }

        outStream.addOutputEvent(Messages.HiddenMarkovModel_ExtractionMsg);
        outStream.addNewLine();

        String event = null;

        while (trace.advance()) {
            event = trace.getCurrentEvent();
            fBatchLargeTrainingSeq.add(fNameToID.getId(event));

        }

        if (isLastTrace) {
            fNumSymbols = fNameToID.getSize();

            outStream.addOutputEvent(Messages.HiddenMarkovModel_BaumWelchMsg);
            outStream.addNewLine();

            int[] seq = new int[fBatchLargeTrainingSeq.size()];
            for (int i = 0; i < fBatchLargeTrainingSeq.size(); i++) {
                seq[i] = fBatchLargeTrainingSeq.get(i);
            }
            fBatchLargeTrainingSeq.clear();// clear memory

            fHmm = trainBaumWelch(seq, fNumStates, fNumSymbols, fNumIterations);

            outStream.addOutputEvent(Messages.HiddenMarkovModel_SaveHMMMsg);
            outStream.addNewLine();
            fHmm.saveHMM(database, dao);

            // Get settings n update them
            String[] settings = new String[2];
            settings[0] = SettingsCollection.NUM_SYMBOLS.toString();
            settings[1] = Integer.toString(fNumSymbols);
            fHmm.verifySaveSettingsCreateDb(settings, database, dao, false, false);

            outStream.addOutputEvent(fHmm.toString());
            outStream.addNewLine();
            // fHmm.saveHMM(database, connection);
            fNameToID.saveMap(dao, database);
        }

    }

    /**
     * Trains using BaumWelch
     *
     * @param seq
     *            Sequence
     * @param numStates
     *            Number of states
     * @param numSymbols
     *            Number of symbols
     * @param numIterations
     *            Number of iteration
     * @return returns a trained HMM
     * @throws TotalADSGeneralException
     *             validation exception
     */
    private HmmMahout trainBaumWelch(int[] seq, int numStates, int numSymbols, int numIterations) throws TotalADSGeneralException {
        try {
            HmmMahout hmm = new HmmMahout();
            hmm.initializeHMMWithCustomizeInitialValues(numSymbols, numStates);
            hmm.learnUsingBaumWelch(numIterations, seq);
            return hmm;

        } catch (Exception ex) {
            if (fNameToID.getSize() > numSymbols) {
                throw new TotalADSGeneralException(Messages.HiddenMarkovModel_EventsOverlaodMsg
                        + Messages.HiddenMarkovModel_HMMErrorMsg);
            }
            throw new TotalADSGeneralException(ex);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#validate
     * (org.eclipse.tracecompass.totalads.readers.ITraceIterator,
     * java.lang.String,
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject,
     * java.lang.Boolean,
     * org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream)
     */
    @Override
    public void validate(ITraceIterator trace, String database, IDataAccessObject dataAccessObject,
            Boolean isLastTrace, IAlgorithmOutStream outStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {

        if (trace == null || isLastTrace == null || database == null || dataAccessObject == null || outStream == null) {
            throw new TotalADSGeneralException(Messages.HiddenMarkovModel_NullArguments);
        }

        int winWidth = 0;
        Double logThreshold;
        String[] options = fHmm.loadSettings(database, dataAccessObject);
        logThreshold = Double.parseDouble(options[7]);
        fSeqLength = Integer.parseInt(options[9]);

        LinkedList<Integer> newSequence = new LinkedList<>();
        outStream.addOutputEvent(Messages.HiddenMarkovModel_ValidationStart);
        outStream.addNewLine();

        Boolean isValidated = false;
        outStream.addOutputEvent(Messages.HiddenMarkovModel_SequenceEvalMsg);
        outStream.addNewLine();

        String event = null;
        while (trace.advance()) {
            event = trace.getCurrentEvent();
            newSequence.add(fNameToID.getId(event));

            winWidth++;
            isValidated = false;
            if (winWidth >= fSeqLength) {
                isValidated = true;
                winWidth--;
                int[] seq = new int[fSeqLength];
                for (int i = 0; i < newSequence.size(); i++) {
                    seq[i] = newSequence.get(i);
                }

                // searching and adding to db
                logThreshold = validationEvaluation(outStream, logThreshold, seq);
                newSequence.remove(0);

            }

        }
        if (!isValidated) {
            int[] seq = new int[fSeqLength];
            for (int i = 0; i < newSequence.size(); i++) {
                seq[i] = newSequence.get(i);
            }
            newSequence.clear();// clear memory

            logThreshold = validationEvaluation(outStream, logThreshold, seq);
        }

        options[7] = logThreshold.toString();

        outStream.addOutputEvent(Messages.HiddenMarkovModel_MinLogLikeliHood + logThreshold.toString());
        outStream.addNewLine();
        outStream.addOutputEvent(Messages.HiddenMarkovModel_ValidationFinished);
        outStream.addNewLine();
        fHmm.verifySaveSettingsCreateDb(options, database, dataAccessObject, false, false);

    }

    /**
     * Performs the evaluation for a likelihood of a sequence during validation
     *
     * @param outStream
     *            Output stream to display messages
     * @param logThreshold
     *            threshold value
     * @param seq
     *            Sequence
     * @return Loglikelihood
     */
    private Double validationEvaluation(IAlgorithmOutStream outStream, Double logThreshold, int[] seq) {
        Double prob = 1.0;
        Double logLikelihood = logThreshold;
        try {
            prob = fHmm.observationLikelihood(seq);

        } catch (Exception ex) {
            outStream.addOutputEvent(Messages.HiddenMarkovModel_ReTrainHMM);
            outStream.addNewLine();

        }

        if (prob < logLikelihood) {
            logLikelihood = prob;
            // console.printTextLn(Arrays.toString(seq));
            outStream.addOutputEvent("Min Log Likelihood Threshold: " + logThreshold.toString()); //$NON-NLS-1$
            outStream.addNewLine();
        }
        return logLikelihood;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#test
     * (org.eclipse.tracecompass.totalads.readers.ITraceIterator,
     * java.lang.String,
     * org.eclipse.tracecompass.totalads.dbms.IDataAccessObject,
     * org.eclipse.tracecompass.totalads.algorithms.IAlgorithmOutStream)
     */
    @Override
    public Results test(ITraceIterator trace, String database, IDataAccessObject dataAccessObject, IAlgorithmOutStream outputStream) throws TotalADSGeneralException, TotalADSDBMSException, TotalADSReaderException {

        if (trace == null || database == null || dataAccessObject == null || outputStream == null) {
            throw new TotalADSGeneralException(Messages.HiddenMarkovModel_NullArguments);
        }

        int winWidth = 0;
        String[] options;

        if (!fIsTestInitialized) {
            fHmm = new HmmMahout();
            options = fHmm.loadSettings(database, dataAccessObject);
            fLogThresholdTest = Double.parseDouble(options[7]);
            fSeqLength = Integer.parseInt(options[9]);
            fHmm.loadHmm(dataAccessObject, database);
            fNameToID.loadMap(dataAccessObject, database);
            fTestNameToIDSize = fNameToID.getSize();
            fIsTestInitialized = true;
        }

        Results results = new Results();
        LinkedList<Integer> newSequence = new LinkedList<>();
        Boolean isTested = false;
        fTotalTestTraces++;
        String event = null;

        outputStream.addOutputEvent(Messages.HiddenMarkovModel_SequenceExtrractionMsg);
        outputStream.addNewLine();
        int seqCount = 1;
        fTestTraceMinThreshold = 0.0;
        while (trace.advance()) {

            event = trace.getCurrentEvent();
            newSequence.add(fNameToID.getId(event));
            winWidth++;
            isTested = false;

            if (winWidth >= fSeqLength) {

                isTested = true;
                winWidth--;
                int[] seq = new int[fSeqLength];
                for (int i = 0; i < newSequence.size(); i++) {
                    seq[i] = newSequence.get(i);
                }

                if (seqCount % 10000 == 0) {
                    outputStream.addOutputEvent(NLS.bind(Messages.HiddenMarkovModel_SpecificSeq, seqCount));
                    outputStream.addNewLine();
                }

                if (testEvaluation(results, fLogThresholdTest, seq) == true) {
                    break;
                }

                newSequence.remove(0);
                seqCount++;
            }

        }
        if (!isTested) {// when it is the last sequence
            int[] seq = new int[fSeqLength];
            for (int i = 0; i < newSequence.size(); i++) {
                seq[i] = newSequence.get(i);
            }
            newSequence.clear();// clear memory

            testEvaluation(results, fLogThresholdTest, seq);
        }

        String logLikelihoodValue = ""; //$NON-NLS-1$
        if (fTestTraceMinThreshold != 0.0) {
            logLikelihoodValue = fTestTraceMinThreshold.toString();
        } else {
            logLikelihoodValue = "NA"; //$NON-NLS-1$
        }

        results.setDetails("\nLog Likelihood: " + logLikelihoodValue + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        outputStream.addOutputEvent("Log Likelihood: " + logLikelihoodValue); //$NON-NLS-1$
        outputStream.addNewLine();
        outputStream.addOutputEvent(Messages.HiddenMarkovModel_Anomaly + results.getAnomaly());
        outputStream.addNewLine();

        if (results.getAnomaly() == true) {
            fTotalTestAnomalies++;
        }

        outputStream.addOutputEvent(Messages.HiddenMarkovModel_Finish);
        outputStream.addNewLine();

        return results;

    }

    /**
     * Helper function for testing
     *
     * @param result
     *            Results
     * @param logThreshold
     *            log likelihood
     * @param seq
     *            sequence
     * @return Return true if anomaly, else returns false
     */
    private boolean testEvaluation(Results result, Double logThreshold, int[] seq) {
        Double loglikelihood = 1.0;
        Double logThresholdValue = logThreshold;
        try {
            loglikelihood = fHmm.observationLikelihood(seq);

        } catch (Exception ex) {
            result.setAnomaly(true);
            if (fNameToID.getSize() > fTestNameToIDSize) {
                Integer diff = fNameToID.getSize() - fTestNameToIDSize;

                if (diff > 100) {
                    result.setDetails(Messages.HiddenMarkovModel_AdditionalEvents);
                } else {
                    result.setDetails(Messages.HiddenMarkovModel_UnkownEvents);
                }
                int eventCount = 0;
                for (int i = fTestNameToIDSize; i < fTestNameToIDSize + diff; i++) {// All
                                                                                    // these
                                                                                    // events
                                                                                    // are
                                                                                    // unknown
                    result.setDetails(fNameToID.getKey(i) + ", "); //$NON-NLS-1$
                    eventCount++;
                    if ((eventCount) % 10 == 0) {
                        result.setDetails("\n"); //$NON-NLS-1$
                    }
                }

            }
            // fTotalTestAnomalies++;
            return true;
        }

        if (loglikelihood < fTestTraceMinThreshold) {
            fTestTraceMinThreshold = loglikelihood;
        }

        if (loglikelihood < logThresholdValue) {
            logThresholdValue = loglikelihood;
            result.setDetails(Messages.HiddenMarkovModel_AnomalousPatterns);

            int firstRange = 10;
            if (seq.length < 10) {
                firstRange = seq.length;
            }
            for (int id = 0; id < firstRange; id++) {
                result.setDetails(fNameToID.getKey(seq[id]) + ", "); //$NON-NLS-1$
            }

            int secondRange = seq.length / 2;
            if (secondRange + 10 < seq.length) {
                result.setDetails("\n.........................................................\n"); //$NON-NLS-1$
                for (int id = secondRange; id < secondRange + 10; id++) {
                    result.setDetails(fNameToID.getKey(seq[id]) + ", "); //$NON-NLS-1$
                }
            }

            int thirdRange = seq.length;
            if (thirdRange - 10 > secondRange + 10) {
                result.setDetails("\n.........................................................\n"); //$NON-NLS-1$
                for (int id = secondRange; id < secondRange + 10; id++) {
                    result.setDetails(fNameToID.getKey(seq[id]) + ", "); //$NON-NLS-1$
                }
            }
            result.setAnomaly(true);

            return true;
        }
        result.setAnomaly(false);
        return false;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * getTotalAnomalyPercentage()
     */
    @Override
    public Double getTotalAnomalyPercentage() {

        Double anomalyPercentage = (fTotalTestAnomalies / fTotalTestTraces) * 100;
        return anomalyPercentage;
    }

    @Override
    public Chart graphicalResults(ITraceIterator traceIterator) {

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * createInstance()
     */
    @Override
    public IDetectionAlgorithm createInstance() {

        return new HiddenMarkovModel();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#getName
     * ()
     */
    @Override
    public String getName() {

        return "Hidden Markov Model (HMM)"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#getAcronym
     * ()
     */
    @Override
    public String getAcronym() {

        return "HMM"; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * getDescription()
     */
    @Override
    public String getDescription() {
        return Messages.HiddenMarkovModel_Description;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.tracecompass.totalads.algorithms.IDetectionAlgorithm#
     * isOnlineLearningSupported()
     */
    @Override
    public boolean isOnlineLearningSupported() {

        return false;
    }

}
