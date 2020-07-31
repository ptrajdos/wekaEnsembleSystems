/**
 * 
 */
package weka.classifiers.meta;

import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;

/**
 * Class that implements Updateable interface but ignores update requests. 
 * @author pawel trajdos
 * @since 1.7.0
 * @version 1.7.0
 *
 */
public class SimpleSingleClassifierEnhancerUpdateSkip extends SimpleSingleClassifierEnhancer implements UpdateableClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2986189407151633186L;

	/**
	 * 
	 */
	public SimpleSingleClassifierEnhancerUpdateSkip() {
		super();
	}

	@Override
	public void updateClassifier(Instance instance) throws Exception {
		// Deliberately ignores update request
	}

}
