/**
 * 
 */
package weka.classifiers.meta.generalOutputCombiners;

import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
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
	
	/**
	 * Combination of the committee output
	 * @param committee
	 * @param testInstance
	 * @return
	 * @throws Exception
	 */
	
	public double getClass(Classifier[] committee, Instance testInstance)throws Exception;
	
	/**
	 * Weighted combination of the committee output
	 * @param committee
	 * @param testInstance
	 * @param weights
	 * @return
	 * @throws Exception
	 */
	public double getClass(Classifier[] committee, Instance testInstance, double[] weights)throws Exception;
	
	
}
