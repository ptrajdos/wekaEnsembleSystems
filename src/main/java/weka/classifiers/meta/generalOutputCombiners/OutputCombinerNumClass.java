/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import weka.core.Instance;

/**
 * @author pawel
 *
 */
public interface OutputCombinerNumClass {

	/**
	 *	Combines numeric outputs 
	 * @param predictions
	 * @param testInstance
	 * @return
	 * @throws Exception
	 */
	public double getClass(double[] predictions,Instance testInstance)throws Exception;
	
	/**
	 * Weighted combination of the outputs
	 * @param predictions
	 * @param testInstance
	 * @param weights
	 * @return
	 * @throws Exception
	 */
	public double getClass(double[] predictions,Instance testInstance, double[] weights)throws Exception;
	
}
