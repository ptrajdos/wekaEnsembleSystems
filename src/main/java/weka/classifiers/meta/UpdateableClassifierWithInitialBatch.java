/**
 * 
 */
package weka.classifiers.meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.SingleClassifierEnhancer;
import weka.classifiers.UpdateableBatchProcessor;
import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.UtilsPT;
import weka.tools.GlobalInfoHandler;

/**
 * The class implements a wrapper for Updateable classifier that needs initial training batch
 * @author pawel trajdos
 * @since 1.3.0
 * @version 1.3.0
 *
 */
public class UpdateableClassifierWithInitialBatch extends SingleClassifierEnhancer
		implements UpdateableBatchProcessor, UpdateableClassifier, GlobalInfoHandler{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6361729013079348002L;
	
	protected int regularBatchSize=100;
	
	protected int initialBatchSize=100;
	
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
	public UpdateableClassifierWithInitialBatch() {
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
		 * Store only header of the dataset
		 */
		this.batch = new Instances(data,0);
		this.numProcessedInstances=0;
		
		if(this.m_Classifier instanceof UpdateableBatchProcessor)
			((UpdateableBatchProcessor) this.m_Classifier).batchFinished();
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
			if(this.m_Classifier instanceof UpdateableClassifier) {
				((UpdateableClassifier) this.m_Classifier).updateClassifier(instance);
			}
			else {
				this.batch.add(instance);
				this.numProcessedInstances++;
				if(this.numProcessedInstances == this.regularBatchSize)
					this.buildClassifier(this.batch);
			}
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
		 
		 newVector.addElement(new Option(
			      "\t Size of the regular batch"+
		          "(default:" + 100  + ").\n",
			      "RBS", 1, "-RBS"));
		 
		 newVector.addAll(Collections.list(super.listOptions()));
		    
		return newVector.elements();
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.SingleClassifierEnhancer#setOptions(java.lang.String[])
	 */
	@Override
	public void setOptions(String[] options) throws Exception {
		
		this.setInitialBatchSize(UtilsPT.parseIntegerOption(options, "IBS", 100));
		
		this.setRegularBatchSize(UtilsPT.parseIntegerOption(options, "RBS", 100));
		
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
	    
	    options.add("-RBS");
	    options.add(""+this.getRegularBatchSize());
	    
	    Collections.addAll(options, super.getOptions());
	    
	    return options.toArray(new String[0]);
	}

	/**
	 * @return the regularBatchSize
	 */
	public int getRegularBatchSize() {
		return this.regularBatchSize;
	}

	/**
	 * @param regularBatchSize the regularBatchSize to set
	 */
	public void setRegularBatchSize(int regularBatchSize) {
		this.regularBatchSize = regularBatchSize;
	}
	
	public String regularBatchSizeTipText() {
		return "Size of the regular batch";
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
