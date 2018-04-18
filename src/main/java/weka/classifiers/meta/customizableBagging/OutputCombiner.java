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
	
	public double[] getDistributionForInstance(IteratedSingleClassifierEnhancer itClassifier,Instance instance)throws Exception;

}
