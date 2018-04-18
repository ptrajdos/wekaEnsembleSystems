/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

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
}
