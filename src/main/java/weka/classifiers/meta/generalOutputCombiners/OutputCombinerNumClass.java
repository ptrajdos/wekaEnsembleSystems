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
	 * 
	 * @param predictions
	 * @param testInstance
	 * @return
	 * @throws Exception
	 */
	public double getClass(double[] predictions,Instance testInstance)throws Exception;
}
