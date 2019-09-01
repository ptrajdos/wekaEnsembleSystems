/**
 * 
 */
package weka.classifiers.meta.committees;

import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 * An interface for the Committee Classifier
 * @author pawel trajdos
 * @since 1.2.0
 * @version 1.2.0
 *
 */
public interface Committee extends Classifier {
	
	/**
	 * Generates response for the Committee classifier.
	 * Each classifier in committee generates its own distributionForInstance()
	 * @param inst
	 * @return
	 * @throws Exception
	 */
	public double[][] distributionForInstanceCommittee(Instance inst)throws Exception;
	
	/**
	 * Classify the instance using classifiers in the committee.
	 * @param inst
	 * @return
	 * @throws Exception
	 */
	public double[] classifyInstanceCommittee(Instance inst)throws Exception;

}
