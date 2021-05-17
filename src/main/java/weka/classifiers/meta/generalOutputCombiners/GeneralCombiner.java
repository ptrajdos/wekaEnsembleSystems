/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import java.io.Serializable;
import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.RevisionHandler;
import weka.core.Utils;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.11.0
 *
 */
public abstract class GeneralCombiner implements OutputCombiner,Serializable, RevisionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 573687151292284645L;
	
	private static double EPS=1E-10;

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
		return "1.11.0";
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
		
		return this.getCombinedDistributionForInstance(predictions, testInstance, weights);
	}
	
	

	public void normalizeOutput(double[] output){
		if(Utils.eq(Utils.sum(output), 0)){
			return;
		}
		Utils.normalize(output);
	}

}
