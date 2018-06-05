/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 * @author pawel
 *
 */
public interface OutputCombiner {

	/**
	 * Combines raw predictions of the classifiers
	 * @param rawPredictions -- obtained via distributionForInstance [models] x [classes] 
	 * @param testInstance -- instance that is being classified
	 * @return
	 * @throws Exception
	 */
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance)throws Exception;
	
	/**
	 * Combines raw predictions of the classifiers.
	 * Weighted version 
	 * @param rawPredictions -- obtained via distributionForInstance [models] x [classes]
	 * @param testInstance -- instance that is being classified
	 * @param weights -- weights
	 * @return
	 * @throws Exception
	 */
	public double[] getCombinedDistributionForInstance(double[][] rawPredictions, Instance testInstance,double[] weights)throws Exception;
	
	/**
	 * Combines prediction of the classifiers constituting the committee
	 * @param committee -- classifier committee
	 * @param testInstance -- instance that is being classified
	 * @return
	 * @throws Exception
	 */
	public double[] getCombinedDistributionForInstance(Classifier[] committee, Instance testInstance)throws Exception;
	
	/**
	 * Combines prediction of the classifiers constituting the committee
	 * @param committee
	 * @param testInstance
	 * @return
	 * @throws Exception
	 */
	public double[] getCombinedDistributionForInstance(Classifier[] committee, Instance testInstance, double[] weights)throws Exception;
}
