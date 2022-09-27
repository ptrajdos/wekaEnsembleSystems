/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionHandler;
import weka.core.Utils;
import weka.core.UtilsPT;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.13.0
 *
 */
public abstract class GeneralCombiner implements OutputCombiner,Serializable, RevisionHandler, OptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 573687151292284645L;
	
	private static double EPS=1E-10;
	
	protected int numExecutionSlots = 1;

	/**
	 * 
	 */

	
	public void checkCompatibility(double[][] rawPredictions, Instance testInstance) throws Exception{
		int numClasses = testInstance.numClasses();
		if(rawPredictions[0].length != numClasses)
			throw new Exception("Test instance and the rawPredictions are incompatible");
	}
	
	public void checkCompatibility(double[][] rawPredictions, Instance testInstance, double[] weights) throws Exception{
		int numClasses = testInstance.numClasses();
		if(rawPredictions[0].length != numClasses)
			throw new Exception("Test instance and the rawPredictions are incompatible");
		if(rawPredictions.length != weights.length)
			throw new Exception("Test instance and the weights are incompatible");
	}
	
	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombiner#getCombinedDistributionForInstance(double[][], weka.core.Instance)
	 */
	@Override
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance)
			throws Exception {
			int numModels = rawPredictions.length;
			double[] weights = new double[numModels];
			Arrays.fill(weights, 1.0);
			
		return this.getCombinedDistributionForInstance(rawPredictions, testInstance, weights);
	}
	
	
	
	/* (non-Javadoc)
	 * @see weka.core.RevisionHandler#getRevision()
	 */
	@Override
	public String getRevision() {
		return "1.13.0";
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombiner#getCombinedDistributionForInstance(weka.classifiers.Classifier[], weka.core.Instance)
	 */
	@Override
	public double[] getCombinedDistributionForInstance(Classifier[] committee, Instance testInstance) throws Exception {
		int comSize = committee.length;
		double[] weights = new double[comSize];
		Arrays.fill(weights, 1.0);
				
		return this.getCombinedDistributionForInstance(committee, testInstance, weights);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombiner#getCombinedDistributionForInstance(weka.classifiers.Classifier[], weka.core.Instance, double[])
	 */
	@Override
	public double[] getCombinedDistributionForInstance(Classifier[] committee, Instance testInstance, double[] weights)
			throws Exception {
		int commSize = committee.length;
		int numClasses = testInstance.numClasses();
		double[][] predictions = new double[commSize][];
		if(this.numExecutionSlots == 1) {
			for(int c =0 ;c<commSize;c++) {
				/**
				 * Ignore classifiers with small weights
				 */
				if(weights[c]>EPS)
					predictions[c] = committee[c].distributionForInstance(testInstance);
				else {
					predictions[c] = new double[numClasses];
				}
			}
		}else {
			int numCores = (this.numExecutionSlots <= 0) ? Runtime.getRuntime().availableProcessors(): this.numExecutionSlots;
			ExecutorService executorPool = Executors.newFixedThreadPool(numCores);

			final CountDownLatch doneSignal = new CountDownLatch(commSize);
			final Instance testInstancePass = testInstance;
			Future<double[]>[] futures = new Future[commSize];
			
			for(int i=0;i<commSize;i++) {
				final Classifier currentClassifier = committee[i];
		        if (currentClassifier == null)
		          continue;
		        
		        final int iteration = i;
		        
		        Callable<double[]> newTask = new Callable<double[]>() {

					@Override
					public double[] call() throws Exception {
						double[] prediction = currentClassifier.distributionForInstance(testInstancePass);
						doneSignal.countDown();
						
						return prediction;
					}
		        };
		        futures[i] = executorPool.submit(newTask);
			}
			
			doneSignal.await();
		    executorPool.shutdownNow();
		    
		    for(int i=0;i<futures.length;i++) {
		    	predictions[i] = futures[i].get();
		    }

		}
		
		return this.getCombinedDistributionForInstance(predictions, testInstance, weights);
	}
	
	

	public void normalizeOutput(double[] output){
		if(Utils.eq(Utils.sum(output), 0)){
			return;
		}
		Utils.normalize(output);
	}

	/**
	 * @return the numExecutionSlots
	 */
	public int getNumExecutionSlots() {
		return numExecutionSlots;
	}
	
	public String numExecutionSlotsTipText() {
		return "The number of execution slots to use. If less or equall zero, then use number of cores";
	}

	/**
	 * @param numExecutionSlots the numExecutionSlots to set
	 */
	public void setNumExecutionSlots(int numExecutionSlots) {
		this.numExecutionSlots = numExecutionSlots;
	}

	@Override
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(1);
		
		 newVector.addElement(new Option(
			      "\tNum Execution Slots to use with parallel calculations"+
		          "(default:" + 1 + ").\n",
			      "ES", 1, "-ES"));
		 
		    
		return newVector.elements();
	}

	@Override
	public void setOptions(String[] options) throws Exception {

		this.setNumExecutionSlots(UtilsPT.parseIntegerOption(options, "ES", 1));
		
	}

	@Override
	public String[] getOptions() {
		
		Vector<String> options = new Vector<String>();

	    options.add("-ES");
	    options.add(UtilsPT.getClassAndOptions( this.getNumExecutionSlots() ));
	    
	    return options.toArray(new String[0]);

	}

}
