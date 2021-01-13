/**
 * 
 */
package weka.classifiers.meta;

import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;

/**
 * @author pawel trajdos
 * @since 1.10.0
 * @version 1.10.0
 *
 */
public class FilteredClassifierUpdateable extends FilteredClassifier implements UpdateableClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -874477584981913409L;


	@Override
	public void updateClassifier(Instance instance) throws Exception {
		if(this.m_Classifier instanceof UpdateableClassifier) {
			Instance fInstance = this.filterInstance(instance);
			((UpdateableClassifier) this.m_Classifier).updateClassifier(fInstance);
		}

	}

}
