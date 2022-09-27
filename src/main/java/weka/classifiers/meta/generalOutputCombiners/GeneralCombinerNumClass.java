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
import weka.core.Utils;
import weka.core.UtilsPT;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.13.0
 *
 */
public abstract class GeneralCombinerNumClass implements OutputCombinerNumClass, Serializable, OptionHandler {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8725511799618036682L;
	
	private static double EPS=1E-10;
	
	protected int numExecutionSlots = 1;

	public void checkCompatibility(double[] predictions,Instance testInstance, double[] weights) throws Exception{
		if (!testInstance.classAttribute().isNumeric())throw new Exception("The class is not a numeric one");
		if(predictions.length != weights.length)throw new Exception("Weights and predictions must have the same length");
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass#getClass(weka.classifiers.Classifier[], weka.core.Instance)
	 */
	@Override
	public double getClass(Classifier[] committee, Instance testInstance) throws Exception {
		
		double[] weights = new double[committee.length];
		Arrays.fill(weights, 1.0);
		
		return this.getClass(committee, testInstance, weights);
	}

	/* (non-Javadoc)
	 * @see weka.classifiers.meta.generalOutputCombiners.OutputCombinerNumClass#getClass(weka.classifiers.Classifier[], weka.core.Instance, double[])
	 */
	@Override
	public double getClass(Classifier[] committee, Instance testInstance, double[] weights) throws Exception {
		double[] outputs = new double[committee.length];
		int commSize = committee.length;
		
		if(this.numExecutionSlots == 1) {
		
		for(int c=0;c<committee.length;c++)
			if(weights[c]>EPS)
				outputs[c]=committee[c].classifyInstance(testInstance);
			else
				outputs[c]=Utils.missingValue();
		}else {
			
			int numCores = (this.numExecutionSlots <= 0) ? Runtime.getRuntime().availableProcessors(): this.numExecutionSlots;
			ExecutorService executorPool = Executors.newFixedThreadPool(numCores);

			final CountDownLatch doneSignal = new CountDownLatch(commSize);
			final Instance testInstancePass = testInstance;
			Future<Double>[] futures = new Future[commSize];
			
			for(int i=0;i<commSize;i++) {
				
				final Classifier currentClassifier = committee[i];
		        if (currentClassifier == null)
		          continue;
		        
		        final int iteration = i;
		        
		        Callable<Double> newTask = new Callable<Double>() {

					@Override
					public Double call() throws Exception {
						double value = currentClassifier.classifyInstance(testInstancePass);
						doneSignal.countDown();
						 
						return  value;
					}
				};
				futures[i] = executorPool.submit(newTask);
				
			}
			doneSignal.await();
		    executorPool.shutdownNow();
		    
		    for(int i=0;i<futures.length;i++) {
		    	outputs[i] =futures[i].get().doubleValue();
		    }

			
		}
		
		return this.getClass(outputs, testInstance,weights);
	}

	/**
	 * @return the numExecutionSlots
	 */
	public int getNumExecutionSlots() {
		return numExecutionSlots;
	}

	/**
	 * @param numExecutionSlots the numExecutionSlots to set
	 */
	public void setNumExecutionSlots(int numExecutionSlots) {
		this.numExecutionSlots = numExecutionSlots;
	}
	
	public String numExecutionSlotsTipText() {
		return "The number of execution slots to use. If less or equall zero, then use number of cores";
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
