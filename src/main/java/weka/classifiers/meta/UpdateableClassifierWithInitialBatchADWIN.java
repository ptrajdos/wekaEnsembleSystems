/**
 * 
 */
package weka.classifiers.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import moa.classifiers.core.driftdetection.ADWIN;
import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.UpdateableBatchProcessor;
import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.core.UtilsPT;
import weka.tools.GlobalInfoHandler;

/**
 * The class implements a wrapper for Updateable classifier that needs initial training batch
 * @author pawel trajdos
 * @since 1.9.0
 * @version 1.9.0
 *
 */
public class UpdateableClassifierWithInitialBatchADWIN extends SingleClassifierEnhancer
		implements UpdateableBatchProcessor, UpdateableClassifier, GlobalInfoHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6361729013079348002L;
	
	/**
	 * The size of the initial bath.
	 * Used for initial training.
	 */
	protected int initialBatchSize=100;
	
	/**
	 * ADWIN estimator used to predict the size of the window.
	 */
	protected ADWIN adwin = new ADWIN();
	
	/**
	 * Determines whether the classifier is in the training mode.
	 */
	protected boolean training=true;
	
	/**
	 * Batch of instances for training purposes.
	 */
	protected Instances batch;
	
	protected int numProcessedInstances;
	


	/**
	 * 
	 */
	public UpdateableClassifierWithInitialBatchADWIN() {
		super();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.Classifier#buildClassifier(weka.core.Instances)
	 */
	@Override
	public void buildClassifier(Instances data) throws Exception {
		int dataSize=data.size();
		/**
		 * Initialization with empty data is treated as dataset initialization only. 
		 */
		if(  dataSize == 0 ) {
				this.batch = new Instances(data,0);
				return;
		}
		
		this.m_Classifier.buildClassifier(data);
		this.training=false;
		
		/**
		 * Save training set as batch
		 */
		this.batch = new Instances(data);
		this.initialiseAdwin();
		
		this.numProcessedInstances=0;
		
		if(this.m_Classifier instanceof UpdateableBatchProcessor)
			((UpdateableBatchProcessor) this.m_Classifier).batchFinished();
		
		
	}
	
	protected void initialiseAdwin() throws Exception {
		Instances tmpValSet = new Instances(this.batch);
		int tmpValSize = tmpValSet.size();
		for(int i=0;i<tmpValSize;i++) 
			this.adwinInstanceCheck(tmpValSet.get(i));
			
		
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.UpdateableClassifier#updateClassifier(weka.core.Instance)
	 */
	@Override
	public void updateClassifier(Instance instance) throws Exception {
		if(this.batch == null) {
			this.batch = new Instances(instance.dataset(),0);
		}
		
		if(this.training) {
			
			this.batch.add(instance);
			this.numProcessedInstances++;
			if(this.numProcessedInstances == this.initialBatchSize)
				this.buildClassifier(this.batch);
			
		}else {
			this.adwinInstanceCheck(instance);
			if(this.m_Classifier instanceof UpdateableClassifier) 	
				((UpdateableClassifier) this.m_Classifier).updateClassifier(instance);			
		}

	}
	protected void adwinInstanceCheck(Instance instance) throws Exception {
		boolean isPredictionCorrect = Utils.eq(instance.classValue(), this.classifyInstance(instance));
		if(this.adwin.setInput(isPredictionCorrect? 0:1)) {
			this.trimValidationSetADWIN();
		}
		
	}
	
	protected void trimValidationSetADWIN() throws Exception {
		int adwinSize = this.adwin.getWidth();
		int valSetSize =this.batch.size();
		if(adwinSize< valSetSize) {
			for(int i=0;i< valSetSize - adwinSize;i++) 
				this.batch.remove(0);
			this.m_Classifier.buildClassifier(this.batch);
		}
		
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.UpdateableBatchProcessor#batchFinished()
	 */
	@Override
	public void batchFinished() throws Exception {
		if(this.training) {
			this.buildClassifier(this.batch);
		}

	}

	/* (non-Javadoc)
	 * @see weka.classifiers.SingleClassifierEnhancer#listOptions()
	 */
	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tSize of the initial batch"+
		          "(default:" + 100  + ").\n",
			      "IBS", 1, "-IBS"));
		 
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.SingleClassifierEnhancer#setOptions(java.lang.String[])
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		this.setInitialBatchSize(UtilsPT.parseIntegerOption(options, "IBS", 100));
		super.setOptions(options);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.SingleClassifierEnhancer#getOptions()
	 */
	@Override
	public String[] getOptions() {
		Vector<String> options = new Vector<String>();
	    

	    options.add("-IBS");
	    options.add(""+ this.getInitialBatchSize());
	    
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}


	

	/**
	 * @return the initialBatchSize
	 */
	public int getInitialBatchSize() {
		return this.initialBatchSize;
	}

	/**
	 * @param initialBatchSize the initialBatchSize to set
	 */
	public void setInitialBatchSize(int initialBatchSize) {
		this.initialBatchSize = initialBatchSize;
	}

	public String initialBatchSizeTipText() {
		return "Size of the initial batch.";
	}

	

	/* (non-Javadoc)
	 * @see weka.classifiers.AbstractClassifier#distributionForInstance(weka.core.Instance)
	 */
	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		if(this.training) {
			/**
			 * Default response if the classifier is not trained yet. 
			 */
			int numClasses = instance.numClasses();
			double[] distribution = new double[numClasses];
			double prediction = 1.0/numClasses;
			Arrays.fill(distribution, prediction);
			return distribution;
		}
		return this.m_Classifier.distributionForInstance(instance);
	}

	@Override
	public String globalInfo() {
		return "Wrapper for an updateable classifier that needs initial training";
	}
	
	
	
	
	

}
