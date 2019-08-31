/**
 * 
 */
package weka.classifiers.meta.simpleVotingLikeCombiners;

import weka.classifiers.IteratedSingleClassifierEnhancer;
import weka.classifiers.MultipleClassifiersCombiner;
import weka.core.Instance;

/**
 * @author pawel trajdos
 * @since 1.1.1
 * @version 1.1.1
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
	
	/**
	 * ets distributionForInstance for multiple classifiers
	 * @param classifier
	 * @param instance
	 * @return
	 * @throws Exception
	 */
	public double[] getDistributionForInstance(MultipleClassifiersCombiner classifier,Instance instance)throws Exception;
	
	
	/**
	 * get output for regression
	 * @param classifier
	 * @param instance
	 * @return
	 * @throws Exception
	 */
	public double getClass(MultipleClassifiersCombiner classifier,Instance instance)throws Exception;
	
	

}
