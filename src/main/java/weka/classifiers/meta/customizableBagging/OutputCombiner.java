/**
 * 
 */
package weka.classifiers.meta.customizableBagging;

import weka.classifiers.IteratedSingleClassifierEnhancer;
import weka.core.Instance;

/**
 * @author pawel
 *
 */
public interface OutputCombiner {
	
	/**
	 * gets distributionForInstance for multiple classifiers
	 * @param itClassifier
	 * @param instance
	 * @return
	 * @throws Exception
	 */
	public double[] getDistributionForInstance(IteratedSingleClassifierEnhancer itClassifier,Instance instance)throws Exception;
	
	/**
	 * get output for regression
	 * @param itClassifier
	 * @param instance
	 * @return
	 * @throws Exception
	 */
	public double getClass(IteratedSingleClassifierEnhancer itClassifier,Instance instance)throws Exception;

}
